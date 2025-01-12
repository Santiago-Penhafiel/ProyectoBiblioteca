package udla.smonroy.speñafiel.gvillacis.proyectofinal;

public class Empleado extends Persona {
    private int idEmpleado;
    private float salario;
    private boolean tiempoCompleto;

    public Empleado(String nombre, int edad, String numeroTelefonico, String cedula, String correo) {
        super(nombre, edad, numeroTelefonico, cedula, correo);
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public boolean isTiempoCompleto() {
        return tiempoCompleto;
    }

    public void setTiempoCompleto(boolean tiempoCompleto) {
        this.tiempoCompleto = tiempoCompleto;
    }
}
