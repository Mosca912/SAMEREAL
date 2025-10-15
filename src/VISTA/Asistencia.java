/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CLASES.Asistencia.Area;
import CLASES.Asistencia.Empleado;
import CONEXIONES.Conexiones;
import com.itextpdf.text.DocumentException;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Facuymayriver
 */
public class Asistencia extends javax.swing.JFrame {

    public int x = 0, c = 0, id, id2;
    String emp = "";
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int cont = 0;
    String veri, veri2;

    public void refrescarCombo() {
        while (empleado.getItemCount() > 1) {
            empleado.removeItemAt(1); // Siempre elimina el segundo, el resto se va corriendo
        }
    }

    public void refrescarTablaEmpleados() {
        tabla1.setRowCount(0); // Limpia
        try {
            CLASES.Empleados.MostrarEmpleados(con, tabla1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
        }
    }

    //Metodos para editar
    public class ModeloEditablePorFila extends DefaultTableModel {

        private int editableRow = -1;

        public ModeloEditablePorFila(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false; // Nunca editar la columna ID
            }
            Object cod = getValueAt(row, 0);

            // Si es una fila nueva → editable
            if (cod == null || cod.toString().trim().isEmpty()) {
                return true;
            }

            // Si estamos en modo edición manual → solo esa fila
            return row == editableRow;
        }

        public void setEditableRow(int row) {
            this.editableRow = row;
            fireTableRowsUpdated(row, row);
        }

        public void bloquearEdicion() {
            this.editableRow = -1;
            fireTableDataChanged();
        }
    }
    //Metodos para editar

    //Tabla
    ModeloEditablePorFila tabla1 = new ModeloEditablePorFila(new String[]{"Cod", "Apellido", "Cargo", "Base", "Entrada", "Salida"}, 0) {
        private final int editableRow = -1;
    };
    //Tabla

    public Asistencia() {
        initComponents();
        this.setLocationRelativeTo(null);
        empleado.setEnabled(false);
        cargar.setEnabled(false);
        generar.setEnabled(false);
        CLASES.Asistencia.Select(con, area);

        Tabla.setModel(tabla1);
        Tabla.setRowHeight(30);
        Tabla.getTableHeader().setReorderingAllowed(false);

        Tabla.getColumnModel().getColumn(0).setMinWidth(40);
        Tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        Tabla.getColumnModel().getColumn(1).setMinWidth(80);
        Tabla.getColumnModel().getColumn(1).setMaxWidth(80);

        Tabla.getColumnModel().getColumn(2).setMinWidth(70);
        Tabla.getColumnModel().getColumn(2).setMaxWidth(70);

        Tabla.getColumnModel().getColumn(3).setMinWidth(60);
        Tabla.getColumnModel().getColumn(3).setMaxWidth(60);

        Tabla.getColumnModel().getColumn(4).setMinWidth(140);
        Tabla.getColumnModel().getColumn(4).setMaxWidth(140);

        //Mostrar tabla
        try {
            CLASES.Asistencia.MostrarAsistencia(con, tabla1);
            if (Tabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún Cliente.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e);
        }

        Tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Detectar doble click
                if (e.getClickCount() == 2 && Tabla.getSelectedRow() != -1) {
                    int filaSeleccionada = Tabla.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        int fila = Tabla.getSelectedRow();

                        String fechaTexto = Tabla.getValueAt(fila, 4).toString();
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime fechaEntrada = LocalDateTime.parse(fechaTexto, formato);

                        CLASES.Asistencia.iniciarContador(con, fechaEntrada, HORAS);
                    }

                }
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
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        terminar = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        empleado = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cargar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        nya = new javax.swing.JLabel();
        cya = new javax.swing.JLabel();
        doc = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        generar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        area = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        HORAS = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Asistencia");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(227, 227, 227)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

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

