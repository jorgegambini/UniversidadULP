package universidadulp.vistas;

import java.awt.Component;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import universidadulp.repositorio.InscripcionRepositorio;
import universidadulp.repositorio.MateriaRepositorio;

public class AlumnoPorMateria extends javax.swing.JInternalFrame {

    private MateriaRepositorio mr;
    private InscripcionRepositorio ir;

    private Alumno alumnoSeleccionado;
    private Materia materiaSeleccionada;
    private Inscripcion inscripSeleccionada;

    private int selectedRow;

    private DefaultTableModel modelo;

    public AlumnoPorMateria() {

        initComponents();
        
        setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));

        mr = new MateriaRepositorio();
        ir = new InscripcionRepositorio();

        chkMatInactiva.setSelected(false);
        chkAluInactivo.setSelected(false);

        getMaterias();

        dibujarTabla();

        chkMatInactiva.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                getMaterias();

            }

        });

        cmbMaterias.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    //// Obtiene el ítem seleccionado
                    materiaSeleccionada = (Materia) cmbMaterias.getSelectedItem();

                    chkAluInactivo.setSelected(false);
                    cargarTabla();

                }
            }
        });

        chkAluInactivo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                cargarTabla();

            }

        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            // El evento se ejecuta cuando cambia la selección de fila
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {

                    alumnoSeleccionado = null;

                    selectedRow = table.getSelectedRow();

                    if (selectedRow > -1) {

                        alumnoSeleccionado = (Alumno) modelo.getValueAt(selectedRow, 4);
                        inscripSeleccionada = (Inscripcion) modelo.getValueAt(selectedRow, 5);
                        //System.out.println(alumnoSeleccionado);
                    }

                }
            }
        });

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                // Verificar si fue un doble clic izquierdo
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {

                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();

                    // Acción a realizar en caso de doble clic izquierdo
                    if (row != -1 && col != -1) {

                        String msgJOP = "";

                        if (inscripSeleccionada.getNota() >= 6) {
                            msgJOP = "La Materia ha sido aprobada con: " + inscripSeleccionada.getNota();
                        } else if (inscripSeleccionada.getNota() > 0) {
                            msgJOP = "La Materia ha sido reprobada con: " + inscripSeleccionada.getNota();
                        } else {
                            msgJOP = "La Materia no ha sido rendida";
                        }

                        JOptionPane.showConfirmDialog(null, msgJOP, "Datos Examen", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);

                    }
                }
            }
        });

        cmdSalir.addActionListener((e) -> {
            dispose();
        });

        pasarFoco(cmbMaterias);

    }

    private void getMaterias() {

        cmbMaterias.removeAllItems();

        try (Connection conn = getInstance();) {

            if (chkMatInactiva.isSelected()) {

                for (Materia mate : mr.buscarTodos()) {

                    cmbMaterias.addItem(mate);

                }

            } else {

                for (Materia mate : mr.buscarTodosActivos()) {

                    cmbMaterias.addItem(mate);

                }

            }

            if (cmbMaterias.getItemCount() > 0) {

                materiaSeleccionada = (Materia) cmbMaterias.getSelectedItem();

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
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Nombre al centro
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Nota a la derecha

        modelo = (DefaultTableModel) table.getModel();

        cargarTabla();

    }

    private void cargarTabla() {

        modelo.setRowCount(0);

        if (materiaSeleccionada != null) {

            try (Connection conn = getInstance();) {

                for (Inscripcion inscripcion : ir.buscarTodosxMat(materiaSeleccionada.getIdMateria(), chkAluInactivo.isSelected())) {

                    Alumno alu = new Alumno(inscripcion.getAlumno().getIdAlumno(), inscripcion.getAlumno().getDni(), inscripcion.getAlumno().getApellido(), inscripcion.getAlumno().getNombre(), LocalDate.now(), true);
                    alu.setInscTemp(inscripcion);
                    modelo.addRow(alu.toTableRow(table.getRowCount() + 1));

                }

                alumnoSeleccionado = null;
                inscripSeleccionada = null;

                if (table.getRowCount() > 0) {

                    table.setRowSelectionInterval(0, 0);

                    selectedRow = 0;

                    alumnoSeleccionado = (Alumno) modelo.getValueAt(0, 4);
                    inscripSeleccionada = (Inscripcion) modelo.getValueAt(0, 5);
                    //System.out.println(alumnoSeleccionado);
                    //System.out.println(inscripSeleccionada);
                }

            } catch (SQLException ex) {

                JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        lblMaterias = new javax.swing.JLabel();
        cmbMaterias = new javax.swing.JComboBox<>();
        chkMatInactiva = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        chkAluInactivo = new javax.swing.JCheckBox();
        cmdSalir = new javax.swing.JButton();

        setResizable(true);
        setTitle("LISTADO DE ALUMNOS POR MATERIA");
        setToolTipText("");

        lblMaterias.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMaterias.setText("SELECCIONE MATERIA");

        chkMatInactiva.setText("INCLUIR INACTIVAS");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "APELLIDO", "NOMBRE", "DNI", "ALU", "INSCR"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(60);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setMinWidth(0);
            table.getColumnModel().getColumn(4).setPreferredWidth(0);
            table.getColumnModel().getColumn(4).setMaxWidth(0);
            table.getColumnModel().getColumn(5).setMinWidth(0);
            table.getColumnModel().getColumn(5).setPreferredWidth(0);
            table.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        chkAluInactivo.setText("INCLUIR INACTIVOS");

        cmdSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Exit_16.png"))); // NOI18N
        cmdSalir.setText("Salir");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkMatInactiva)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(lblMaterias)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkAluInactivo)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaterias)
                    .addComponent(cmbMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkMatInactiva)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAluInactivo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdSalir)
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
    private javax.swing.JComboBox<Materia> cmbMaterias;
    private javax.swing.JButton cmdSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMaterias;
    private javax.swing.JPanel panel;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
