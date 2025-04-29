import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaPrincipal extends JFrame {
    private JButton registrarUsuarioButton, registrarAccesoButton, reportarEmergenciaButton, 
                    verHistorialEmergenciasButton, olvideContrasenaButton;

    public PaginaPrincipal() {
        setTitle("Sistema Universitario - Página Principal");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        registrarUsuarioButton = new JButton("Registrar Usuario");
        registrarUsuarioButton.setBounds(100, 30, 200, 40);
        add(registrarUsuarioButton);

        registrarAccesoButton = new JButton("Registrar Acceso");
        registrarAccesoButton.setBounds(100, 90, 200, 40);
        add(registrarAccesoButton);

        reportarEmergenciaButton = new JButton("Reportar Emergencia");
        reportarEmergenciaButton.setBounds(100, 150, 200, 40);
        add(reportarEmergenciaButton);
        
        verHistorialEmergenciasButton = new JButton("Ver Historial Emergencias");
        verHistorialEmergenciasButton.setBounds(100, 210, 200, 40);
        add(verHistorialEmergenciasButton);
        
        olvideContrasenaButton = new JButton("Olvidé mi contraseña");
        olvideContrasenaButton.setBounds(100, 270, 200, 40);
        add(olvideContrasenaButton);

        registrarUsuarioButton.addActionListener(e -> new Main().setVisible(true));
        registrarAccesoButton.addActionListener(e -> new RegistroAcceso().setVisible(true));
        reportarEmergenciaButton.addActionListener(e -> new InterfazEmergencia().setVisible(true));
        verHistorialEmergenciasButton.addActionListener(e -> new HistorialEmergencias().setVisible(true));
        
        olvideContrasenaButton.addActionListener(e -> {
            String correo = JOptionPane.showInputDialog("Ingrese su correo institucional:");
            if (correo != null && !correo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Correo de recuperación enviado.");
            }
        });
    }

    public static void main(String[] args) {
        new PaginaPrincipal().setVisible(true);
    }
}
