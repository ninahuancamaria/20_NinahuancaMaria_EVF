package vallegrande.edu.pe.view;

import vallegrande.edu.pe.controller.IncidenciaController;
import vallegrande.edu.pe.model.Incidencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class IncidenciaView extends JFrame {

    private IncidenciaController controller = new IncidenciaController();

    private JTable tabla;
    private DefaultTableModel tablaModel;
    private JTextField txtBuscar;
    private JButton btnNuevo, btnEditar, btnEliminar, btnBuscar, btnReportePendientes, btnRefrescar;

    public IncidenciaView() {
        initComponents();
        cargarTabla();
        setTitle("Sistema de Registro de Incidencias Técnicas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Panel superior - búsqueda y acciones
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        txtBuscar = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnReportePendientes = new JButton("Reporte: Pendientes");
        btnRefrescar = new JButton("Refrescar");

        topPanel.add(new JLabel("Buscar (tipo/aula/estado):"));
        topPanel.add(txtBuscar);
        topPanel.add(btnBuscar);
        topPanel.add(btnRefrescar);
        topPanel.add(btnNuevo);
        topPanel.add(btnEditar);
        topPanel.add(btnEliminar);
        topPanel.add(btnReportePendientes);

        // Tabla
        tablaModel = new DefaultTableModel(new Object[]{
                "ID", "Tipo", "Aula", "Fecha", "Estado", "Descripción"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editable directamente
            }
        };
        tabla = new JTable(tablaModel);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(300);

        JScrollPane scroll = new JScrollPane(tabla);

        // Añadir a frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        // Listeners
        btnNuevo.addActionListener(e -> abrirFormularioNuevo());
        btnEditar.addActionListener(e -> abrirFormularioEditar());
        btnEliminar.addActionListener(e -> eliminarSeleccion());
        btnBuscar.addActionListener(e -> buscar());
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnReportePendientes.addActionListener(e -> mostrarReportePendientes());

        // Enter en campo Buscar ejecuta buscar
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) buscar();
            }
        });
    }

    private void cargarTabla() {
        List<Incidencia> lista = controller.listar();
        cargarModelo(lista);
    }

    private void cargarModelo(List<Incidencia> lista) {
        tablaModel.setRowCount(0);
        for (Incidencia i : lista) {
            tablaModel.addRow(new Object[]{
                    i.getId(),
                    i.getTipo(),
                    i.getAula(),
                    i.getFecha(),
                    i.getEstado(),
                    i.getDescripcion()
            });
        }
    }

    private void abrirFormularioNuevo() {
        IncidenciaForm form = new IncidenciaForm(this, "Registrar Incidencia", null);
        form.setVisible(true);
        if (form.isGuardado()) {
            // registrar
            Incidencia nueva = form.getIncidencia();
            boolean ok = controller.registrar(nueva);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Incidencia registrada.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Revisa la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirFormularioEditar() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia para editar.");
            return;
        }
        int id = Integer.parseInt(tablaModel.getValueAt(row, 0).toString());
        String tipo = tablaModel.getValueAt(row, 1).toString();
        String aula = tablaModel.getValueAt(row, 2).toString();
        String fecha = tablaModel.getValueAt(row, 3).toString();
        String estado = tablaModel.getValueAt(row, 4).toString();
        String descripcion = tablaModel.getValueAt(row, 5).toString();

        Incidencia existente = new Incidencia(id, tipo, aula, fecha, estado, descripcion);
        IncidenciaForm form = new IncidenciaForm(this, "Editar Incidencia", existente);
        form.setVisible(true);
        if (form.isGuardado()) {
            Incidencia editada = form.getIncidencia();
            boolean ok = controller.actualizar(editada);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Incidencia actualizada.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarSeleccion() {
        int row = tabla.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una incidencia para eliminar.");
            return;
        }
        int id = Integer.parseInt(tablaModel.getValueAt(row, 0).toString());
        int opcion = JOptionPane.showConfirmDialog(this, "¿Eliminar incidencia ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = controller.eliminar(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Incidencia eliminada.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarTabla();
            return;
        }
        List<Incidencia> resultado = controller.buscar(filtro);
        cargarModelo(resultado);
    }

    private void mostrarReportePendientes() {
        // filtrar en memoria (también se puede mediante DAO con WHERE)
        List<Incidencia> lista = controller.listar();
        DefaultTableModel modeloReporte = new DefaultTableModel(new Object[]{
                "ID", "Tipo", "Aula", "Fecha", "Estado", "Descripción"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column){ return false; }
        };
        for (Incidencia i : lista) {
            if ("Pendiente".equalsIgnoreCase(i.getEstado())) {
                modeloReporte.addRow(new Object[]{
                        i.getId(), i.getTipo(), i.getAula(), i.getFecha(), i.getEstado(), i.getDescripcion()
                });
            }
        }

        JTable tablaReporte = new JTable(modeloReporte);
        JScrollPane scroll = new JScrollPane(tablaReporte);
        scroll.setPreferredSize(new Dimension(800, 400));
        JOptionPane.showMessageDialog(this, scroll, "Incidencias Pendientes", JOptionPane.INFORMATION_MESSAGE);
    }

    // main para probar la vista independiente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IncidenciaView().setVisible(true);
        });
    }
}
