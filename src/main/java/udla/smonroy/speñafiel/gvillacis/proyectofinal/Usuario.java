package udla.smonroy.speñafiel.gvillacis.proyectofinal;

public class Usuario extends Persona{
    private int multa;

    public Usuario(String nombre, int edad, String numeroTelefonico, String cedula, String correo) {
        super(nombre, edad, numeroTelefonico, cedula, correo);
    }

    public int getMulta() {
        return multa;
    }

    public void setMulta(int multa) {
        this.multa = multa;
    }
}
