public class Paquete implements Comparable<Paquete> {
    private String codigo;
    private String destino;
    private double peso;
    private int prioridad;
    private int tiempoEstimado;

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        // Validaciones estrictas según las reglas del sistema
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser nulo ni vacío.");
        }
        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException("El destino no puede ser nulo ni vacío.");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar comprendida entre 1 y 5.");
        }
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero.");
        }

        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Getters
    public String getCodigo() { return codigo; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }
    public int getTiempoEstimado() { return tiempoEstimado; }

    // Setters (con validaciones equivalentes)
    public void setDestino(String destino) {
        if (destino == null || destino.trim().isEmpty()) throw new IllegalArgumentException("Destino inválido.");
        this.destino = destino;
    }

    public void setPeso(double peso) {
        if (peso <= 0) throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        this.peso = peso;
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 5) throw new IllegalArgumentException("Prioridad inválida.");
        this.prioridad = prioridad;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        if (tiempoEstimado <= 0) throw new IllegalArgumentException("Tiempo estimado inválido.");
        this.tiempoEstimado = tiempoEstimado;
    }

    // Orden natural: ascendente por código
    @Override
    public int compareTo(Paquete otroPaquete) {
        return this.codigo.compareTo(otroPaquete.getCodigo());
    }

    @Override
    public String toString() {
        return codigo + " -> " + destino + " (" + peso + "kg, Prioridad: " + prioridad + ", " + tiempoEstimado + " min)";
    }
}