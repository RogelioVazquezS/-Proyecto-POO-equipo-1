import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String contrasena;
    private String correoInstitucional;

    public Usuario(String nombre, String contrasena, String correoInstitucional) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correoInstitucional = correoInstitucional;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }
}