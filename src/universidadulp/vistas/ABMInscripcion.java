package universidadulp.vistas;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import static universidadulp.connection.DatabaseConnection.getInstance;
import universidadulp.entidades.Alumno;
import universidadulp.entidades.Inscripcion;
import universidadulp.entidades.Materia;
import universidadulp.repositorio.AlumnoRepositorio;
import universidadulp.repositorio.InscripcionRepositorio;
import universidadulp.repositorio.MateriaRepositorio;

public class ABMInscripcion extends javax.swing.JInternalFrame {

    AlumnoRepositorio ar;
    InscripcionRepositorio ir;
    MateriaRepositorio mr;
    Alumno alumnoSeleccionado;
    Materia materiaSeleccionada;

    private DefaultTableModel modelo;

    public ABMInscripcion() {

        initComponents();
        
        ar = new AlumnoRepositorio();
        mr = new MateriaRepositorio();
        ir = new InscripcionRepositorio();
        
         setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));

        chkAluInactivo.setSelected(false);
        chkMatInactiva.setSelected(false);

        getAlumnos();

        groupOptions.add(optInscriptas);
        groupOptions.add(optNoInscriptas);
        optInscriptas.setSelected(true);

        dibujarTabla();

        chkAluInactivo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

//                if (e.getStateChange() == ItemEvent.SELECTED) {// Si el JCheckBox se tilda, recarga el JComboBox
//                  
//                } else {// Si el JCheckBox se destilda, restaura los elementos originales
//                  
//                }
                getAlumnos();

            }

        });

        cmbAlumnos.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    //// Obtiene el ítem seleccionado
                    alumnoSeleccionado = (Alumno) cmbAlumnos.getSelectedItem();
                    //System.out.println(alumnoSeleccionado.getIdAlumno());

                    optInscriptas.setSelected(true);
                    chkMatInactiva.setSelected(false);
                    cargarTabla();

                }
            }
        });

        // Llamar a la función para asignar el ItemListener a los JRadioButtons
        asignarItemListener(new JRadioButton[]{optInscriptas, optNoInscriptas}, new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    //JRadioButton selectedButton = (JRadioButton) e.getItem();
                    //System.out.println("RadioButton seleccionado: " + selectedButton.getText());
                    chkMatInactiva.setSelected(false);
                    cargarTabla();

                }

            }

        });

        chkMatInactiva.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                cargarTabla();

            }

        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                // El evento se ejecuta cuando cambia la selección de fila
                if (!e.getValueIsAdjusting()) {

                    materiaSeleccionada = null;

                    int selectedRow = table.getSelectedRow();

                    if (selectedRow > -1) {

                        materiaSeleccionada = (Materia) modelo.getValueAt(selectedRow, 3);
                        controlaEstados();

                    }

                }
            }
        });

        cmdSalir.addActionListener((e) -> {

            dispose();

        });

        cmdInscribir.addActionListener((e) -> {

            try (Connection conn = getInstance();) {

                int row = table.getSelectedRow();

                if (row >= 0) {

                    int valueId = (int) table.getValueAt(row, 0);

                    ir.guardar(new Inscripcion(alumnoSeleccionado, materiaSeleccionada, 0));

                    modelo.removeRow(row);

                    if (modelo.getRowCount() == 0) {

                        cmdInscribir.setEnabled(false);
                        pasarFoco(cmbAlumnos);

                    } else {

                        table.setRowSelectionInterval(0, 0);

                    }

                    JOptionPane.showConfirmDialog(this, "La Inscripción fue Registrada Exitosamente", "Registrar Inscripcion", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                }

            } catch (SQLException ex) {

                JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

            }

        });

        cmdAnular.addActionListener((e) -> {

            try (Connection conn = getInstance();) {

                int row = table.getSelectedRow();

                if (row >= 0) {

                    int valueId = (int) table.getValueAt(row, 0);

                    double nota = ir.devolverNotaPorAluyMat(alumnoSeleccionado.getIdAlumno(), valueId);

                    if (nota > 0) {

                        if (JOptionPane.showConfirmDialog(null, "La inscripcion posee Nota de " + nota + ". Anula?", "Anular Inscripcion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {

                            eliminarInscripcion(valueId, row);

                        }

                    } else if (nota == 0) {

                        eliminarInscripcion(valueId, row);

                    }

                }

            } catch (SQLException ex) {

                JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

            }

        });

        pasarFoco(cmbAlumnos);

    }

    private void getAlumnos() {

        cmbAlumnos.removeAllItems();

        try (Connection conn = getInstance();) {

            if (chkAluInactivo.isSelected()) {

                for (Alumno alu : ar.buscarTodos()) {

                    cmbAlumnos.addItem(alu);

                }

            } else {

                for (Alumno alu : ar.buscarTodosActivos()) {

                    cmbAlumnos.addItem(alu);

                }

            }

            if (cmbAlumnos.getItemCount() > 0) {

                alumnoSeleccionado = (Alumno) cmbAlumnos.getSelectedItem();
                //alumnoSeleccionado1 = cmbAlumnos.getItemAt(0);

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        }

    }

    private void dibujarTabla() {

        // Personalizar la alineación y la fuente de la cabecera
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();

        // Cambiar la alineación de la cabecera (izquierda en este caso)
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Cambiar la fuente de la cabecera
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Crear renderizadores personalizados para diferentes alineaciones
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);   // Id a la izquierda
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Nombre al centro
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Año a la derecha

        modelo = (DefaultTableModel) table.getModel();

        cargarTabla();

    }

    private void cargarTabla() {

        String where = "";

        modelo.setRowCount(0);

        if (alumnoSeleccionado != null) {

            //System.out.println(alumnoSeleccionado.getIdAlumno());
            if (optInscriptas.isSelected()) {

                if (chkMatInactiva.isSelected()) {

                    where = "WHERE m.idMateria IN (SELECT i.idMateria FROM inscripcion i WHERE i.idAlumno = " + alumnoSeleccionado.getIdAlumno() + ") ORDER BY m.idMateria";

                } else {

                    where = "WHERE m.estado = 1 AND m.idMateria IN (SELECT i.idMateria FROM inscripcion i WHERE i.idAlumno = " + alumnoSeleccionado.getIdAlumno() + ") ORDER BY m.idMateria";

                }

            } else {

                if (chkMatInactiva.isSelected()) {

                    where = "WHERE m.idMateria NOT IN (SELECT i.idMateria FROM inscripcion i WHERE i.idAlumno = " + alumnoSeleccionado.getIdAlumno() + ") ORDER BY m.idMateria";

                } else {

                    where = "WHERE m.estado = 1 AND m.idMateria NOT IN (SELECT i.idMateria FROM inscripcion i WHERE i.idAlumno = " + alumnoSeleccionado.getIdAlumno() + ") ORDER BY m.idMateria";

                }

            }

            try (Connection conn = getInstance();) {

                for (Materia materia : mr.buscarMateriasConWhere(where)) {

                    modelo.addRow(new Materia(materia.getIdMateria(), materia.getNombre(), materia.getAnioMateria(), materia.isActivo()).toTableRow(table.getRowCount() + 1));

                }

                materiaSeleccionada = null;

                if (table.getRowCount() > 0) {

                    table.setRowSelectionInterval(0, 0);

                    materiaSeleccionada = (Materia) modelo.getValueAt(0, 3);

                    controlaEstados();

                } else {

                    manejoBotones(false, false);

                }

            } catch (SQLException ex) {

                JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

            }
        }

    }

    private void eliminarInscripcion(int valueId, int row) {

        ir.eliminarPorAluyMat(alumnoSeleccionado.getIdAlumno(), valueId);

        modelo.removeRow(row);

        if (modelo.getRowCount() == 0) {

            cmdAnular.setEnabled(false);
            pasarFoco(cmbAlumnos);

        } else {

            table.setRowSelectionInterval(0, 0);

        }

        JOptionPane.showConfirmDialog(this, "La Inscripción fue Anulada Exitosamente", "Anular Inscripcion", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

    }

    private void manejoBotones(boolean btnInscribir, boolean btnAnular) {

        cmdInscribir.setEnabled(btnInscribir);
        cmdAnular.setEnabled(btnAnular);

    }

    // Función genérica para asignar el ItemListener a un arreglo de JRadioButtons
    private static void asignarItemListener(JRadioButton[] buttons, ItemListener listener) {

        for (JRadioButton button : buttons) {

            button.addItemListener(listener);

        }

    }

    private void controlaEstados() {

        if (alumnoSeleccionado.isActivo()) {

            if (optInscriptas.isSelected()) {

                if (materiaSeleccionada.isActivo()) {

                    manejoBotones(false, true);

                } else {

                    manejoBotones(false, false);

                }

            } else {

                if (materiaSeleccionada.isActivo()) {

                    manejoBotones(true, false);

                } else {

                    manejoBotones(false, false);

                }
            }

        } else {

            manejoBotones(false, false);

        }

    }

    private void pasarFoco(Component com) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                com.requestFocusInWindow();

//                if (com instanceof JTextField) {
//                    ((JTextField) com).selectAll();
//                }
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

        groupOptions = new javax.swing.ButtonGroup();
        panel = new javax.swing.JPanel();
        lblAlumno = new javax.swing.JLabel();
        cmbAlumnos = new javax.swing.JComboBox<>();
        chkAluInactivo = new javax.swing.JCheckBox();
        lblTitle = new javax.swing.JLabel();
        optInscriptas = new javax.swing.JRadioButton();
        optNoInscriptas = new javax.swing.JRadioButton();
        scroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        chkMatInactiva = new javax.swing.JCheckBox();
        cmdInscribir = new javax.swing.JButton();
        cmdAnular = new javax.swing.JButton();
        cmdSalir = new javax.swing.JButton();

        setTitle("INSCRIPCIONES");

        lblAlumno.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAlumno.setText("SELECCIONE ALUMNO");

        chkAluInactivo.setText("INCLUIR INACTIVOS");

        lblTitle.setFont(new java.awt.Font("Verdana", 1, 16)); // NOI18N
        lblTitle.setText("LISTADO DE MATERIAS");

        optInscriptas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        optInscriptas.setText("MATERIAS INSCRIPTAS");

        optNoInscriptas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        optNoInscriptas.setText("MATERIAS NO INSCRIPTAS");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE", "AÑO", "MATERIA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(60);
            table.getColumnModel().getColumn(3).setMinWidth(0);
            table.getColumnModel().getColumn(3).setPreferredWidth(0);
            table.getColumnModel().getColumn(3).setMaxWidth(0);
        }

        chkMatInactiva.setText("INCLUIR INACTIVAS");

        cmdInscribir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Inscribir_16.png"))); // NOI18N
        cmdInscribir.setText("Inscribir");

        cmdAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Anular_16.png"))); // NOI18N
        cmdAnular.setText("Anular Inscripcion");

        cmdSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Exit_16.png"))); // NOI18N
        cmdSalir.setText("Salir");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(cmdInscribir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdAnular, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkAluInactivo)
                            .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelLayout.createSequentialGroup()
                                    .addComponent(optInscriptas)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(optNoInscriptas))
                                .addGroup(panelLayout.createSequentialGroup()
                                    .addComponent(lblAlumno)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(cmbAlumnos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(chkMatInactiva)))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(lblTitle)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAlumno))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAluInactivo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optInscriptas)
                    .addComponent(optNoInscriptas))
                .addGap(18, 18, 18)
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkMatInactiva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdInscribir)
                    .addComponent(cmdAnular)
                    .addComponent(cmdSalir))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAluInactivo;
    private javax.swing.JCheckBox chkMatInactiva;
    private javax.swing.JComboBox<Alumno> cmbAlumnos;
    private javax.swing.JButton cmdAnular;
    private javax.swing.JButton cmdInscribir;
    private javax.swing.JButton cmdSalir;
    private javax.swing.ButtonGroup groupOptions;
    private javax.swing.JLabel lblAlumno;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton optInscriptas;
    private javax.swing.JRadioButton optNoInscriptas;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
