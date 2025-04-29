import java.io.Serializable;

public class Emergencia implements Serializable {
    private String tipo;
    private String ubicacion;
    private String prioridad;
    private String fechaHora;

    public Emergencia(String tipo, String ubicacion, String prioridad, String fechaHora) {
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.prioridad = prioridad;
        this.fechaHora = fechaHora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getFechaHora() {
        return fechaHora;
    }
}
