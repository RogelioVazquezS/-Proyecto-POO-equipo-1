import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.prefs.Preferences;
import java.net.URI;
import java.awt.event.MouseAdapter;




public class PaginaWebFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private JLabel lblFotoPerfil;
    private JLabel lblFotoSimulada;

    private String matriculaActiva;


    public static String obtenerRutaFoto(String matricula) {
        try (Connection conn = ConexionSQLite.conectar()) {
            String sql = "SELECT foto_perfil FROM usuarios WHERE matricula = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(matricula));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String ruta = rs.getString("foto_perfil");
                if (ruta != null && new File(ruta).exists()) {
                    System.out.println("Ruta obtenida de BD: " + ruta);
                    return ruta.replace("\\", "/"); 
                }
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo ruta de foto: " + e.getMessage());
           

        }
        return null;
    }

    private ImageIcon getImagenEscalada(String ruta, int tamaño) {
        try {
            Image original = new ImageIcon(ruta).getImage().getScaledInstance(tamaño, tamaño, Image.SCALE_SMOOTH);
            return new ImageIcon(original);
        } catch (Exception e) {
            return null;
        }
    }
    
    public PaginaWebFrame() {
        // Crear carpeta 
        File directorioFotos = new File("fotos");
        if (!directorioFotos.exists()) {
            directorioFotos.mkdirs();
        }
    
        setTitle("Registro Universitario UDLAP - Formato Web");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    
        
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(new Color(0x006341));
        barraSuperior.setPreferredSize(new Dimension(900, 80));
    
        // Imagen de Logo
        ImageIcon logoIcon = new ImageIcon("UDLAP.jpg");
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH)));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        barraSuperior.add(logoLabel, BorderLayout.WEST);
    
        
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        botonera.setOpaque(false);
    
        String[] secciones = {"Inicio", "Información", "Contacto", "Servicios Médicos", "Accesos", "Emergencias"};
        for (String seccion : secciones) {
            JButton btn = new JButton(seccion);
            btn.setBackground(new Color(0xFF6F00));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.addActionListener(new CambiarPanelListener(seccion));
            botonera.add(btn);
        }
    
        
        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.setBackground(Color.GRAY);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener((ActionEvent e) -> {
            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            prefs.remove("matricula_activa");
            dispose();
            new MenuInicial().setVisible(true);
        });
        botonera.add(btnSalir);
    
        
        Preferences prefs = Preferences.userRoot().node("sesion_udlap");
        matriculaActiva = prefs.get("matricula_activa", "Desconocido");
    
        JLabel lblUsuario = new JLabel("Matrícula: " + matriculaActiva);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));

        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblFotoPerfil.setPreferredSize(new Dimension(50, 50)); 
        actualizarImagenPerfil(matriculaActiva);

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.add(botonera, BorderLayout.CENTER);
        panelDerecho.add(lblUsuario, BorderLayout.SOUTH);
        panelDerecho.add(lblFotoPerfil, BorderLayout.EAST);
    
        barraSuperior.add(panelDerecho, BorderLayout.CENTER);
    
        
        panelContenido = new JPanel();
        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);
    
        panelContenido.add(crearPanelSimulado("Bienvenido al sistema"), "Inicio");
        panelContenido.add(new PanelContacto(), "Contacto");
        panelContenido.add(crearPanelServiciosMedicos(), "Servicios Médicos");
        panelContenido.add(crearPanelAccesos(), "Accesos");
        panelContenido.add(crearPanelEmergencias(), "Emergencias");
        panelContenido.add(crearPanelInformacion(), "Información"); 
    
        add(barraSuperior, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);
    }
    
        private JPanel crearPanelSimulado(String titulo) {
    JPanel panel = new JPanel();
    panel.setBackground(Color.WHITE);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel labelTitulo = new JLabel(titulo);
    labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
    labelTitulo.setForeground(new Color(0x006341));
    labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(Box.createVerticalStrut(20));
    panel.add(labelTitulo);
    panel.add(Box.createVerticalStrut(20));

    Preferences prefs = Preferences.userRoot().node("sesion_udlap");
    String matricula = prefs.get("matricula_activa", null);


    try {
        File logoFile = new File("udlap_grande.png"); 
if (logoFile.exists()) {
    ImageIcon logo = new ImageIcon(logoFile.getAbsolutePath());
    Image scaledLogo = logo.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
    JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
    logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoLabel.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        URI uri = new URI("https://www.udlap.mx");
                        desktop.browse(uri);
                    } else {
                        JOptionPane.showMessageDialog(null, "Tu sistema no soporta abrir el navegador desde Java.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Tu sistema no soporta Desktop API.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No se pudo abrir el navegador: " + ex.getMessage());
            }
        }
    });
    panel.add(logoLabel);
    panel.add(Box.createVerticalStrut(20));
}

             } catch (Exception e) {
            e.printStackTrace();
            }

    
            if (matricula != null) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT nombre, correo FROM usuarios WHERE matricula = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(matricula));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JLabel lblMatricula = new JLabel("Matrícula: " + matricula);
                JLabel lblNombre = new JLabel("Nombre completo: " + rs.getString("nombre"));
                JLabel lblCorreo = new JLabel("Correo: " + rs.getString("correo"));

                lblMatricula.setFont(new Font("Arial", Font.PLAIN, 18));
                lblNombre.setFont(new Font("Arial", Font.PLAIN, 18));
                lblCorreo.setFont(new Font("Arial", Font.PLAIN, 18));

                lblMatricula.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);

                panel.add(lblMatricula);
                panel.add(Box.createVerticalStrut(10));
                panel.add(lblNombre);
                panel.add(Box.createVerticalStrut(10));
                panel.add(lblCorreo);
            } else {
                panel.add(new JLabel("⚠️ No se encontró el usuario."));
            }
            } catch (Exception ex) {
            ex.printStackTrace();
            JLabel error = new JLabel("❌ Error al cargar los datos.");
            error.setForeground(Color.RED);
            error.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(error);
                }
             } else {
        panel.add(new JLabel("⚠️ No hay sesión activa."));
            }

            return panel;
        }


        private JPanel crearPanelInformacion() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);
        
            JLabel titulo = new JLabel("Información del Usuario", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 24));
            titulo.setForeground(new Color(0x006341));
            panel.add(titulo, BorderLayout.NORTH);
        
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;
        
            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
        
            JTextField campoNombre = new JTextField(20); campoNombre.setEditable(false);
            JTextField campoCorreo = new JTextField(20); campoCorreo.setEditable(false);
            JTextField campoTelefono = new JTextField(20);
            JTextField campoMatricula = new JTextField(20); campoMatricula.setText(matricula); campoMatricula.setEditable(false);
            JLabel fotoLabel = new JLabel();
        
            
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT nombre, correo, telefono, foto_perfil FROM usuarios WHERE matricula = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(matricula));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    campoNombre.setText(rs.getString("nombre"));
                    campoCorreo.setText(rs.getString("correo"));
                    campoTelefono.setText(rs.getString("telefono"));
        
                    String rutaFoto = obtenerRutaFoto(matricula);
                    if (rutaFoto != null) {
                        fotoLabel.setIcon(getImagenEscalada(rutaFoto, 100));
                    }
                    
                    
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "Error al cargar información: " + e.getMessage());
            }
        
            JButton btnActualizar = new JButton("Actualizar Teléfono");
            btnActualizar.setBackground(new Color(0xFF6F00));
            btnActualizar.setForeground(Color.WHITE);
        
            JButton btnSubirFoto = new JButton("Subir Foto de Perfil");
            btnSubirFoto.setBackground(new Color(0x006341));
            btnSubirFoto.setForeground(Color.WHITE);

            JButton btnCambiarPassword = new JButton("Cambiar Contraseña");
            btnCambiarPassword.setBackground(Color.GRAY);
            btnCambiarPassword.setForeground(Color.WHITE);
        
            
            gbc.gridx = 0; gbc.gridy = 0; infoPanel.add(new JLabel("Nombre completo:"), gbc);
            gbc.gridx = 1; infoPanel.add(campoNombre, gbc);
            gbc.gridx = 2; gbc.gridheight = 4; infoPanel.add(fotoLabel, gbc); gbc.gridheight = 1;
        
            gbc.gridx = 0; gbc.gridy = 1; infoPanel.add(new JLabel("Correo institucional:"), gbc);
            gbc.gridx = 1; infoPanel.add(campoCorreo, gbc);
        
            gbc.gridx = 0; gbc.gridy = 2; infoPanel.add(new JLabel("Teléfono:"), gbc);
            gbc.gridx = 1; infoPanel.add(campoTelefono, gbc);
        
            gbc.gridx = 0; gbc.gridy = 3; infoPanel.add(new JLabel("Matrícula:"), gbc);
            gbc.gridx = 1; infoPanel.add(campoMatricula, gbc);
        
            gbc.gridx = 0; gbc.gridy = 4; infoPanel.add(btnActualizar, gbc);
            gbc.gridx = 1; infoPanel.add(btnSubirFoto, gbc);

            gbc.gridx = 2;  gbc.gridy = 4; infoPanel.add(btnCambiarPassword, gbc);
        
            panel.add(infoPanel, BorderLayout.CENTER);
        
            
            btnActualizar.addActionListener(e -> {
                String nuevoTelefono = campoTelefono.getText().trim();
                if (!nuevoTelefono.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(panel, "Número inválido. Usa 10 dígitos.");
                    return;
                }
                try (Connection conn = ConexionSQLite.conectar()) {
                    String updateSql = "UPDATE usuarios SET telefono = ? WHERE matricula = ?";
                    PreparedStatement stmt = conn.prepareStatement(updateSql);
                    stmt.setString(1, nuevoTelefono);
                    stmt.setInt(2, Integer.parseInt(matricula));
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Teléfono actualizado.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            });
        
            btnSubirFoto.addActionListener(e -> {
                JFileChooser selector = new JFileChooser();
                int resultado = selector.showOpenDialog(panel);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivo = selector.getSelectedFile();
                    String extension = archivo.getName().substring(archivo.getName().lastIndexOf("."));
                    File destino = new File("fotos/" + matricula + extension);
                    try {
                        java.nio.file.Files.copy(archivo.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        try (Connection conn = ConexionSQLite.conectar()) {
                            PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET foto_perfil = ? WHERE matricula = ?");
                            stmt.setString(1, destino.getPath().replace("\\", "/")); // <--- NORMALIZA la ruta aquí
                            stmt.setInt(2, Integer.parseInt(matricula));
                            stmt.executeUpdate();
                            actualizarImagenPerfil(matricula);
                            JOptionPane.showMessageDialog(panel, "Foto actualizada.");
                            fotoLabel.setIcon(getImagenEscalada(destino.getPath(), 100));
                            actualizarImagenPerfil(matricula);
                            actualizarImagenSimulada(matricula);
                            panelContenido.remove(0); 
                            panelContenido.add(crearPanelSimulado("Bienvenido al sistema"), "Inicio"); 
                            cardLayout.show(panelContenido, "Información"); 

                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Error al subir foto: " + ex.getMessage());
                    }
                }
            });

            btnCambiarPassword.addActionListener(e -> {
                JPasswordField actualField = new JPasswordField(15);
                JPasswordField nuevaField = new JPasswordField(15);
                JPasswordField confirmarField = new JPasswordField(15);
            
                JPanel cambioPanel = new JPanel(new GridLayout(3, 2));
                cambioPanel.add(new JLabel("Contraseña actual:"));
                cambioPanel.add(actualField);
                cambioPanel.add(new JLabel("Nueva contraseña:"));
                cambioPanel.add(nuevaField);
                cambioPanel.add(new JLabel("Confirmar nueva:"));
                cambioPanel.add(confirmarField);
            
                int option = JOptionPane.showConfirmDialog(panel, cambioPanel, "Cambiar Contraseña", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String actual = new String(actualField.getPassword());
                    String nueva = new String(nuevaField.getPassword());
                    String confirmar = new String(confirmarField.getPassword());
            
                    if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "No dejes campos vacíos.");
                        return;
                    }
                    if (!nueva.equals(confirmar)) {
                        JOptionPane.showMessageDialog(panel, "Las nuevas contraseñas no coinciden.");
                        return;
                    }
            
                    try (Connection conn = ConexionSQLite.conectar()) {
                        PreparedStatement stmt = conn.prepareStatement("SELECT contraseña FROM usuarios WHERE matricula = ?");
                        stmt.setInt(1, Integer.parseInt(matricula));
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            String passBD = rs.getString("contraseña");
                            if (!actual.equals(passBD)) {
                                JOptionPane.showMessageDialog(panel, "Contraseña actual incorrecta.");
                                return;
                            }
            
                            PreparedStatement update = conn.prepareStatement("UPDATE usuarios SET contraseña = ? WHERE matricula = ?");
                            update.setString(1, nueva);
                            update.setInt(2, Integer.parseInt(matricula));
                            update.executeUpdate();
                            JOptionPane.showMessageDialog(panel, "Contraseña actualizada correctamente.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Error al cambiar contraseña: " + ex.getMessage());
                    }
                }
            });

            
            
        
            return panel;
        }
        
        
        private JPanel crearPanelServiciosMedicos() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);
        
            JLabel titulo = new JLabel("Módulo de Servicios Médicos", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 24));
            titulo.setForeground(new Color(0x006341));
            panel.add(titulo, BorderLayout.NORTH);
        
            JPanel tablasPanel = new JPanel(new GridLayout(2, 1, 10, 10));
            tablasPanel.setBackground(Color.WHITE);
        
            // ----------------------- CITAS MÉDICAS -----------------------
            JPanel panelCitas = new JPanel(new BorderLayout());
            panelCitas.setBackground(Color.WHITE);
        
            JPanel filtroCitas = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filtroCitas.setBackground(Color.WHITE);
            JTextField campoFechaCitas = new JTextField(10);
            JButton btnBuscarCitas = new JButton("Buscar por fecha");
            filtroCitas.add(new JLabel("Filtrar Citas por Fecha:"));
            filtroCitas.add(campoFechaCitas);
            filtroCitas.add(btnBuscarCitas);
            panelCitas.add(filtroCitas, BorderLayout.NORTH);
        
            String[] columnasCitas = {"ID", "Especialidad", "Fecha", "Hora", "Estado"};
            DefaultTableModel modeloCitas = new DefaultTableModel(columnasCitas, 0);
            JTable tablaCitas = new JTable(modeloCitas);
            JScrollPane scrollCitas = new JScrollPane(tablaCitas);
            panelCitas.add(scrollCitas, BorderLayout.CENTER);
            cargarCitasMedicas(modeloCitas);
        
            // ----------------------- JUSTIFICANTES -----------------------
            JPanel panelJust = new JPanel(new BorderLayout());
            panelJust.setBackground(Color.WHITE);
        
            JPanel filtroJust = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filtroJust.setBackground(Color.WHITE);
            JTextField campoFechaJust = new JTextField(10);
            JButton btnBuscarJust = new JButton("Buscar por fecha");
            filtroJust.add(new JLabel("Filtrar Justificantes por Fecha:"));
            filtroJust.add(campoFechaJust);
            filtroJust.add(btnBuscarJust);
            panelJust.add(filtroJust, BorderLayout.NORTH);
        
            String[] columnasJust = {"ID", "Motivo", "Fecha", "Archivo PDF"};
            DefaultTableModel modeloJust = new DefaultTableModel(columnasJust, 0);
            JTable tablaJust = new JTable(modeloJust);
            JScrollPane scrollJust = new JScrollPane(tablaJust);
            panelJust.add(scrollJust, BorderLayout.CENTER);
            cargarJustificantesMedicos(modeloJust);
        
            // selección de PDF's
            tablaJust.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int fila = tablaJust.getSelectedRow();
                        if (fila != -1) {
                            String rutaArchivo = (String) modeloJust.getValueAt(fila, 3);
                            try {
                                File archivo = new File(rutaArchivo);
                                if (archivo.exists()) {
                                    Desktop.getDesktop().open(archivo);
                                } else {
                                    JOptionPane.showMessageDialog(panel, "Archivo no encontrado.");
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panel, "No se pudo abrir el archivo.");
                            }
                        }
                    }
                }
            });
        
            // ----------------------- BOTONES -----------------------
            JPanel pie = new JPanel();
            pie.setBackground(Color.WHITE);
        
            JButton btnCita = new JButton("Agendar Nueva Cita");
            btnCita.setBackground(new Color(0xFF6F00));
            btnCita.setForeground(Color.WHITE);
            btnCita.addActionListener(e -> new FormularioCitaMedica().setVisible(true));
        
            JButton btnJustificante = new JButton("Subir Justificante Médico");
            btnJustificante.setBackground(new Color(0x006341));
            btnJustificante.setForeground(Color.WHITE);
            btnJustificante.addActionListener(e -> new FormularioJustificanteMedico().setVisible(true));
        
            JButton btnEliminarCita = new JButton("Eliminar Cita Seleccionada");
            btnEliminarCita.setBackground(Color.RED);
            btnEliminarCita.setForeground(Color.WHITE);
            btnEliminarCita.addActionListener(e -> {
                int fila = tablaCitas.getSelectedRow();
                if (fila != -1) {
                    int id = (int) modeloCitas.getValueAt(fila, 0);
                    int confirm = JOptionPane.showConfirmDialog(panel, "¿Eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection conn = ConexionSQLite.conectar()) {
                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM citas_medicas WHERE id_cita = ?");
                            stmt.setInt(1, id);
                            stmt.executeUpdate();
                            cargarCitasMedicas(modeloCitas);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Error al eliminar cita.");
                        }
                    }
                }
            });
        
            JButton btnEliminarJust = new JButton("Eliminar Justificante Seleccionado");
            btnEliminarJust.setBackground(Color.RED);
            btnEliminarJust.setForeground(Color.WHITE);
            btnEliminarJust.addActionListener(e -> {
                int fila = tablaJust.getSelectedRow();
                if (fila != -1) {
                    int id = (int) modeloJust.getValueAt(fila, 0);
                    int confirm = JOptionPane.showConfirmDialog(panel, "¿Eliminar este justificante?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection conn = ConexionSQLite.conectar()) {
                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM justificantes_medicos WHERE id_justificante = ?");
                            stmt.setInt(1, id);
                            stmt.executeUpdate();
                            cargarJustificantesMedicos(modeloJust);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Error al eliminar justificante.");
                        }
                    }
                }
            });
        
            pie.add(btnCita);
            pie.add(btnJustificante);
            pie.add(btnEliminarCita);
            pie.add(btnEliminarJust);
        
            
            panelCitas.add(pie, BorderLayout.SOUTH);
            tablasPanel.add(panelCitas);
            tablasPanel.add(panelJust);
        
            panel.add(tablasPanel, BorderLayout.CENTER);
        
            // Filtro de búsqueda por fecha
            btnBuscarCitas.addActionListener(e -> {
                String fecha = campoFechaCitas.getText().trim();
                if (fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    cargarCitasMedicasPorFecha(modeloCitas, fecha);
                } else {
                    JOptionPane.showMessageDialog(panel, "Formato de fecha inválido.");
                }
            });
        
            btnBuscarJust.addActionListener(e -> {
                String fecha = campoFechaJust.getText().trim();
                if (fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    cargarJustificantesPorFecha(modeloJust, fecha);
                } else {
                    JOptionPane.showMessageDialog(panel, "Formato de fecha inválido.");
                }
            });
        
            return panel;
        }
        
        private void cargarCitasMedicas(DefaultTableModel modelo) {
            modelo.setRowCount(0);
            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
            if (matricula == null) return;
        
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT * FROM citas_medicas WHERE matricula = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(matricula));
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_cita"),
                        rs.getString("especialidad"),
                        rs.getString("fecha"),
                        rs.getString("hora"),
                        rs.getString("estado")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar citas: " + ex.getMessage());
            }
        }
        
        private void cargarJustificantesMedicos(DefaultTableModel modelo) {
            modelo.setRowCount(0);
            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
            if (matricula == null) return;
        
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT * FROM justificantes_medicos WHERE matricula = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(matricula));
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_justificante"),
                        rs.getString("motivo"),
                        rs.getString("fecha"),
                        rs.getString("archivo_pdf")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar justificantes: " + ex.getMessage());
            }
        }
        
        private void cargarCitasMedicasPorFecha(DefaultTableModel modelo, String fecha) {
            modelo.setRowCount(0);
            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
            if (matricula == null) return;
        
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT * FROM citas_medicas WHERE matricula = ? AND fecha = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(matricula));
                stmt.setString(2, fecha);
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_cita"),
                        rs.getString("especialidad"),
                        rs.getString("fecha"),
                        rs.getString("hora"),
                        rs.getString("estado")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al filtrar citas: " + ex.getMessage());
            }
        }
        
        private void cargarJustificantesPorFecha(DefaultTableModel modelo, String fecha) {
            modelo.setRowCount(0);
            Preferences prefs = Preferences.userRoot().node("sesion_udlap");
            String matricula = prefs.get("matricula_activa", null);
            if (matricula == null) return;
        
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT * FROM justificantes_medicos WHERE matricula = ? AND fecha = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(matricula));
                stmt.setString(2, fecha);
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("id_justificante"),
                        rs.getString("motivo"),
                        rs.getString("fecha"),
                        rs.getString("archivo_pdf")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al filtrar justificantes: " + ex.getMessage());
            }
        }
        
        private class CambiarPanelListener implements ActionListener {
            private String nombrePanel;
        
            public CambiarPanelListener(String nombrePanel) {
                this.nombrePanel = nombrePanel;
            }
        
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, nombrePanel);
            }
        }
        
        private JPanel crearPanelAccesos() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);
        
            JLabel titulo = new JLabel("Registro de Accesos - Visitantes Externos", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 24));
            titulo.setForeground(new Color(0x006341));
            panel.add(titulo, BorderLayout.NORTH);
        
            // Botón registrar visitante
            JButton btnRegistrarVisitante = new JButton("Registrar Visitante y Generar QR");
            btnRegistrarVisitante.setFont(new Font("Arial", Font.PLAIN, 18));
            btnRegistrarVisitante.setBackground(new Color(0xFF6F00));
            btnRegistrarVisitante.setForeground(Color.WHITE);
            btnRegistrarVisitante.addActionListener(e -> new FormularioRegistroVisitante().setVisible(true));
        
             // Tabla historial QR
             String[] columnas = {"Nombre", "Apellidos", "Fecha", "Hora", "Lugar", "Vehículo", "Placas", "QR"};
             DefaultTableModel modeloQR = new DefaultTableModel(columnas, 0);
             JTable tablaQR = new JTable(modeloQR);
             JScrollPane scrollQR = new JScrollPane(tablaQR);
             panel.add(scrollQR, BorderLayout.CENTER);

            JButton btnAbrirQR = new JButton("Abrir QR Seleccionado");
            btnAbrirQR.setBackground(Color.DARK_GRAY);
            btnAbrirQR.setForeground(Color.WHITE);

        btnAbrirQR.addActionListener(e -> {
        int fila = tablaQR.getSelectedRow();
        if (fila != -1) {
        String archivoQR = (String) modeloQR.getValueAt(fila, 7);
        try {
            File qr = new File(archivoQR);
            System.out.println("Intentando abrir: " + qr.getAbsolutePath());
            if (qr.exists()) {
                Desktop.getDesktop().open(qr);
            } else {
                JOptionPane.showMessageDialog(panel, "Archivo no encontrado.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, "No se pudo abrir el archivo QR.");
        }
    } else {
        JOptionPane.showMessageDialog(panel, "Selecciona una fila primero.");
    }
    });
            JPanel superior = new JPanel();
            superior.setBackground(Color.WHITE);
            superior.add(btnRegistrarVisitante);
            superior.add(btnAbrirQR);
            panel.add(superior, BorderLayout.NORTH);
        
           
        
            cargarHistorialQR(modeloQR);
        
            // Doble clic para abrir QR
            tablaQR.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int fila = tablaQR.getSelectedRow();
                        if (fila != -1) {
                            String archivoQR = (String) modeloQR.getValueAt(fila, 7);
                            try {
                                File qr = new File(archivoQR);
                                if (qr.exists()) {
                                    Desktop.getDesktop().open(qr);
                                } else {
                                    JOptionPane.showMessageDialog(panel, "Archivo no encontrado.");
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panel, "No se pudo abrir el archivo QR.");
                            }
                        }
                    }
                }
            });
        
    
            
    JPanel panelMiembros = new JPanel(new BorderLayout());
        panelMiembros.setBackground(Color.WHITE);

    JLabel lblMiembros = new JLabel("Registro de Entradas y Salidas UDLAP", SwingConstants.CENTER);
    lblMiembros.setFont(new Font("Arial", Font.BOLD, 20));
    lblMiembros.setForeground(new Color(0x006341));
    panelMiembros.add(lblMiembros, BorderLayout.NORTH);

    // Panel de entrada
    JPanel formPanel = new JPanel();
    formPanel.setBackground(Color.WHITE);
    formPanel.setLayout(new FlowLayout());

    // Filtro de fecha
    JTextField campoFechaFiltro = new JTextField(10);
    campoFechaFiltro.setToolTipText("Formato: YYYY-MM-DD");
    JButton btnBuscarFecha = new JButton("Buscar por Fecha");

    formPanel.add(new JLabel("Filtrar por fecha:"));
    formPanel.add(campoFechaFiltro);
    formPanel.add(btnBuscarFecha);

    JTextField campoMatricula = new JTextField(10);
    String[] lugares = {"Acceso Recta", "Acceso Periférico", "Acceso Gaos", "Acceso Distribuidores"};
    JComboBox<String> comboLugar = new JComboBox<>(lugares);
    JButton btnRegistrar = new JButton("Registrar Acceso");
    btnRegistrar.setBackground(new Color(0xFF6F00));
    btnRegistrar.setForeground(Color.WHITE);

    formPanel.add(new JLabel("Matrícula:"));
    formPanel.add(campoMatricula);
    formPanel.add(new JLabel("Lugar de acceso:"));
    formPanel.add(comboLugar);
    formPanel.add(btnRegistrar);
    panelMiembros.add(formPanel, BorderLayout.CENTER);

    // Tabla historial de accesos UDLAP
    String[] columnasHistorial = {"Matrícula", "Lugar", "Fecha", "Entrada", "Salida"};
    DefaultTableModel modeloHistorial = new DefaultTableModel(columnasHistorial, 0);
    JTable tablaHistorial = new JTable(modeloHistorial);
    JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
    panelMiembros.add(scrollHistorial, BorderLayout.SOUTH);

    cargarHistorialEntradasSalidas(modeloHistorial);
    // Acción del botón
    btnRegistrar.addActionListener(e -> {
        String matricula = campoMatricula.getText().trim();
        String lugar = comboLugar.getSelectedItem().toString();

        if (!matricula.matches("\\d+")) {
        JOptionPane.showMessageDialog(panel, "La matrícula debe ser numérica.");
        return;
        }

        registrarEntradaSalida(Integer.parseInt(matricula), lugar, modeloHistorial);
        });
        
        btnBuscarFecha.addActionListener(e -> {
            String fecha = campoFechaFiltro.getText().trim();
            if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(panel, "Formato de fecha inválido (usa YYYY-MM-DD).");
                return;
            }
            cargarHistorialPorFecha(modeloHistorial, fecha);
        });
        
        panel.add(panelMiembros, BorderLayout.SOUTH);
        return panel;
    }

        private JPanel crearPanelEmergencias() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);
        
            JLabel titulo = new JLabel("Registro de Emergencias", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 24));
            titulo.setForeground(new Color(0xB71C1C));
            panel.add(titulo, BorderLayout.NORTH);
        
            JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
            form.setBackground(Color.WHITE);
        
            JComboBox<String> comboCategoria = new JComboBox<>(new String[]{
                "Médica", "Seguridad", "Protocolo de Género"
            });
        
            JTextArea campoDescripcion = new JTextArea(3, 20);
            campoDescripcion.setLineWrap(true);
            campoDescripcion.setWrapStyleWord(true);
            JScrollPane scrollDescripcion = new JScrollPane(campoDescripcion);
        
            JCheckBox checkUrgente = new JCheckBox("¿Requiere atención inmediata?");
            checkUrgente.setBackground(Color.WHITE);
        
            JButton btnRegistrar = new JButton("Registrar Emergencia");
            btnRegistrar.setBackground(new Color(0xB71C1C));
            btnRegistrar.setForeground(Color.WHITE);
        
            form.add(new JLabel("Categoría:"));
            form.add(comboCategoria);
            form.add(new JLabel("Descripción:"));
            form.add(scrollDescripcion);
            form.add(new JLabel(""));
            form.add(checkUrgente);
            form.add(new JLabel(""));
            form.add(btnRegistrar);
        
            panel.add(form, BorderLayout.NORTH);
        

            // Filtros
            JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelFiltros.setBackground(Color.WHITE);

            JComboBox<String> comboFiltroCategoria = new JComboBox<>(new String[]{
            "Todas", "Médica", "Seguridad", "Protocolo de Género"
            });
            JTextField campoFiltroFecha = new JTextField(10);
            campoFiltroFecha.setToolTipText("YYYY-MM-DD");

            JButton btnFiltrar = new JButton("Aplicar Filtros");
            btnFiltrar.setBackground(new Color(0x006341));
            btnFiltrar.setForeground(Color.WHITE);

            panelFiltros.add(new JLabel("Categoría:"));
            panelFiltros.add(comboFiltroCategoria);
            panelFiltros.add(new JLabel("Fecha:"));
            panelFiltros.add(campoFiltroFecha);
            panelFiltros.add(btnFiltrar);

            panel.add(panelFiltros, BorderLayout.SOUTH);

            // Tabla de historial
            String[] columnas = {"Categoría", "Descripción", "Atención", "Fecha", "Hora"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
            JTable tabla = new JTable(modelo);
            JScrollPane scroll = new JScrollPane(tabla);
            panel.add(scroll, BorderLayout.CENTER);
        
            cargarHistorialEmergencias(modelo);
        
            // Acción del botón
            btnRegistrar.addActionListener(e -> {
                String categoria = comboCategoria.getSelectedItem().toString();
                String descripcion = campoDescripcion.getText().trim();
                String requiere = checkUrgente.isSelected() ? "Sí" : "No";
                String fecha = java.time.LocalDate.now().toString();
                String hora = java.time.LocalTime.now().withSecond(0).withNano(0).toString();
        
                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "La descripción no puede estar vacía.");
                    return;
                }
        
                try (Connection conn = ConexionSQLite.conectar()) {
                    String sql = "INSERT INTO emergencias (categoria, descripcion, requiere_atencion, fecha, hora) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, categoria);
                    stmt.setString(2, descripcion);
                    stmt.setString(3, requiere);
                    stmt.setString(4, fecha);
                    stmt.setString(5, hora);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Emergencia registrada correctamente.");
                    campoDescripcion.setText("");
                    checkUrgente.setSelected(false);
                    cargarHistorialEmergencias(modelo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al registrar: " + ex.getMessage());
                }
            });

            btnFiltrar.addActionListener(e -> {
                String categoria = comboFiltroCategoria.getSelectedItem().toString();
                String fecha = campoFiltroFecha.getText().trim();
                if (!fecha.isEmpty() && !fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(panel, "Fecha inválida. Usa formato YYYY-MM-DD.");
                    return;
                }
                cargarHistorialFiltrado(modelo, categoria, fecha);
            });
            
        
            return panel;
        }
    
        
    


        
        private void cargarHistorialQR(DefaultTableModel modelo) {
            modelo.setRowCount(0);
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT nombre, apellidos, fecha, hora, lugar, tipo_vehiculo, placas, qr_generado FROM accesos_visitantes";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("fecha"),
                        rs.getString("hora"),
                        rs.getString("lugar"),
                        rs.getString("tipo_vehiculo"),
                        rs.getString("placas"),
                        rs.getString("qr_generado")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar historial de accesos: " + ex.getMessage());
            }
        }
        
        private void registrarEntradaSalida(int matricula, String lugar, DefaultTableModel modelo) {
            try (Connection conn = ConexionSQLite.conectar()) {
                String fecha = java.time.LocalDate.now().toString();
                String horaActual = java.time.LocalTime.now().withSecond(0).withNano(0).toString();
        
                // Verificar si ya hay una entrada sin salida hoy
                String sqlVerificar = "SELECT * FROM historial_accesos_udlap WHERE matricula = ? AND fecha = ? AND hora_salida IS NULL";
                PreparedStatement verificar = conn.prepareStatement(sqlVerificar);
                verificar.setInt(1, matricula);
                verificar.setString(2, fecha);
                ResultSet rs = verificar.executeQuery();
        
                if (rs.next()) {
                    // Ya hay una entrada sin salida → registrar salida
                    int id = rs.getInt("id_acceso");
                    String sqlSalida = "UPDATE historial_accesos_udlap SET hora_salida = ? WHERE id_acceso = ?";
                    PreparedStatement update = conn.prepareStatement(sqlSalida);
                    update.setString(1, horaActual);
                    update.setInt(2, id);
                    update.executeUpdate();
        
                    JOptionPane.showMessageDialog(this, "Salida registrada correctamente.");
                } else {
                    // No hay entrada previa hoy → registrar nueva entrada
                    String sqlEntrada = "INSERT INTO historial_accesos_udlap (matricula, lugar, fecha, hora_entrada) VALUES (?, ?, ?, ?)";
                    PreparedStatement insert = conn.prepareStatement(sqlEntrada);
                    insert.setInt(1, matricula);
                    insert.setString(2, lugar);
                    insert.setString(3, fecha);
                    insert.setString(4, horaActual);
                    insert.executeUpdate();
        
                    JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.");
                }
        
                cargarHistorialEntradasSalidas(modelo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar acceso: " + ex.getMessage());
            }
        }
        

        private void cargarHistorialEntradasSalidas(DefaultTableModel modelo) {
            modelo.setRowCount(0);
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT * FROM historial_accesos_udlap ORDER BY fecha DESC, hora_entrada DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("matricula"),
                        rs.getString("lugar"),
                        rs.getString("fecha"),
                        rs.getString("hora_entrada"),
                        rs.getString("hora_salida")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
            }
        }
        
        private void cargarHistorialPorFecha(DefaultTableModel modelo, String fecha) {
            modelo.setRowCount(0);
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT * FROM historial_accesos_udlap WHERE fecha = ? ORDER BY hora_entrada DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, fecha);
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getInt("matricula"),
                        rs.getString("lugar"),
                        rs.getString("fecha"),
                        rs.getString("hora_entrada"),
                        rs.getString("hora_salida")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al filtrar historial: " + ex.getMessage());
            }
        }       
        
        private void cargarHistorialEmergencias(DefaultTableModel modelo) {
            modelo.setRowCount(0);
            try (Connection conn = ConexionSQLite.conectar()) {
                String sql = "SELECT categoria, descripcion, requiere_atencion, fecha, hora FROM emergencias ORDER BY fecha DESC, hora DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getString("requiere_atencion"),
                        rs.getString("fecha"),
                        rs.getString("hora")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
            }
        }

        private void cargarHistorialFiltrado(DefaultTableModel modelo, String categoria, String fecha) {
            modelo.setRowCount(0);
            try (Connection conn = ConexionSQLite.conectar()) {
                StringBuilder sql = new StringBuilder("SELECT * FROM emergencias WHERE 1=1");
                if (!categoria.equals("Todas")) {
                    sql.append(" AND categoria = ?");
                }
                if (!fecha.isEmpty()) {
                    sql.append(" AND fecha = ?");
                }
                sql.append(" ORDER BY fecha DESC, hora DESC");
        
                PreparedStatement stmt = conn.prepareStatement(sql.toString());
                int index = 1;
                if (!categoria.equals("Todas")) {
                    stmt.setString(index++, categoria);
                }
                if (!fecha.isEmpty()) {
                    stmt.setString(index, fecha);
                }
        
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getString("requiere_atencion"),
                        rs.getString("fecha"),
                        rs.getString("hora")
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al aplicar filtros: " + ex.getMessage());
            }
        }

        private void actualizarImagenPerfil(String matricula) {
            String rutaFoto = obtenerRutaFoto(matricula);
            if (rutaFoto != null) {
                lblFotoPerfil.setIcon(getImagenEscalada(rutaFoto, 50));
            } else {
                lblFotoPerfil.setIcon(null);
            }
            lblFotoPerfil.revalidate();
            lblFotoPerfil.repaint();
        }

        private void actualizarImagenSimulada(String matricula) {
            String rutaFoto = obtenerRutaFoto(matricula);
            if (rutaFoto != null) {
                lblFotoSimulada.setIcon(getImagenEscalada(rutaFoto, 100));
            } else {
                lblFotoSimulada.setIcon(null);
            }
            lblFotoSimulada.revalidate();
            lblFotoSimulada.repaint();
        }
        
    } 
