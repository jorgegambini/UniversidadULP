package universidadulp.vistas;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static universidadulp.connection.DatabaseConnection.getInstance;
import universidadulp.entidades.TipoUsuario;
import universidadulp.entidades.Usuario;
import universidadulp.repositorio.TipoUsuarioRepositorio;
import universidadulp.repositorio.UsuarioRepositorio;

public class ABMUsuarios extends javax.swing.JInternalFrame {

    private UsuarioRepositorio ur;
    private TipoUsuarioRepositorio tr;
    private Usuario usu;
    private int estado;
    private TipoUsuario tipoSeleccionado;

    public ABMUsuarios() {

        ur = new UsuarioRepositorio();
        tr = new TipoUsuarioRepositorio();
        tipoSeleccionado = new TipoUsuario();
        
        setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));

        estado = 0;

        initComponents();

        init();

    }

    private void init() {

        getTipos();

        limpiarDatosUsuarios();

        cmbTipos.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    //// Obtiene el ítem seleccionado
                    tipoSeleccionado = (TipoUsuario) cmbTipos.getSelectedItem();

                }
            }
        });

        cmdNew.addActionListener((e) -> {

            estado = 1;

            limpiarCampos();
            estadosCampos(false, true, true, true, true, true, true, true, true, true, true);
            estadosBotones(false, false, false, true, true, false);

        });

        cmdEdit.addActionListener((e) -> {

            estado = 2;

            estadosCampos(false, true, true, true, true, true, true, true, true, true, true);
            estadosBotones(false, false, false, true, true, false);

        });

        cmdDelete.addActionListener((e) -> {

            if (JOptionPane.showConfirmDialog(this, "Esta Seguro que desea eliminar el Usuario?", "Eliminar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {

                int idUsuario = Integer.parseInt(txtIdUsuario.getText());
                limpiarDatosUsuarios();

                try (Connection conn = getInstance();) {

                    int result = ur.eliminarPorId(idUsuario);

                    if (result == 1) {

                        JOptionPane.showConfirmDialog(this, "El Usuario fue eliminado Correctamente", "Usuario Eliminado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    } else {

                        JOptionPane.showConfirmDialog(this, "El Usuario fue eliminado Anteriormente", "Usuario Eliminado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    }

                } catch (SQLException ex) {

                    JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                } finally {

                    estado = 0;

                }
            }

        });

        cmdSearch.addActionListener((e) -> {

            if (validarCodigo(txtIdUsuario.getText())) {

                try (Connection conn = getInstance();) {

                    if (ur.buscarPorId(Integer.parseInt(txtIdUsuario.getText())) != null) {

                        usu = ur.buscarPorId(Integer.parseInt(txtIdUsuario.getText()));

                        restaurarDatosUsuarios();

                    } else {

                        txtIdUsuario.setText("0");
                        JOptionPane.showConfirmDialog(this, "ID Inexistente en la BD.", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                    }
                } catch (SQLException ex) {

                    limpiarDatosUsuarios();
                    JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                } finally {

                    estado = 0;
                }

            } else {

                JOptionPane.showConfirmDialog(this, "Ingrese un Id Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

            }

        });

        cmdCancel.addActionListener((e) -> {

            switch (estado) {

                case 1:
                    limpiarDatosUsuarios();
                    break;

                case 2:
                    restaurarDatosUsuarios();
                    break;

                default:
                    limpiarDatosUsuarios();
                    break;

            }

            estado = 0;

        });

        cmdSave.addActionListener((e) -> {

            if (!validarNomApe(txtFirstName.getText())) {

                JOptionPane.showConfirmDialog(this, "Ingrese un Nombre Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                return;

            }

            if (!validarNomApe(txtLastName.getText())) {

                JOptionPane.showConfirmDialog(this, "Ingrese un Apellido Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                return;

            }

            if (!validarUser(txtUsername.getText())) {

                JOptionPane.showConfirmDialog(this, "Ingrese un Usuario Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                return;

            }

            if (new String(txtPassword.getPassword()).isBlank() || new String(txtConfirmPass.getPassword()).isBlank()) {

                JOptionPane.showConfirmDialog(this, "Los Pass no pueden estar vacios", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                return;

            }

            if (!new String(txtPassword.getPassword()).equalsIgnoreCase(new String(txtConfirmPass.getPassword()))) {

                JOptionPane.showConfirmDialog(this, "Los Passwords no coinciden", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                return;

            }

            if (estado == 1) {

                usu = new Usuario(txtFirstName.getText(), txtLastName.getText(), optMale.isSelected(), tipoSeleccionado, txtUsername.getText(), new String(txtPassword.getPassword()), chkRememberMe.isSelected(), chkState.isSelected());

            } else if (estado == 2) {

                usu = new Usuario(Integer.parseInt(txtIdUsuario.getText()), txtFirstName.getText(), txtLastName.getText(), optMale.isSelected(), tipoSeleccionado, txtUsername.getText(), new String(txtPassword.getPassword()), chkRememberMe.isSelected(), chkState.isSelected());

            }

            limpiarDatosUsuarios();

            try (Connection conn = getInstance();) {

                if (ur.guardar(usu) != null) {

                    if (estado == 1) {

                        JOptionPane.showConfirmDialog(this, "El Usuario fue agregado correctamente", "Usuario Agregado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    } else if (estado == 2) {

                        JOptionPane.showConfirmDialog(this, "El Usuario fue modificado correctamente", "Usuario Modificado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    }

                }

            } catch (SQLException ex) {

                JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

            } finally {

                estado = 0;

            }

        });

        cmdExit.addActionListener((e) -> dispose());

        addSelectAllOnFocusToTextFields(this);

    }

    private void getTipos() {

        cmbTipos.removeAllItems();

        try (Connection conn = getInstance();) {

            tr.buscarTodos().forEach(tipo -> cmbTipos.addItem(tipo));

            if (cmbTipos.getItemCount() > 0) {

                tipoSeleccionado = (TipoUsuario) cmbTipos.getSelectedItem();

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        }

    }

    private void limpiarDatosUsuarios() {

        limpiarCampos();
        estadosCampos(true, false, false, false, false, false, false, false, false, false, false);
        estadosBotones(true, false, false, false, false, true);

    }

    private void restaurarDatosUsuarios() {

        estadosBotones(true, true, true, false, true, false);
        estadosCampos(false, false, false, false, false, false, false, false, false, false, false);
        txtIdUsuario.setText("" + usu.getIdUsuario());
        txtLastName.setText(usu.getApellido());
        txtFirstName.setText(usu.getNombre());
        optMale.setSelected(usu.isGenero());
        optFemale.setSelected(!usu.isGenero());
        cmbTipos.setSelectedIndex(usu.getTipoUsuario().getIdTipoUsuario() - 1);
        txtUsername.setText(usu.getUsuario());
        txtPassword.setText(usu.getPassword());
        txtConfirmPass.setText(usu.getPassword());
        chkRememberMe.setSelected(usu.isRemember());
        chkState.setSelected(usu.isActivo());
        cmdDelete.setEnabled(usu.isActivo());

    }

    private void estadosBotones(boolean btnNuevo, boolean btnEditar, boolean btnEliminar, boolean btnGuardar, boolean btnCancelar, boolean btnBuscar) {

        cmdNew.setEnabled(btnNuevo);
        cmdEdit.setEnabled(btnEditar);
        cmdDelete.setEnabled(btnEliminar);
        cmdSave.setEnabled(btnGuardar);
        cmdCancel.setEnabled(btnCancelar);
        cmdSearch.setEnabled(btnBuscar);

    }

    private void estadosCampos(boolean tfIdUsuario, boolean tfApellido, boolean tfNombre, boolean tfImage, boolean tfGenero, boolean tfTipoUsuario, boolean tfUsuario, boolean tfPass, boolean tfConfPass, boolean ckRemember, boolean ckActivo) {

        txtIdUsuario.setEnabled(tfIdUsuario);
        txtLastName.setEnabled(tfApellido);
        txtFirstName.setEnabled(tfNombre);
        optMale.setEnabled(tfGenero);
        optFemale.setEnabled(tfGenero);
        cmbTipos.setEnabled(tfTipoUsuario);
        txtUsername.setEnabled(tfUsuario);
        txtPassword.setEnabled(tfPass);
        txtConfirmPass.setEnabled(tfConfPass);
        chkRememberMe.setEnabled(ckRemember);
        chkState.setEnabled(ckActivo);

    }

    private void limpiarCampos() {

        txtIdUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        txtIdUsuario.setText("0");
        txtLastName.setText("");
        txtFirstName.setText("");
        optMale.setSelected(true);
        cmbTipos.setSelectedIndex(0);
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPass.setText("");
        chkRememberMe.setSelected(false);
        chkState.setSelected(true);

    }

    private boolean validarCodigo(String idMateria) {

        return idMateria.matches("^[1-9]\\d{0,6}$");

    }

    private boolean validarNomApe(String nomApe) {

        return nomApe.matches("^([A-ZÁÉÍÓÚÜÑ]([.]|[a-záéíóüñ]+))([ ][A-ZÁÉÍÓÚÜÑ]([.]|[a-záéíóüñ]+)){0,5}");

    }

    private boolean validarUser(String nomApe) {

        return nomApe.matches("^([A-ZÁÉÍÓÚÜÑ]|[a-záéíóüñ])[a-záéíóüñ]+");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupGender = new javax.swing.ButtonGroup();
        chkRememberMe = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        lblDni = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        cmdSearch = new javax.swing.JButton();
        lblApe = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        lblNom = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        chkState = new javax.swing.JCheckBox();
        optMale = new javax.swing.JRadioButton();
        optFemale = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        cmbTipos = new javax.swing.JComboBox<>();
        lblApe1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblNom1 = new javax.swing.JLabel();
        lblApe2 = new javax.swing.JLabel();
        lblNom2 = new javax.swing.JLabel();
        cmdNew = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPass = new javax.swing.JPasswordField();

        chkRememberMe.setText("jCheckBox1");

        setTitle("USUARIOS");

        lblDni.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDni.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDni.setText("ID");

        cmdSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search_16.png"))); // NOI18N
        cmdSearch.setText("Buscar");
        cmdSearch.setSelected(true);

        lblApe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApe.setText("APELLIDO");

        lblNom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNom.setText("NOMBRE");

        lblEstado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEstado.setText("ESTADO");

        chkState.setText("ACTIVO");

        groupGender.add(optMale);
        optMale.setText("MASCULINO");

        groupGender.add(optFemale);
        optFemale.setText("FEMENINO");

        lblApe1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApe1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApe1.setText("USUARIO");

        lblNom1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNom1.setText("PASSWORD");

        lblApe2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApe2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApe2.setText("TIPO USUARIO");

        lblNom2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNom2.setText("RE-PASSWORD");

        cmdNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/New_16.png"))); // NOI18N
        cmdNew.setText("Nuevo");

        cmdEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Edit_File_16.png"))); // NOI18N
        cmdEdit.setText("Editar");

        cmdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Delete_File_16.png"))); // NOI18N
        cmdDelete.setText("Eliminar");

        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Exit_16.png"))); // NOI18N
        cmdExit.setText("Salir");

        cmdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Save_File_16.png"))); // NOI18N
        cmdSave.setText("Guardar");

        cmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Cancel_File_16.png"))); // NOI18N
        cmdCancel.setText("Cancelar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblApe1)
                                    .addComponent(lblNom1)
                                    .addComponent(lblApe2))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbTipos, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                    .addComponent(txtPassword)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblApe)
                                    .addComponent(lblDni)
                                    .addComponent(lblNom))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtFirstName)
                                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblNom2)
                                        .addGap(16, 16, 16))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblEstado)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkState)
                                    .addComponent(txtConfirmPass)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(cmdNew, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmdSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(optMale)
                .addGap(18, 18, 18)
                .addComponent(optFemale)
                .addGap(130, 130, 130))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDni)
                    .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApe))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optMale)
                    .addComponent(optFemale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApe2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApe1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNom1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNom2)
                    .addComponent(txtConfirmPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstado)
                    .addComponent(chkState, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdExit)
                    .addComponent(cmdNew)
                    .addComponent(cmdEdit)
                    .addComponent(cmdDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdSave)
                    .addComponent(cmdCancel))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkRememberMe;
    private javax.swing.JCheckBox chkState;
    private javax.swing.JComboBox<TipoUsuario> cmbTipos;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdSearch;
    private javax.swing.ButtonGroup groupGender;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblApe;
    private javax.swing.JLabel lblApe1;
    private javax.swing.JLabel lblApe2;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblNom;
    private javax.swing.JLabel lblNom1;
    private javax.swing.JLabel lblNom2;
    private javax.swing.JRadioButton optFemale;
    private javax.swing.JRadioButton optMale;
    private javax.swing.JPasswordField txtConfirmPass;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

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

}
