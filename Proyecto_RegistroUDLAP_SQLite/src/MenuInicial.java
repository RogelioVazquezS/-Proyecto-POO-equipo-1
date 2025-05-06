import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

public class MenuInicial extends JFrame {

    public MenuInicial() {
        setTitle("Bienvenido al Sistema UDLAP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        Preferences prefs = Preferences.userRoot().node("sesion_udlap");
        String matricula = prefs.get("matricula_activa", null);

        if (matricula != null) {
            System.out.println("Sesión activa detectada con matrícula: " + matricula);
            new PaginaWebFrame().setVisible(true);
            dispose();
            return;
        }

        JLabel titulo = new JLabel("Sistema UDLAP");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(new Color(0x006341));
        add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel();
        opciones.setBackground(Color.WHITE);
        opciones.setLayout(new GridLayout(2, 1, 20, 20));
        opciones.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(0xFF6F00));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener((ActionEvent e) -> {
            new FormularioLogin().setVisible(true);
            dispose();
        });

        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.setBackground(new Color(0xFF6F00));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.addActionListener((ActionEvent e) -> {
            new FormularioRegistro().setVisible(true);
            dispose();
        });

        JButton btnOlvide = new JButton("¿Olvidaste tu contraseña?");
        btnOlvide.setForeground(Color.BLUE);
        btnOlvide.setBorderPainted(false);
        btnOlvide.setContentAreaFilled(false);
        btnOlvide.setFocusPainted(false);
        btnOlvide.setFont(new Font("Arial", Font.PLAIN, 12));
        btnOlvide.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOlvide.addActionListener(e -> {
            new RecuperarContrasenaFrame().setVisible(true);
        });

        opciones.add(btnLogin);
        opciones.add(btnRegistro);
        opciones.add(btnOlvide);

        add(opciones, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuInicial().setVisible(true);
        });
    }
}
