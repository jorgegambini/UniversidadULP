package universidadulp.vistas;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static universidadulp.connection.DatabaseConnection.getInstance;
import universidadulp.entidades.TipoUsuario;
import universidadulp.entidades.Usuario;
import universidadulp.repositorio.TipoUsuarioRepositorio;
import universidadulp.repositorio.UsuarioRepositorio;

public class TiposUsuario extends javax.swing.JInternalFrame {

    private TipoUsuarioRepositorio tr;
    private TipoUsuario tipoSeleccionado;
    private UsuarioRepositorio ur;
    private Usuario usuarioSeleccionado;

    private ItemListener radioButtonListener;

    private boolean isSave;

    public TiposUsuario() {

        tr = new TipoUsuarioRepositorio();
        ur = new UsuarioRepositorio();

        setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));

        initComponents();

        init();

    }

    private void init() {

        getTiposUsuario();

        this.radioButtonListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                try (Connection conn = getInstance();) {
                    
                    if (e.getStateChange() == ItemEvent.SELECTED && isSave) {
                        // Se seleccionó un botón, determina cuál fue seleccionado

                        if (e.getSource() == optAdmin) {

                            if (tr.guardar(new TipoUsuario(tipoSeleccionado.getIdTipoUsuario(), tipoSeleccionado.getDescripcion(), true, false, false)) != null) {

                                JOptionPane.showConfirmDialog(null, "Tipo Usuario Actualizado", "Actualizado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                            }

                        } else if (e.getSource() == optCrud) {

                            if (tr.guardar(new TipoUsuario(tipoSeleccionado.getIdTipoUsuario(), tipoSeleccionado.getDescripcion(), false, true, false)) != null) {

                                JOptionPane.showConfirmDialog(null, "Tipo Usuario Actualizado", "Actualizado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                            }

                        } else if (e.getSource() == optQueries) {

                            if (tr.guardar(new TipoUsuario(tipoSeleccionado.getIdTipoUsuario(), tipoSeleccionado.getDescripcion(), false, false, true)) != null) {

                                JOptionPane.showConfirmDialog(null, "Tipo Usuario Actualizado", "Actualizado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                            }

                        }
//                        else if (e.getStateChange() == ItemEvent.DESELECTED) {
//
//                         //Se deseleccionó un botón, puedes manejarlo aquí si es necesario
//                                 
//                                 }
                    }

                } catch (SQLException ex) {

                    JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                }

            }

        };

        optAdmin.addItemListener(radioButtonListener);
        optCrud.addItemListener(radioButtonListener);
        optQueries.addItemListener(radioButtonListener);

        cmbTipos.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    //// Obtiene el ítem seleccionado
                    tipoSeleccionado = (TipoUsuario) cmbTipos.getSelectedItem();
                    
                    getAcceso();

                }
            }
        });

        cmdExit.addActionListener((e) -> dispose());

    }

    private void getTiposUsuario() {

        cmbTipos.removeAllItems();

        try (Connection conn = getInstance();) {

            tr.buscarTodos().forEach(alu -> cmbTipos.addItem(alu));

            if (cmbTipos.getItemCount() > 0) {

                tipoSeleccionado = (TipoUsuario) cmbTipos.getSelectedItem();
                
                getAcceso();

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        }

    }

    private void getAcceso() {

        isSave = false;

        try (Connection conn = getInstance();) {

            String result = tr.buscarAcceso(tipoSeleccionado.getIdTipoUsuario());

            switch (result) {
               
                case "ADMIN":
                    optAdmin.setSelected(true);
                    isSave = true;
                    break;
                case "CRUD":
                    optCrud.setSelected(true);
                    isSave = true;
                    break;
                case "QUERIES":
                    optQueries.setSelected(true);
                    isSave = true;
                    break;
                default:
                    optAdmin.setSelected(true);
                    isSave = true;
                    break;

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupTypes = new javax.swing.ButtonGroup();
        cmbTipos = new javax.swing.JComboBox<>();
        lblApe2 = new javax.swing.JLabel();
        optAdmin = new javax.swing.JRadioButton();
        optCrud = new javax.swing.JRadioButton();
        optQueries = new javax.swing.JRadioButton();
        cmdExit = new javax.swing.JButton();

        setTitle("ACCESOS TIPOS DE USUARIO");
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

        lblApe2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApe2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApe2.setText("TIPO USUARIO");

        groupTypes.add(optAdmin);
        optAdmin.setText("ADMINISTRAR");

        groupTypes.add(optCrud);
        optCrud.setText("CRUD");

        groupTypes.add(optQueries);
        optQueries.setText("QUERIES");

        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Exit_16.png"))); // NOI18N
        cmdExit.setText("Salir");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblApe2)
                        .addGap(18, 18, 18)
                        .addComponent(cmbTipos, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(optAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addComponent(optCrud)
                        .addGap(56, 56, 56)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optQueries)
                    .addComponent(cmdExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblApe2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optAdmin)
                    .addComponent(optCrud)
                    .addComponent(optQueries))
                .addGap(18, 18, 18)
                .addComponent(cmdExit)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        //
    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<TipoUsuario> cmbTipos;
    private javax.swing.JButton cmdExit;
    private javax.swing.ButtonGroup groupTypes;
    private javax.swing.JLabel lblApe2;
    private javax.swing.JRadioButton optAdmin;
    private javax.swing.JRadioButton optCrud;
    private javax.swing.JRadioButton optQueries;
    // End of variables declaration//GEN-END:variables
}
