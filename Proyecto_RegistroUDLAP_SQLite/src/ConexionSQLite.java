import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {
    private static final String DB_PATH = System.getProperty("user.dir") + "/Usuarios_Udlap.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection conectar() {
        Connection conn = null;
        try {

            Class.forName("org.sqlite.JDBC");

            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                System.out.println("⚠️ Archivo de base de datos NO encontrado en: " + DB_PATH);
                return null;
            }

            conn = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión establecida con SQLite.");
        } catch (Exception e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
        return conn;
    }
}
