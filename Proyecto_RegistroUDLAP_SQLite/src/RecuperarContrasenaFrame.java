import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class RecuperarContrasenaFrame extends JFrame {
    public RecuperarContrasenaFrame() {
        setTitle("Recuperar Contraseña");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));

        JTextField campoMatricula = new JTextField();
        JTextField campoCorreo = new JTextField();
        JPasswordField campoNueva = new JPasswordField();
        JPasswordField campoConfirmar = new JPasswordField();

        add(new JLabel("Matrícula:"));
        add(campoMatricula);
        add(new JLabel("Correo registrado:"));
        add(campoCorreo);
        add(new JLabel("Nueva contraseña:"));
        add(campoNueva);
        add(new JLabel("Confirmar contraseña:"));
        add(campoConfirmar);

        JButton btnCambiar = new JButton("Cambiar contraseña");
        add(btnCambiar);

        btnCambiar.addActionListener(e -> {
            String matricula = campoMatricula.getText().trim();
            String correo = campoCorreo.getText().trim();
            String nueva = new String(campoNueva.getPassword());
            String confirmar = new String(campoConfirmar.getPassword());

            if (!nueva.equals(confirmar)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
                return;
            }

            try (Connection conn = ConexionSQLite.conectar()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM usuarios WHERE matricula = ? AND correo = ?"
                );
                stmt.setString(1, matricula);
                stmt.setString(2, correo);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    PreparedStatement update = conn.prepareStatement(
                        "UPDATE usuarios SET contraseña = ? WHERE matricula = ?"
                    );
                    update.setString(1, nueva);
                    update.setString(2, matricula);
                    update.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Datos incorrectos.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
