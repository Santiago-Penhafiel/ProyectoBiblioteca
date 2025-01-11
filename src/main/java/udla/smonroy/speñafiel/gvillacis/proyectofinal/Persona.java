package udla.smonroy.speñafiel.gvillacis.proyectofinal;

public abstract class Persona {
    private String nombre;
    private int edad;
    private int numeroTelefonico;
    private int cedula;
    private String correo;

    public Persona(String nombre, int edad, int numeroTelefonico, int cedula, String correo) {
        this.nombre = nombre;
        this.edad = edad;
        this.numeroTelefonico = numeroTelefonico;
        this.cedula = cedula;
        this.correo = correo;
    }
}
