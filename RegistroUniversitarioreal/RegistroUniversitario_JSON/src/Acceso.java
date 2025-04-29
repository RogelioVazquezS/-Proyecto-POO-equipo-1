import java.io.Serializable;

public class Acceso implements Serializable {
    private String nombre;
    private String tipo;
    private String fechaHora;

    public Acceso(String nombre, String tipo, String fechaHora) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        return "Acceso{" +
                "nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                '}';
    }
}
