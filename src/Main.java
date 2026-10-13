import java.util.*;

public class Main {
    private static QuindioExpress sistema = new QuindioExpress();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Cargar los datos "quemados" iniciales para tener con qué trabajar
        cargarDatosIniciales();

        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("   SISTEMA QUINDÍO EXPRESS - MENÚ");
            System.out.println("========================================");
            System.out.println("1. Registrar nuevo Repartidor");
            System.out.println("2. Registrar nuevo Paquete");
            System.out.println("3. Consultar Paquete por Código");
            System.out.println("4. Despachar Paquete (Prioridad)");
            System.out.println("5. Ver Historial de Entregas");
            System.out.println("6. Ejecutar pruebas automáticas (Parcial)");
            System.out.println("7. Ejecutar Prueba de Estrés (500.000 paquetes - Parte A.2)");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                opcion = 0;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                opcion = 0;
            }
        } while (opcion != 8);

        System.out.println("Cerrando el sistema...");
        scanner.close();
    }

    private static void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarRepartidorInteractvo();
                break;
            case 2:
                registrarPaqueteInteractivo();
                break;
            case 3: 
                System.out.print("Ingrese el código del paquete: ");
                String cod = scanner.nextLine().toUpperCase();
                Paquete p = sistema.consultarPaquete(cod);
                if (p != null) System.out.println("Encontrado: " + p);
                else System.out.println("Paquete no encontrado.");
                break;
            case 4:
                System.out.print("Ingrese la identificación del repartidor: ");
                String id = scanner.nextLine();
                if (sistema.despacharSiguientePaquetePrioritario(id)) {
                    System.out.println("Despacho realizado con éxito.");
                } else {
                    System.out.println("No se pudo despachar (cola vacía o zonas incompatibles).");
                }
                break;
            case 5:
                System.out.println("--- HISTORIAL DE ENTREGAS ---");
                for (Paquete entregado : sistema.getHistorial()) {
                    System.out.println(entregado);
                }
                break;
            case 6:
                ejecutarPruebasAutomaticas();
                break;
            case 7:
                ejecutarPruebaEstres();
                break;
            case 8:
                // Salir
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void registrarRepartidorInteractvo() {
        System.out.print("ID Repartidor: ");
        String id = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Zona (Ej. Armenia, Salento): ");
        String zona = scanner.nextLine();

        sistema.registrarRepartidor(new Repartidor(id, nombre, zona, true));
        System.out.println("Repartidor registrado exitosamente.");
    }

    private static void registrarPaqueteInteractivo() {
        System.out.print("Código del paquete (Ej. PQ999): ");
        String cod = scanner.nextLine().toUpperCase();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        System.out.print("Peso (kg): ");
        double peso = Double.parseDouble(scanner.nextLine());
        System.out.print("Prioridad (1-5): ");
        int prioridad = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo estimado (min): ");
        int tiempo = Integer.parseInt(scanner.nextLine());

        sistema.registrarPaquete(new Paquete(cod, destino, peso, prioridad, tiempo));
        System.out.println("Paquete registrado exitosamente.");
    }

    private static void cargarDatosIniciales() {
        System.out.println("Cargando 30 paquetes y 6 repartidores iniciales...");
        sistema.registrarRepartidor(new Repartidor("1094001", "Santiago Gordillo", "Armenia", true));
        sistema.registrarRepartidor(new Repartidor("1094002", "Sebastián Román", "Salento", true));
        sistema.registrarRepartidor(new Repartidor("1094003", "Valentina Rojas", "Calarcá", true));
        sistema.registrarRepartidor(new Repartidor("1094004", "Andrés Felipe", "Montenegro", true));
        sistema.registrarRepartidor(new Repartidor("1094005", "Camila Aristizábal", "Circasia", true));
        sistema.registrarRepartidor(new Repartidor("1094006", "Jorge Iván", "Armenia", true));

        String[] destinos = {"Armenia", "Salento", "Calarcá", "Montenegro", "Circasia"};
        String[] codigos = {
                "PQ731", "PQ105", "PQ942", "PQ318", "PQ567", "PQ829", "PQ420", "PQ111", "PQ222", "PQ333",
                "PQ444", "PQ555", "PQ666", "PQ777", "PQ888", "PQ999", "PQ123", "PQ234", "PQ345", "PQ456",
                "PQ568", "PQ679", "PQ780", "PQ891", "PQ902", "PQ013", "PQ124", "PQ235", "PQ346", "PQ457"
        };

        for (int i = 0; i < 30; i++) {
            String destino = destinos[i % 5];
            double peso = 1.5 + (i * 0.5);
            int prioridad = (i % 5) + 1;
            int tiempo = 15 + (i * 2);
            sistema.registrarPaquete(new Paquete(codigos[i], destino, peso, prioridad, tiempo));
        }
    }

    private static void ejecutarPruebasAutomaticas() {
        System.out.println("\n--- PRUEBA DE COLECCIONES ---");
        System.out.println("Municipios sin repetir (TreeSet ordenado):");
        for (String m : sistema.obtenerMunicipiosSinRepetir()) {
            System.out.println("- " + m);
        }

        System.out.println("\n--- PRUEBA DE ALGORITMOS (ANALISIS DE DATOS) ---");
        List<Paquete> paquetesArmenia = sistema.obtenerPaquetesPorMunicipio("Armenia");

        double pesoArmenia = AnalisisDatos.calcularPesoTotalPorMunicipio(paquetesArmenia, "Armenia");
        System.out.println("Peso total en Armenia (Recursivo): " + pesoArmenia + " kg");

        List<Paquete> todosLosPaquetes = new ArrayList<>();
        for (String m : sistema.obtenerMunicipiosSinRepetir()) {
            todosLosPaquetes.addAll(sistema.obtenerPaquetesPorMunicipio(m));
        }

        int pMin = 4;
        System.out.println("Paquetes con prioridad >=" + pMin + " (Recursivo): " +
                AnalisisDatos.contarPaquetesPorPrioridadMinima(todosLosPaquetes, pMin));

        Collections.sort(todosLosPaquetes); // Orden Natural
        System.out.println("Búsqueda Binaria de PQ318: " + AnalisisDatos.buscarPaqueteBinario(todosLosPaquetes, "PQ318"));

        Paquete masPesado = AnalisisDatos.encontrarPaqueteMayorPeso(todosLosPaquetes);
        System.out.println("Paquete más pesado (Divide y Vencerás): " + masPesado.getCodigo());

        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private static void ejecutarPruebaEstres() {
        System.out.println("\n--- INICIANDO PRUEBA DE 500.000 PAQUETES (Parte A.2) ---");
        System.out.println("Generando datos simulados... por favor espere.");

        List<Paquete> listaArrayList = new ArrayList<>();
        Map<String, Paquete> mapaHashMap = new HashMap<>();

        for (int i = 0; i < 500000; i++) {
            Paquete p = new Paquete("TEST" + i, "Armenia", 2.0, 3, 20);
            listaArrayList.add(p);
            mapaHashMap.put(p.getCodigo(), p);
        }

        String codigoABuscar = "TEST499999"; // El peor caso, el último elemento

        System.out.println("\nBuscando el código " + codigoABuscar + "...");

        // Prueba con ArrayList (Búsqueda secuencial O(n))
        long inicioLista = System.nanoTime();
        Paquete encontradoLista = null;
        for (Paquete p : listaArrayList) {
            if (p.getCodigo().equals(codigoABuscar)) {
                encontradoLista = p;
                break;
            }
        }
        long finLista = System.nanoTime();
        long tiempoLista = finLista - inicioLista;

        // Prueba con HashMap (Búsqueda directa O(1))
        long inicioMapa = System.nanoTime();
        Paquete encontradoMapa = mapaHashMap.get(codigoABuscar);
        long finMapa = System.nanoTime();
        long tiempoMapa = finMapa - inicioMapa;

        System.out.println("\n--- RESULTADOS DE TIEMPO ---");
        System.out.println("Tiempo con ArrayList O(n): " + tiempoLista + " nanosegundos.");
        System.out.println("Tiempo con HashMap O(1):   " + tiempoMapa + " nanosegundos.");

        double diferencia = (double) tiempoLista / tiempoMapa;
        System.out.printf("¡El HashMap es aproximadamente %.2f veces más rápido para 500.000 paquetes!\n", diferencia);
        System.out.println("\nJustificación: ArrayList requiere recorrer paquete por paquete hasta encontrar coincidencias. HashMap utiliza una función Hash que calcula directamente en memoria la posición del objeto, ahorrando procesamiento, aunque sacrifica un poco más de memoria RAM para estructurar los nodos.");

        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}