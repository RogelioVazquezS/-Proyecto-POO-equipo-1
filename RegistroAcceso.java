public class RegistroAcceso {
    private Usuario usuario;
    private String timestamp;

    public RegistroAcceso(Usuario usuario, String timestamp) {
        this.usuario = usuario;
        this.timestamp = timestamp;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTimestamp() {
        return timestamp;
    }
}