package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.EmptyStackException;

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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String  getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico(String numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
