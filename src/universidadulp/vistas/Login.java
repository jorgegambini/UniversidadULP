package universidadulp.vistas;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static universidadulp.connection.DatabaseConnection.getInstance;
import universidadulp.entidades.Usuario;
import universidadulp.repositorio.*;
import universidadulp.event.LoginListener;

public class Login extends javax.swing.JInternalFrame {

    private LoginListener loginListener;
    private UsuarioRepositorio ur;

    public Login() {
        
        ur = new UsuarioRepositorio();
        
        initComponents();
        init();
        
        setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));
        
        addSelectAllOnFocusToTextFields(this);
        
    }

    private void init() {

        cmdLogin.addActionListener((e) -> {

            try (Connection conn = getInstance();) {

                if (ur.login(txtUserName.getText(), new String(txtPassword.getPassword()), chkRememberMe.isSelected())!= null) {

                   Usuario usu = ur.login(txtUserName.getText(), new String(txtPassword.getPassword()), chkRememberMe.isSelected());

                    dispose();
                    notifyLoginSuccess(usu.getIdUsuario(), usu.getUsuario().toUpperCase());
                    notifyMethodExecution(); 

                } else {
                    
                    JOptionPane.showConfirmDialog(this, "Error de Logueo", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                    pasarFoco(txtUserName);

                }

            } catch (SQLException ex) {

            }

        });
        
        txtUserName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                buscarUsuario();
            }
        });

        txtUserName.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarUsuario();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarUsuario();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarUsuario();
            }
        });

    }
    
    private void buscarUsuario() {

        try (Connection conn = getInstance();) {

            Usuario usu = ur.buscarPorUsuario(txtUserName.getText());
            
            if (usu != null) {
           
                
                if (usu.isRemember()) {

                    chkRememberMe.setSelected(true);
                    txtPassword.setText(usu.getPassword());

                }


            } else {

                chkRememberMe.setSelected(false);
                txtPassword.setText("");

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblApe = new javax.swing.JLabel();
        lblNom = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        cmdLogin = new javax.swing.JButton();
        chkRememberMe = new javax.swing.JCheckBox();
        txtPassword = new javax.swing.JPasswordField();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        lblApe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApe.setText("USUARIO");

        lblNom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNom.setText("PASSWORD");

        cmdLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Edit_File_16.png"))); // NOI18N
        cmdLogin.setText("Iniciar Sesión");

        chkRememberMe.setText("Recordarme");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblApe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(chkRememberMe, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(117, 117, 117))
                            .addComponent(txtPassword))))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApe)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNom)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(chkRememberMe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(cmdLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        
        pasarFoco(txtUserName);
        
    }//GEN-LAST:event_formInternalFrameOpened

    public static void addSelectAllOnFocusToTextFields(Container container) {
        
        for (Component component : container.getComponents()) {
            
            if (component instanceof JTextField) {
                
                JTextField textField = (JTextField) component;
                
                textField.addFocusListener(new FocusAdapter() {
                    
                    @Override
                    public void focusGained(FocusEvent e) {
                        
                        textField.selectAll();
                        
                    }
                });
                
            } else if (component instanceof Container) {
                
                // Si es un contenedor, busca campos de texto dentro del contenedor
                addSelectAllOnFocusToTextFields((Container) component);
                
            }
        }
    }

    private void pasarFoco(Component com) {

        SwingUtilities.invokeLater(() -> {

            com.requestFocusInWindow();

//                if (com instanceof JTextField) {
//                    ((JTextField) com).selectAll();
//                }
        });

    }

    public void setLoginListener(LoginListener listener) {

        this.loginListener = listener;

    }

    private void notifyLoginSuccess(int idUser, String Username) {

        if (loginListener != null) {

            loginListener.onLoginSuccess(idUser,Username);

        }

    }

    private void notifyMethodExecution() {

        if (loginListener != null) {

            loginListener.onMethodExecution();

        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkRememberMe;
    private javax.swing.JButton cmdLogin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblApe;
    private javax.swing.JLabel lblNom;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
