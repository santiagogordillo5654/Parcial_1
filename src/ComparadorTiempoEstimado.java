import java.util.Comparator;

public class ComparadorTiempoEstimado implements Comparator<Paquete> {
    @Override
    public int compare(Paquete p1, Paquete p2) {
        // Orden ascendente (de menor a mayor tiempo estimado)
        return Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
    }
}