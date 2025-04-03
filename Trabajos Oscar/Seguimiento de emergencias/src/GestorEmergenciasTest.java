import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GestorEmergenciasTest {

    @Test
    public void testNombreValido() {
        GestorEmergencias emergencia = new GestorEmergencias("Incendio", "Fuego", "Edificio A", 101, "Alta");
        assertEquals("Incendio", emergencia.getNombre());
    }

    @Test
    public void testCategoriaValida() {
        GestorEmergencias emergencia = new GestorEmergencias("Incendio", "Fuego", "Edificio A", 101, "Alta");
        assertEquals("Fuego", emergencia.getCategoria());
    }

    @Test
    public void testUbicacionValida() {
        GestorEmergencias emergencia = new GestorEmergencias("Terremoto", "Sismo", "Zona 5", 104, "Media");
        assertEquals("Zona 5", emergencia.getUbicacion());
    }

    @Test
    public void testIdValido() {
        GestorEmergencias emergencia = new GestorEmergencias("Explosión", "Fuego", "Edificio B", 200, "Alta");
        assertEquals(200, emergencia.getId());
    }

    @Test
    public void testPrioridadValida() {
        GestorEmergencias emergencia = new GestorEmergencias("Cortocircuito", "Electrico", "Planta Baja", 106, "Baja");
        assertEquals("Baja", emergencia.getPrioridad());
    }

    @Test
    public void testUbicacionInvalida() {
        try {
            new GestorEmergencias("Terremoto", "Sismo", "Zona@5", 105, "Baja");
        } catch (IllegalArgumentException e) {
            assertEquals("La ubicación solo puede contener letras, números y espacios.", e.getMessage());
        }
    }

    @Test
    public void testPrioridadInvalida() {
        try {
            new GestorEmergencias("Cortocircuito", "Electrico", "Planta Baja", 106, "Muy Alta");
        } catch (IllegalArgumentException e) {
            assertEquals("La prioridad debe ser 'Alta', 'Media' o 'Baja'.", e.getMessage());
        }
    }
    
}

    
   