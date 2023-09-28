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
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import universidadulp.table.NotaCellEditor;
import static universidadulp.connection.DatabaseConnection.getInstance;
import universidadulp.entidades.Alumno;
import universidadulp.entidades.Inscripcion;
import universidadulp.repositorio.AlumnoRepositorio;
import universidadulp.repositorio.InscripcionRepositorio;

public class CargaNotas extends javax.swing.JInternalFrame {

    private AlumnoRepositorio ar;
    private InscripcionRepositorio ir;

    private Alumno alumnoSeleccionado;
    private Inscripcion inscripSeleccionada;

    private int selectedRow;

    private DefaultTableModel modelo;

    public CargaNotas() {
        initComponents();

        ar = new AlumnoRepositorio();
        ir = new InscripcionRepositorio();

        chkAluInactivo.setSelected(false);
        chkMatInactiva.setSelected(false);
        
         setFrameIcon(new ImageIcon(getClass().getResource("/icon/logo1.png")));

        getAlumnos();

        dibujarTabla();

        chkAluInactivo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                getAlumnos();

            }

        });

        cmbAlumnos.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == ItemEvent.SELECTED) {

                    //// Obtiene el ítem seleccionado
                    alumnoSeleccionado = (Alumno) cmbAlumnos.getSelectedItem();

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
            // El evento se ejecuta cuando cambia la selección de fila
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {

                    inscripSeleccionada = null;

                    selectedRow = table.getSelectedRow();

                    if (selectedRow > -1) {

                        inscripSeleccionada = (Inscripcion) modelo.getValueAt(selectedRow, 3);

                        //System.out.println(inscripSeleccionada);
                    }

                }
            }
        });

        //Registrar oyentes que estarán atentos a cambios en el modelo de la tabla
        //En este caso, el oyente reacciona a eventos relacionados con el modelo de la tabla, como la adición o eliminación de filas, la modificación de datos en una celda, etc.
        //TableModelChange
//        table.getModel().addTableModelListener(new TableModelListener(){
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        });
        table.getModel().addTableModelListener((TableModelEvent tme) -> {

            int column = tme.getColumn();
            int row = tme.getFirstRow();

            if (column == 2) {

                try (Connection conn = getInstance();) {

                    inscripSeleccionada.setNota((modelo.getValueAt(row, column).equals("SIN NOTA")) ? 0 : (double) modelo.getValueAt(row, column));

                    ir.guardar(inscripSeleccionada);

                } catch (SQLException ex) {

                    JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                }

            }

        });

        cmdSalir.addActionListener((e) -> {

            dispose();

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

            }

        } catch (SQLException ex) {

            JOptionPane.showConfirmDialog(this, ex.getMessage(), "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

        }

    }

    private void dibujarTabla() {

        modelo = new DefaultTableModel() {
 
            @Override
            public boolean isCellEditable(int row, int column) {
                
                inscripSeleccionada = (Inscripcion) modelo.getValueAt(row, 3);
                
                if (inscripSeleccionada.getAlumno().isActivo() && inscripSeleccionada.getMateria().isActivo() && column == 2) {
                  
                    return true;
                }
                
                return false;
                
            }

        };
        
        table.setModel(modelo);

        String[] columnNames = {"INSCRIP.", "MATERIA", "NOTA", "INS.SEL"};
        modelo.setColumnIdentifiers(columnNames);

        // Define el ancho de las columnas
        int[] columnWidths = {30, 200, 35, 0}; // Ancho en píxeles para cada columna
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        
        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        
        

        //-----------------------------------------------------------------------------------------
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
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Nota a la derecha

        table.getColumnModel().getColumn(2).setCellEditor(new NotaCellEditor());

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                return this;

            }

        });

        cargarTabla();

    }

    private void cargarTabla() {

        modelo.setRowCount(0);

        if (alumnoSeleccionado != null) {

            try (Connection conn = getInstance();) {

                for (Inscripcion inscripcion : ir.buscarTodosxAlu(alumnoSeleccionado.getIdAlumno(), chkMatInactiva.isSelected())) {

                    modelo.addRow(new Inscripcion(inscripcion.getIdInscripcion(), inscripcion.getAlumno(), inscripcion.getMateria(), inscripcion.getNota()).toTableRow(table.getRowCount() + 1));

                }

                inscripSeleccionada = null;

                if (table.getRowCount() > 0) {

                    table.setRowSelectionInterval(0, 0);

                    selectedRow = 0;

                    inscripSeleccionada = (Inscripcion) modelo.getValueAt(0, 3);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        lblAlumno = new javax.swing.JLabel();
        cmbAlumnos = new javax.swing.JComboBox<>();
        chkAluInactivo = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        chkMatInactiva = new javax.swing.JCheckBox();
        cmdSalir = new javax.swing.JButton();

        setTitle("CARGA DE NOTAS");

        lblAlumno.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblAlumno.setText("SELECCIONE ALUMNO");

        chkAluInactivo.setText("INCLUIR INACTIVOS");
        chkAluInactivo.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "INSCRIP.", "MATERIA", "NOTA", "INSCRIP"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(23);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(35);
            table.getColumnModel().getColumn(3).setMinWidth(0);
            table.getColumnModel().getColumn(3).setPreferredWidth(0);
            table.getColumnModel().getColumn(3).setMaxWidth(0);
        }

        chkMatInactiva.setText("INCLUIR INACTIVAS");

        cmdSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Exit_16.png"))); // NOI18N
        cmdSalir.setText("Salir");
        cmdSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkMatInactiva)
                    .addComponent(chkAluInactivo)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(lblAlumno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlumno)
                    .addComponent(cmbAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAluInactivo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkMatInactiva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmdSalir)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAluInactivo;
    private javax.swing.JCheckBox chkMatInactiva;
    private javax.swing.JComboBox<Alumno> cmbAlumnos;
    private javax.swing.JButton cmdSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlumno;
    private javax.swing.JPanel panel;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
