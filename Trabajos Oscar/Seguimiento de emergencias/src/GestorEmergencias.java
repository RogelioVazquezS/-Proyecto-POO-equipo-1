public class GestorEmergencias {
    private String categoria, ubicacion, prioridad;
    private int id;
    private boolean completada;

    public GestorEmergencias(String categoria, String ubicacion, int id, String prioridad) {
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.id = id;
        this.prioridad = prioridad;
        this.completada = false;
    }

    public void marcarCompletada() {
        this.completada = true;
    }

    public void setPrioridad(String nuevaPrioridad) {
        this.prioridad = nuevaPrioridad;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return "[ID: " + id + "] " + categoria + " | Ubicación: " + ubicacion +
                " | Prioridad: " + prioridad + (completada ? "  Completada" : "");
    }
}
