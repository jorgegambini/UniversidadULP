package universidadulp.table;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.DefaultFormatter;

public class NotaCellEditor extends DefaultCellEditor {

    private JSpinner input;
    JSpinner.NumberEditor editor;
    private JTable table;
    private int row;

    private double nota;
    private DecimalFormat decimalFormat;

    public NotaCellEditor(){

        super(new JCheckBox());
        SpinnerNumberModel numberModel = new SpinnerNumberModel(0.0, 0.0, 10.0, 0.1); // (valorInicial, valorMínimo, valorMáximo, paso)
        input = new JSpinner(numberModel);
  
        editor = (JSpinner.NumberEditor) input.getEditor();
        
        decimalFormat = editor.getFormat();
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        decimalFormat.applyPattern("0.0");
        
        //Hacemos que cuando el formato del valor del Spinner sea valido, se aplique y no esperar a que se salga del spinner para validar
        DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
        formatter.setCommitsOnValidEdit(true);
        
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        super.getTableCellEditorComponent(table, value, isSelected, row, column);
        this.table = table;
        this.row = row;
        
        nota = (value instanceof String && "SIN NOTA".equals(value)) ? 0.0 : Double.parseDouble(value.toString());

        input.setValue(nota);
        input.setEnabled(false);
        enable();

        // Obtener el componente de texto dentro del JSpinner
        JFormattedTextField textField = editor.getTextField();

        // Agregar enfoque al componente de texto
        SwingUtilities.invokeLater(() -> {
            
            textField.requestFocusInWindow();

            // Utilizar un temporizador para seleccionar el texto después de un breve retraso
            Timer timer = new Timer(100, e -> {
                textField.setSelectionStart(0);
                textField.setSelectionEnd(textField.getText().length());
            });
            timer.setRepeats(false); // Ejecutar el temporizador solo una vez
            timer.start();
            
        });

        return input;

    }

    private void enable() {

        new Thread(() -> {

            try {
                Thread.sleep(100);
                input.setEnabled(true);
            } catch (InterruptedException ex) {

            }

        }).start();

    }

    @Override
    public Object getCellEditorValue() {
       
        nota = Double.parseDouble(decimalFormat.format(input.getValue())); // Obtener el valor actual del editor

        // Verificar si la nueva nota es igual a 0 y establecer "SIN NOTA" si es así
        if (nota == 0.0) {
            
            return "SIN NOTA";
            
        } else {
            
            return nota; // Devolver la nota actual si no es igual a 0
            
        }
        
    }

}
