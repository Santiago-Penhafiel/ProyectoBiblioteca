package udla.smonroy.speñafiel.gvillacis.proyectofinal;


public abstract class Persona {
    private String nombre;
    private int edad;
    private String numeroTelefonico;
    private String cedula;
    private String correo;

    public Persona(String nombre, int edad, String numeroTelefonico, String cedula, String correo) {
        this.nombre = nombre;
        this.edad = edad;
        this.numeroTelefonico = numeroTelefonico;
        this.cedula = cedula;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }


    public int getEdad() {
        return edad;
    }


    public String  getNumeroTelefonico() {
        return numeroTelefonico;
    }


    public String getCedula() {
        return cedula;
    }


    public String getCorreo() {
        return correo;
    }


}
