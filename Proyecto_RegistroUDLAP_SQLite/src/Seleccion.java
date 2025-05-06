import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Seleccion extends JFrame {
    private JLabel lblConfirmacion;
    private JButton btnConfirmar;

    public Seleccion() {
        setTitle("Selección de emergencia");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Seleccione el tipo de emergencia requerido:"));
        add(panelSuperior, BorderLayout.NORTH);

        
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(3, 1)); 

        JButton btnServiciosMedicos = new JButton("Servicios Médicos");
        JButton btnSeguridad = new JButton("Seguridad");
        JButton btnProtocoloGenero = new JButton("Protocolo de género");

        panelCentral.add(btnServiciosMedicos);
        panelCentral.add(btnSeguridad);
        panelCentral.add(btnProtocoloGenero);

        add(panelCentral, BorderLayout.CENTER);

        
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(4, 1));

        lblConfirmacion = new JLabel("", SwingConstants.CENTER);
        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setVisible(false); 

        panelInferior.add(lblConfirmacion);
        panelInferior.add(btnConfirmar);

        add(panelInferior, BorderLayout.SOUTH);

        
        ActionListener mostrarMensaje = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblConfirmacion.setText("Presiona Confirmar para enviar la solicitud");
                btnConfirmar.setVisible(true); // Mostrar botón de Confirmar
            }
        };

        btnServiciosMedicos.addActionListener(mostrarMensaje);
        btnSeguridad.addActionListener(mostrarMensaje);
        btnProtocoloGenero.addActionListener(mostrarMensaje);

        
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblConfirmacion.setText("Solicitud enviada");
                btnConfirmar.setVisible(false); // Ocultar botón después de confirmar
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Seleccion());
    }
}
