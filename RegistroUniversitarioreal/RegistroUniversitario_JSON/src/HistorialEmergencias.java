import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HistorialEmergencias extends JFrame {
    private JButton verHistorialButton, regresarButton;
    private JTextArea emergenciasArea;

    public HistorialEmergencias() {
        setTitle("Historial de Emergencias");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        verHistorialButton = new JButton("Ver Emergencias Registradas");
        verHistorialButton.setBounds(150, 20, 200, 30);
        add(verHistorialButton);
        
        regresarButton = new JButton("Regresar");
        regresarButton.setBounds(150, 320, 100, 30);
        add(regresarButton);

        emergenciasArea = new JTextArea();
        emergenciasArea.setBounds(50, 70, 400, 250);
        emergenciasArea.setEditable(false);
        add(emergenciasArea);

        verHistorialButton.addActionListener(e -> {
            List<Emergencia> emergencias = BDJSON.obtenerEmergencias();
            StringBuilder mensaje = new StringBuilder("Historial de Emergencias:\n");
        
            if (emergencias.isEmpty()) {
                mensaje.append("No hay emergencias registradas.");
            } else {
                for (Emergencia emergencia : emergencias) {
                    mensaje.append("Tipo: ").append(emergencia.getTipo())
                           .append(" | Ubicación: ").append(emergencia.getUbicacion())
                           .append(" | Prioridad: ").append(emergencia.getPrioridad())
                           .append(" | Fecha: ").append(emergencia.getFechaHora())
                           .append("\n");
                }
            }
            emergenciasArea.setText(mensaje.toString());
            emergenciasArea.repaint(); // Forzar actualización de la vista
        });

        regresarButton.addActionListener(e -> {
            dispose();
            new PaginaPrincipal().setVisible(true);
        });
    }
}
