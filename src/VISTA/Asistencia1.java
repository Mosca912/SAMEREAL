/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CLASES.Asistencia.Area;
import CLASES.Asistencia.Empleado;
import CONEXIONES.Conexiones;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Facuymayriver
 */
public class Asistencia1 extends javax.swing.JFrame {

    public int x = 0, c = 0, id, id2;
    String emp = "";
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int cont = 0, band = 0;
    String veri, veri2;

    public void refrescarCombo() {
        while (empleado.getItemCount() > 1) {
            empleado.removeItemAt(1); // Siempre elimina el segundo, el resto se va corriendo
        }
    }

    // Métodos para editar
    public class ModeloEditablePorFila extends DefaultTableModel {

        private int editableRow = -1; // fila editable (-1 = ninguna)

        public ModeloEditablePorFila(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            // 🚫 Nunca permitir editar la primera columna (DIA)
            if (column == 0 || column == 1 || column == 2) {
                return false;
            }

            // 🔹 Obtener el día actual del sistema
            int diaActual = java.time.LocalDate.now().getDayOfMonth();

            // 🔹 Obtener el valor de la columna DIA (columna 0)
            Object valorDia = getValueAt(row, 0);

            // Si no hay valor en la columna DIA → no editable
            if (valorDia == null) {
                return false;
            }

            int diaFila;
            try {
                diaFila = Integer.parseInt(valorDia.toString());
            } catch (NumberFormatException e) {
                return false;
            }

            // 🚫 Si el día de la fila es mayor al actual → no editable
            if (diaFila > diaActual) {
                return false;
            }

            // 🔹 Si no hay ninguna fila en modo edición → todo bloqueado
            if (editableRow == -1) {
                return false;
            }

            // ✅ Solo permitir editar la fila seleccionada
            return row == editableRow;
        }

        // 🔓 Permitir editar una fila específica
        public void setEditableRow(int row) {
            this.editableRow = row;
            fireTableRowsUpdated(row, row);
        }

        // 🔒 Bloquear toda la edición
        public void bloquearEdicion() {
            this.editableRow = -1;
            Actualizar.setEnabled(false);
            fireTableDataChanged();
        }

        // (Opcional) Saber qué fila está editable
        public int getEditableRow() {
            return editableRow;
        }
    }

    //Tabla
    ModeloEditablePorFila tabla1 = new ModeloEditablePorFila(
            new String[]{"DIA", "Entrada/HS", "Salida/HS", "Observaciones"}, 0
    );
    //Tabla

    //Tabla
    ModeloEditablePorFila tabla2 = new ModeloEditablePorFila(new String[]{"DIA", "Entrada/HS", "Salida/HS", "Observaciones"}, 0) {
        private final int editableRow = -1;
    };
    //Tabla

