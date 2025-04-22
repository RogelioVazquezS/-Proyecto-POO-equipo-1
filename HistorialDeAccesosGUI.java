import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HistorialDeAccesosGUI {

    // Lista estática para guardar todos los registros
    private static ArrayList<RegistroRecord> registros = new ArrayList<>();

    // Métodos helper para testing (y para la funcionalidad de la aplicación)
    public static void clearRegistros() {
        registros.clear();
    }

    public static int getRegistrosSize() {
        return registros.size();
    }

    public static void addRegistro(RegistroRecord rr) {
        registros.add(rr);
    }

    public static String generateRegistroListContent() {
        StringBuilder sb = new StringBuilder();
        for (RegistroRecord rr : registros) {
            HistorialDeAccesos acceso = rr.getAcceso();
            if (rr.getTipo().equals("entrada")) {
                sb.append("Entrada: ");
            } else if (rr.getTipo().equals("salida")) {
                sb.append("Salida: ");
            }
            sb.append("Nombre: ").append(acceso.getNombre())
              .append(", Licenciatura: ").append(acceso.getLicenciatura())
              .append(", ID: ").append(acceso.getId())
              .append(", QR: ").append(acceso.tieneQr() ? "Sí" : "No")
              .append("\n");
        }
        return sb.toString();
    }

    // Métodos GUI (no son usados en los tests, pero se mantienen para la aplicación)
    public HistorialDeAccesosGUI() {
        showMainMenu();
    }

    private void showMainMenu() {
        JFrame mainFrame = new JFrame("Sistema de Acceso al Campus - Menú");
        mainFrame.setSize(300, 200);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnEntrada = new JButton("Registrar Entrada");
        JButton btnSalida = new JButton("Registrar Salida");
        JButton btnListado = new JButton("Registro de entradas y salidas");

        btnEntrada.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                showRegistrationForm("entrada");
            }
        });

        btnSalida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                showRegistrationForm("salida");
            }
        });

        btnListado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                showRegistroList();
            }
        });

        mainFrame.add(btnEntrada);
        mainFrame.add(btnSalida);
        mainFrame.add(btnListado);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void showRegistrationForm(String tipo) {
        JFrame frame = new JFrame("Sistema de Acceso al Campus - Registrar " + (tipo.equals("entrada") ? "Entrada" : "Salida"));
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel superior: botones "Regresar a inicio" y "Cerrar Sistema"
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnRegresar = new JButton("Regresar a inicio");
        JButton btnCerrar = new JButton("Cerrar Sistema");
        topPanel.add(btnRegresar);
        topPanel.add(btnCerrar);
        frame.add(topPanel, BorderLayout.NORTH);

        // Panel central: formulario de registro
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        JLabel lblLicenciatura = new JLabel("Licenciatura:");
        JTextField txtLicenciatura = new JTextField();
        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField();
        JLabel lblQr = new JLabel("Código QR:");
        JCheckBox chkQr = new JCheckBox();

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblLicenciatura);
        panel.add(txtLicenciatura);
        panel.add(lblId);
        panel.add(txtId);
        panel.add(lblQr);
        panel.add(chkQr);

        // Botón de registro con etiqueta según el tipo
        JButton btnRegistrar = new JButton(tipo.equals("entrada") ? "Registrar Entrada" : "Registrar Salida");
        panel.add(btnRegistrar);
        panel.add(new JLabel("")); // Espacio vacío para completar la grilla

        frame.add(panel, BorderLayout.CENTER);

        // Área de mensaje
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        frame.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validación de campos vacíos (NUEVO)
                if (txtNombre.getText().trim().isEmpty() 
                        || txtLicenciatura.getText().trim().isEmpty() 
                        || txtId.getText().trim().isEmpty()) {
                    outputArea.setText("Error: Todos los campos deben estar rellenados.");
                    return;
                }
                
                String nombre = txtNombre.getText().trim();
                String licenciatura = txtLicenciatura.getText().trim();
                int id;
                try {
                    id = Integer.parseInt(txtId.getText().trim());
                } catch (NumberFormatException ex) {
                    outputArea.setText("Error: El ID debe ser un número.");
                    return;
                }
                boolean tieneQR = chkQr.isSelected();
                HistorialDeAccesos acceso = new HistorialDeAccesos(nombre, licenciatura, id, tieneQR);
                // Agregar registro
                registros.add(new RegistroRecord(tipo, acceso));
                if (acceso.pasar()) {
                    outputArea.setText(tipo.equals("entrada") ? "Entrada Registrada con éxito" : "Salida registrada con éxito");
                } else {
                    outputArea.setText("Acceso denegado a " + nombre);
                }
            }
        });

        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showMainMenu();
            }
        });

        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showRegistroList() {
        JFrame frame = new JFrame("Registro de Entradas y Salidas");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnRegresar = new JButton("Regresar a inicio");
        JButton btnCerrar = new JButton("Cerrar Sistema");
        topPanel.add(btnRegresar);
        topPanel.add(btnCerrar);
        frame.add(topPanel, BorderLayout.NORTH);

        JTextArea listaArea = new JTextArea();
        listaArea.setEditable(false);
        listaArea.setText(generateRegistroListContent());
        frame.add(new JScrollPane(listaArea), BorderLayout.CENTER);

        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showMainMenu();
            }
        });
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new HistorialDeAccesosGUI();
    }
}