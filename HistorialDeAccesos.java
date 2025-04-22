public class HistorialDeAccesos {
    private String nombre;
    private String licenciatura;
    private int id;
    private boolean qr;

    public HistorialDeAccesos(String nombre, String licenciatura, int id, boolean qr) {
        this.nombre = nombre;
        this.licenciatura = licenciatura;
        this.id = id;
        this.qr = qr;
        // Mensajes para la consola (simulando la lógica de acceso)
        if (!qr) {
            System.out.println("Acceso denegado. QR inválido");
        } else {
            System.out.println("Puede pasar");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getLicenciatura() {
        return licenciatura;
    }

    public int getId() {
        return id;
    }

    public boolean tieneQr() {
        return qr;
    }

    public boolean pasar() {
        return qr;
    }
}
