/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CLASES.Movimientos.Trip;
import CONEXIONES.Conexiones;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Facuymayriver
 */
public class Movimiento extends javax.swing.JFrame {

    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int id = 0, band, cont = 0, idtrip, block = 0, rango, valid, iduser;
    String veri, fechaActual;

    public void refrescarCombo() {
        try {
            CLASES.Movimientos.Select(con, Trip, fechaActual);
            tabla1.setRowCount(0);
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
    ModeloEditablePorFila tabla1 = new ModeloEditablePorFila(new String[]{"Nº", "Salida", "Llegada", "KmSalida", "Destino", "Nº Serv."}, 0) {
        private final int editableRow = -1;
    };
    //Tabla

    public Movimiento() {

        CLASES.MenuClass.Configuracion();

        initComponents();
        this.setLocationRelativeTo(null);
        CLASES.MenuClass menuHelper = new CLASES.MenuClass();
        menuHelper.MenuConfig(Movimientos, Menu, Asistencia, Empleados, Estadisticas, Ayuda, Configuracion, Salir, this);

        Tabla.setModel(tabla1);

        rango = CLASES.Usuario.rango();
        if (rango == 2) {
            AddT.setEnabled(false);
            AddV.setEnabled(false);
            Editar.setEnabled(false);
        }
        Relevar.setEnabled(false);
        Nuevo.setEnabled(false);
        Actualizar.setEnabled(false);
        Eliminar.setEnabled(false);
        Terminar.setEnabled(false);
        Cancelar.setEnabled(false);

        iduser = CLASES.Usuario.iduser();

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        fechaActual = hoy.format(formato);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        Tabla.setDefaultRenderer(Object.class, centerRenderer);

        int ventana = CLASES.MenuClass.Ventana();
        if (ventana == 0) {
            Tabla.setRowHeight(30);
            Tabla.getTableHeader().setReorderingAllowed(false);
            Tabla.getTableHeader().setResizingAllowed(false);

            Tabla.getColumnModel().getColumn(0).setMinWidth(50);
            Tabla.getColumnModel().getColumn(0).setMaxWidth(50);

            Tabla.getColumnModel().getColumn(1).setMinWidth(90);
            Tabla.getColumnModel().getColumn(1).setMaxWidth(90);

            Tabla.getColumnModel().getColumn(2).setMinWidth(90);
            Tabla.getColumnModel().getColumn(2).setMaxWidth(90);

            Tabla.getColumnModel().getColumn(3).setMinWidth(100);
            Tabla.getColumnModel().getColumn(3).setMaxWidth(100);

            Tabla.getColumnModel().getColumn(4).setMinWidth(180);
            Tabla.getColumnModel().getColumn(4).setMaxWidth(180);
            this.setExtendedState(NORMAL);
        } else if (ventana == 1) {
            // Configuración de la tabla maximizada
            this.setExtendedState(MAXIMIZED_BOTH);

            Font fuenteGrande = new Font("Arial", Font.BOLD, 25);

            // Configuración del Renderizador (cómo se VE la celda)
            Tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
            Tabla.getTableHeader().setPreferredSize(new Dimension(0, 50));
            Tabla.setRowHeight(70);
            Tabla.setFont(fuenteGrande); // La fuente grande de la tabla

            DefaultCellEditor editor = (DefaultCellEditor) Tabla.getDefaultEditor(String.class);

            // 2. Acceder al componente interno (JTextField) que usa el editor
            if (editor != null) {
                JTextField textField = (JTextField) editor.getComponent();

                // 3. Aplicar la misma fuente grande
                textField.setFont(fuenteGrande);
            }

            Tabla.getColumnModel().getColumn(0).setMinWidth(100);
            Tabla.getColumnModel().getColumn(0).setMaxWidth(100);

            Tabla.getColumnModel().getColumn(1).setMinWidth(160);
            Tabla.getColumnModel().getColumn(1).setMaxWidth(160);

            Tabla.getColumnModel().getColumn(2).setMinWidth(160);
            Tabla.getColumnModel().getColumn(2).setMaxWidth(160);

            Tabla.getColumnModel().getColumn(3).setMinWidth(200);
            Tabla.getColumnModel().getColumn(3).setMaxWidth(200);

            Tabla.getColumnModel().getColumn(4).setMinWidth(435);
            Tabla.getColumnModel().getColumn(4).setMaxWidth(435);
        }

        CLASES.Movimientos.Select(con, Trip, fechaActual);

        if (ventana == 1) {
            // 1. Crear el MaskFormatter como lo tenías
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter("##:##:##");
            } catch (ParseException ex) {
                Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
            }

            formatter.setPlaceholderCharacter('0');
            JFormattedTextField horaField = new JFormattedTextField(formatter);

            Font fuenteGrande = new Font("Arial", Font.BOLD, 25);
            horaField.setFont(fuenteGrande);

            horaField.setHorizontalAlignment(JTextField.CENTER);

            DefaultCellEditor editor = new DefaultCellEditor(horaField);
            Tabla.getColumnModel().getColumn(1).setCellEditor(editor); // salida
            Tabla.getColumnModel().getColumn(2).setCellEditor(editor); // llegada

            centerRenderer.setHorizontalAlignment(JTextField.CENTER);
            centerRenderer.setFont(fuenteGrande);

            Tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            Tabla.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        } else if (ventana == 0) {
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter("##:##:##");
            } catch (ParseException ex) {
                Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
            }
            formatter.setPlaceholderCharacter('0');
            JFormattedTextField horaField = new JFormattedTextField(formatter);

            // Asignar el editor a las columnas "salida" y "llegada"
            DefaultCellEditor editor = new DefaultCellEditor(horaField);
            Tabla.getColumnModel().getColumn(1).setCellEditor(editor); // salida
            Tabla.getColumnModel().getColumn(2).setCellEditor(editor); // llegada

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Trip = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        AddT = new javax.swing.JButton();
        Editar = new javax.swing.JButton();
        Eliminartrip = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        AddV = new javax.swing.JButton();
        Eliminarvic = new javax.swing.JButton();
        EditarVic = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        medico = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        chofer = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        enfermero = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cantidad = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        patente = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        modelo = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        victor = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        marca = new javax.swing.JLabel();
        Cancelar = new javax.swing.JButton();
        Nuevo = new javax.swing.JButton();
        Actualizar = new javax.swing.JButton();
        Eliminar = new javax.swing.JButton();
        Terminar = new javax.swing.JButton();
        Relevar = new javax.swing.JButton();
        Barra = new javax.swing.JMenuBar();
        Menu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
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
        setTitle("Movimientos");
        setUndecorated(true);
        setResizable(false);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel4.setBackground(new java.awt.Color(52, 170, 121));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/movimiento.png"))); // NOI18N
        jLabel1.setText("Selección:");

        Trip.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Trip.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Trip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TripActionPerformed(evt);
            }
        });

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tripulación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        AddT.setBackground(new java.awt.Color(78, 247, 177));
        AddT.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        AddT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/mas.png"))); // NOI18N
        AddT.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddTActionPerformed(evt);
            }
        });

        Editar.setBackground(new java.awt.Color(78, 247, 177));
        Editar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Editar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/edit.png"))); // NOI18N
        Editar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Editar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarActionPerformed(evt);
            }
        });

        Eliminartrip.setBackground(new java.awt.Color(78, 247, 177));
        Eliminartrip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/x.png"))); // NOI18N
        Eliminartrip.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Eliminartrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminartripActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(AddT, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(Editar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(Eliminartrip, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(AddT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Editar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(Eliminartrip, javax.swing.GroupLayout.PREFERRED_SIZE, 45, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Victor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPanel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        AddV.setBackground(new java.awt.Color(78, 247, 177));
        AddV.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        AddV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/mas.png"))); // NOI18N
        AddV.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddVActionPerformed(evt);
            }
        });

        Eliminarvic.setBackground(new java.awt.Color(78, 247, 177));
        Eliminarvic.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Eliminarvic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/x.png"))); // NOI18N
        Eliminarvic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Eliminarvic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarvicActionPerformed(evt);
            }
        });

        EditarVic.setBackground(new java.awt.Color(78, 247, 177));
        EditarVic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/edit.png"))); // NOI18N
        EditarVic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        EditarVic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarVicActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(AddV, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(EditarVic, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(Eliminarvic, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Eliminarvic, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(AddV, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(EditarVic, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Trip, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Trip, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
        jScrollPane1.setViewportView(Tabla);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("Tripulación");

        jPanel13.setBackground(new java.awt.Color(204, 204, 204));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Enfermero:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Chofer:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(medico, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(medico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chofer, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chofer, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enfermero, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(enfermero, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Medico:");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Cantidad de movimientos");

        cantidad.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        cantidad.setText("0");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(cantidad)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(7, 7, 7))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(117, 117, 117))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        patente.setText("-");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(patente, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(patente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Vehiculo");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Patente:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Victor:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Modelo:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Marca:");

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        modelo.setText("-");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modelo, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        victor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        victor.setText("000");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(victor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(victor, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        marca.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        marca.setText("-");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(marca, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(jLabel9))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(133, 133, 133)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14))
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Cancelar.setBackground(new java.awt.Color(78, 247, 177));
        Cancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Cancelar.setText("Cancelar");
        Cancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Cancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarActionPerformed(evt);
            }
        });

        Nuevo.setBackground(new java.awt.Color(78, 247, 177));
        Nuevo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Nuevo.setText("Nuevo");
        Nuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Nuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoActionPerformed(evt);
            }
        });

        Actualizar.setBackground(new java.awt.Color(78, 247, 177));
        Actualizar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Actualizar.setText("Actualizar");
        Actualizar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Actualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });

        Eliminar.setBackground(new java.awt.Color(78, 247, 177));
        Eliminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Eliminar.setText("Eliminar");
        Eliminar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });

        Terminar.setBackground(new java.awt.Color(78, 247, 177));
        Terminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Terminar.setText("Terminar");
        Terminar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Terminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Terminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TerminarActionPerformed(evt);
            }
        });

        Relevar.setBackground(new java.awt.Color(78, 247, 177));
        Relevar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Relevar.setText("Relevar");
        Relevar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Relevar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Relevar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelevarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(116, 116, 116)
                                .addComponent(Nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                                .addComponent(Terminar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Relevar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Terminar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Relevar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

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

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem2.setText("Inicio");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        Menu.add(jMenuItem2);

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
        Salir.setText("Cerrar Sesión");
        Salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Salir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });
        Barra.add(Salir);

        setJMenuBar(Barra);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddVActionPerformed
        int ban = 0;
        AddVic ventana = new AddVic(this, ban);
        ventana.setVisible(true);
    }//GEN-LAST:event_AddVActionPerformed

    private void AddTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTActionPerformed
        idtrip = 0;
        AddTri ventana = new AddTri(idtrip, this);
        ventana.setVisible(true);

        this.refrescarCombo();
    }//GEN-LAST:event_AddTActionPerformed

    private void TripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TripActionPerformed
        Object value = Trip.getSelectedItem();
        if (value instanceof Trip) {
            Trip dat = (Trip) value;
            id = dat.getId();
            veri = dat.getNombre();

            if (!veri.equals("Opciones")) {
                try {
                    tabla1.setRowCount(0);
                    CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
                    CLASES.Movimientos.Datos(con, chofer, enfermero, medico, victor, patente, modelo, marca, id, cantidad, fechaActual);
                    if (Tabla.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "No se encontró ningún movimiento.");
                    }
                } catch (HeadlessException | SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                if (rango == 1) {
                    Relevar.setEnabled(true);
                    Nuevo.setEnabled(true);
                    Actualizar.setEnabled(true);
                    Eliminar.setEnabled(true);
                }
            } else {
                tabla1.setRowCount(0);
                Nuevo.setEnabled(false);
                Relevar.setEnabled(false);
                Actualizar.setEnabled(false);
                Eliminar.setEnabled(false);
                Terminar.setEnabled(false);
            }
        }
    }//GEN-LAST:event_TripActionPerformed

    private void NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoActionPerformed
        Actualizar.setEnabled(false);
        Eliminar.setEnabled(false);
        Terminar.setEnabled(true);
        cont++;
        band = 0;
        Object[] nuevaFila = {"", "", "", "", "", ""};
        tabla1.addRow(nuevaFila);
        Cancelar.setEnabled(true);

        // Obtener el índice de la nueva fila
        int nuevaFilaIndex = tabla1.getRowCount() - 1;

        // Hacerla editable
        tabla1.setEditableRow(nuevaFilaIndex);

        // Seleccionar automáticamente
        Tabla.setRowSelectionInterval(nuevaFilaIndex, nuevaFilaIndex);
        Tabla.editCellAt(nuevaFilaIndex, 1); // enfocar en la primera celda editable
        Tabla.requestFocus();
    }//GEN-LAST:event_NuevoActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed
        Nuevo.setEnabled(false);
        Eliminar.setEnabled(false);
        Cancelar.setEnabled(true);
        band = 1;
        int filaSeleccionada = Tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            tabla1.setEditableRow(filaSeleccionada);
            Terminar.setEnabled(true);
            Nuevo.setEnabled(true);
            Cancelar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila primero.");
            Eliminar.setEnabled(true);
            Terminar.setEnabled(false);
            Nuevo.setEnabled(true);
            Cancelar.setEnabled(false);
        }
    }//GEN-LAST:event_ActualizarActionPerformed

    private void TerminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TerminarActionPerformed
        if (band == 0) {  //Guardar nuevo registro

            //validacion
            for (int i = 0; i < Tabla.getRowCount(); i++) {
                Object idObj = Tabla.getValueAt(i, 0);
                // Si es una fila nueva (sin código), la validamos
                if (idObj == null || idObj.toString().trim().isEmpty()) {
                    boolean filaCompleta = true;
                    // Recorremos todas las columnas
                    for (int j = 1; j < tabla1.getColumnCount(); j++) {
                        Object valor = tabla1.getValueAt(i, j);
                        if (valor == null || valor.toString().trim().isEmpty()) {
                            filaCompleta = false;
                            break;
                        }
                    }

                    if (!filaCompleta) {
                        JOptionPane.showMessageDialog(null, "Hay filas nuevas con campos vacíos. Completá todos los datos antes de guardar.");
                        return; // Cancela el proceso de guardado
                    }
                }

                // Obtener los datos actualizados desde la tabla
                String Km = Tabla.getValueAt(i, 3).toString();
                String Num = Tabla.getValueAt(i, 5).toString();
                try {
                    Double.parseDouble(Km);
                    Double.parseDouble(Num);
                } catch (NumberFormatException e) {
                    i = i + 1;
                    JOptionPane.showMessageDialog(null, "¡Ha ingresado un valor NO numerico en la fila " + i + "! Porfavor, revise");
                    return;
                }
            }
            //validacion

            //Insert
            for (int i = 0; i < Tabla.getRowCount(); i++) {
                Object idObj = Tabla.getValueAt(i, 0);
                if (idObj == null || idObj.toString().trim().isEmpty()) {
                    int cod = 0;
                    String Salida = Tabla.getValueAt(i, 1).toString();
                    String Llegada = Tabla.getValueAt(i, 2).toString();
                    String Km = Tabla.getValueAt(i, 3).toString();
                    String Destino = Tabla.getValueAt(i, 4).toString();
                    String NumServicio = Tabla.getValueAt(i, 5).toString();
                    try {
                        CLASES.Movimientos.movimientos(con, cod, Salida, Llegada, Km, Destino, NumServicio, band, id, fechaActual, iduser);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR1");
                    }
                }
            }
            //Insert

            JOptionPane.showMessageDialog(null, "Agregao");
            Eliminar.setEnabled(true);
            Terminar.setEnabled(false);
            Actualizar.setEnabled(true);
            try {
                tabla1.setRowCount(0);
                CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
                CLASES.Movimientos.Contador(con, id, cantidad, fechaActual);
                tabla1.bloquearEdicion();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR1");
            }

        } else if (band == 1) { //Editar
            for (int i = 0; i < Tabla.getRowCount(); i++) {
                boolean filaCompleta = true;
                // Recorremos todas las columnas
                for (int j = 1; j < Tabla.getColumnCount(); j++) {
                    Object valor = Tabla.getValueAt(i, j);

                    if (valor == null || valor.toString().trim().isEmpty()) {
                        filaCompleta = false;
                        break;
                    }
                }

                if (!filaCompleta) {
                    JOptionPane.showMessageDialog(null, "Hay filas nuevas con campos vacíos. Completá todos los datos antes de guardar.");
                    return; // Cancela el proceso de guardado
                }

            }
            for (int i = 0; i < Tabla.getRowCount(); i++) {
                // Obtener los datos actualizados desde la tabla
                String Km = Tabla.getValueAt(i, 3).toString();
                String Num = Tabla.getValueAt(i, 5).toString();
                try {
                    Double.parseDouble(Km);
                    Double.parseDouble(Num);
                } catch (NumberFormatException e) {
                    i = i + 1;
                    JOptionPane.showMessageDialog(null, "¡Ha ingresado un valor NO numerico en la fila " + i + "! Porfavor, revise");
                    return;
                }
            }

            for (int i = 0; i < Tabla.getRowCount(); i++) {
                // Obtener los datos actualizados desde la tabla
                int cod = Integer.parseInt(Tabla.getValueAt(i, 0).toString());
                String Salida = Tabla.getValueAt(i, 1).toString();
                String Llegada = Tabla.getValueAt(i, 2).toString();
                String Km = Tabla.getValueAt(i, 3).toString();
                String Destino = Tabla.getValueAt(i, 4).toString();
                String NumServicio = Tabla.getValueAt(i, 5).toString();
                try {
                    CLASES.Movimientos.movimientos(con, cod, Salida, Llegada, Km, Destino, NumServicio, band, id, fechaActual, iduser);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR");
                }
            }

            JOptionPane.showMessageDialog(null, "Actualizao");
            Eliminar.setEnabled(true);
            Terminar.setEnabled(false);
            Actualizar.setEnabled(true);
            Cancelar.setEnabled(false);
            try {
                tabla1.setRowCount(0);
                CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR1");
            }
        }
    }//GEN-LAST:event_TerminarActionPerformed

    private void RelevarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelevarActionPerformed
        if (id == 0) {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA TRIPULACION");
        } else {
            Relevar ventana = new Relevar(id, this);
            ventana.setVisible(true);
            this.refrescarCombo();
        }
    }//GEN-LAST:event_RelevarActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        int filaSeleccionada = Tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseás Eliminar?",
                    "Confirmar acción",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.OK_OPTION) {
                int fila = Tabla.getSelectedRow();
                int cod = Integer.parseInt(Tabla.getValueAt(fila, 0).toString());
                try {
                    CLASES.Movimientos.EliminarMovimiento(con, cod, iduser, id);
                    JOptionPane.showMessageDialog(null, "Eliminao");
                    tabla1.setRowCount(0);
                    CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
                    CLASES.Movimientos.Contador(con, id, cantidad, fechaActual);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR");
                }
            } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                // El usuario eligió "Cancelar" o cerró la ventana
                System.out.println("Cancelado");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila primero.");
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarActionPerformed
        if (band == 0) {
            for (int x = 0; x < cont; x++) {
                int ultimaFila = tabla1.getRowCount() - 1;

                if (ultimaFila >= 0) {
                    if (Tabla.isEditing()) {
                        Tabla.getCellEditor().stopCellEditing();
                    }
                    Object valor = tabla1.getValueAt(ultimaFila, 0); // columna Cod

                    if (valor == null || valor.toString().isEmpty()) {
                        tabla1.removeRow(ultimaFila);
                        Tabla.clearSelection(); // <-- esta línea es clave para "descongelar"
                        Tabla.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se puede cancelar un producto ya guardado.");
                    }
                }
            }
        } else if (band == 1) {
            Tabla.clearSelection();
            tabla1.bloquearEdicion();
        }
        Cancelar.setEnabled(false);
        Terminar.setEnabled(false);
        Editar.setEnabled(true);
        Eliminar.setEnabled(true);
        Salir.setEnabled(true);
        Nuevo.setEnabled(true);
        Actualizar.setEnabled(true);
        cont = 0;
    }//GEN-LAST:event_CancelarActionPerformed

    private void EditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarActionPerformed
        idtrip = 1;
        AddTri ventana = new AddTri(idtrip, this);
        ventana.setVisible(true);
        this.refrescarCombo();
    }//GEN-LAST:event_EditarActionPerformed

    private void iniciomovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciomovActionPerformed

    }//GEN-LAST:event_iniciomovActionPerformed

    private void nuevtripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevtripActionPerformed
        AddTri ventana = new AddTri(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_nuevtripActionPerformed

    private void nuevovicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevovicActionPerformed
        int ban = 0;
        AddVic ventana = new AddVic(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
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
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_modActionPerformed

    private void elimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_elimActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Estadisticas ventana = new Estadisticas();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Menu ventana = new Menu();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void MenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuActionPerformed

    private void EliminarvicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarvicActionPerformed
        int ban = 0;
        AddDelVicTrip ventana = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
        this.refrescarCombo();
    }//GEN-LAST:event_EliminarvicActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SalirActionPerformed

    private void EditarVicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarVicActionPerformed
        int ban = 1;
        AddVic ventana = new AddVic(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
        this.refrescarCombo();
    }//GEN-LAST:event_EditarVicActionPerformed

    private void EliminartripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminartripActionPerformed
        int ban = 1;
        AddDelVicTrip ventana = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
        this.refrescarCombo();
    }//GEN-LAST:event_EliminartripActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Movimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Movimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Movimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Movimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Movimiento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Actualizar;
    private javax.swing.JButton AddT;
    private javax.swing.JButton AddV;
    private javax.swing.JMenu Asistencia;
    private javax.swing.JMenu Ayuda;
    private javax.swing.JMenuBar Barra;
    private javax.swing.JButton Cancelar;
    private javax.swing.JMenu Configuracion;
    private javax.swing.JButton Editar;
    private javax.swing.JButton EditarVic;
    private javax.swing.JButton Eliminar;
    private javax.swing.JButton Eliminartrip;
    private javax.swing.JButton Eliminarvic;
    private javax.swing.JMenu Empleados;
    private javax.swing.JMenu Estadisticas;
    private javax.swing.JMenu Menu;
    private javax.swing.JMenu Movimientos;
    private javax.swing.JButton Nuevo;
    private javax.swing.JButton Relevar;
    private javax.swing.JMenu Salir;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton Terminar;
    private javax.swing.JComboBox<Trip> Trip;
    private javax.swing.JLabel cantidad;
    private javax.swing.JMenu cargoemp;
    private javax.swing.JLabel chofer;
    private javax.swing.JMenuItem elim;
    private javax.swing.JLabel enfermero;
    private javax.swing.JMenuItem historial;
    private javax.swing.JMenuItem inicemp;
    private javax.swing.JMenuItem inicioas;
    private javax.swing.JMenuItem iniciomov;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel marca;
    private javax.swing.JLabel medico;
    private javax.swing.JMenuItem mod;
    private javax.swing.JLabel modelo;
    private javax.swing.JMenuItem nuevovic;
    private javax.swing.JMenuItem nuevtrip;
    private javax.swing.JLabel patente;
    private javax.swing.JLabel victor;
    // End of variables declaration//GEN-END:variables
}
