public class RegistroRecord {
    private String tipo;
    private HistorialDeAccesos acceso;

    public RegistroRecord(String tipo, HistorialDeAccesos acceso) {
        this.tipo = tipo;
        this.acceso = acceso;
    }

    public String getTipo() {
        return tipo;
    }

    public HistorialDeAccesos getAcceso() {
        return acceso;
    }
}
