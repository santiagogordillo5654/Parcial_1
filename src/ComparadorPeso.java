import java.util.Comparator;

public class ComparadorPeso implements Comparator<Paquete> {
    @Override
    public int compare(Paquete p1, Paquete p2) {
        // Orden descendente (de mayor a menor peso)
        return Double.compare(p2.getPeso(), p1.getPeso());
    }
}