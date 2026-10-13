import java.util.Iterator;
import java.util.NoSuchElementException;

public class HistorialEntregas<T> implements ColeccionPropia<T> {

    // Nodo encapsulado, inaccesible desde el exterior
    private static class Nodo<T> {
        T valor;
        Nodo<T> siguiente;

        public Nodo(T valor) {
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Nodo<T> cabeza;
    private int cantidadElementos;

    public HistorialEntregas() {
        this.cabeza = null;
        this.cantidadElementos = 0;
    }

    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        if (estaVacia()) {
            cabeza = nuevoNodo; // Escenario: Inserción en lista vacía
        } else {
            // Escenario: Inserción al final para conservar el orden exacto de entrega
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        cantidadElementos++;
    }

    @Override
    public boolean eliminar(T elemento) {
        if (estaVacia()) {
            return false; // Escenario: intento de eliminación en lista vacía
        }

        // Escenario: eliminación del primer elemento (o si es el único elemento)
        if (cabeza.valor.equals(elemento)) {
            cabeza = cabeza.siguiente;
            cantidadElementos--;
            return true;
        }

        Nodo<T> actual = cabeza;
        Nodo<T> anterior = null;

        // Búsqueda del elemento guardando la referencia al nodo anterior
        while (actual != null && !actual.valor.equals(elemento)) {
            anterior = actual;
            actual = actual.siguiente;
        }

        if (actual == null) {
            return false; // Escenario: intento de eliminación de un elemento inexistente
        }

        // Escenario: eliminación de un elemento intermedio o el último elemento
        // El nodo anterior se enlaza con el siguiente del nodo actual, "saltando" el nodo a eliminar
        anterior.siguiente = actual.siguiente;
        cantidadElementos--;
        return true;
    }

    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= cantidadElementos) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }

        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    @Override
    public int tamano() {
        return cantidadElementos;
    }

    @Override
    public boolean estaVacia() {
        return cabeza == null;
    }

    // Iterador para cumplir con el recorrido estándar sin exponer los Nodos
    @Override
    public Iterator<T> iterator() {
        return new IteradorHistorial();
    }

    private class IteradorHistorial implements Iterator<T> {
        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en el historial");
            }
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }
}