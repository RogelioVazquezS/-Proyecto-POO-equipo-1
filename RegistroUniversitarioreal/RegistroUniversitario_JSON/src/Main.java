import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main extends JFrame {
    private JButton registrarUsuarioButton;
    private JButton verUsuariosButton;

    public Main() {
        setTitle("Sistema Universitario");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        registrarUsuarioButton = new JButton("Registrar Usuario");
        registrarUsuarioButton.setBounds(100, 50, 200, 40);
        add(registrarUsuarioButton);

        verUsuariosButton = new JButton("Ver Usuarios Registrados");
        verUsuariosButton.setBounds(100, 120, 200, 40);
        add(verUsuariosButton);

        registrarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = JOptionPane.showInputDialog("Ingrese nombre de usuario:");
                String contrasena = JOptionPane.showInputDialog("Ingrese contraseña:");
                String correo = JOptionPane.showInputDialog("Ingrese su correo institucional:");

                if (nombre != null && contrasena != null && correo != null && 
                    !nombre.isEmpty() && !contrasena.isEmpty() && !correo.isEmpty()) {
                    Usuario usuario = new Usuario(nombre, contrasena, correo);
                    BDJSON.agregarUsuario(usuario);
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Debe ingresar datos válidos");
                }
            }
        });

        verUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<Usuario> usuarios = BDJSON.obtenerUsuarios();
                StringBuilder mensaje = new StringBuilder("Usuarios Registrados:\n");

                if (usuarios.isEmpty()) {
                    mensaje.append("No hay usuarios registrados.");
                } else {
                    for (Usuario usuario : usuarios) {
                        mensaje.append("- ").append(usuario.getNombre()).append(" | ")
                               .append(usuario.getCorreoInstitucional()).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(null, mensaje.toString());
            }
        });
    }
}
