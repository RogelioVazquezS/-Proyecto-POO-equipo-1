import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registro extends JPanel {
    private JTextField txtMatricula;
    private JPasswordField txtPassword;
    private JLabel lblMessage;

    public Registro() {
        setSize(350, 400);
        
        setLayout(new GridLayout(3, 3));

        
        add(new JLabel("ID:"));
        txtMatricula = new JTextField();
        add(txtMatricula);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnRegister = new JButton("Registrar");
        lblMessage = new JLabel("");
        add(btnRegister);
        add(lblMessage);

        
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarRegistro();
            }
        });

        setVisible(true);
    }

    public void validarRegistro() {
        String matricula = txtMatricula.getText();
        String password = new String(txtPassword.getPassword());

        if (matricula.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Todos los campos son obligatorios.");
        } else {
            lblMessage.setText("Registro exitoso.");
        }
    }


    public String getValidationMessage() {
        return lblMessage.getText();
    }


    public void setFields(String matricula, String password) {
        txtMatricula.setText(matricula);
        txtPassword.setText(password);
    }

    public void clickRegisterButton() {
        validarRegistro();
    }

   


}