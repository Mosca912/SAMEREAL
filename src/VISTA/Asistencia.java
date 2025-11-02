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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Facuymayriver
 */
public class Asistencia extends javax.swing.JFrame implements CLASES.IBlockableFrame {

    public int x = 0, c = 0, id, id2;
    String emp = "";
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int cont = 0, band = 0;
    public int block = 0, block2=0;
    String veri, veri2;

    public void refrescarCombo() {
        while (empleado.getItemCount() > 1) {
            empleado.removeItemAt(1); // Siempre elimina el segundo, el resto se va corriendo
        }
    }
    
    public int getBlockStateSalir() {
        return this.block2;
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
            new String[]{"Día", "Entrada/HS", "Salida/HS", "Observaciones"}, 0
    );
    //Tabla

    public Asistencia() {
        CLASES.MenuClass.Configuracion();

        initComponents();
        this.setLocationRelativeTo(null);
        CLASES.MenuClass menuHelper = new CLASES.MenuClass();
        menuHelper.MenuConfig(Movimientos, Menu, Asistencia, Empleados, Estadisticas, Ayuda, Configuracion, Salir, this);

        int ventana = CLASES.MenuClass.Ventana();
        if (ventana == 0) {
            this.setExtendedState(NORMAL);
            String rutaImagen = "/IMAGENES/logosameasis.png";
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
            logito.setIcon(icono);
        } else if (ventana == 1) {
            this.setExtendedState(MAXIMIZED_BOTH);
            String rutaImagen = "/IMAGENES/logosameasismax.png";
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
            logito.setIcon(icono);
        }

        empleado.setEnabled(false);
        cargar.setEnabled(false);
        CLASES.Asistencia.Select(con, area);
        Actualizar.setEnabled(false);

        for (int i = 1; i <= 31; i++) {
            Object[] fila = {i, "", "", ""};
            tabla1.addRow(fila);
        }

        Tabla.setModel(tabla1);

        Tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        Tabla.getTableHeader().setPreferredSize(new Dimension(0, 30));
        Tabla.setRowHeight(30);
        Tabla.setFont(new Font("Arial", Font.BOLD, 15));
        Tabla.getTableHeader().setReorderingAllowed(false);

        Tabla.getColumnModel().getColumn(0).setMinWidth(40);
        Tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        Tabla.getColumnModel().getColumn(1).setMinWidth(200);
        Tabla.getColumnModel().getColumn(1).setMaxWidth(200);

        Tabla.getColumnModel().getColumn(2).setMinWidth(200);
        Tabla.getColumnModel().getColumn(2).setMaxWidth(200);

        Tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                setHorizontalAlignment(SwingConstants.CENTER);

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
                        Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
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
                int filaSeleccionada = Tabla.getSelectedRow();
                if (filaSeleccionada == -1) {
                    return;
                } else {
                    Object valEntrada = Tabla.getValueAt(filaSeleccionada, 1);
                    String Entrada = (valEntrada != null) ? valEntrada.toString().trim() : "";
                    try {
                        int val = CLASES.Asistencia.VerificacionClick(con, Entrada, id2);
                        if (val == 1) {
                            return;
                        } else {
                            Tabla.setValueAt("", filaSeleccionada, 2);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                tabla1.bloquearEdicion();
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

        PanelBlanco = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        empleado = new javax.swing.JComboBox<>();
        area = new javax.swing.JComboBox<>();
        cargar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        Actualizar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        logito = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        Barra = new javax.swing.JMenuBar();
        Menu = new javax.swing.JMenu();
        incmen = new javax.swing.JMenuItem();
        Movimientos = new javax.swing.JMenu();
        iniciomov = new javax.swing.JMenuItem();
        nuevtrip = new javax.swing.JMenuItem();
        nuevovic = new javax.swing.JMenuItem();
        historial = new javax.swing.JMenuItem();
        Asistencia = new javax.swing.JMenu();
        inicioas = new javax.swing.JMenuItem();
        Empleados = new javax.swing.JMenu();
        inicemp = new javax.swing.JMenuItem();
        cargoemp = new javax.swing.JMenu();
        mod = new javax.swing.JMenuItem();
        elim = new javax.swing.JMenuItem();
        Estadisticas = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        Ayuda = new javax.swing.JMenu();
        Configuracion = new javax.swing.JMenu();
        Salir = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Asistencia");
        setMaximumSize(new java.awt.Dimension(1200, 700));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setUndecorated(true);
        setResizable(false);

        PanelBlanco.setBackground(new java.awt.Color(255, 255, 255));
        PanelBlanco.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PanelBlanco.setMaximumSize(new java.awt.Dimension(1200, 700));
        PanelBlanco.setMinimumSize(new java.awt.Dimension(1200, 700));
        PanelBlanco.setPreferredSize(new java.awt.Dimension(1200, 700));

        jPanel6.setBackground(new java.awt.Color(52, 170, 121));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        empleado.setBackground(new java.awt.Color(52, 170, 121));
        empleado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        empleado.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        empleado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empleadoActionPerformed(evt);
            }
        });

        area.setBackground(new java.awt.Color(52, 170, 121));
        area.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        area.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Area", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        area.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                areaActionPerformed(evt);
            }
        });

        cargar.setBackground(new java.awt.Color(78, 247, 177));
        cargar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cargar.setText("Cargar");
        cargar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"))); // NOI18N
        jLabel4.setText("Nueva asistencia:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(39, 39, 39)
                .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(cargar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(area)
                        .addComponent(empleado)
                        .addComponent(cargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Actualizar.setBackground(new java.awt.Color(78, 247, 177));
        Actualizar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Actualizar.setText("Guardar");
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout PanelBlancoLayout = new javax.swing.GroupLayout(PanelBlanco);
        PanelBlanco.setLayout(PanelBlancoLayout);
        PanelBlancoLayout.setHorizontalGroup(
            PanelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBlancoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelBlancoLayout.createSequentialGroup()
                        .addGroup(PanelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PanelBlancoLayout.createSequentialGroup()
                                .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(743, 743, 743))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelBlancoLayout.setVerticalGroup(
            PanelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBlancoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBlancoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelBlancoLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        getContentPane().add(PanelBlanco, java.awt.BorderLayout.CENTER);

        Barra.setBackground(new java.awt.Color(52, 170, 121));
        Barra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Menu.setBackground(new java.awt.Color(204, 255, 204));
        Menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menui.png"))); // NOI18N
        Menu.setText("Menu");
        Menu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Menu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuActionPerformed(evt);
            }
        });

        incmen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        incmen.setText("Inicio");
        incmen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incmenActionPerformed(evt);
            }
        });
        Menu.add(incmen);

        Barra.add(Menu);

        Movimientos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/movimiento.png"))); // NOI18N
        Movimientos.setText("Movimientos");
        Movimientos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Movimientos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        iniciomov.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        iniciomov.setText("Inicio");
        iniciomov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciomovActionPerformed(evt);
            }
        });
        Movimientos.add(iniciomov);

        nuevtrip.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nuevtrip.setText("Nueva tripulación");
        nuevtrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevtripActionPerformed(evt);
            }
        });
        Movimientos.add(nuevtrip);

        nuevovic.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nuevovic.setText("Nuevo victor");
        nuevovic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevovicActionPerformed(evt);
            }
        });
        Movimientos.add(nuevovic);

        historial.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        historial.setText("Historial");
        historial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historialActionPerformed(evt);
            }
        });
        Movimientos.add(historial);

        Barra.add(Movimientos);

        Asistencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"))); // NOI18N
        Asistencia.setText("Asistencia");
        Asistencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Asistencia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Asistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AsistenciaActionPerformed(evt);
            }
        });

        inicioas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inicioas.setText("Inicio");
        inicioas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioasActionPerformed(evt);
            }
        });
        Asistencia.add(inicioas);

        Barra.add(Asistencia);

        Empleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado.png"))); // NOI18N
        Empleados.setText("Empleados");
        Empleados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Empleados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        inicemp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inicemp.setText("Inicio");
        inicemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicempActionPerformed(evt);
            }
        });
        Empleados.add(inicemp);

        cargoemp.setBackground(new java.awt.Color(52, 170, 121));
        cargoemp.setText("Cargo");
        cargoemp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        mod.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        mod.setText("Modificar");
        mod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modActionPerformed(evt);
            }
        });
        cargoemp.add(mod);

        elim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        elim.setText("Eliminar");
        elim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elimActionPerformed(evt);
            }
        });
        cargoemp.add(elim);

        Empleados.add(cargoemp);

        Barra.add(Empleados);

        Estadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/estats.png"))); // NOI18N
        Estadisticas.setText("Estadisticas");
        Estadisticas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Estadisticas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem1.setText("Consultar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        Estadisticas.add(jMenuItem1);

        Barra.add(Estadisticas);

        Ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/ayuda.png"))); // NOI18N
        Ayuda.setText("Ayuda");
        Ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ayuda.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Barra.add(Ayuda);

        Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/config.png"))); // NOI18N
        Configuracion.setText("Configuración");
        Configuracion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Configuracion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Barra.add(Configuracion);

        Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/salir.png"))); // NOI18N
        Salir.setText("Salir");
        Salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Salir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Barra.add(Salir);

        setJMenuBar(Barra);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
                block = 0;
                block2=0;
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
                cargar.setEnabled(true);
                block = 1;
                block2 = 1;

                try {
                    CLASES.Asistencia.Verificacion(con, id2);
                    //CLASES.Asistencia.Verificacion(con, cargar, generar, id2);
                } catch (SQLException ex) {
                    Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    CLASES.Asistencia.MostrarTabla(con, id2, Tabla);
                    //CLASES.Asistencia.Verificacion(con, cargar, generar, id2);
                } catch (SQLException ex) {
                    Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                cargar.setEnabled(false);
                Menu.setEnabled(true);
                Movimientos.setEnabled(true);
                Asistencia.setEnabled(true);
                Empleados.setEnabled(true);
                Estadisticas.setEnabled(true);
                Ayuda.setEnabled(true);
                Configuracion.setEnabled(true);
                Salir.setEnabled(true);
                block = 0;
                block = 1;
                // Limpiar la tabla
                tabla1.setRowCount(0);

                // Primero agregás todos los días con vacío
                for (int i = 1; i <= 31; i++) {
                    tabla1.addRow(new Object[]{i, "", "", ""});
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
            CLASES.Asistencia.MostrarTabla(con, id2, Tabla);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar categorías: " + e);
        }
    }//GEN-LAST:event_cargarActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed

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
            tabla1.bloquearEdicion();
            try {
                CLASES.Asistencia.MostrarTabla(con, id2, Tabla);
            } catch (SQLException er) {
                JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay fila seleccionada.");
        }
    }//GEN-LAST:event_ActualizarActionPerformed

    private void incmenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incmenActionPerformed
        Menu ventana = new Menu();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_incmenActionPerformed

    private void iniciomovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciomovActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_iniciomovActionPerformed

    private void nuevtripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevtripActionPerformed
        AddTri ventana = new AddTri(0, this);
        ventana.setVisible(true);
    }//GEN-LAST:event_nuevtripActionPerformed

    private void nuevovicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevovicActionPerformed
        AddVic ventana = new AddVic(this);
        ventana.setVisible(true);
    }//GEN-LAST:event_nuevovicActionPerformed

    private void historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialActionPerformed
        Historial ventana = new Historial(this);
        ventana.setVisible(true);
    }//GEN-LAST:event_historialActionPerformed

    private void inicioasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioasActionPerformed
        Asistencia ventana = new Asistencia();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_inicioasActionPerformed

    private void AsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AsistenciaActionPerformed
        Asistencia ventana = new Asistencia();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AsistenciaActionPerformed

    private void inicempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicempActionPerformed
        Empleados ventana = new Empleados();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_inicempActionPerformed

    private void modActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(1, this);
        ventana.setVisible(true);
    }//GEN-LAST:event_modActionPerformed

    private void elimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(0, this);
        ventana.setVisible(true);
    }//GEN-LAST:event_elimActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Estadisticas ventana = new Estadisticas();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void MenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuActionPerformed

    }//GEN-LAST:event_MenuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Asistencia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Actualizar;
    private javax.swing.JMenu Asistencia;
    private javax.swing.JMenu Ayuda;
    private javax.swing.JMenuBar Barra;
    private javax.swing.JMenu Configuracion;
    private javax.swing.JMenu Empleados;
    private javax.swing.JMenu Estadisticas;
    private javax.swing.JMenu Menu;
    private javax.swing.JMenu Movimientos;
    private javax.swing.JPanel PanelBlanco;
    private javax.swing.JMenu Salir;
    private javax.swing.JTable Tabla;
    private javax.swing.JComboBox<Area> area;
    private javax.swing.JButton cargar;
    private javax.swing.JMenu cargoemp;
    private javax.swing.JMenuItem elim;
    private javax.swing.JComboBox<Empleado> empleado;
    private javax.swing.JMenuItem historial;
    private javax.swing.JMenuItem incmen;
    private javax.swing.JMenuItem inicemp;
    private javax.swing.JMenuItem inicioas;
    private javax.swing.JMenuItem iniciomov;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logito;
    private javax.swing.JMenuItem mod;
    private javax.swing.JMenuItem nuevovic;
    private javax.swing.JMenuItem nuevtrip;
    // End of variables declaration//GEN-END:variables
    @Override
    public int getBlockState() {
        return this.block;
    }
}
