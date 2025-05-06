public class Usuario {
    private String nombre;
    private int id;
    private String contrasena;

    public Usuario(String nombre, int id, String contrasena) {
        this.nombre = nombre;
        this.id = id;
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public String getContrasena() {
        return contrasena;
    }
}