        terminar.setText("Terminar");
        terminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(terminar)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(terminar)
                .addContainerGap())
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

        cargar.setText("Cargar");
        cargar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarActionPerformed(evt);
            }
        });

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Datos:");

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        nya.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nya.setText("Empleado:");

        cya.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cya.setText("Cargo:                  -    Area:");

        doc.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        doc.setText("Documento:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(doc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cya, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addComponent(nya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nya, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cya, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(doc, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Generar PDF de asistencia");

        generar.setText("Generar");
        generar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        generar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Area:");

        area.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        area.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                areaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator3)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(cargar)
                                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(generar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(jLabel7)))
                                .addGap(0, 35, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(area, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
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
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel8))
                    .addComponent(area))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cargar))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(36, 36, 36)
                                .addComponent(generar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(177, 177, 213));
        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Al trabajador le falta:");

        HORAS.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        HORAS.setText("Faltan: 00h 00min");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(218, 218, 218))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(HORAS)
                        .addGap(113, 113, 113))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(HORAS)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
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
                refrescarCombo();
                CLASES.Asistencia.jCombo(con, empleado, id);
                empleado.setSelectedIndex(0);
            } else {
                empleado.setEnabled(false);
                refrescarCombo();
                empleado.setSelectedIndex(0);
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
                generar.setEnabled(false);
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
                    CLASES.Asistencia.Datos(con, nya, cya, doc, id2);
                    //CLASES.Asistencia.Verificacion(con, cargar, generar, id2);
                } catch (SQLException ex) {
                    Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                cargar.setEnabled(false);
                terminar.setEnabled(true);
                nya.setText("Empleado:");
                cya.setText("Cargo:                  -    Area:");
                doc.setText("Documento:");

                Menu.setEnabled(true);
                Movimientos.setEnabled(true);
                Asistencia.setEnabled(true);
                Empleados.setEnabled(true);
                Estadisticas.setEnabled(true);
                Ayuda.setEnabled(true);
                Configuracion.setEnabled(true);
                generar.setEnabled(false);
                Salir.setEnabled(true);
            }
        }
    }//GEN-LAST:event_empleadoActionPerformed

    private void cargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarActionPerformed

        try {
            CLASES.Asistencia.Asistencia(con, id2);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "error principal" + ex);
        }
        try {
            tabla1.setRowCount(0); // Limpia
            CLASES.Asistencia.MostrarAsistencia(con, tabla1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
        }

        cargar.setEnabled(false);
        terminar.setEnabled(true);
        nya.setText("Empleado:");
        cya.setText("Cargo:                  -    Area:");
        doc.setText("Documento:");

        empleado.setSelectedIndex(0);
        area.setSelectedIndex(0);

        Menu.setEnabled(true);
        Movimientos.setEnabled(true);
        Asistencia.setEnabled(true);
        Empleados.setEnabled(true);
        Estadisticas.setEnabled(true);
        Ayuda.setEnabled(true);
        Configuracion.setEnabled(true);
        Salir.setEnabled(true);
    }//GEN-LAST:event_cargarActionPerformed

    private void generarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarActionPerformed
        String direccion="C:/db/PlanillaAsistencia.pdf";
        try {
            CLASES.Asistencia.GenerarPDF(con, 4, 10, direccion);
        } catch (SQLException ex) {
            Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_generarActionPerformed

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
    private javax.swing.JLabel HORAS;
    private javax.swing.JButton Menu;
    private javax.swing.JMenuItem Menu2;
    private javax.swing.JButton Movimientos;
    private javax.swing.JMenuItem Movimientos1;
    private javax.swing.JMenu Opciones2;
    private javax.swing.JButton Salir;
    private javax.swing.JTable Tabla;
    private javax.swing.JComboBox<Area> area;
    private javax.swing.JButton cargar;
    private javax.swing.JLabel cya;
    private javax.swing.JLabel doc;
    private javax.swing.JComboBox<Empleado> empleado;
    private javax.swing.JButton generar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel nya;
    private javax.swing.JButton terminar;
    // End of variables declaration//GEN-END:variables
}
