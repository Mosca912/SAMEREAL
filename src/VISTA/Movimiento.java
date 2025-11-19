/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CLASES.Movimientos.Trip;
import CONEXIONES.Conexiones;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Facuymayriver
 */
public class Movimiento extends javax.swing.JFrame implements CLASES.IBlockableFrame {

    @Override
    public int getBlockStateSalir() {
        return this.block2;
    }

    @Override
    public int getBlockState() {
        return this.block;
    }
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int id = 0, band, cont = 0, idtrip, rango, valid, iduser;
    String veri, fechaActual, ayuda2 = "Movimiento";
    public int block = 0, block2 = 0;
    int ventana;

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

        String rutaIcono = "/IMAGENES/iconosame.png";

        try {
            // Cargar la imagen desde los recursos del proyecto (la forma recomendada)
            Image icono = new ImageIcon(getClass().getResource(rutaIcono)).getImage();
            this.setIconImage(icono);

        } catch (Exception e) {
            System.err.println("Error al cargar el ícono: " + e.getMessage());
        }

        this.setLocationRelativeTo(null);
        CLASES.MenuClass menuHelper = new CLASES.MenuClass();
        menuHelper.MenuConfig(Movimientos, Menu, Asistencia, Empleados, Estadisticas, Ayuda, Configuracion, Salir, this, ayuda2);

        Tabla.setModel(tabla1);

        rango = CLASES.Usuario.rango();
        if (rango == 2) {
            AddT.setEnabled(false);
            AddV.setEnabled(false);
            Editar.setEnabled(false);
            Eliminartrip.setEnabled(false);
            EditarVic.setEnabled(false);
            Eliminarvic.setEnabled(false);
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

        ventana = CLASES.MenuClass.Ventana();
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

            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter("##:##:##");
                formatter.setPlaceholderCharacter('0');
            } catch (ParseException ex) {
                Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
            }

            JFormattedTextField horaField = new JFormattedTextField(formatter);
            Font fuenteGrande = new Font("Arial", Font.BOLD, 25);
            horaField.setFont(fuenteGrande);

            horaField.setHorizontalAlignment(JTextField.CENTER);

            DefaultCellEditor editor = new DefaultCellEditor(horaField);
            Tabla.getColumnModel().getColumn(1).setCellEditor(editor); // salida
            Tabla.getColumnModel().getColumn(2).setCellEditor(editor); // salida

