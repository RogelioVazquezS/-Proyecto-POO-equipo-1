import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class InterfazEmergencyGUI extends JFrame {
    private List<GestorEmergencias> emergencias = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> emergencyList = new JList<>(listModel);
    private JTextField txtCategoria, txtUbicacion, txtId;
    private JComboBox<String> cmbPrioridad, cmbNuevaPrioridad;
    private JButton btnAgregar, btnMarcarCompletada, btnCambiarPrioridad;

    public InterfazEmergencyGUI() {
        setTitle("Gestor de Emergencias - UDLAP");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelRegistro = new JPanel(new GridLayout(5, 2, 5, 5));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registrar Emergencia"));

        txtCategoria = new JTextField();
        txtUbicacion = new JTextField();
        txtId = new JTextField();
        cmbPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});

        btnAgregar = new JButton("Agregar Emergencia");
        btnAgregar.addActionListener(e -> agregarEmergencia());

        panelRegistro.add(new JLabel("Categoría:"));
        panelRegistro.add(txtCategoria);
        panelRegistro.add(new JLabel("Ubicación:"));
        panelRegistro.add(txtUbicacion);
        panelRegistro.add(new JLabel("ID:"));
        panelRegistro.add(txtId);
        panelRegistro.add(new JLabel("Prioridad:"));
        panelRegistro.add(cmbPrioridad);
        panelRegistro.add(new JLabel());
        panelRegistro.add(btnAgregar);

        JScrollPane scrollPane = new JScrollPane(emergencyList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Emergencias Registradas"));

        JPanel panelAcciones = new JPanel(new GridLayout(1, 3, 5, 5));
        btnMarcarCompletada = new JButton("Marcar Completada");
        btnCambiarPrioridad = new JButton("Cambiar Prioridad");
        cmbNuevaPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});

        btnMarcarCompletada.addActionListener(e -> marcarCompletada());
        btnCambiarPrioridad.addActionListener(e -> cambiarPrioridad());

        panelAcciones.add(btnMarcarCompletada);
        panelAcciones.add(cmbNuevaPrioridad);
        panelAcciones.add(btnCambiarPrioridad);

        add(panelRegistro, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void agregarEmergencia() {
        String categoria = txtCategoria.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String idTexto = txtId.getText().trim();
        String prioridad = (String) cmbPrioridad.getSelectedItem();

        if (categoria.isEmpty() || ubicacion.isEmpty() || idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
            return;
        }

       //Verificar emergencias duplicadas
        for (int i = 0; i < emergencias.size(); i++) {
            GestorEmergencias emergencia = emergencias.get(i);
            if (emergencia.getCategoria().equalsIgnoreCase(categoria) &&
                emergencia.getUbicacion().equalsIgnoreCase(ubicacion) &&
                emergencia.getPrioridad().equalsIgnoreCase(prioridad)) {
                JOptionPane.showMessageDialog(this, "La emergencia ya esta registrada.");
                return;
            }
        }

        
        emergencias.add(new GestorEmergencias(categoria, ubicacion, id, prioridad));
        actualizarLista();

        // Limpiar campos
        txtCategoria.setText("");
        txtUbicacion.setText("");
        txtId.setText("");
    }

    private void marcarCompletada() {
        int index = emergencyList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una emergencia.");
            return;
        }
        emergencias.get(index).marcarCompletada();
        actualizarLista();
    }

    private void cambiarPrioridad() {
        int index = emergencyList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una emergencia.");
            return;
        }
        String nuevaPrioridad = (String) cmbNuevaPrioridad.getSelectedItem();
        emergencias.get(index).setPrioridad(nuevaPrioridad);
        actualizarLista();
    }

    private void actualizarLista() {
        listModel.clear();
        for (int i = 0; i < emergencias.size(); i++) {
            listModel.addElement(emergencias.get(i).toString());
        }
    }

}
