package vallegrande.edu.pe.view;

import vallegrande.edu.pe.model.Incidencia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class IncidenciaForm extends JDialog {

    private JTextField txtAula;
    private JTextField txtFecha;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbEstado;
    private JTextArea txtDescripcion;
    private JButton btnGuardar, btnCancelar;

    private boolean guardado = false;
    private Incidencia incidencia; // objeto a crear o editar

    public IncidenciaForm(Frame owner, String title, Incidencia incidenciaExistente) {
        super(owner, title, true);
        this.incidencia = incidenciaExistente;
        initComponents();
        pack();
        setLocationRelativeTo(owner);
        if (incidenciaExistente != null) cargarDatos(incidenciaExistente);
        else cargarFechaHoy();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));

        // Campos
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.anchor = GridBagConstraints.WEST;

        // Tipo
        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("Tipo:"), gc);
        gc.gridx = 1;
        cbTipo = new JComboBox<>(new String[]{"PC", "Proyector", "Conectividad", "Impresora", "Otro"});
        cbTipo.setSelectedIndex(0);
        cbTipo.setPreferredSize(new Dimension(200, 25));
        form.add(cbTipo, gc);

        // Aula
        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("Aula/ambiente:"), gc);
        gc.gridx = 1;
        txtAula = new JTextField(20);
        form.add(txtAula, gc);

        // Fecha (autogenerada, no editable)
        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("Fecha reporte:"), gc);
        gc.gridx = 1;
        txtFecha = new JTextField(15);
        txtFecha.setEditable(false);
        form.add(txtFecha, gc);

        // Estado
        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("Estado:"), gc);
        gc.gridx = 1;
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "Procesando", "Resuelto"});
        cbEstado.setPreferredSize(new Dimension(200, 25));
        form.add(cbEstado, gc);

        // Descripción
        gc.gridx = 0; gc.gridy++;
        gc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Descripción:"), gc);
        gc.gridx = 1;
        txtDescripcion = new JTextArea(6, 30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        form.add(scrollDesc, gc);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Listeners
        btnCancelar.addActionListener(e -> {
            guardado = false;
            dispose();
        });

        btnGuardar.addActionListener(e -> {
            if (validar()) {
                guardarIncidencia();
                guardado = true;
                dispose();
            }
        });

        // cerrar con ESC
        getRootPane().registerKeyboardAction(e -> {
            guardado = false;
            dispose();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void cargarFechaHoy() {
        txtFecha.setText(LocalDate.now().toString());
    }

    private void cargarDatos(Incidencia i) {
        cbTipo.setSelectedItem(i.getTipo());
        txtAula.setText(i.getAula());
        txtFecha.setText(i.getFecha());
        cbEstado.setSelectedItem(i.getEstado());
        txtDescripcion.setText(i.getDescripcion());
    }

    private boolean validar() {
        if (txtAula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Aula es obligatorio.");
            txtAula.requestFocus();
            return false;
        }
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria.");
            txtDescripcion.requestFocus();
            return false;
        }
        return true;
    }

    private void guardarIncidencia() {
        String tipo = cbTipo.getSelectedItem().toString();
        String aula = txtAula.getText().trim();
        String fecha = txtFecha.getText().trim();
        String estado = cbEstado.getSelectedItem().toString();
        String descripcion = txtDescripcion.getText().trim();

        if (incidencia == null) {
            incidencia = new Incidencia(0, tipo, aula, fecha, estado, descripcion);
        } else {
            incidencia.setTipo(tipo);
            incidencia.setAula(aula);
            incidencia.setFecha(fecha);
            incidencia.setEstado(estado);
            incidencia.setDescripcion(descripcion);
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }
}
