import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FormularioRegistro extends JFrame {

    public FormularioRegistro() {
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));
        getContentPane().setBackground(Color.WHITE);

        JTextField campoMatricula = new JTextField();
        JTextField campoNombre = new JTextField();
        JTextField campoCorreo = new JTextField();
        JPasswordField campoPassword = new JPasswordField();

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBackground(new Color(0xFF6F00));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener((ActionEvent e) -> {
            String matriculaStr = campoMatricula.getText().trim();
            String nombre = campoNombre.getText().trim();
            String correo = campoCorreo.getText().trim();
            String contraseña = new String(campoPassword.getPassword());

            // Validación de matrícula numérica
            if (!matriculaStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "La matrícula debe ser numérica.");
                return;
            }

            int matricula = Integer.parseInt(matriculaStr);

            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "INSERT INTO usuarios (matricula, nombre, correo, contraseña) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, matricula);
                stmt.setString(2, nombre);
                stmt.setString(3, correo);
                stmt.setString(4, contraseña);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.");
                new PaginaWebFrame().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        add(new JLabel("Matrícula (solo números):"));
        add(campoMatricula);
        add(new JLabel("Nombre completo:"));
        add(campoNombre);
        add(new JLabel("Correo:"));
        add(campoCorreo);
        add(new JLabel("Contraseña:"));
        add(campoPassword);
        add(btnRegistrar);
    }
}
