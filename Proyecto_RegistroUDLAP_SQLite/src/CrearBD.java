import java.sql.Connection;
import java.sql.Statement;


public class CrearBD {
    public static void main(String[] args) {
        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "matricula INTEGER NOT NULL UNIQUE," +
                         "nombre TEXT NOT NULL," +
                         "correo TEXT NOT NULL UNIQUE," +
                         "contraseña TEXT NOT NULL)";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Tabla 'usuarios' con matrícula creada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }
}
