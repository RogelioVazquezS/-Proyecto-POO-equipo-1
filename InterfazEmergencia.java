import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;

public class InterfazEmergencia {
    private static java.util.List<String> registros = new ArrayList<>();
    private static Map<Integer, String> credenciales = new HashMap<>();
    private static Usuario usuarioActual;
    private static String codigoRecuperacion;

    public InterfazEmergencia() {
        mostrarLoginRegistro();
    }

    private void mostrarLoginRegistro() {
        JFrame frame = new JFrame("Inicio de Sesión");
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(7, 2, 5, 5));

        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        JLabel lblContrasena = new JLabel("Contraseña:");
        JPasswordField txtContrasena = new JPasswordField();
        JLabel lblMensaje = new JLabel("");
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegistro = new JButton("Registrarse");

        frame.add(lblId);
        frame.add(txtId);
        frame.add(lblContrasena);
        frame.add(txtContrasena);
        frame.add(lblMensaje);
        frame.add(new JLabel(""));
        frame.add(btnLogin);
        frame.add(btnRegistro);

        btnLogin.addActionListener(e -> {
            int id;
            try {
                id = Integer.parseInt(txtId.getText().trim());
            } catch (NumberFormatException ex) {
                lblMensaje.setText("El ID debe ser un número.");
                return;
            }
            String contrasena = new String(txtContrasena.getPassword());

            if (credenciales.containsKey(id) && credenciales.get(id).equals(contrasena)) {
                usuarioActual = new Usuario("Usuario", id, contrasena);
                frame.dispose();
                mostrarBotonEmergencia(false);
            } else {
                lblMensaje.setText("Contraseña incorrecta.");
                mostrarOpcionesRecuperacion();
            }
        });

        btnRegistro.addActionListener(e -> {
            int id;
            try {
                id = Integer.parseInt(txtId.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String contrasena = new String(txtContrasena.getPassword());
            credenciales.put(id, contrasena);
            JOptionPane.showMessageDialog(frame, "Usuario registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarOpcionesRecuperacion() {
        JFrame frame = new JFrame("Recuperación de Cuenta");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnAutoridades = new JButton("Contactar Autoridades");
        JButton btnCodigo = new JButton("Enviar Código de Emergencia");

        btnAutoridades.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Las autoridades están en camino.", "Alerta", JOptionPane.WARNING_MESSAGE));

        btnCodigo.addActionListener(e -> {
            codigoRecuperacion = String.valueOf(new Random().nextInt(9000) + 1000);
            JOptionPane.showMessageDialog(frame, "Código de emergencia: " + codigoRecuperacion, "Código Generado", JOptionPane.INFORMATION_MESSAGE);
            verificarCodigo();
            frame.dispose();
        });

        frame.add(btnAutoridades);
        frame.add(btnCodigo);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void verificarCodigo() {
        JFrame frame = new JFrame("Verificar Código");
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(2, 1, 5, 5));

        JTextField txtCodigo = new JTextField();
        JButton btnVerificar = new JButton("Verificar");

        btnVerificar.addActionListener(e -> {
            if (txtCodigo.getText().equals(codigoRecuperacion)) {
                JOptionPane.showMessageDialog(frame, "Código correcto. Acceso permitido.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                mostrarBotonEmergencia(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Código incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(txtCodigo);
        frame.add(btnVerificar);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarBotonEmergencia(boolean accesoPorCodigo) {
        JFrame frame = new JFrame("Botón de Emergencia");
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(2, 1, 10, 10));

        JButton btnEmergencia = new JButton("¡Presionar Botón de Emergencia!");
        JButton btnHistorial = new JButton("Ver Historial");

        btnEmergencia.addActionListener(e -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (accesoPorCodigo) {
                registros.add("Código: " + codigoRecuperacion + " - Fecha: " + timestamp);
            } else {
                registros.add("Usuario: " + usuarioActual.getNombre() + " - ID: " + usuarioActual.getId() + " - Fecha: " + timestamp);
            }
            JOptionPane.showMessageDialog(frame, "Botón de emergencia presionado.", "Alerta", JOptionPane.WARNING_MESSAGE);
        });

        btnHistorial.addActionListener(e -> mostrarHistorial());

        frame.add(btnEmergencia);
        frame.add(btnHistorial);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void mostrarHistorial() {
        JFrame frame = new JFrame("Historial de Accesos");
        frame.setSize(400, 300);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder historial = new StringBuilder();
        for (String registro : registros) {
            historial.append(registro).append("\n");
        }

        textArea.setText(historial.toString());
        frame.add(new JScrollPane(textArea));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new InterfazEmergencia();
    }
}