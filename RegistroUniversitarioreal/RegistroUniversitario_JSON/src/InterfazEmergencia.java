import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InterfazEmergencia extends JFrame {
    private JButton seguridadButton;
    private JButton emergenciaGeneralButton;
    private JButton protocoloGeneroButton;
    private JButton regresarButton;

    public InterfazEmergencia() {
        setTitle("Reportar Emergencia UDLAP");
        setSize(400, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        seguridadButton = new JButton("Emergencia de Seguridad UDLAP");
        seguridadButton.setBounds(80, 50, 250, 40);
        add(seguridadButton);

        emergenciaGeneralButton = new JButton("Emergencias UDLAP");
        emergenciaGeneralButton.setBounds(80, 110, 250, 40);
        add(emergenciaGeneralButton);

        protocoloGeneroButton = new JButton("Protocolo de Género UDLAP");
        protocoloGeneroButton.setBounds(80, 170, 250, 40);
        add(protocoloGeneroButton);
        
        regresarButton = new JButton("Regresar");
        regresarButton.setBounds(150, 230, 100, 30);
        add(regresarButton);

        seguridadButton.addActionListener(e -> reportarEmergencia("Emergencia de Seguridad UDLAP"));
        emergenciaGeneralButton.addActionListener(e -> reportarEmergencia("Emergencias UDLAP"));
        protocoloGeneroButton.addActionListener(e -> reportarEmergencia("Protocolo de Género UDLAP"));

        regresarButton.addActionListener(e -> {
            dispose();
            new PaginaPrincipal().setVisible(true);
        });
    }

    private void reportarEmergencia(String tipo) {
        String ubicacion = JOptionPane.showInputDialog("Ingrese su ubicación:");
        if (ubicacion != null && !ubicacion.isEmpty()) {
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Emergencia emergencia = new Emergencia(tipo, ubicacion, "Alta", fechaHora);
            BDJSON.agregarEmergencia(emergencia);
            JOptionPane.showMessageDialog(null, "Emergencia recibida, en proceso de atenderte.");
        }
    }
}