            centerRenderer.setHorizontalAlignment(JTextField.CENTER);
            centerRenderer.setFont(fuenteGrande);
        } else if (ventana == 0) {
            MaskFormatter formatter = null;
            try {
                formatter = new MaskFormatter("##:##:##");
                formatter.setPlaceholderCharacter('0');
            } catch (ParseException ex) {
                Logger.getLogger(Movimiento.class.getName()).log(Level.SEVERE, null, ex);
            }

            JFormattedTextField horaField = new JFormattedTextField(formatter);
            horaField.setValue("0");
            DefaultCellEditor editor = new DefaultCellEditor(horaField);
            Tabla.getColumnModel().getColumn(1).setCellEditor(editor); // salida
            Tabla.getColumnModel().getColumn(2).setCellEditor(editor); // salida

        }

        int ventanaTheme = CLASES.MenuClass.VentanaOpcThemeRet();
        if (ventanaTheme == 1) {
            Color colorPersonalizado = new Color(44, 44, 53);
            Fondo.setBackground(colorPersonalizado);

            JTableHeader header = Tabla.getTableHeader();
            header.setBackground(new Color(44, 44, 53));
            header.setForeground(Color.WHITE);

            Tabla.setBackground(new Color(50, 50, 50));
            Tabla.setForeground(Color.WHITE);
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

        Fondo = new javax.swing.JPanel();
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
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        Movimientos = new javax.swing.JMenu();
        iniciomov = new javax.swing.JMenuItem();
        historial = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        Asistencia = new javax.swing.JMenu();
        Empleados = new javax.swing.JMenu();
        inicemp = new javax.swing.JMenuItem();
        cargoemp = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        mod = new javax.swing.JMenuItem();
        elim = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        Estadisticas = new javax.swing.JMenu();
        Ayuda = new javax.swing.JMenu();
        Configuracion = new javax.swing.JMenu();
        Salir = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Movimientos");
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);

        Fondo.setBackground(new java.awt.Color(255, 255, 255));
        Fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel4.setBackground(new java.awt.Color(52, 170, 121));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/movimiento.png"))); // NOI18N
        jLabel1.setText("Movimientos");

        Trip.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Trip.setToolTipText("Seleccione una tripulación para empezar a cargar movimientos!");
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
        AddT.setToolTipText("Añadir nueva tripulación");
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
        Editar.setToolTipText("Editar tripulación");
        Editar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Editar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarActionPerformed(evt);
            }
        });

        Eliminartrip.setBackground(new java.awt.Color(78, 247, 177));
        Eliminartrip.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menos.png"))); // NOI18N
        Eliminartrip.setToolTipText("Eliminar tripulación");
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
        AddV.setToolTipText("Añadir nuevo victor");
        AddV.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddVActionPerformed(evt);
            }
        });

        Eliminarvic.setBackground(new java.awt.Color(78, 247, 177));
        Eliminarvic.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Eliminarvic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menos.png"))); // NOI18N
        Eliminarvic.setToolTipText("Eliminar un victor");
        Eliminarvic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Eliminarvic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarvicActionPerformed(evt);
            }
        });

        EditarVic.setBackground(new java.awt.Color(78, 247, 177));
        EditarVic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/edit.png"))); // NOI18N
        EditarVic.setToolTipText("Editar un victor existente");
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
                .addComponent(Trip, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        Tabla.setToolTipText("Seleccione cualquier fila para actualizar/eliminar");
        Tabla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TablaKeyTyped(evt);
            }
        });
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
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
        Cancelar.setToolTipText("Cancela cualquier nuevo movimiento");
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
        Nuevo.setToolTipText("Agrega una nueva fila");
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
        Actualizar.setToolTipText("Actualiza una movimiento ya guardado");
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
        Eliminar.setToolTipText("Elimina un movimiento ya guardado");
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
        Terminar.setToolTipText("Guarda el/los movimiento/s");
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
        Relevar.setToolTipText("Al finalizar el turno, releve la ambulancia!");
        Relevar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Relevar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Relevar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RelevarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FondoLayout.createSequentialGroup()
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
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Relevar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Terminar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Relevar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(Fondo, java.awt.BorderLayout.CENTER);

        Barra.setBackground(new java.awt.Color(52, 170, 121));
        Barra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Menu.setBackground(new java.awt.Color(204, 255, 204));
        Menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menui.png"))); // NOI18N
        Menu.setText("Menu");
        Menu.setToolTipText("Menu");
        Menu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Menu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem1.setText("Nuevo usuario");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        Menu.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem2.setText("Modificar usuario");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        Menu.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem3.setText("Eliminar usuario");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        Menu.add(jMenuItem3);

        Barra.add(Menu);

        Movimientos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/movimiento.png"))); // NOI18N
        Movimientos.setText("Movimientos");
        Movimientos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Movimientos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Movimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MovimientosActionPerformed(evt);
            }
        });

        iniciomov.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        iniciomov.setText("Inicio");
        iniciomov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciomovActionPerformed(evt);
            }
        });
        Movimientos.add(iniciomov);

        historial.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        historial.setText("Historial");
        historial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historialActionPerformed(evt);
            }
        });
        Movimientos.add(historial);

        jMenu3.setText("Tripulación");
        jMenu3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem4.setText("Nuevo");
        jMenuItem4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem5.setText("Modificar");
        jMenuItem5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem6.setText("Eliminar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        Movimientos.add(jMenu3);

        jMenu4.setText("Victor");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenu4.setHideActionText(true);

        jMenuItem7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem7.setText("Nuevo");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem8.setText("Modificar");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem9.setText("Eliminar");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        Movimientos.add(jMenu4);

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
        Barra.add(Asistencia);

        Empleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado.png"))); // NOI18N
        Empleados.setText("Empleados");
        Empleados.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Empleados.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        inicemp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        inicemp.setText("Inicio");
        inicemp.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        inicemp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicempActionPerformed(evt);
            }
        });
        Empleados.add(inicemp);

        cargoemp.setBackground(new java.awt.Color(52, 170, 121));
        cargoemp.setText("Cargo");
        cargoemp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cargoemp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem10.setText("Nuevo");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        cargoemp.add(jMenuItem10);

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

        jMenu5.setText("Area");
        jMenu5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem11.setText("Nueva");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem11);

        jMenuItem12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem12.setText("Modificar");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);

        jMenuItem13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jMenuItem13.setText("Eliminar");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        Empleados.add(jMenu5);

        Barra.add(Empleados);

        Estadisticas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/estats.png"))); // NOI18N
        Estadisticas.setText("Estadisticas");
        Estadisticas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Estadisticas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Barra.add(Estadisticas);

        Ayuda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/ayuda.png"))); // NOI18N
        Ayuda.setText("Ayuda");
        Ayuda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ayuda.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Ayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AyudaActionPerformed(evt);
            }
        });
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
        AddVic ventana1 = new AddVic(this, ban);
        ventana1.setVisible(true);

    }//GEN-LAST:event_AddVActionPerformed

    private void AddTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTActionPerformed
        idtrip = 0;

        int veritrip = CLASES.Movimientos.VerificacionTrip(con);

        if (veritrip == 0) {
            AddTri ventana1 = new AddTri(idtrip, this);
            ventana1.setVisible(true);

            this.refrescarCombo();
        }
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

                    AddT.setEnabled(false);
                    Editar.setEnabled(false);
                    Eliminartrip.setEnabled(false);

                    AddV.setEnabled(false);
                    EditarVic.setEnabled(false);
                    Eliminarvic.setEnabled(false);
                }
            } else {
                tabla1.setRowCount(0);
                Nuevo.setEnabled(false);
                Relevar.setEnabled(false);
                Actualizar.setEnabled(false);
                Eliminar.setEnabled(false);
                Terminar.setEnabled(false);
                Cancelar.setEnabled(false);

                Menu.setEnabled(true);
                Movimientos.setEnabled(true);
                Asistencia.setEnabled(true);
                Empleados.setEnabled(true);
                Estadisticas.setEnabled(true);
                Ayuda.setEnabled(true);
                Configuracion.setEnabled(true);
                Salir.setEnabled(true);

                if (rango == 1) {
                    AddT.setEnabled(true);
                    Editar.setEnabled(true);
                    Eliminartrip.setEnabled(true);

                    AddV.setEnabled(true);
                    EditarVic.setEnabled(true);
                    Eliminarvic.setEnabled(true);
                }

                chofer.setText("");
                enfermero.setText("");
                medico.setText("");
                patente.setText("-");
                victor.setText("000");
                modelo.setText("-");
                marca.setText("-");
                cantidad.setText("0");

                block = 0;
                block2 = 0;
            }
        }
    }//GEN-LAST:event_TripActionPerformed

    private void NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoActionPerformed
        Actualizar.setEnabled(false);
        Eliminar.setEnabled(false);
        Terminar.setEnabled(true);

        AddT.setEnabled(false);
        Editar.setEnabled(false);
        Eliminartrip.setEnabled(false);
        EditarVic.setEnabled(false);
        Eliminarvic.setEnabled(false);
        AddV.setEnabled(false);

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
        Tabla.editCellAt(nuevaFilaIndex, 3); // enfocar en la primera celda editable
        Tabla.requestFocus();

        String horaActual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Tabla.setValueAt(horaActual, nuevaFilaIndex, 1);

        Menu.setEnabled(false);
        Movimientos.setEnabled(false);
        Asistencia.setEnabled(false);
        Empleados.setEnabled(false);
        Estadisticas.setEnabled(false);
        Ayuda.setEnabled(false);
        Configuracion.setEnabled(false);
        Salir.setEnabled(false);

        block = 1;
        block2 = 1;
    }//GEN-LAST:event_NuevoActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed

        Nuevo.setEnabled(false);
        Eliminar.setEnabled(false);
        Cancelar.setEnabled(true);

        AddT.setEnabled(false);
        Editar.setEnabled(false);
        Eliminartrip.setEnabled(false);
        EditarVic.setEnabled(false);
        Eliminarvic.setEnabled(false);
        AddV.setEnabled(false);

        Menu.setEnabled(false);
        Movimientos.setEnabled(false);
        Asistencia.setEnabled(false);
        Empleados.setEnabled(false);
        Estadisticas.setEnabled(false);
        Ayuda.setEnabled(false);
        Configuracion.setEnabled(false);
        Salir.setEnabled(false);

        block = 1;
        block2 = 1;
        band = 1;
        int filaSeleccionada = Tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            tabla1.setEditableRow(filaSeleccionada);
            String horaActual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            Tabla.setValueAt(horaActual, filaSeleccionada, 2);
            Terminar.setEnabled(true);
            Nuevo.setEnabled(false);
            Cancelar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila primero.");
            Eliminar.setEnabled(true);
            Terminar.setEnabled(false);
            Nuevo.setEnabled(true);
            Cancelar.setEnabled(false);

            Menu.setEnabled(true);
            Movimientos.setEnabled(true);
            Asistencia.setEnabled(true);
            Empleados.setEnabled(true);
            Estadisticas.setEnabled(true);
            Ayuda.setEnabled(true);
            Configuracion.setEnabled(true);
            Salir.setEnabled(true);

            block = 0;
            block2 = 0;
        }
    }//GEN-LAST:event_ActualizarActionPerformed

    private void TerminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TerminarActionPerformed
        int verisalida = 0;
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
                            if (j == 2) {
                                verisalida = 1;
                            } else {
                                filaCompleta = false;
                                break;
                            }
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

                String llegada = Tabla.getValueAt(i, 1).toString();
                String[] partes = llegada.split(":");

                // 4. Obtener las horas, minutos y segundos como enteros
                int horas = Integer.parseInt(partes[0]);
                int minutos = Integer.parseInt(partes[1]);
                int segundos = Integer.parseInt(partes[2]);

                String salida = Tabla.getValueAt(i, 2).toString();
                String[] partes2 = salida.split(":");

                // 5. Comprobar los rangos
                if (horas < 0 || horas > 23) { // Máximo 23 horas
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": La hora (" + horas + ") debe estar entre 00 y 23.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (minutos < 0 || minutos > 59) { // Máximo 59 minutos
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": Los minutos (" + minutos + ") deben estar entre 00 y 59.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (segundos < 0 || segundos > 59) { // Máximo 59 segundos
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": Los segundos (" + segundos + ") deben estar entre 00 y 59.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (verisalida == 0) {
                    // 4. Obtener las horas, minutos y segundos como enteros
                    int horas2 = Integer.parseInt(partes2[0]);
                    int minutos2 = Integer.parseInt(partes2[1]);
                    int segundos2 = Integer.parseInt(partes2[2]);

                    // 5. Comprobar los rangos
                    if (horas2 < 0 || horas2 > 23) { // Máximo 23 horas
                        JOptionPane.showMessageDialog(null,
                                "Error en la Fila " + (i + 1) + ": La hora (" + horas + ") debe estar entre 00 y 23.",
                                "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (minutos2 < 0 || minutos2 > 59) { // Máximo 59 minutos
                        JOptionPane.showMessageDialog(null,
                                "Error en la Fila " + (i + 1) + ": Los minutos (" + minutos + ") deben estar entre 00 y 59.",
                                "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (segundos2 < 0 || segundos2 > 59) { // Máximo 59 segundos
                        JOptionPane.showMessageDialog(null,
                                "Error en la Fila " + (i + 1) + ": Los segundos (" + segundos + ") deben estar entre 00 y 59.",
                                "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
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
                    int tamañoKm = Km.length();
                    int tamañoDestino = Destino.length();
                    int tamañoServicio = NumServicio.length();
                    if (tamañoKm >= 44 || tamañoDestino >= 44 || tamañoServicio >= 44) {
                        JOptionPane.showMessageDialog(null, "DEMASIADOS CARACTERES!");
                        return;
                    } else {
                        try {
                            CLASES.Movimientos.movimientos(con, cod, Salida, Llegada, Km, Destino, NumServicio, band, id, fechaActual, iduser);
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "ERROR" + e);
                        }
                    }
                }
            }
            //Insert

            JOptionPane.showMessageDialog(null, "Agregado");
            Eliminar.setEnabled(true);
            Terminar.setEnabled(false);
            Actualizar.setEnabled(true);
            Cancelar.setEnabled(false);

            AddT.setEnabled(true);
            Editar.setEnabled(true);
            Eliminartrip.setEnabled(true);
            EditarVic.setEnabled(true);
            Eliminarvic.setEnabled(true);
            AddV.setEnabled(true);

            Menu.setEnabled(true);
            Movimientos.setEnabled(true);
            Asistencia.setEnabled(true);
            Empleados.setEnabled(true);
            Estadisticas.setEnabled(true);
            Ayuda.setEnabled(true);
            Configuracion.setEnabled(true);
            Salir.setEnabled(true);

            block = 0;
            block2 = 0;

            try {
                tabla1.setRowCount(0);
                CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
                CLASES.Movimientos.Contador(con, id, cantidad, fechaActual);
                tabla1.bloquearEdicion();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR" + e);
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

                String llegada = Tabla.getValueAt(i, 1).toString();
                String[] partes = llegada.split(":");

                // 4. Obtener las horas, minutos y segundos como enteros
                int horas = Integer.parseInt(partes[0]);
                int minutos = Integer.parseInt(partes[1]);
                int segundos = Integer.parseInt(partes[2]);

                String salida = Tabla.getValueAt(i, 2).toString();
                String[] partes2 = salida.split(":");

                // 5. Comprobar los rangos
                if (horas < 0 || horas > 23) { // Máximo 23 horas
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": La hora (" + horas + ") debe estar entre 00 y 23.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (minutos < 0 || minutos > 59) { // Máximo 59 minutos
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": Los minutos (" + minutos + ") deben estar entre 00 y 59.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (segundos < 0 || segundos > 59) { // Máximo 59 segundos
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": Los segundos (" + segundos + ") deben estar entre 00 y 59.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 4. Obtener las horas, minutos y segundos como enteros
                int horas2 = Integer.parseInt(partes2[0]);
                int minutos2 = Integer.parseInt(partes2[1]);
                int segundos2 = Integer.parseInt(partes2[2]);

                // 5. Comprobar los rangos
                if (horas2 < 0 || horas2 > 23) { // Máximo 23 horas
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": La hora (" + horas + ") debe estar entre 00 y 23.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (minutos2 < 0 || minutos2 > 59) { // Máximo 59 minutos
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": Los minutos (" + minutos + ") deben estar entre 00 y 59.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (segundos2 < 0 || segundos2 > 59) { // Máximo 59 segundos
                    JOptionPane.showMessageDialog(null,
                            "Error en la Fila " + (i + 1) + ": Los segundos (" + segundos + ") deben estar entre 00 y 59.",
                            "Rango Inválido", JOptionPane.ERROR_MESSAGE);
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
                int tamañoKm = Km.length();
                int tamañoDestino = Destino.length();
                int tamañoServicio = NumServicio.length();
                if (tamañoKm >= 44 || tamañoDestino >= 44 || tamañoServicio >= 44) {
                    JOptionPane.showMessageDialog(null, "DEMASIADOS CARACTERES!");
                    return;
                } else {
                    try {
                        CLASES.Movimientos.movimientos(con, cod, Salida, Llegada, Km, Destino, NumServicio, band, id, fechaActual, iduser);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "ERROR" + ex);
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Actualizado");
            Eliminar.setEnabled(true);
            Terminar.setEnabled(false);
            Actualizar.setEnabled(true);
            Cancelar.setEnabled(false);

            AddT.setEnabled(true);
            Nuevo.setEnabled(true);
            Editar.setEnabled(true);
            Eliminartrip.setEnabled(true);
            EditarVic.setEnabled(true);
            Eliminarvic.setEnabled(true);
            AddV.setEnabled(true);

            Menu.setEnabled(true);
            Movimientos.setEnabled(true);
            Asistencia.setEnabled(true);
            Empleados.setEnabled(true);
            Estadisticas.setEnabled(true);
            Ayuda.setEnabled(true);
            Configuracion.setEnabled(true);
            Salir.setEnabled(true);

            block = 0;
            block2 = 0;

            Tabla.clearSelection();
            tabla1.bloquearEdicion();
            try {
                tabla1.setRowCount(0);
                CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR" + ex);
            }
        }
    }//GEN-LAST:event_TerminarActionPerformed

    private void RelevarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RelevarActionPerformed
        if (id == 0) {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA TRIPULACION");
        } else {
            Relevar ventana1 = new Relevar(id, this);
            ventana1.setVisible(true);
            Relevar.setEnabled(false);
            Terminar.setEnabled(false);
            Eliminar.setEnabled(false);
            Actualizar.setEnabled(false);
            Nuevo.setEnabled(false);
            Cancelar.setEnabled(false);

            Menu.setEnabled(true);
            Movimientos.setEnabled(true);
            Asistencia.setEnabled(true);
            Empleados.setEnabled(true);
            Estadisticas.setEnabled(true);
            Ayuda.setEnabled(true);
            Configuracion.setEnabled(true);
            Salir.setEnabled(true);

            block = 0;
            block2 = 0;
            this.refrescarCombo();
            chofer.setText("");
            enfermero.setText("");
            medico.setText("");
            patente.setText("-");
            victor.setText("000");
            modelo.setText("-");
            marca.setText("-");
            cantidad.setText("0");

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
                    JOptionPane.showMessageDialog(null, "Eliminado");
                    tabla1.setRowCount(0);
                    CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
                    CLASES.Movimientos.Contador(con, id, cantidad, fechaActual);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR"+ ex);
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
        Eliminar.setEnabled(true);
        Editar.setEnabled(true);
        Nuevo.setEnabled(true);
        Actualizar.setEnabled(true);

        Menu.setEnabled(true);
        Movimientos.setEnabled(true);
        Asistencia.setEnabled(true);
        Empleados.setEnabled(true);
        Estadisticas.setEnabled(true);
        Ayuda.setEnabled(true);
        Configuracion.setEnabled(true);
        Salir.setEnabled(true);

        AddT.setEnabled(true);
        Editar.setEnabled(true);
        Eliminartrip.setEnabled(true);
        EditarVic.setEnabled(true);
        Eliminarvic.setEnabled(true);
        AddV.setEnabled(true);

        block = 0;
        block2 = 0;
        cont = 0;

        try {
            tabla1.setRowCount(0);
            CLASES.Movimientos.MostrarMov(con, tabla1, fechaActual, id);
            CLASES.Movimientos.Contador(con, id, cantidad, fechaActual);
            tabla1.bloquearEdicion();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR1");
        }
    }//GEN-LAST:event_CancelarActionPerformed

    private void EditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarActionPerformed
        idtrip = 1;

        int veritrip = CLASES.Movimientos.VerificacionTrip2(con);

        if (veritrip == 0) {
            AddTri ventana1 = new AddTri(idtrip, this);
            ventana1.setVisible(true);
            this.refrescarCombo();
        }
    }//GEN-LAST:event_EditarActionPerformed

    private void EliminarvicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarvicActionPerformed
        int ban = 0;
        AddDelVicTrip ventana1 = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
        this.refrescarCombo();
    }//GEN-LAST:event_EliminarvicActionPerformed

    private void EditarVicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarVicActionPerformed
        int ban = 1;

        int veritrip = CLASES.Movimientos.VerificacionVictor(con);

        if (veritrip == 0) {
            AddVic ventana1 = new AddVic(this, ban);
            if (rango == 1) {
                ventana1.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Es un usuario lector!");
            }
            this.refrescarCombo();
        }
    }//GEN-LAST:event_EditarVicActionPerformed

    private void EliminartripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminartripActionPerformed
        int ban = 1;
        AddDelVicTrip ventana1 = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
        this.refrescarCombo();
    }//GEN-LAST:event_EliminartripActionPerformed

    private void TablaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaKeyTyped

    }//GEN-LAST:event_TablaKeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        RegistrarFrm ventana1 = new RegistrarFrm(this, 1);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        RegistrarFrm ventana1 = new RegistrarFrm(this, 2);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        AddDelUser ventana1 = new AddDelUser(this, 1);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void iniciomovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciomovActionPerformed
        Movimiento ventana1 = new Movimiento();
        ventana1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_iniciomovActionPerformed

    private void historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialActionPerformed
        Historial ventana1 = new Historial(this);
        ventana1.setVisible(true);
    }//GEN-LAST:event_historialActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        AddTri ventana1 = new AddTri(0, this);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        AddTri ventana1 = new AddTri(1, this);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        int ban = 1;
        AddDelVicTrip ventana1 = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        int ban = 0;
        AddVic ventana1 = new AddVic(this, ban);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        int ban = 1;
        AddVic ventana1 = new AddVic(this, ban);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        int ban = 0;
        AddDelVicTrip ventana1 = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void MovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MovimientosActionPerformed
        Movimiento ventana1 = new Movimiento();
        ventana1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MovimientosActionPerformed

    private void AsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AsistenciaActionPerformed
        Asistencia ventana1 = new Asistencia();
        ventana1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AsistenciaActionPerformed

    private void inicempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicempActionPerformed
        Empleados ventana1 = new Empleados();
        ventana1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_inicempActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        AddAreaCargo ventana3 = new AddAreaCargo(this, 1);
        if (rango == 1) {
            ventana3.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void modActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modActionPerformed
        ModElimCargo1 ventana1 = new ModElimCargo1(1, this);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_modActionPerformed

    private void elimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimActionPerformed
        ModElimCargo1 ventana1 = new ModElimCargo1(0, this);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_elimActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        AddAreaCargo ventana3 = new AddAreaCargo(this, 0);
        if (rango == 1) {
            ventana3.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        ModElimArea1 ventana1 = new ModElimArea1(1, this);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ModElimArea1 ventana1 = new ModElimArea1(0, this);
        if (rango == 1) {
            ventana1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void AyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AyudaActionPerformed
        String direccion1 = "\\RECURSOS\\Movimientos.pdf";
        CLASES.Movimientos.abrirPDF(direccion1);
    }//GEN-LAST:event_AyudaActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SalirActionPerformed

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
    private javax.swing.JPanel Fondo;
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
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JLabel patente;
    private javax.swing.JLabel victor;
    // End of variables declaration//GEN-END:variables
}
