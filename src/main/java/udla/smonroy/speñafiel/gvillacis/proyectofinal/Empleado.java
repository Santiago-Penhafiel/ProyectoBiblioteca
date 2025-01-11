package udla.smonroy.speñafiel.gvillacis.proyectofinal;

public class Empleado extends Persona {
    private int idEmpleado;
    private float salario;
    private boolean tiempoCompleto;

    public Empleado(String nombre, int edad, int numeroTelefonico, int cedula, String correo) {
        super(nombre, edad, numeroTelefonico, cedula, correo);
    }
}
