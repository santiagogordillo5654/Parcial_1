import java.util.Comparator;

public class ComparadorPrioridad implements Comparator<Paquete> {
    @Override
    public int compare(Paquete p1, Paquete p2) {
        // Orden descendente (de mayor a menor prioridad)
        return Integer.compare(p2.getPrioridad(), p1.getPrioridad());
    }
}