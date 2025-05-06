import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class PanelContacto extends JPanel {

    public PanelContacto() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Contactos Oficiales UDLAP");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0x006341));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelContactos = new JPanel();
        panelContactos.setLayout(new BoxLayout(panelContactos, BoxLayout.Y_AXIS));
        panelContactos.setBackground(Color.WHITE);

        List<Contacto> contactos = Arrays.asList(
            new Contacto("Admisiones", "informes.admisiones@udlap.mx", "(222) 229 2112"),
            new Contacto("Servicios Escolares", "servicios.escolares@udlap.mx", "(222) 229 2000 Ext. 4533"),
            new Contacto("Crédito Educativo", "credito.educativo@udlap.mx", "(222) 229 2010"),
            new Contacto("Departamento de Becas", "informacion.beca@udlap.mx", "(222) 229 2702"),
            new Contacto("Service Desk", "service.desk@udlap.mx", "(222) 229 2127"),
            new Contacto("Rectoría", "rectoria.udlap@udlap.mx", "(222) 229 2275"),
            new Contacto("Consejo Estudiantil", "ejecutivo.ceudlap@udlap.mx", "(222) 229 2072"),
            new Contacto("Colegios Residenciales", "colegios.universitarios@udlap.mx", "(222) 229 2510"),
            new Contacto("Egresados", "comunica.egresados@udlap.mx", "(222) 229 3271"),
            new Contacto("Educación a Distancia", "educacion.adistancia@udlap.mx", "(222) 229 2000 Ext. 4339"),
            new Contacto("Sorteo UDLAP", "sorteo.udlap@udlap.mx", "(222) 229 2105"),
            new Contacto("UDLAP Consultores", "udlap.consultores@udlap.mx", "(222) 229 2713"),
            new Contacto("Verano UDLAP", "verano.udlap@udlap.mx", "(222) 229 2000 Ext. 4828"),
            new Contacto("Web UDLAP", "servicios.web@udlap.mx", "")
        );

        for (Contacto c : contactos) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xFF6F00), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel lblArea = new JLabel("Área: " + c.area);
            lblArea.setFont(new Font("Arial", Font.BOLD, 16));
            lblArea.setForeground(new Color(0x006341));

            JLabel lblCorreo = new JLabel("<html>Correo: <a href='mailto:" + c.correo + "'>" + c.correo + "</a></html>");
            lblCorreo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblCorreo.setFont(new Font("Arial", Font.PLAIN, 14));
            lblCorreo.setForeground(Color.BLUE);
            lblCorreo.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().mail(new URI("mailto:" + c.correo));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "No se pudo abrir el cliente de correo.");
                    }
                }
            });

            JLabel lblTelefono = new JLabel("Teléfono: " + c.telefono);
            lblTelefono.setFont(new Font("Arial", Font.PLAIN, 14));

            panel.add(lblArea);
            panel.add(Box.createVerticalStrut(5));
            panel.add(lblCorreo);
            panel.add(Box.createVerticalStrut(5));
            panel.add(lblTelefono);

            panelContactos.add(panel);
            panelContactos.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(panelContactos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    class Contacto {
        String area;
        String correo;
        String telefono;

        public Contacto(String area, String correo, String telefono) {
            this.area = area;
            this.correo = correo;
            this.telefono = telefono;
        }
    }
}
