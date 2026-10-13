import java.util.List;

public class AnalisisDatos {

    // --- RECURSIVIDAD: SUMA DE PESOS POR MUNICIPIO ---

    // Método envoltorio (wrapper) para facilitar el llamado desde el exterior
    public static double calcularPesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        if (paquetes == null || paquetes.isEmpty()) return 0.0;
        return sumarPesoRecursivo(paquetes, municipio, 0);
    }

    private static double sumarPesoRecursivo(List<Paquete> paquetes, String municipio, int indice) {
        // Caso base: se recorrió toda la lista
        if (indice == paquetes.size()) {
            return 0.0;
        }

        // Caso recursivo: se evalúa el paquete actual
        Paquete actual = paquetes.get(indice);
        double pesoAcumular = 0.0;

        if (actual.getDestino().equalsIgnoreCase(municipio)) {
            pesoAcumular = actual.getPeso();
        }

        // Se avanza hacia el caso base incrementando el índice
        return pesoAcumular + sumarPesoRecursivo(paquetes, municipio, indice + 1);
    }

    // --- RECURSIVIDAD: CONTEO POR PRIORIDAD MÍNIMA ---

    public static int contarPaquetesPorPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        if (paquetes == null || paquetes.isEmpty()) return 0;
        return contarPrioridadRecursivo(paquetes, prioridadMinima, 0);
    }

    private static int contarPrioridadRecursivo(List<Paquete> paquetes, int prioridadMinima, int indice) {
        // Caso base
        if (indice == paquetes.size()) {
            return 0;
        }

        int conteo = (paquetes.get(indice).getPrioridad() >= prioridadMinima) ? 1 : 0;

        // Llamada pendiente que acumula el retorno
        return conteo + contarPrioridadRecursivo(paquetes, prioridadMinima, indice + 1);
    }

    // --- BÚSQUEDA BINARIA ---

    /**
     * Precondición: La lista de paquetes debe estar previamente ordenada por su código (orden natural).
     */
    public static Paquete buscarPaqueteBinario(List<Paquete> paquetesOrdenados, String codigoBuscado) {
        int izquierda = 0;
        int derecha = paquetesOrdenados.size() - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            Paquete paqueteMedio = paquetesOrdenados.get(medio);

            int comparacion = paqueteMedio.getCodigo().compareTo(codigoBuscado);

            if (comparacion == 0) {
                return paqueteMedio; // Encontrado
            } else if (comparacion < 0) {
                izquierda = medio + 1; // Buscar en la mitad derecha
            } else {
                derecha = medio - 1; // Buscar en la mitad izquierda
            }
        }
        return null; // No encontrado
    }

    // --- DIVIDE Y VENCERÁS: PAQUETE DE MAYOR PESO ---

    public static Paquete encontrarPaqueteMayorPeso(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) return null;
        return aplicarDivideYVenceras(paquetes, 0, paquetes.size() - 1);
    }

    private static Paquete aplicarDivideYVenceras(List<Paquete> paquetes, int inicio, int fin) {
        // 1. Caso base: el subproblema tiene un solo elemento
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        // 2. División del problema
        int mitad = inicio + (fin - inicio) / 2;

        // 3. Resolución recursiva de subproblemas
        // Subproblema izquierdo
        Paquete maxIzquierdo = aplicarDivideYVenceras(paquetes, inicio, mitad);
        // Subproblema derecho
        Paquete maxDerecho = aplicarDivideYVenceras(paquetes, mitad + 1, fin);

        // 4. Combinación de resultados
        if (maxIzquierdo.getPeso() >= maxDerecho.getPeso()) {
            return maxIzquierdo;
        } else {
            return maxDerecho;
        }
    }
}