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

    // Método para marcar la emergencia como completada
    public void marcarCompletada() {
        this.completada = true;
    }

    // Método para cambiar la prioridad de la emergencia
    public void setPrioridad(String nuevaPrioridad) {
        this.prioridad = nuevaPrioridad;
    }

    // Métodos getters
    public String getCategoria() {
        return categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    // Método para fusionar dos emergencias, actualizando el ID
    public void fusionarEmergencia(int nuevoId) {
        // Fusionamos las emergencias de alguna manera. Aquí actualizamos el ID, pero podrías hacer más
        if (nuevoId != this.id) {
            this.id = nuevoId;  // Actualizamos el ID o cualquier otro campo necesario
            // Si hay algún otro campo o acción para la fusión, agrégalo aquí
        }
    }

    @Override
    public String toString() {
        return "[ID: " + id + "] " + categoria + " | Ubicación: " + ubicacion +
                " | Prioridad: " + prioridad + (completada ? "  Completada" : "");
    }
}

