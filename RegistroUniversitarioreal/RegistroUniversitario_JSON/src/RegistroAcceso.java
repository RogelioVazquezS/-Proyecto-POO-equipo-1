import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class RegistroAcceso extends JFrame {
    private JButton entradaButton;
    private JButton salidaButton;
    private JButton verHistorialButton;
    private JButton regresarButton;

    public RegistroAcceso() {
        setTitle("Accesos Universitarios");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        entradaButton = new JButton("Registrar Entrada");
        entradaButton.setBounds(100, 50, 200, 40);
        add(entradaButton);

        salidaButton = new JButton("Registrar Salida");
        salidaButton.setBounds(100, 110, 200, 40);
        add(salidaButton);

        verHistorialButton = new JButton("Ver Historial de Accesos");
        verHistorialButton.setBounds(100, 170, 200, 40);
        add(verHistorialButton);
        
        regresarButton = new JButton("Regresar");
        regresarButton.setBounds(150, 230, 100, 30);
        add(regresarButton);

        entradaButton.addActionListener(e -> registrarAcceso("Entrada"));
        salidaButton.addActionListener(e -> registrarAcceso("Salida"));
        
        verHistorialButton.addActionListener(e -> {
            java.util.List<Acceso> accesos = BDJSON.obtenerAccesos();
            StringBuilder mensaje = new StringBuilder("Historial de Accesos:\n");

            if (accesos.isEmpty()) {
                mensaje.append("No hay accesos registrados.");
            } else {
                for (Acceso acceso : accesos) {
                    mensaje.append("- ").append(acceso.getNombre()).append(" | ")
                           .append(acceso.getTipo()).append(" | ")
                           .append(acceso.getFechaHora()).append("\n");
                }
            }

            JOptionPane.showMessageDialog(null, mensaje.toString());
        });

        regresarButton.addActionListener(e -> {
            dispose();
            new PaginaPrincipal().setVisible(true);
        });
    }
    
    private void registrarAcceso(String tipo) {
        String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");
        if (nombre != null && !nombre.isEmpty()) {
            String[] opciones = {"Acceso Recta", "Acceso Periférico", "Acceso Gaos", "Acceso Proveedores"};
            String puntoAcceso = (String) JOptionPane.showInputDialog(null, "Seleccione su punto de acceso:",
                    "Punto de Acceso", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            
            if (puntoAcceso != null) {
                String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Acceso acceso = new Acceso(nombre + " - " + puntoAcceso, tipo, fechaHora);
                BDJSON.registrarAcceso(acceso);
                JOptionPane.showMessageDialog(null, tipo + " registrada exitosamente.");
            }
        }
    }
}