    public Asistencia1() {
        initComponents();
        this.setLocationRelativeTo(null);
        empleado.setEnabled(false);
        cargar.setEnabled(false);
        CLASES.Asistencia.Select(con, area);
        Actualizar.setEnabled(false);

        for (int i = 1; i <= 15; i++) {
            Object[] fila = {i, "", "", ""};
            tabla1.addRow(fila);
        }

        for (int i = 16; i <= 31; i++) {
            Object[] fila = {i, "", "", ""};
            tabla2.addRow(fila);
        }

        Tabla.setModel(tabla1);
        Tabla2.setModel(tabla2);

        Tabla.setRowHeight(30);
        Tabla.getTableHeader().setReorderingAllowed(false);

        Tabla.getColumnModel().getColumn(0).setMinWidth(40);
        Tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        Tabla.getColumnModel().getColumn(1).setMinWidth(200);
        Tabla.getColumnModel().getColumn(1).setMaxWidth(200);

        Tabla.getColumnModel().getColumn(2).setMinWidth(200);
        Tabla.getColumnModel().getColumn(2).setMaxWidth(200);

        Tabla2.setRowHeight(30);
        Tabla2.getTableHeader().setReorderingAllowed(false);

        Tabla2.getColumnModel().getColumn(0).setMinWidth(40);
        Tabla2.getColumnModel().getColumn(0).setMaxWidth(40);

        Tabla2.getColumnModel().getColumn(1).setMinWidth(200);
        Tabla2.getColumnModel().getColumn(1).setMaxWidth(200);

        Tabla2.getColumnModel().getColumn(2).setMinWidth(200);
        Tabla2.getColumnModel().getColumn(2).setMaxWidth(200);

        Tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object diaObj = table.getValueAt(row, 0);
                if (diaObj != null) {
                    try {
                        int dia = Integer.parseInt(diaObj.toString());
                        int diaActual = java.time.LocalDate.now().getDayOfMonth();

                        if (dia > diaActual) {
                            c.setBackground(new Color(230, 230, 230)); // gris clarito
                            c.setForeground(Color.DARK_GRAY);
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    } catch (NumberFormatException ex) {
                        c.setBackground(Color.WHITE);
                    }
                }

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }

                return c;
            }
        });

        Tabla2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Object diaObj = table.getValueAt(row, 0);
                if (diaObj != null) {
                    try {
                        int dia = Integer.parseInt(diaObj.toString());
                        int diaActual = java.time.LocalDate.now().getDayOfMonth();

                        if (dia > diaActual) {
                            c.setBackground(new Color(230, 230, 230)); // gris clarito
                            c.setForeground(Color.DARK_GRAY);
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    } catch (NumberFormatException ex) {
                        c.setBackground(Color.WHITE);
                    }
                }

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }

                return c;
            }
        });

        Tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble click
                if (e.getClickCount() == 2 && Tabla.getSelectedRow() != -1) {
                    int filaSeleccionada = Tabla.getSelectedRow();
                    if (filaSeleccionada == -1) {
                        return;
                    }
                    Object valEntrada = Tabla.getValueAt(filaSeleccionada, 1);
                    Object valObserv = Tabla.getValueAt(filaSeleccionada, 3);

                    String Entrada = (valEntrada != null) ? valEntrada.toString().trim() : "";
                    String Observ = (valObserv != null) ? valObserv.toString().trim() : "";

                    if (Entrada.isEmpty() && Observ.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se puede editar esta fila (vacía o sin asistencia).");
                        return;
                    }

                    if (Observ.equalsIgnoreCase("No asistió")) {
                        JOptionPane.showMessageDialog(null, "No se puede modificar un registro de 'No asistió'.");
                        return;
                    }
                    try {
                        int val = CLASES.Asistencia.VerificacionClick(con, Entrada, id2);
                        if (val == 1) {
                            return;
                        }
                        // Obtener fecha y hora actual
                        LocalDateTime ahora = LocalDateTime.now();
                        if (band==2){
                            tabla2.bloquearEdicion();
                        }
                        // Formatear a "2025-10-14 23:14:14"
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String fechaHora = ahora.format(formato);
                        band = 1;
                        Actualizar.setEnabled(true);
                        Tabla.setValueAt(fechaHora, filaSeleccionada, 2);
                        cargar.setEnabled(false);
                        tabla1.setEditableRow(filaSeleccionada); // ✅ habilita solo esa fila
                        Actualizar.setText("Guardar");
                    } catch (SQLException ex) {
                        Logger.getLogger(Asistencia1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });

        Tabla2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble click
                if (e.getClickCount() == 2 && Tabla2.getSelectedRow() != -1) {
                    int filaSeleccionada = Tabla2.getSelectedRow();
                    if (filaSeleccionada == -1) {
                        return;
                    }
                    Object valEntrada = Tabla2.getValueAt(filaSeleccionada, 1);
                    Object valObserv = Tabla2.getValueAt(filaSeleccionada, 3);

                    String Entrada = (valEntrada != null) ? valEntrada.toString().trim() : "";
                    String Observ = (valObserv != null) ? valObserv.toString().trim() : "";

                    if (Entrada.isEmpty() && Observ.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No se puede editar esta fila (vacía o sin asistencia).");
                        return;
                    }

                    if (Observ.equalsIgnoreCase("No asistió")) {
                        JOptionPane.showMessageDialog(null, "No se puede modificar un registro de 'No asistió'.");
                        return;
                    }
                    try {
                        int val = CLASES.Asistencia.VerificacionClick(con, Entrada, id2);
                        if (val == 1) {
                            return;
                        }
                        // Obtener fecha y hora actual
                        LocalDateTime ahora = LocalDateTime.now();
                        if (band==1){
                            tabla1.bloquearEdicion();
                        }
                        // Formatear a "2025-10-14 23:14:14"
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String fechaHora = ahora.format(formato);
                        band = 2;
                        Actualizar.setEnabled(true);
                        Tabla2.setValueAt(fechaHora, filaSeleccionada, 2);
                        cargar.setEnabled(false);
                        tabla2.setEditableRow(filaSeleccionada); // ✅ habilita solo esa fila
                        Actualizar.setText("Guardar");
                    } catch (SQLException ex) {
                        Logger.getLogger(Asistencia1.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });

        InputMap inputMap = Tabla.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = Tabla.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "bloquearEdicion");

        actionMap.put("bloquearEdicion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (band == 1) {
                    int filaSeleccionada = Tabla.getSelectedRow();
                    if (filaSeleccionada == -1) {
                        return;
                    } else {
                        Tabla.setValueAt("", filaSeleccionada, 2);
                    }
                    tabla1.bloquearEdicion();
                } else if (band == 2) {
                    int filaSeleccionada = Tabla2.getSelectedRow();
                    if (filaSeleccionada == -1) {
                        return;
                    } else {
                        Tabla2.setValueAt("", filaSeleccionada, 2);
                    }
                    tabla2.bloquearEdicion();
                }
                cargar.setEnabled(true);
                Actualizar.setEnabled(false);
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

        jPanel1 = new javax.swing.JPanel();
        Menu = new javax.swing.JButton();
        Movimientos = new javax.swing.JButton();
        Asistencia = new javax.swing.JButton();
        Salir = new javax.swing.JButton();
        Empleados = new javax.swing.JButton();
        Estadisticas = new javax.swing.JButton();
        Ayuda = new javax.swing.JButton();
        Configuracion = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        Actualizar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        empleado = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        area = new javax.swing.JComboBox<>();
        cargar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla2 = new javax.swing.JTable();
        terminar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        Opciones2 = new javax.swing.JMenu();
        Menu2 = new javax.swing.JMenuItem();
        Movimientos1 = new javax.swing.JMenuItem();
        Asistencia2 = new javax.swing.JMenuItem();
        Empleados2 = new javax.swing.JMenuItem();
        Estadisticas2 = new javax.swing.JMenuItem();
        Ayuda2 = new javax.swing.JMenuItem();
        Configuracion2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Asistencia");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 102, 0));
        jPanel1.setForeground(new java.awt.Color(153, 153, 0));

        Menu.setBackground(new java.awt.Color(0, 102, 0));
        Menu.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Menu.setForeground(new java.awt.Color(255, 255, 255));
        Menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menui.png"))); // NOI18N
        Menu.setText("Menu");
        Menu.setToolTipText("ir al menu");
        Menu.setBorder(null);
        Menu.setContentAreaFilled(false);
        Menu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Menu.setDefaultCapable(false);
        Menu.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menui2.png"))); // NOI18N
        Menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuActionPerformed(evt);
            }
        });

        Movimientos.setBackground(new java.awt.Color(0, 102, 0));
        Movimientos.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Movimientos.setForeground(new java.awt.Color(255, 255, 255));
        Movimientos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/movimiento.png"))); // NOI18N
        Movimientos.setText("Movimientos");
        Movimientos.setToolTipText("ir a movimientos");
        Movimientos.setBorder(null);
        Movimientos.setContentAreaFilled(false);
        Movimientos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Movimientos.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/movimiento2.png"))); // NOI18N
        Movimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MovimientosActionPerformed(evt);
            }
        });

        Asistencia.setBackground(new java.awt.Color(0, 102, 0));
        Asistencia.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Asistencia.setForeground(new java.awt.Color(255, 255, 255));
        Asistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"))); // NOI18N
        Asistencia.setText("Asistencia");
        Asistencia.setToolTipText("ir a asistencia");
        Asistencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Asistencia.setContentAreaFilled(false);
        Asistencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Asistencia.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia2.png"))); // NOI18N
        Asistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AsistenciaActionPerformed(evt);
            }
        });

        Salir.setBackground(new java.awt.Color(0, 102, 0));
        Salir.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Salir.setForeground(new java.awt.Color(255, 255, 255));
        Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/salir.png"))); // NOI18N
        Salir.setText("Salir");
        Salir.setToolTipText("Salir del programa");
        Salir.setBorderPainted(false);
        Salir.setContentAreaFilled(false);
        Salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Salir.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/salir2.png"))); // NOI18N
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });

        Empleados.setBackground(new java.awt.Color(0, 102, 0));
        Empleados.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Empleados.setForeground(new java.awt.Color(255, 255, 255));
        Empleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado.png"))); // NOI18N
        Empleados.setText("Empleados");
        Empleados.setToolTipText("ir a empleados");
        Empleados.setBorderPainted(false);
        Empleados.setContentAreaFilled(false);
        Empleados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Empleados.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado2.png"))); // NOI18N
        Empleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmpleadosActionPerformed(evt);
            }
        });

        Estadisticas.setBackground(new java.awt.Color(0, 102, 0));
        Estadisticas.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Estadisticas.setForeground(new java.awt.Color(255, 255, 255));
        Estadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/estats.png"))); // NOI18N
        Estadisticas.setText("Estadisticas");
        Estadisticas.setToolTipText("ir a estadisticas");
        Estadisticas.setBorderPainted(false);
        Estadisticas.setContentAreaFilled(false);
        Estadisticas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Estadisticas.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/estats2.png"))); // NOI18N
        Estadisticas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EstadisticasActionPerformed(evt);
            }
        });

        Ayuda.setBackground(new java.awt.Color(0, 102, 0));
        Ayuda.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Ayuda.setForeground(new java.awt.Color(255, 255, 255));
        Ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/ayuda.png"))); // NOI18N
        Ayuda.setText("Ayuda");
        Ayuda.setToolTipText("ir a ayuda");
        Ayuda.setBorderPainted(false);
        Ayuda.setContentAreaFilled(false);
        Ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ayuda.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/ayuda2.png"))); // NOI18N
        Ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AyudaActionPerformed(evt);
            }
        });

        Configuracion.setBackground(new java.awt.Color(0, 102, 0));
        Configuracion.setFont(new java.awt.Font("Dubai Medium", 1, 14)); // NOI18N
        Configuracion.setForeground(new java.awt.Color(255, 255, 255));
        Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/config.png"))); // NOI18N
        Configuracion.setText("Configuración");
        Configuracion.setToolTipText("ir a configuración");
        Configuracion.setBorderPainted(false);
        Configuracion.setContentAreaFilled(false);
        Configuracion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Configuracion.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/config2.png"))); // NOI18N
        Configuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfiguracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Movimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Asistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Empleados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Estadisticas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Ayuda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Configuracion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(Salir))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(Menu)
                .addComponent(Asistencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Empleados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Estadisticas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Ayuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Salir)
                .addComponent(Configuracion)
                .addComponent(Movimientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(153, 255, 204));

        jPanel4.setBackground(new java.awt.Color(177, 177, 213));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        Tabla.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(Tabla);

        Actualizar.setText("Guardar");
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Actualizar)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(Actualizar)
                .addGap(4, 4, 4))
        );

        jPanel6.setBackground(new java.awt.Color(177, 177, 213));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Nueva asistencia");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(220, 220, 220))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        empleado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        empleado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empleadoActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Empleado:");

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Area:");

        area.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        area.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                areaActionPerformed(evt);
            }
        });

        cargar.setText("Cargar");
        cargar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(cargar)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(area)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cargar))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(177, 177, 213));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Tabla2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(Tabla2);

        terminar.setText("Terminar");
        terminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(terminar)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(terminar)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(19, 19, 19))))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        Opciones2.setText("Opciones");
        Opciones2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        Menu2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        Menu2.setText("Menu");
        Menu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu2ActionPerformed(evt);
            }
        });
        Opciones2.add(Menu2);

        Movimientos1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        Movimientos1.setText("Movimientos");
        Movimientos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Movimientos1ActionPerformed(evt);
            }
        });
        Opciones2.add(Movimientos1);

        Asistencia2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        Asistencia2.setText("Asistencia");
        Asistencia2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Asistencia2ActionPerformed(evt);
            }
        });
        Opciones2.add(Asistencia2);

        Empleados2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        Empleados2.setText("Empleados");
        Empleados2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Empleados2ActionPerformed(evt);
            }
        });
        Opciones2.add(Empleados2);

        Estadisticas2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        Estadisticas2.setText("Estadisticas");
        Estadisticas2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Estadisticas2ActionPerformed(evt);
            }
        });
        Opciones2.add(Estadisticas2);

        Ayuda2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        Ayuda2.setText("Ayuda");
        Ayuda2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ayuda2ActionPerformed(evt);
            }
        });
        Opciones2.add(Ayuda2);

        Configuracion2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        Configuracion2.setText("Configuracion");
        Configuracion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Configuracion2ActionPerformed(evt);
            }
        });
        Opciones2.add(Configuracion2);

        jMenuBar1.add(Opciones2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EstadisticasActionPerformed
        Estadisticas ventana = new Estadisticas();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_EstadisticasActionPerformed

    private void AsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AsistenciaActionPerformed

    }//GEN-LAST:event_AsistenciaActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_SalirActionPerformed

    private void ConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfiguracionActionPerformed
        Configuracion ventana = new Configuracion();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ConfiguracionActionPerformed

    private void MenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuActionPerformed
        Menu ventana = new Menu();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MenuActionPerformed

    private void MovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MovimientosActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MovimientosActionPerformed

    private void EmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmpleadosActionPerformed
        Empleados ventana = new Empleados();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_EmpleadosActionPerformed

    private void AyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AyudaActionPerformed
        Ayuda ventana = new Ayuda();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AyudaActionPerformed

    private void Menu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu2ActionPerformed
        Menu ventana = new Menu();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Menu2ActionPerformed

    private void Movimientos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Movimientos1ActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Movimientos1ActionPerformed

    private void Asistencia2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Asistencia2ActionPerformed

    }//GEN-LAST:event_Asistencia2ActionPerformed

    private void Empleados2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Empleados2ActionPerformed
        Empleados ventana = new Empleados();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Empleados2ActionPerformed

    private void Estadisticas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Estadisticas2ActionPerformed
        Estadisticas ventana = new Estadisticas();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Estadisticas2ActionPerformed

    private void Ayuda2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ayuda2ActionPerformed
        Ayuda ventana = new Ayuda();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Ayuda2ActionPerformed

    private void Configuracion2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Configuracion2ActionPerformed
        Configuracion ventana = new Configuracion();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_Configuracion2ActionPerformed

    private void areaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_areaActionPerformed
        Object value = area.getSelectedItem();
        if (value instanceof Area) {
            Area dat = (Area) value;
            id = dat.getId();
            veri = dat.getNombre();

            if (!veri.equals("Opciones")) {
                empleado.setEnabled(true);
                Actualizar.setEnabled(false);
                refrescarCombo();
                CLASES.Asistencia.jCombo(con, empleado, id);
                empleado.setSelectedIndex(0);
            } else {
                empleado.setEnabled(false);
                Actualizar.setEnabled(false);
                refrescarCombo();
                empleado.setSelectedIndex(0);
                tabla1.bloquearEdicion();
                tabla2.bloquearEdicion();
            }
        }
    }//GEN-LAST:event_areaActionPerformed

    private void empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empleadoActionPerformed
        Object value = empleado.getSelectedItem();
        if (value instanceof Empleado) {
            Empleado dat = (Empleado) value;
            id2 = dat.getId();
            veri2 = dat.getNombre();
            if (!veri2.equals("Opciones")) {
                Menu.setEnabled(false);
                Movimientos.setEnabled(false);
                Asistencia.setEnabled(false);
                Empleados.setEnabled(false);
                Estadisticas.setEnabled(false);
                Ayuda.setEnabled(false);
                Configuracion.setEnabled(false);
                Salir.setEnabled(false);
                terminar.setEnabled(false);
                cargar.setEnabled(true);

                try {
                    CLASES.Asistencia.Verificacion(con, id2);
                    //CLASES.Asistencia.Verificacion(con, cargar, generar, id2);
                } catch (SQLException ex) {
                    Logger.getLogger(Asistencia1.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Asistencia1.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    CLASES.Asistencia.MostrarTabla(con, id2, Tabla, Tabla2);
                    //CLASES.Asistencia.Verificacion(con, cargar, generar, id2);
                } catch (SQLException ex) {
                    Logger.getLogger(Asistencia1.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                cargar.setEnabled(false);
                terminar.setEnabled(true);
                Menu.setEnabled(true);
                Movimientos.setEnabled(true);
                Asistencia.setEnabled(true);
                Empleados.setEnabled(true);
                Estadisticas.setEnabled(true);
                Ayuda.setEnabled(true);
                Configuracion.setEnabled(true);
                Salir.setEnabled(true);

                // Limpiar la tabla
                tabla1.setRowCount(0);
                tabla2.setRowCount(0);

                // Primero agregás todos los días con vacío
                for (int i = 1; i <= 15; i++) {
                    tabla1.addRow(new Object[]{i, "", "", ""});
                }

                // Primero agregás todos los días con vacío
                for (int i = 16; i <= 31; i++) {
                    tabla2.addRow(new Object[]{i, "", "", ""});
                }
            }
        }
    }//GEN-LAST:event_empleadoActionPerformed

    private void cargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarActionPerformed

        try {
            CLASES.Asistencia.AsistenciaReal(con, id2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "error principal" + ex);
        }
        try {
            CLASES.Asistencia.MostrarTabla(con, id2, Tabla, Tabla2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar categorías: " + e);
        }
    }//GEN-LAST:event_cargarActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed
        if (band == 1) {
            int filaSeleccionada = Tabla.getSelectedRow();
            if (filaSeleccionada != -1) {

                int Dia = Integer.parseInt(Tabla.getValueAt(filaSeleccionada, 0).toString());
                String Entrada = Tabla.getValueAt(filaSeleccionada, 1).toString();
                String Observaciones = Tabla.getValueAt(filaSeleccionada, 3).toString();

                try {
                    CLASES.Asistencia.ActualizarAsistencia(con, Dia, Entrada, Observaciones, id2);
                    JOptionPane.showMessageDialog(null, "Fila actualizada correctamente.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "⚠️ Error al actualizar: " + ex.getMessage());
                }
                Actualizar.setText("Guardar");
                Actualizar.setEnabled(false);
                try {
                    CLASES.Asistencia.MostrarTabla(con, id2, Tabla, Tabla2);
                } catch (SQLException er) {
                    JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay fila seleccionada.");
            }
        } else if (band == 2) {
            int filaSeleccionada = Tabla2.getSelectedRow();
            if (filaSeleccionada != -1) {

                int Dia = Integer.parseInt(Tabla2.getValueAt(filaSeleccionada, 0).toString());
                String Entrada = Tabla2.getValueAt(filaSeleccionada, 1).toString();
                String Observaciones = Tabla2.getValueAt(filaSeleccionada, 3).toString();

                try {
                    CLASES.Asistencia.ActualizarAsistencia(con, Dia, Entrada, Observaciones, id2);
                    JOptionPane.showMessageDialog(null, "Fila actualizada correctamente.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "⚠️ Error al actualizar: " + ex.getMessage());
                }
                Actualizar.setText("Guardar");
                Actualizar.setEnabled(false);
                try {
                    CLASES.Asistencia.MostrarTabla(con, id2, Tabla, Tabla2);
                } catch (SQLException er) {
                    JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay fila seleccionada.");
            }
        }
    }//GEN-LAST:event_ActualizarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            CLASES.Asistencia.Verificacion2(con, id);
        } catch (Exception ex) {
            Logger.getLogger(Asistencia1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Asistencia1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Actualizar;
    private javax.swing.JButton Asistencia;
    private javax.swing.JMenuItem Asistencia2;
    private javax.swing.JButton Ayuda;
    private javax.swing.JMenuItem Ayuda2;
    private javax.swing.JButton Configuracion;
    private javax.swing.JMenuItem Configuracion2;
    private javax.swing.JButton Empleados;
    private javax.swing.JMenuItem Empleados2;
    private javax.swing.JButton Estadisticas;
    private javax.swing.JMenuItem Estadisticas2;
    private javax.swing.JButton Menu;
    private javax.swing.JMenuItem Menu2;
    private javax.swing.JButton Movimientos;
    private javax.swing.JMenuItem Movimientos1;
    private javax.swing.JMenu Opciones2;
    private javax.swing.JButton Salir;
    private javax.swing.JTable Tabla;
    private javax.swing.JTable Tabla2;
    private javax.swing.JComboBox<Area> area;
    private javax.swing.JButton cargar;
    private javax.swing.JComboBox<Empleado> empleado;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JButton terminar;
    // End of variables declaration//GEN-END:variables
}
