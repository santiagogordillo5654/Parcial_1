import java.util.*;

public class QuindioExpress {

    // Estructuras de datos elegidas en la Parte A
    private Map<String, Paquete> paquetesPendientes; // LinkedHashMap para orden de llegada
    private Set<String> municipiosDestino;           // TreeSet para municipios únicos y ordenados
    private Queue<Paquete> colaPrioridad;            // PriorityQueue para reglas de despacho
    private Map<String, Repartidor> repartidores;    // HashMap para búsquedas rápidas
    private HistorialEntregas<Paquete> historial;    // Colección Propia

    // OPTIMIZACIÓN NUEVA: Agrupación de paquetes por municipio con acceso O(1)
    private Map<String, List<Paquete>> paquetesPorMunicipio; // HashMap

    public QuindioExpress() {
        this.paquetesPendientes = new LinkedHashMap<>();
        this.municipiosDestino = new TreeSet<>();
        this.repartidores = new HashMap<>();
        this.historial = new HistorialEntregas<>();
        this.paquetesPorMunicipio = new HashMap<>();

        // Comparador en cascada para despachos prioritarios
        Comparator<Paquete> comparadorPrioritario = new Comparator<Paquete>() {
            @Override
            public int compare(Paquete p1, Paquete p2) {
                // 1. Mayor prioridad primero (descendente)
                int cmpPrioridad = Integer.compare(p2.getPrioridad(), p1.getPrioridad());
                if (cmpPrioridad != 0) return cmpPrioridad;

                // 2. Menor tiempo estimado primero (ascendente)
                int cmpTiempo = Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
                if (cmpTiempo != 0) return cmpTiempo;

                // 3. Código alfabéticamente (ascendente)
                return p1.getCodigo().compareTo(p2.getCodigo());
            }
        };

        this.colaPrioridad = new PriorityQueue<>(comparadorPrioritario);
    }

    // --- REGISTRO Y CONSULTAS BÁSICAS ---

    public void registrarPaquete(Paquete paquete) {
        if (paquetesPendientes.containsKey(paquete.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un paquete registrado con el código: " + paquete.getCodigo());
        }

        paquetesPendientes.put(paquete.getCodigo(), paquete);
        municipiosDestino.add(paquete.getDestino());
        colaPrioridad.add(paquete);

        // OPTIMIZACIÓN: Se agrega el paquete a su lista correspondiente en O(1)
        paquetesPorMunicipio.putIfAbsent(paquete.getDestino(), new ArrayList<>());
        paquetesPorMunicipio.get(paquete.getDestino()).add(paquete);
    }

    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidores.containsKey(repartidor.getIdentificacion())) {
            throw new IllegalArgumentException("Ya existe un repartidor con identificación: " + repartidor.getIdentificacion());
        }
        repartidores.put(repartidor.getIdentificacion(), repartidor);
    }

    public Paquete consultarPaquete(String codigo) {
        return paquetesPendientes.get(codigo); // O(1) de complejidad temporal
    }

    public Set<String> obtenerMunicipiosSinRepetir() {
        return municipiosDestino; // Ya está ordenado gracias al TreeSet
    }

    public List<Paquete> obtenerPaquetesPorMunicipio(String municipio) {
        // OPTIMIZADO: Retorna la lista directamente en O(1) en vez de recorrer todos los paquetes O(n)
        return paquetesPorMunicipio.getOrDefault(municipio, new ArrayList<>());
    }

    // --- OPERACIONES DE DESPACHO ---

    public boolean despacharSiguientePaquetePrioritario(String idRepartidor) {
        if (colaPrioridad.isEmpty()) return false;

        Repartidor repartidor = repartidores.get(idRepartidor);
        validarRepartidor(repartidor);

        Paquete paqueteAsignar = colaPrioridad.peek();

        if (!repartidor.getZona().equalsIgnoreCase(paqueteAsignar.getDestino())) {
            throw new IllegalStateException("La zona del repartidor (" + repartidor.getZona() +
                    ") no coincide con el destino del paquete (" + paqueteAsignar.getDestino() + ")");
        }

        // Procesamiento del despacho
        paqueteAsignar = colaPrioridad.poll();
        removerPaqueteDeEstructurasPendientes(paqueteAsignar);
        historial.agregar(paqueteAsignar);

        return true;
    }

    public boolean despacharSiguientePaqueteOrdenLlegada(String idRepartidor) {
        if (paquetesPendientes.isEmpty()) return false;

        Repartidor repartidor = repartidores.get(idRepartidor);
        validarRepartidor(repartidor);

        Paquete paqueteAsignar = paquetesPendientes.values().iterator().next();

        if (!repartidor.getZona().equalsIgnoreCase(paqueteAsignar.getDestino())) {
            throw new IllegalStateException("Zona de repartidor no coincide con destino del paquete.");
        }

        colaPrioridad.remove(paqueteAsignar); // Mantenemos sincronizada la cola
        removerPaqueteDeEstructurasPendientes(paqueteAsignar);
        historial.agregar(paqueteAsignar);

        return true;
    }

    // Metodo auxiliar para mantener las estructuras consistentes al despachar
    private void removerPaqueteDeEstructurasPendientes(Paquete paquete) {
        paquetesPendientes.remove(paquete.getCodigo());

        // Lo sacamos de la agrupación por municipio
        List<Paquete> listaMunicipio = paquetesPorMunicipio.get(paquete.getDestino());
        if (listaMunicipio != null) {
            listaMunicipio.remove(paquete);
            // Si el municipio se queda sin paquetes pendientes, lo eliminamos del Set
            if (listaMunicipio.isEmpty()) {
                municipiosDestino.remove(paquete.getDestino());
            }
        }
    }

    private void validarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("Repartidor no encontrado.");
        }
        if (!repartidor.isDisponible()) {
            throw new IllegalStateException("El repartidor no se encuentra disponible.");
        }
    }

    // --- MÉTODOS DE HISTORIAL ---

    public HistorialEntregas<Paquete> getHistorial() {
        return historial;
    }
}