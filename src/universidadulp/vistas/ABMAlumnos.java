package universidadulp.vistas;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import static universidadulp.connection.DatabaseConnection.getInstance;
import universidadulp.entidades.Alumno;
import universidadulp.repositorio.AlumnoRepositorio;
import universidadulp.repositorio.InscripcionRepositorio;

public class ABMAlumnos extends javax.swing.JInternalFrame {

    private AlumnoRepositorio ar;
    private InscripcionRepositorio ir;

    private Alumno alu;
    private int estado;

    public ABMAlumnos() {

        initComponents();

        ar = new AlumnoRepositorio();
        ir = new InscripcionRepositorio();

        dcFNac.setDateFormatString("dd MMMM yyyy");
        dcFNac.setIcon(new ImageIcon(getClass().getResource("/icon/Calendar_16.png")));
        setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));

        estado = 0;

        limpiarDatosAlumnos();

        cmdSalir.addActionListener((e) -> {

            dispose();

        });

        addSelectAllOnFocusToTextFields(this);

        pasarFoco(txtDNI);

    }

    private void limpiarDatosAlumnos() {

        limpiarCampos();
        estadosCampos(true, false, false, false, false);
        estadosBotones(true, false, false, false, false, true);

    }

    private void restaurarDatosAlumnos() {

        estadosBotones(true, true, true, false, true, false);
        estadosCampos(false, false, false, false, false);
        txtId.setText("" + alu.getIdAlumno());
        txtDNI.setText("" + alu.getDni());
        txtApellido.setText(alu.getApellido());
        txtNombre.setText(alu.getNombre());
        chkActivo.setSelected(alu.isActivo());
        cmdEliminar.setEnabled(alu.isActivo());
        dcFNac.setDate(Date.from(alu.getFechaNac().atStartOfDay(ZoneId.systemDefault()).toInstant()));

    }

    private void estadosBotones(boolean btnNuevo, boolean btnEditar, boolean btnEliminar, boolean btnGuardar, boolean btnCancelar, boolean btnBuscar) {

        cmdNuevo.setEnabled(btnNuevo);
        cmdEditar.setEnabled(btnEditar);
        cmdEliminar.setEnabled(btnEliminar);
        cmdGuardar.setEnabled(btnGuardar);
        cmdCancelar.setEnabled(btnCancelar);
        cmdBuscar.setEnabled(btnBuscar);

    }

    private void estadosCampos(boolean tfDni, boolean tfApellido, boolean tfNombre, boolean ckActivo, boolean dpFNac) {

        txtDNI.setEnabled(tfDni);
        txtApellido.setEnabled(tfApellido);
        txtNombre.setEnabled(tfNombre);
        chkActivo.setEnabled(ckActivo);
        dcFNac.setEnabled(dpFNac);

    }

    private void limpiarCampos() {

        txtDNI.setHorizontalAlignment(SwingConstants.RIGHT);
        txtDNI.setText("0");
        txtApellido.setText("");
        txtNombre.setText("");
        chkActivo.setSelected(true);
        dcFNac.setDate(Date.from(LocalDate.parse("2000-01-01").atStartOfDay(ZoneId.systemDefault()).toInstant()));

    }

    private boolean validarDNI(String dni) {

        return dni.matches("^[1-9]\\d{0,8}$");

    }

    private boolean validarNomApe(String nomApe) {

        return nomApe.matches("^([A-ZÁÉÍÓÚÜÑ]([.]|[a-záéíóüñ]+))([ ][A-ZÁÉÍÓÚÜÑ]([.]|[a-záéíóüñ]+)){0,5}");

    }

    private boolean validarFecha(Date date) {

        LocalDate fecha = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        try {
            // Intenta crear una fecha válida a partir de los componentes de LocalDate
            LocalDate.of(fecha.getYear(), fecha.getMonth(), fecha.getDayOfMonth());
            return true;

        } catch (Exception e) {

            return false;

        }

    }

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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                com.requestFocusInWindow();

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtId = new javax.swing.JTextField();
        panel = new javax.swing.JPanel();
        lblDni = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        cmdBuscar = new javax.swing.JButton();
        lblApe = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        lblNom = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        chkActivo = new javax.swing.JCheckBox();
        lblFNac = new javax.swing.JLabel();
        dcFNac = new com.toedter.calendar.JDateChooser();
        cmdNuevo = new javax.swing.JButton();
        cmdEditar = new javax.swing.JButton();
        cmdEliminar = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();
        cmdGuardar = new javax.swing.JButton();
        cmdCancelar = new javax.swing.JButton();

        txtId.setText("jTextField1");

        setTitle("ALUMNOS");

        lblDni.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDni.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDni.setText("DNI");

        cmdBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Search_16.png"))); // NOI18N
        cmdBuscar.setText("Buscar");
        cmdBuscar.setSelected(true);
        cmdBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBuscarActionPerformed(evt);
            }
        });

        lblApe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblApe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApe.setText("APELLIDO");

        lblNom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNom.setText("NOMBRE");

        lblEstado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEstado.setText("ESTADO");

        chkActivo.setText("ACTIVO");

        lblFNac.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFNac.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFNac.setText("F. NAC.");

        dcFNac.setNextFocusableComponent(cmdGuardar);

        cmdNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/New_16.png"))); // NOI18N
        cmdNuevo.setText("Nuevo");
        cmdNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNuevoActionPerformed(evt);
            }
        });

        cmdEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Edit_File_16.png"))); // NOI18N
        cmdEditar.setText("Editar");
        cmdEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditarActionPerformed(evt);
            }
        });

        cmdEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Delete_File_16.png"))); // NOI18N
        cmdEliminar.setText("Eliminar");
        cmdEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEliminarActionPerformed(evt);
            }
        });

        cmdSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Exit_16.png"))); // NOI18N
        cmdSalir.setText("Salir");

        cmdGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Save_File_16.png"))); // NOI18N
        cmdGuardar.setText("Guardar");
        cmdGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGuardarActionPerformed(evt);
            }
        });

        cmdCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Cancel_File_16.png"))); // NOI18N
        cmdCancelar.setText("Cancelar");
        cmdCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addComponent(cmdGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmdNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(cmdEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblApe)
                    .addComponent(lblDni)
                    .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblNom)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addComponent(lblFNac)))
                    .addComponent(lblEstado))
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkActivo)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(dcFNac, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellido))
                .addGap(55, 55, 55))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDni)
                    .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApe)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNom)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstado)
                    .addComponent(chkActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFNac)
                    .addComponent(dcFNac, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdEliminar)
                    .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmdSalir)
                        .addComponent(cmdNuevo)
                        .addComponent(cmdEditar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdGuardar)
                    .addComponent(cmdCancelar))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNuevoActionPerformed

        estado = 1;

        limpiarCampos();
        estadosCampos(true, true, true, true, true);
        estadosBotones(false, false, false, true, true, false);
        pasarFoco(txtDNI);

    }//GEN-LAST:event_cmdNuevoActionPerformed

    private void cmdEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditarActionPerformed

        estado = 2;

        estadosCampos(true, true, true, true, true);
        estadosBotones(false, false, false, true, true, false);
        pasarFoco(txtDNI);

    }//GEN-LAST:event_cmdEditarActionPerformed

    private void cmdEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEliminarActionPerformed

        
        int dni = Integer.parseInt(txtDNI.getText());
        limpiarDatosAlumnos();

        try (Connection conn = getInstance();) {

            if (ir.devolverNotaPorAlu(dni)) {

                if (JOptionPane.showConfirmDialog(null, "La Alumno posee Notas en Materias Activas. Elimina?", "Eliminar Alumno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {

                    int result = ar.eliminarPorDNI(dni);

                    if (result == 1) {

                        JOptionPane.showConfirmDialog(this, "El Alumno fue eliminado correctamente", "Alumno Eliminado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    } else {

                        JOptionPane.showConfirmDialog(this, "El Alumno ya estaba eliminado", "Alumno Inactivo", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    }

                }

            } else {

                if (JOptionPane.showConfirmDialog(this, "Esta Seguro que desea eliminar el Alumno?", "Eliminar Alumno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {

                    int result = ar.eliminarPorDNI(dni);

                    if (result == 1) {

                        JOptionPane.showConfirmDialog(this, "El Alumno fue eliminado correctamente", "Alumno Eliminado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    } else {

                        JOptionPane.showConfirmDialog(this, "El Alumno ya estaba eliminado", "Alumno Inactivo", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    }

                }

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {

            estado = 0;

            pasarFoco(txtDNI);

        }
        //}
    }//GEN-LAST:event_cmdEliminarActionPerformed

    private void cmdBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBuscarActionPerformed

        if (validarDNI(txtDNI.getText())) {

            try (Connection conn = getInstance();) {

                alu = ar.buscarPorDNI(Integer.parseInt(txtDNI.getText()));

                if (alu != null) {

                    restaurarDatosAlumnos();
                    pasarFoco(cmdEditar);

                } else {

                    txtDNI.setText("0");
                    JOptionPane.showConfirmDialog(this, "El DNI del Alumno ingresado no existe en la BD", "DNI Inexistente", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    pasarFoco(txtDNI);

                }
            } catch (SQLException ex) {

                limpiarDatosAlumnos();
                JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                pasarFoco(txtDNI);

            } finally {

                estado = 0;
            }

        } else {

            JOptionPane.showConfirmDialog(this, "Ingrese un DNI Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            pasarFoco(txtDNI);

        }
    }//GEN-LAST:event_cmdBuscarActionPerformed

    private void cmdCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelarActionPerformed

        if (estado == 1) {

            limpiarDatosAlumnos();
            pasarFoco(txtDNI);

        } else if (estado == 2) {

            restaurarDatosAlumnos();
            pasarFoco(cmdEditar);

        } else {

            limpiarDatosAlumnos();
            pasarFoco(txtDNI);

        }

        estado = 0;

    }//GEN-LAST:event_cmdCancelarActionPerformed

    private void cmdGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGuardarActionPerformed

        if (!validarDNI(txtDNI.getText())) {

            JOptionPane.showConfirmDialog(this, "Ingrese un DNI Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            pasarFoco(txtDNI);
            return;

        }

        if (!validarNomApe(txtApellido.getText())) {

            JOptionPane.showConfirmDialog(this, "Ingrese un Apellido Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            pasarFoco(txtApellido);
            return;

        }

        if (!validarNomApe(txtNombre.getText())) {

            JOptionPane.showConfirmDialog(this, "Ingrese un Nombre Correcto", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            pasarFoco(txtNombre);
            return;

        }

        if (dcFNac.getDate() != null) {

            if (!validarFecha(dcFNac.getDate())) {

                JOptionPane.showConfirmDialog(this, "Ingrese una Fecha Correcta", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                pasarFoco(dcFNac);
                return;

            } else if (LocalDate.now().compareTo(dcFNac.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) <= 0) {

                JOptionPane.showConfirmDialog(this, "La Fecha debe ser menor a Hoy", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                pasarFoco(dcFNac);
                return;

            }

        } else {

            JOptionPane.showConfirmDialog(this, "Ingrese una Fecha Correcta", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            pasarFoco(dcFNac);
            return;

        }

        if (estado == 1) {

            alu = new Alumno(Integer.parseInt(txtDNI.getText()), txtApellido.getText(), txtNombre.getText(), dcFNac.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), chkActivo.isSelected());

        } else if (estado == 2) {

            alu = new Alumno(Integer.parseInt(txtId.getText()), Integer.parseInt(txtDNI.getText()), txtApellido.getText(), txtNombre.getText(), dcFNac.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), chkActivo.isSelected());

        }

        limpiarDatosAlumnos();

        try (Connection conn = getInstance();) {

            alu = ar.guardar(alu);

            if (alu != null) {

                if (estado == 1) {

                    JOptionPane.showConfirmDialog(this, "El Alumno fue agregado correctamente", "Alumno Creado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                } else if (estado == 2) {

                    JOptionPane.showConfirmDialog(this, "El Alumno fue modificado correctamente", "Alumno Modificado", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                }

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {

            estado = 0;

            pasarFoco(txtDNI);

        }

    }//GEN-LAST:event_cmdGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JButton cmdBuscar;
    private javax.swing.JButton cmdCancelar;
    private javax.swing.JButton cmdEditar;
    private javax.swing.JButton cmdEliminar;
    private javax.swing.JButton cmdGuardar;
    private javax.swing.JButton cmdNuevo;
    private javax.swing.JButton cmdSalir;
    private com.toedter.calendar.JDateChooser dcFNac;
    private javax.swing.JLabel lblApe;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFNac;
    private javax.swing.JLabel lblNom;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

}
