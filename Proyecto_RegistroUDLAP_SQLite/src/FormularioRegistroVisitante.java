import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class FormularioRegistroVisitante extends JFrame {

    public FormularioRegistroVisitante() {
        setTitle("Registro de Visitante Externo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));
        getContentPane().setBackground(Color.WHITE);

        JTextField campoNombre = new JTextField();
        JTextField campoApellidos = new JTextField();
        JTextField campoFecha = new JTextField(LocalDate.now().toString());
        JTextField campoHora = new JTextField(LocalTime.now().withSecond(0).withNano(0).toString());
        JTextField campoLugar = new JTextField();
        JComboBox<String> comboVehiculo = new JComboBox<>(new String[]{"Ninguno", "Automóvil", "Motocicleta", "Bicicleta"});
        JTextField campoPlacas = new JTextField();

        JButton btnGenerarQR = new JButton("Generar QR");
        btnGenerarQR.setBackground(new Color(0x006341));
        btnGenerarQR.setForeground(Color.WHITE);

        btnGenerarQR.addActionListener((ActionEvent e) -> {
            String nombre = campoNombre.getText().trim();
            String apellidos = campoApellidos.getText().trim();
            String fecha = campoFecha.getText().trim();
            String hora = campoHora.getText().trim();
            String lugar = campoLugar.getText().trim();
            String tipoVehiculo = comboVehiculo.getSelectedItem().toString();
            String placas = campoPlacas.getText().trim();

            if (nombre.isEmpty() || apellidos.isEmpty() || fecha.isEmpty() || hora.isEmpty() || lugar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos obligatorios deben ser llenados.");
                return;
            }

            String datosQR = String.format("Nombre: %s %s\nFecha: %s\nHora: %s\nLugar: %s\nVehículo: %s\nPlacas: %s",
                    nombre, apellidos, fecha, hora, lugar, tipoVehiculo, placas);

            try {
                // QR
                QRCodeWriter qrWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrWriter.encode(datosQR, BarcodeFormat.QR_CODE, 300, 300);
                File qrDir = new File("qr_visitantes");
                qrDir.mkdirs();
                String nombreArchivo = "qr_visitantes/" + System.currentTimeMillis() + "_" + nombre + ".png";
                File qrFile = new File(nombreArchivo);
                try (FileOutputStream fos = new FileOutputStream(qrFile)) {
                    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", fos);
                }

                // Guardar en la base de datos
                try (Connection conn = ConexionSQLite.conectar()) {
                    String sql = "INSERT INTO accesos_visitantes (nombre, apellidos, fecha, hora, lugar, tipo_vehiculo, placas, qr_generado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nombre);
                    stmt.setString(2, apellidos);
                    stmt.setString(3, fecha);
                    stmt.setString(4, hora);
                    stmt.setString(5, lugar);
                    stmt.setString(6, tipoVehiculo);
                    stmt.setString(7, placas);
                    stmt.setString(8, nombreArchivo);
                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "QR generado y guardado exitosamente.");
                dispose();

            } catch (WriterException we) {
                JOptionPane.showMessageDialog(this, "Error al generar el QR: " + we.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + ex.getMessage());
            }
        });

        add(new JLabel("Nombre:"));
        add(campoNombre);
        add(new JLabel("Apellidos:"));
        add(campoApellidos);
        add(new JLabel("Fecha (YYYY-MM-DD):"));
        add(campoFecha);
        add(new JLabel("Hora (HH:MM):"));
        add(campoHora);
        add(new JLabel("Lugar de acceso:"));
        add(campoLugar);
        add(new JLabel("Tipo de vehículo:"));
        add(comboVehiculo);
        add(new JLabel("Placas (si aplica):"));
        add(campoPlacas);
        add(new JLabel());
        add(btnGenerarQR);
    }
}
