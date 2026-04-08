package ar.unrn;

/**
 * Representa un punto en el espacio bidimensional (coordenadas x, y).
 * Ejemplifica el concepto de Encapsulamiento.
 * 
 * En C, solíamos usar una struct Punto { int x; int y; }.
 * En Java, protegemos los datos (campos privados) y controlamos el acceso
 * mediante métodos públicos (getters y setters). Esto permite validar 
 * los datos antes de asignarlos.
 */
public class Punto {
    private int x;
    private int y;

    /**
     * Constructor para inicializar el punto.
     * @param x posición en el eje horizontal
     * @param y posición en el eje vertical
     */
    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters y Setters: El reemplazo de acceder directamente a los miembros de una struct.
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Redefinición del método toString.
     * Similar a crear una función imprimirPunto(Punto p) en C.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
