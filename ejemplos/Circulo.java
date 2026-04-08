package ar.unrn;

/**
 * Representa un círculo que hereda de Punto para definir su centro.
 * Ejemplifica Herencia y Reutilización de Código.
 * 
 * En Programación Orientada a Objetos, decimos que un Círculo "es un" Punto 
 * especializado (tiene coordenadas y además un radio).
 * 
 * En C, esto se lograba mediante composición (una struct que contiene otra)
 * o punteros. Java lo resuelve de forma nativa con la palabra 'extends'.
 */
public class Circulo extends Punto {
    private double radio;

    /**
     * Constructor del Círculo.
     * Notá el uso de 'super' para invocar al constructor de la clase base (Punto).
     */
    public Circulo(int x, int y, double radio) {
        super(x, y); 
        this.radio = radio;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        if (radio >= 0) {
            this.radio = radio;
        }
    }

    /**
     * Calcula el área del círculo.
     */
    public double calcularArea() {
        return Math.PI * Math.pow(radio, 2);
    }

    /**
     * Sobreescritura (Override): Cambiamos el comportamiento de toString 
     * para adaptarlo a las necesidades del Círculo, aprovechando la 
     * funcionalidad de la clase padre.
     */
    @Override
    public String toString() {
        return "Círculo con centro en " + super.toString() + " y radio " + radio;
    }

    public static void main(String[] args) {
        Circulo c = new Circulo(10, 20, 5.5);
        
        System.out.println("Datos del círculo: " + c);
        System.out.println("Área calculada: " + String.format("%.2f", c.calcularArea()));
        
        // Polimorfismo: Tratamos al círculo como un punto.
        Punto p = c;
        System.out.println("El centro (visto como Punto) es: " + p.getX() + ", " + p.getY());
    }
}
