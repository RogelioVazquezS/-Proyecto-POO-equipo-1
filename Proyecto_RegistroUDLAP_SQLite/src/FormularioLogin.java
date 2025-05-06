import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class FormularioLogin extends JFrame {

    public FormularioLogin() {
        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));
        getContentPane().setBackground(Color.WHITE);

        JTextField campoMatricula = new JTextField();
        JPasswordField campoPassword = new JPasswordField();

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(0xFF6F00));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.addActionListener((ActionEvent e) -> {
            String matriculaStr = campoMatricula.getText().trim();
            String contraseña = new String(campoPassword.getPassword());

            // Validación de matrícula 
            if (!matriculaStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "La matrícula debe ser numérica.");
                return;
            }

            int matricula = Integer.parseInt(matriculaStr);

            try (Connection conn = ConexionSQLite.conectar()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Error de conexión: la base de datos no fue encontrada.");
                    return;
                }

                String sql = "SELECT * FROM usuarios WHERE matricula = ? AND contraseña = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, matricula);
                stmt.setString(2, contraseña);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");

                    // Guardar sesión
                    Preferences prefs = Preferences.userRoot().node("sesion_udlap");
                    prefs.put("matricula_activa", String.valueOf(matricula));

                    new PaginaWebFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Matrícula o contraseña incorrectos.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + ex.getMessage());
            }
        });

        add(new JLabel("Matrícula:"));
        add(campoMatricula);
        add(new JLabel("Contraseña:"));
        add(campoPassword);
        add(btnEntrar);
    }
}
