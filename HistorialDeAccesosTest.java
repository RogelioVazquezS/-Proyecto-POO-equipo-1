import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HistorialDeAccesosTest {

    @Before
    public void setUp() {
        HistorialDeAccesosGUI.clearRegistros(); // Limpiar registros antes de cada test
    }

    // Prueba 1: Añadir un registro
    @Test
    public void testAddRegistro() {
        HistorialDeAccesos acceso = new HistorialDeAccesos("Ana", "Informática", 123, true);
        HistorialDeAccesosGUI.addRegistro(new RegistroRecord("entrada", acceso));
        assertEquals(1, HistorialDeAccesosGUI.getRegistrosSize());
    }

    // Prueba 2: Limpiar registros
    @Test
    public void testClearRegistros() {
        HistorialDeAccesosGUI.addRegistro(new RegistroRecord("salida", new HistorialDeAccesos("Luis", "Derecho", 456, true)));
        HistorialDeAccesosGUI.clearRegistros();
        assertEquals(0, HistorialDeAccesosGUI.getRegistrosSize());
    }

    // Prueba 3: Validar contenido generado
    @Test
    public void testGenerateContentContieneNombre() {
        HistorialDeAccesosGUI.addRegistro(new RegistroRecord("entrada", new HistorialDeAccesos("Marta", "Biología", 789, true)));
        String content = HistorialDeAccesosGUI.generateRegistroListContent();
        assertTrue(content.contains("Marta")); // Verificación simple
    }

    // Prueba 4: Acceso denegado sin QR
    @Test
    public void testAccesoDenegadoSinQR() {
        HistorialDeAccesos acceso = new HistorialDeAccesos("Pedro", "Química", 202, false);
        assertFalse(acceso.pasar());
    }

    // Prueba 5: Acceso permitido con QR
    @Test
    public void testAccesoPermitidoConQR() {
        HistorialDeAccesos acceso = new HistorialDeAccesos("Sofía", "Matemáticas", 303, true);
        assertTrue(acceso.pasar());
    }
}