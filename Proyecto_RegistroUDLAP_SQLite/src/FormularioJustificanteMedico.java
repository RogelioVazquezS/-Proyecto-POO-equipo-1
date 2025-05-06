import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.prefs.Preferences;

public class FormularioJustificanteMedico extends JFrame {

    private File archivoSeleccionado = null;

    public FormularioJustificanteMedico() {
        setTitle("Subir Justificante Médico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        getContentPane().setBackground(Color.WHITE);

        JTextField campoMotivo = new JTextField();
        JTextField campoFecha = new JTextField(LocalDate.now().toString());

        JButton btnSeleccionarPDF = new JButton("Seleccionar PDF");
        JLabel lblArchivo = new JLabel("Ningún archivo seleccionado");

        btnSeleccionarPDF.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int opcion = fileChooser.showOpenDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                archivoSeleccionado = fileChooser.getSelectedFile();
                lblArchivo.setText("Archivo: " + archivoSeleccionado.getName());
            }
        });

        JButton btnSubir = new JButton("Subir Justificante");
        btnSubir.setBackground(new Color(0xFF6F00));
        btnSubir.setForeground(Color.WHITE);

        btnSubir.addActionListener((ActionEvent e) -> {
            String motivo = campoMotivo.getText().trim();
            String fecha = campoFecha.getText().trim();

            if (motivo.isEmpty() || fecha.isEmpty() || archivoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido (YYYY-MM-DD).");
                return;
            }

            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
            if (matricula == null) {
                JOptionPane.showMessageDialog(this, "Sesión no válida.");
                return;
            }

            try {
                // Copiar archivo PDF al directorio local
                String destino = "justificantes/" + System.currentTimeMillis() + "_" + archivoSeleccionado.getName();
                File destinoFile = new File(destino);
                destinoFile.getParentFile().mkdirs();
                Files.copy(archivoSeleccionado.toPath(), destinoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                try (Connection conn = ConexionSQLite.conectar()) {
                    String sql = "INSERT INTO justificantes_medicos (matricula, motivo, fecha, archivo_pdf) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(matricula));
                    stmt.setString(2, motivo);
                    stmt.setString(3, fecha);
                    stmt.setString(4, destino);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Justificante registrado correctamente.");
                    dispose();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al subir justificante: " + ex.getMessage());
            }
        });

        add(new JLabel("Motivo del justificante:"));
        add(campoMotivo);
        add(new JLabel("Fecha (YYYY-MM-DD):"));
        add(campoFecha);
        add(btnSeleccionarPDF);
        add(lblArchivo);
        add(btnSubir);
    }
}
