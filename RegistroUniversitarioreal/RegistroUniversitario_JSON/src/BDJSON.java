import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BDJSON {
    private static final String USUARIOS_FILE = "db/usuarios.json";
    private static final String ACCESOS_FILE = "db/accesos.json";
    private static final String EMERGENCIAS_FILE = "db/emergencias.json";
    private static final Gson gson = new Gson();

    public static <T> List<T> leerArchivo(String filePath, Type typeOfT) {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();
        
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, typeOfT);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void escribirArchivo(String filePath, List<T> data) {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try (Writer writer = new FileWriter(filePath)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Usuario> obtenerUsuarios() {
        return leerArchivo(USUARIOS_FILE, new TypeToken<List<Usuario>>() {}.getType());
    }

    public static void agregarUsuario(Usuario usuario) {
        List<Usuario> usuarios = obtenerUsuarios();
        usuarios.add(usuario);
        escribirArchivo(USUARIOS_FILE, usuarios);
    }

    public static List<Acceso> obtenerAccesos() {
        return leerArchivo(ACCESOS_FILE, new TypeToken<List<Acceso>>() {}.getType());
    }

    public static void registrarAcceso(Acceso acceso) {
        List<Acceso> accesos = obtenerAccesos();
        accesos.add(acceso);
        escribirArchivo(ACCESOS_FILE, accesos);
    }

    public static List<Emergencia> obtenerEmergencias() {
        return leerArchivo(EMERGENCIAS_FILE, new TypeToken<List<Emergencia>>() {}.getType());
    }

    public static void agregarEmergencia(Emergencia emergencia) {
        List<Emergencia> emergencias = obtenerEmergencias();
        emergencias.add(emergencia);
        escribirArchivo(EMERGENCIAS_FILE, emergencias);
    }
}
