import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.prefs.Preferences;

public class FormularioCitaMedica extends JFrame {

    public FormularioCitaMedica() {
        setTitle("Agendar Cita Médica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        getContentPane().setBackground(Color.WHITE);

        String[] especialidades = {
            "Consulta General",
            "Medicina Interna",
            "Dermatología",
            "Psicología",
            "Enfermería",
            "Nutrición"
        };

        JComboBox<String> comboEspecialidad = new JComboBox<>(especialidades);
        JTextField campoFecha = new JTextField("2024-05-01"); // yyyy-mm-dd
        JTextField campoHora = new JTextField("09:00");

        JButton btnAgendar = new JButton("Agendar Cita");
        btnAgendar.setBackground(new Color(0xFF6F00));
        btnAgendar.setForeground(Color.WHITE);

        btnAgendar.addActionListener((ActionEvent e) -> {
            String especialidad = comboEspecialidad.getSelectedItem().toString();
            String fecha = campoFecha.getText().trim();
            String hora = campoHora.getText().trim();

            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}") || !hora.matches("\\d{2}:\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Formato de fecha u hora incorrecto.");
                return;
            }

            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
            if (matricula == null) {
                JOptionPane.showMessageDialog(this, "Sesión no válida.");
                return;
            }

            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "INSERT INTO citas_medicas (matricula, especialidad, fecha, hora) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(matricula));
                stmt.setString(2, especialidad);
                stmt.setString(3, fecha);
                stmt.setString(4, hora);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Cita agendada correctamente.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agendar cita: " + ex.getMessage());
            }
        });

        add(new JLabel("Especialidad:"));
        add(comboEspecialidad);
        add(new JLabel("Fecha (YYYY-MM-DD):"));
        add(campoFecha);
        add(new JLabel("Hora (HH:MM):"));
        add(campoHora);
        add(btnAgendar);
    }
}
