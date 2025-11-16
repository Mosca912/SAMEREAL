/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import java.sql.Connection;
import java.sql.ResultSet;
import CONEXIONES.Conexiones;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Facuymayriver
 */
public class Empleados extends javax.swing.JFrame implements CLASES.IBlockableFrame {

    @Override
    public int getBlockState() {
        return this.block;
    }

    @Override
    public int getBlockStateSalir() {
        return this.block2;
    }

    public int x = 0, c = 0, id;
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int cont = 0;
    String ayuda2 = "Empleados";
    public int block = 0, block2 = 0, rango = 0, iduser;

    public void refrescarTablaEmpleados() {
        tabla1.setRowCount(0); // Limpia
        try {
            CLASES.Empleados.MostrarEmpleados(con, tabla1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
        }
    }

    public void refrescarCombo() {
        while (Cargo.getItemCount() > 1) {
            Cargo.removeItemAt(1); // Siempre elimina el segundo, el resto se va corriendo
        }
        CLASES.Empleados.ActjCombo(con, Cargo);
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
    ModeloEditablePorFila tabla1 = new ModeloEditablePorFila(new String[]{"Nº", "Nombre", "Apellido", "DNI", "Telefono", "Cargo"}, 0) {
        private final int editableRow = -1;
    };
    //Tabla

    public Empleados() {
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

        int ventana = CLASES.MenuClass.Ventana();

        rango = CLASES.Usuario.rango();
        if (rango == 2) {
            Cargar.setEnabled(false);
            Eliminar.setEnabled(false);
            Modificar.setEnabled(false);
            opcCargo.setEnabled(false);
            opcArea.setEnabled(false);
        }

        iduser = CLASES.Usuario.iduser();

        Nombre.setEnabled(false);
        Apellido.setEnabled(false);
        DNI.setEnabled(false);
        Domicilio.setEnabled(false);
        Email.setEnabled(false);
        Telefono.setEnabled(false);
        Fecha.setEnabled(false);
        GS.setEnabled(false);
        Cargo.setEnabled(false);
        Limpiar.setEnabled(false);
        Guardar.setEnabled(false);

        CLASES.Empleados.jCombo(con, GS, Cargo);

        Tabla.setModel(tabla1);

        Tabla.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        Tabla.setDefaultRenderer(Object.class, centerRenderer);

        if (ventana == 0) {
            this.setExtendedState(NORMAL);
            Tabla.setRowHeight(30);
            Tabla.getColumnModel().getColumn(0).setMinWidth(40);
            Tabla.getColumnModel().getColumn(0).setMaxWidth(40);

            Tabla.getColumnModel().getColumn(1).setMinWidth(110);
            Tabla.getColumnModel().getColumn(1).setMaxWidth(110);

            Tabla.getColumnModel().getColumn(2).setMinWidth(90);
            Tabla.getColumnModel().getColumn(2).setMaxWidth(90);

            Tabla.getColumnModel().getColumn(3).setMinWidth(100);
            Tabla.getColumnModel().getColumn(3).setMaxWidth(100);

            Tabla.getColumnModel().getColumn(4).setMinWidth(100);
            Tabla.getColumnModel().getColumn(4).setMaxWidth(100);

        } else if (ventana == 1) {
            this.setExtendedState(MAXIMIZED_BOTH);
            Tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
            Tabla.getTableHeader().setPreferredSize(new Dimension(0, 50));
            Tabla.setRowHeight(60);

            Tabla.setFont(new Font("Arial", Font.BOLD, 20));
            Tabla.getColumnModel().getColumn(0).setMinWidth(40);
            Tabla.getColumnModel().getColumn(0).setMaxWidth(40);

            Tabla.getColumnModel().getColumn(1).setMinWidth(170);
            Tabla.getColumnModel().getColumn(1).setMaxWidth(170);

            Tabla.getColumnModel().getColumn(2).setMinWidth(170);
            Tabla.getColumnModel().getColumn(2).setMaxWidth(170);

            Tabla.getColumnModel().getColumn(3).setMinWidth(150);
            Tabla.getColumnModel().getColumn(3).setMaxWidth(150);

            Tabla.getColumnModel().getColumn(4).setMinWidth(150);
            Tabla.getColumnModel().getColumn(4).setMaxWidth(150);
        }

        int ventanaTheme = CLASES.MenuClass.VentanaOpcThemeRet();
        if (ventanaTheme == 1) {
            Color colorPersonalizado = new Color(44, 44, 53);
            Fondo.setBackground(colorPersonalizado);
            opccarla.setForeground(Color.WHITE);
            opcareala.setForeground(Color.WHITE);

            JTableHeader header = Tabla.getTableHeader();
            header.setBackground(new Color(44, 44, 53));
            header.setForeground(Color.WHITE);

            Tabla.setBackground(new Color(50, 50, 50));
            Tabla.setForeground(Color.WHITE);

            // Crea una lista y añade todos los JText
            List<JTextField> camposDeTexto = new ArrayList<>();
            camposDeTexto.add(Nombre);
            camposDeTexto.add(Apellido);
            camposDeTexto.add(DNI);
            camposDeTexto.add(Domicilio);
            camposDeTexto.add(Email);
            camposDeTexto.add(Telefono);

            for (JTextField campo : camposDeTexto) {
                campo.setBackground(colorPersonalizado);
                campo.setForeground(Color.WHITE);

                if (campo.getBorder() instanceof TitledBorder) {
                    TitledBorder titledBorder = (TitledBorder) campo.getBorder();
                    titledBorder.setTitleColor(Color.WHITE);
                    campo.repaint();
                }
            }

            List<JComboBox> componentesEstilo = new ArrayList<>();
            componentesEstilo.add(GS);
            componentesEstilo.add(Cargo);
            for (JComboBox campo : componentesEstilo) {
                campo.setBackground(colorPersonalizado);
                campo.setForeground(Color.WHITE);

                if (campo.getBorder() instanceof TitledBorder) {
                    TitledBorder titledBorder = (TitledBorder) campo.getBorder();
                    titledBorder.setTitleColor(Color.WHITE);
                    campo.repaint();
                }
            }

            List<JDateChooser> Fechacomponente = new ArrayList<>();
            Fechacomponente.add(Fecha);
            for (JDateChooser campo : Fechacomponente) {
                campo.setBackground(colorPersonalizado);
                campo.setForeground(Color.WHITE);

                if (campo.getBorder() instanceof TitledBorder) {
                    TitledBorder titledBorder = (TitledBorder) campo.getBorder();
                    titledBorder.setTitleColor(Color.WHITE);
                    campo.repaint();
                }
            }
        }

        //Mostrar tabla
        try {
            CLASES.Empleados.MostrarEmpleados(con, tabla1);
            if (Tabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún Empleado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR13" + e);
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
        Nombre = new javax.swing.JTextField();
        Apellido = new javax.swing.JTextField();
        DNI = new javax.swing.JTextField();
        Domicilio = new javax.swing.JTextField();
        Email = new javax.swing.JTextField();
        Telefono = new javax.swing.JTextField();
        Fecha = new com.toedter.calendar.JDateChooser();
        GS = new javax.swing.JComboBox<>();
        Cargo = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        Cargar = new javax.swing.JButton();
        Limpiar = new javax.swing.JButton();
        Guardar = new javax.swing.JButton();
        opcareala = new javax.swing.JLabel();
        opcCargo = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Eliminar = new javax.swing.JButton();
        Modificar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        opccarla = new javax.swing.JLabel();
        opcArea = new javax.swing.JComboBox<>();
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
        setTitle("Empleados");
        setMaximumSize(new java.awt.Dimension(1200, 700));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setName("Empleados"); // NOI18N
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);

        Fondo.setBackground(new java.awt.Color(255, 255, 255));
        Fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Nombre.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Nombre/s", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreActionPerformed(evt);
            }
        });
        Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                NombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                NombreKeyTyped(evt);
            }
        });

        Apellido.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Apellido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApellidoActionPerformed(evt);
            }
        });
        Apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ApellidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ApellidoKeyTyped(evt);
            }
        });

        DNI.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "DNI", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        DNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DNIActionPerformed(evt);
            }
        });
        DNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DNIKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DNIKeyTyped(evt);
            }
        });

        Domicilio.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Domicilio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Domicilio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DomicilioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DomicilioKeyTyped(evt);
            }
        });

        Email.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Correo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailActionPerformed(evt);
            }
        });
        Email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                EmailKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                EmailKeyTyped(evt);
            }
        });

        Telefono.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Telefono", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TelefonoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TelefonoKeyTyped(evt);
            }
        });

        Fecha.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Fecha de Nacimiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                FechaKeyReleased(evt);
            }
        });

        GS.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        GS.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opciones" }));
        GS.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Grupo Sanguineo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        GS.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        GS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                GSKeyReleased(evt);
            }
        });

        Cargo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Cargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opciones" }));
        Cargo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Cargo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Cargo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Cargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(GS, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addComponent(Cargo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Apellido, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                            .addComponent(Nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                            .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Domicilio)
                            .addComponent(DNI, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DNI, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Cargo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(GS, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));
        jPanel9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Tabla.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Apellido", "Cargo"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addContainerGap())
        );

        Cargar.setBackground(new java.awt.Color(78, 247, 177));
        Cargar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Cargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/mas.png"))); // NOI18N
        Cargar.setToolTipText("Agregar/cancelar carga");
        Cargar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarActionPerformed(evt);
            }
        });

        Limpiar.setBackground(new java.awt.Color(78, 247, 177));
        Limpiar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Limpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/limp.png"))); // NOI18N
        Limpiar.setToolTipText("Limipiar los campos");
        Limpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LimpiarActionPerformed(evt);
            }
        });

        Guardar.setBackground(new java.awt.Color(78, 247, 177));
        Guardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/guardar.png"))); // NOI18N
        Guardar.setToolTipText("Guardar nuevo empleado");
        Guardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        opcareala.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        opcareala.setText("Opciones de Area:");

        opcCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opciones", "Nuevo", "Modificar", "Eliminar" }));
        opcCargo.setToolTipText("Seleccione una de las tres opciones disponibles");
        opcCargo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        opcCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcCargoActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(52, 170, 121));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado.png"))); // NOI18N
        jLabel1.setText("Cargar nuevo empleado");
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 340, 40));

        Eliminar.setBackground(new java.awt.Color(78, 247, 177));
        Eliminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/x.png"))); // NOI18N
        Eliminar.setToolTipText("Seleccione un empleado para eliminar!");
        Eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Eliminar.setMaximumSize(new java.awt.Dimension(60, 60));
        Eliminar.setMinimumSize(new java.awt.Dimension(60, 60));
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });

        Modificar.setBackground(new java.awt.Color(78, 247, 177));
        Modificar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/edit.png"))); // NOI18N
        Modificar.setToolTipText("Seleccione un empleado para editar!");
        Modificar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Modificar.setMaximumSize(new java.awt.Dimension(60, 60));
        Modificar.setMinimumSize(new java.awt.Dimension(60, 60));
        Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificarActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(52, 170, 121));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Tabla de empleados");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        opccarla.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        opccarla.setText("Opciones de cargo:");

        opcArea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opciones", "Nuevo", "Modificar", "Eliminar" }));
        opcArea.setToolTipText("Seleccione una de las tres opciones disponibles");
        opcArea.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        opcArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcAreaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addComponent(Cargar, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(FondoLayout.createSequentialGroup()
                                .addComponent(opccarla)
                                .addGap(18, 18, 18)
                                .addComponent(opcareala))
                            .addGroup(FondoLayout.createSequentialGroup()
                                .addComponent(opcCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(opcArea, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGap(139, 478, Short.MAX_VALUE)
                        .addComponent(Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Modificar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(opcareala, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(opccarla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(opcArea, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opcCargo)))
                    .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Guardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addComponent(Limpiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Cargar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Eliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Modificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void DNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DNIActionPerformed

    private void NombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NombreActionPerformed

    private void ApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ApellidoActionPerformed

    private void CargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarActionPerformed
        c = 0;
        ImageIcon icon1 = new ImageIcon(getClass().getResource("/IMAGENES/mas.png"));
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/IMAGENES/menos.png"));
        if (x == 0) {
            x = 1;
            Nombre.setEnabled(true);
            Apellido.setEnabled(true);
            DNI.setEnabled(true);
            Domicilio.setEnabled(true);
            Email.setEnabled(true);
            Telefono.setEnabled(true);
            Fecha.setEnabled(true);
            Limpiar.setEnabled(true);
            Guardar.setEnabled(true);
            GS.setEnabled(true);
            Cargo.setEnabled(true);
            opcCargo.setEnabled(false);
            Eliminar.setEnabled(false);
            Modificar.setEnabled(false);

            Menu.setEnabled(false);
            Movimientos.setEnabled(false);
            Asistencia.setEnabled(false);
            Empleados.setEnabled(false);
            Estadisticas.setEnabled(false);
            Configuracion.setEnabled(false);
            Salir.setEnabled(false);
            opcArea.setEnabled(false);

            Cargar.setIcon(icon2);
            Nombre.requestFocus();

            block = 1;
            block2= 1;
        } else if (x == 1) {
            Nombre.setEnabled(false);
            Apellido.setEnabled(false);
            DNI.setEnabled(false);
            Domicilio.setEnabled(false);
            Email.setEnabled(false);
            Telefono.setEnabled(false);
            Fecha.setEnabled(false);
            GS.setEnabled(false);
            Cargo.setEnabled(false);
            Limpiar.setEnabled(false);
            Guardar.setEnabled(false);
            opcCargo.setEnabled(true);
            Eliminar.setEnabled(true);
            Modificar.setEnabled(true);

            Menu.setEnabled(true);
            Movimientos.setEnabled(true);
            Asistencia.setEnabled(true);
            Empleados.setEnabled(true);
            Estadisticas.setEnabled(true);
            Ayuda.setEnabled(true);
            Configuracion.setEnabled(true);
            Salir.setEnabled(true);

            Nombre.setText("");
            Apellido.setText("");
            DNI.setText("");
            Domicilio.setText("");
            Email.setText("");
            Telefono.setText("");
            GS.setSelectedIndex(0);
            Cargo.setSelectedIndex(0);
            Fecha.setDate(null);
            block = 0;
            block2=0;
            opcArea.setEnabled(true);
            Cargar.setIcon(icon1);
            x = 0;
        }
    }//GEN-LAST:event_CargarActionPerformed

    private void LimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LimpiarActionPerformed
        Nombre.setText("");
        Apellido.setText("");
        DNI.setText("");
        Domicilio.setText("");
        Email.setText("");
        Telefono.setText("");
        GS.setSelectedIndex(0);
        Cargo.setSelectedIndex(0);
        Fecha.setDate(null);
    }//GEN-LAST:event_LimpiarActionPerformed

    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed
        if (c == 0) {
            String nomb = Nombre.getText();
            String ap = Apellido.getText();
            String dni = DNI.getText();
            String dom = Domicilio.getText();
            String em = Email.getText();
            String tel = Telefono.getText();

            Date fecha1 = Fecha.getDate();
            if (fecha1 == null) {
                JOptionPane.showMessageDialog(null, "¡HAY CAMPOS VACIOS! Por favor, revise");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fe = sdf.format(fecha1);

            String gs = (String) GS.getSelectedItem();
            String car = (String) Cargo.getSelectedItem();
            if (!nomb.trim().isEmpty() && !ap.trim().isEmpty() && !dni.trim().isEmpty() && !dom.trim().isEmpty() && !em.trim().isEmpty() && !tel.trim().isEmpty() && !gs.equals("Opciones") && !car.equals("Opciones")) {
                if (em.contains("@") && em.contains(".")) {
                    try {
                        int validacionunique=CLASES.Empleados.AgregarEmpleados(con, nomb, ap, dni, dom, em, tel, fe, gs, car, iduser);
                        if (validacionunique==0){
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "Guardado");
                    } catch (HeadlessException | SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR1");
                    }

                    try {
                        tabla1.setRowCount(0); // Limpia
                        CLASES.Empleados.MostrarEmpleados(con, tabla1);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
                    }

                    Nombre.setText("");
                    Apellido.setText("");
                    DNI.setText("");
                    Domicilio.setText("");
                    Email.setText("");
                    Telefono.setText("");
                    GS.setSelectedIndex(0);
                    Cargo.setSelectedIndex(0);
                    Fecha.setDate(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Introduzca un correo valido!");
                }

            } else {
                JOptionPane.showMessageDialog(null, "¡HAY CAMPOS VACIOS! Por favor, revise");
            }

        } else if (c == 1) {
            String nomb = Nombre.getText();
            String ap = Apellido.getText();
            String dni = DNI.getText();
            String dom = Domicilio.getText();
            String em = Email.getText();
            String tel = Telefono.getText();

            Date fecha1 = Fecha.getDate();
            if (fecha1 == null) {
                JOptionPane.showMessageDialog(null, "¡HAY CAMPOS VACIOS! Por favor, revise");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Puedes cambiar el formato según lo necesites
            String fe = sdf.format(fecha1);

            String gs = (String) GS.getSelectedItem();
            String car = (String) Cargo.getSelectedItem();

            if (!nomb.trim().isEmpty() && !ap.trim().isEmpty() && !dni.trim().isEmpty() && !dom.trim().isEmpty() && !em.trim().isEmpty() && !tel.trim().isEmpty() && !gs.equals("Opciones") && !car.equals("Opciones")) {
                if (em.contains("@") && em.contains(".")) {
                    try {
                        int validacionunique=CLASES.Empleados.ModificarEmpleados(con, id, nomb, ap, dni, dom, em, tel, fe, gs, car, iduser);
                        if (validacionunique==0){
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "Actualizado");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "ERROR1");
                    }

                    try {
                        tabla1.setRowCount(0); // Limpia
                        CLASES.Empleados.MostrarEmpleados(con, tabla1);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al refrescar categorías");
                    }
                    Nombre.setText("");
                    Apellido.setText("");
                    DNI.setText("");
                    Domicilio.setText("");
                    Email.setText("");
                    Telefono.setText("");

                    Nombre.setEnabled(false);
                    Apellido.setEnabled(false);
                    DNI.setEnabled(false);
                    Domicilio.setEnabled(false);
                    Email.setEnabled(false);
                    Telefono.setEnabled(false);
                    Fecha.setEnabled(false);
                    GS.setEnabled(false);
                    Cargo.setEnabled(false);
                    Limpiar.setEnabled(false);
                    Guardar.setEnabled(false);
                    opcCargo.setEnabled(true);
                    Eliminar.setEnabled(true);
                    Modificar.setEnabled(true);

                    Menu.setEnabled(true);
                    Movimientos.setEnabled(true);
                    Asistencia.setEnabled(true);
                    Empleados.setEnabled(true);
                    Estadisticas.setEnabled(true);
                    Ayuda.setEnabled(true);
                    Configuracion.setEnabled(true);
                    Salir.setEnabled(true);
                    Cargar.setText("Cargar");
                    GS.setSelectedIndex(0);
                    Cargo.setSelectedIndex(0);
                    Fecha.setDate(null);
                    x = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "Introduzca un correo valido!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "¡HAY CAMPOS VACIOS! Por favor, revise");
            }
        }
    }//GEN-LAST:event_GuardarActionPerformed

    private void NombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NombreKeyTyped
        char c = evt.getKeyChar();

        //Permitir teclas de control como Backspace, Delete, Enter, etc.
        if (Character.isISOControl(c)) {
            return;
        }

        //Solo permitir letras y espacios
        if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
            getToolkit().beep();
            evt.consume();
        }

        String textoActual = Nombre.getText();
        int longitud = textoActual.length();

        //Evita que se repita mas de dos letras seguidas
        if (longitud >= 2) {
            char ultimo = textoActual.charAt(longitud - 1);
            char penultimo = textoActual.charAt(longitud - 2);

            if (ultimo == penultimo && penultimo == c) {
                getToolkit().beep();
                evt.consume(); // evita que se escriba
            }
        }
        
        if (Nombre.getText().length() >= 44) {
            evt.consume();
        }
    }//GEN-LAST:event_NombreKeyTyped

    private void ApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ApellidoKeyTyped
        char e = evt.getKeyChar();

        //Permitir teclas de control como Backspace, Delete, Enter, etc.
        if (Character.isISOControl(e)) {
            return;
        }

        //Solo permitir letras y espacios
        if (!Character.isLetter(e) && !Character.isWhitespace(e)) {
            getToolkit().beep();
            evt.consume();
            return;
        }

        String textoActual = Apellido.getText();
        int longitud = textoActual.length();

        if (longitud >= 2) {
            char ultimo = textoActual.charAt(longitud - 1);
            char penultimo = textoActual.charAt(longitud - 2);

            if (ultimo == penultimo && penultimo == e) {
                getToolkit().beep();
                evt.consume(); // evita que se escriba
            }
        }
        
        if (Apellido.getText().length() >= 44) {
            evt.consume();
        }
    }//GEN-LAST:event_ApellidoKeyTyped

    private void DNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DNIKeyTyped
        char r = evt.getKeyChar();

        if (Character.isISOControl(r)) {
            return; // permite borrar, mover, etc.
        }

        //Solo permite letras, numeros y signos no
        if (!Character.isDigit(r)) {
            getToolkit().beep();
            evt.consume();
        }
        
        if (DNI.getText().length() >= 44) {
            evt.consume();
        }
    }//GEN-LAST:event_DNIKeyTyped

    private void DomicilioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DomicilioKeyTyped
        char t = evt.getKeyChar();

        //Permitir teclas de control como Backspace, Delete, Enter, etc.
        if (Character.isISOControl(t)) {
            return;
        }

        String textoActual = Domicilio.getText();
        int longitud = textoActual.length();

        //Solo controlar repetición si es una letra
        if (Character.isLetter(t) && longitud >= 2) {
            char ultimo = textoActual.charAt(longitud - 1);
            char penultimo = textoActual.charAt(longitud - 2);

            if (ultimo == penultimo && penultimo == t) {
                getToolkit().beep();
                evt.consume(); // evita que se escriba
            }
        }
        
        if (Domicilio.getText().length() >= 199) {
            evt.consume();
        }
    }//GEN-LAST:event_DomicilioKeyTyped

    private void TelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TelefonoKeyTyped
        char r = evt.getKeyChar();

        if (Character.isISOControl(r)) {
            return; // permite borrar, mover, etc.
        }

        //Solo permite letras, numeros y signos no
        if (!Character.isDigit(r)) {
            getToolkit().beep();
            evt.consume();
        }
        
        if (Telefono.getText().length() >= 44) {
            evt.consume();
        }
    }//GEN-LAST:event_TelefonoKeyTyped

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
                int borrado = 1;
                try {
                    CLASES.Empleados.EliminarEmpleados(con, cod, borrado, iduser);
                    JOptionPane.showMessageDialog(null, "Eliminado");
                    tabla1.setRowCount(0); // Limpia
                    CLASES.Empleados.MostrarEmpleados(con, tabla1);
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

    private void ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificarActionPerformed
        x = 1;
        c = 1;
        int filaSeleccionada = Tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            int fila = Tabla.getSelectedRow();
            int cod = Integer.parseInt(Tabla.getValueAt(fila, 0).toString());
            id = Integer.parseInt(Tabla.getValueAt(fila, 0).toString());
            Guardar.setEnabled(true);
            ImageIcon icon2 = new ImageIcon(getClass().getResource("/IMAGENES/menos.png"));
            try {
                CLASES.Empleados.SacarEmpleados(con, cod, Nombre, Apellido, DNI, Domicilio, Email, Telefono, Fecha, GS, Cargo);
                Nombre.setEnabled(true);
                Apellido.setEnabled(true);
                DNI.setEnabled(true);
                Domicilio.setEnabled(true);
                Email.setEnabled(true);
                Telefono.setEnabled(true);
                Fecha.setEnabled(true);
                Cargar.setIcon(icon2);
                
                Limpiar.setEnabled(true);
                Guardar.setEnabled(true);
                GS.setEnabled(true);
                Cargo.setEnabled(true);
                opcCargo.setEnabled(false);
                opcArea.setEnabled(false);
                Eliminar.setEnabled(false);

                Menu.setEnabled(false);
                Movimientos.setEnabled(false);
                Asistencia.setEnabled(false);
                Empleados.setEnabled(false);
                Estadisticas.setEnabled(false);
                Ayuda.setEnabled(false);
                Configuracion.setEnabled(false);
                Salir.setEnabled(false);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR" + ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione una fila primero.");

        }
    }//GEN-LAST:event_ModificarActionPerformed

    private void opcCargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcCargoActionPerformed
        String seleccion = (String) opcCargo.getSelectedItem();

        switch (seleccion) {
            case "Nuevo":
                AddAreaCargo ventana3 = new AddAreaCargo(this, 1);
                ventana3.setVisible(true);
                refrescarCombo();
                break;
            case "Modificar":
                // Abrís ventana de modificación
                ModElimCargo ventana = new ModElimCargo(1, this);
                ventana.setVisible(true);
                refrescarCombo();
                break;

            case "Eliminar":
                // Abrís ventana de eliminación
                ModElimCargo ventana2 = new ModElimCargo(0, this);
                ventana2.setVisible(true);
                refrescarCombo();
                break;
        }

        opcCargo.setSelectedIndex(0);
    }//GEN-LAST:event_opcCargoActionPerformed

    private void CargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CargoActionPerformed

    private void EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailActionPerformed

    private void opcAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcAreaActionPerformed
        String seleccion = (String) opcArea.getSelectedItem();

        switch (seleccion) {
            case "Nuevo":
                AddAreaCargo ventana3 = new AddAreaCargo(this, 0);
                ventana3.setVisible(true);
                refrescarCombo();
                break;
            case "Modificar":
                // Abrís ventana de modificación
                ModElimArea ventana = new ModElimArea(1, this);
                ventana.setVisible(true);
                refrescarCombo();
                break;

            case "Eliminar":
                // Abrís ventana de eliminación
                ModElimArea ventana2 = new ModElimArea(0, this);
                ventana2.setVisible(true);
                refrescarCombo();
                break;
        }

        opcArea.setSelectedIndex(0);
        try {
            tabla1.setRowCount(0);
            CLASES.Empleados.MostrarEmpleados(con, tabla1);
            if (Tabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún Empleado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR13" + e);
        }
    }//GEN-LAST:event_opcAreaActionPerformed

    private void NombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NombreKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            Apellido.requestFocus();
        }
    }//GEN-LAST:event_NombreKeyReleased

    private void ApellidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ApellidoKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            DNI.requestFocus();
        }
    }//GEN-LAST:event_ApellidoKeyReleased

    private void DNIKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DNIKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            Domicilio.requestFocus();
        }
    }//GEN-LAST:event_DNIKeyReleased

    private void DomicilioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DomicilioKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            Email.requestFocus();
        }
    }//GEN-LAST:event_DomicilioKeyReleased

    private void TelefonoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TelefonoKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            Fecha.requestFocus();
        }
    }//GEN-LAST:event_TelefonoKeyReleased

    private void FechaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FechaKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            GS.requestFocus();
        }
    }//GEN-LAST:event_FechaKeyReleased

    private void GSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GSKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            Cargo.requestFocus();
        }
    }//GEN-LAST:event_GSKeyReleased

    private void EmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmailKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            Telefono.requestFocus();
        }
    }//GEN-LAST:event_EmailKeyReleased

    private void EmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmailKeyTyped
        if (Email.getText().length() >= 44) {
            evt.consume();
        }
    }//GEN-LAST:event_EmailKeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        RegistrarFrm ventana = new RegistrarFrm(this, 1);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        RegistrarFrm ventana = new RegistrarFrm(this, 2);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        AddDelUser ventana = new AddDelUser(this, 1);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void iniciomovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciomovActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_iniciomovActionPerformed

    private void historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialActionPerformed
        Historial ventana = new Historial(this);
        ventana.setVisible(true);
    }//GEN-LAST:event_historialActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        AddTri ventana = new AddTri(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        AddTri ventana = new AddTri(1, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        int ban = 1;
        AddDelVicTrip ventana = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        int ban = 0;
        AddVic ventana = new AddVic(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        int ban = 1;
        AddVic ventana = new AddVic(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        int ban = 0;
        AddDelVicTrip ventana = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void MovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MovimientosActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MovimientosActionPerformed

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

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        AddAreaCargo ventana3 = new AddAreaCargo(this, 1);
        if (rango == 1) {
            ventana3.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

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

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        AddAreaCargo ventana3 = new AddAreaCargo(this, 0);
        if (rango == 1) {
            ventana3.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        ModElimArea1 ventana = new ModElimArea1(1, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ModElimArea1 ventana = new ModElimArea1(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void AyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AyudaActionPerformed
        String direccion1 = "\\RECURSOS\\Menu.pdf";
        CLASES.Movimientos.abrirPDF(direccion1);
    }//GEN-LAST:event_AyudaActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SalirActionPerformed

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
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Apellido;
    private javax.swing.JMenu Asistencia;
    private javax.swing.JMenu Ayuda;
    private javax.swing.JMenuBar Barra;
    private javax.swing.JButton Cargar;
    private javax.swing.JComboBox<String> Cargo;
    private javax.swing.JMenu Configuracion;
    private javax.swing.JTextField DNI;
    private javax.swing.JTextField Domicilio;
    private javax.swing.JButton Eliminar;
    private javax.swing.JTextField Email;
    private javax.swing.JMenu Empleados;
    private javax.swing.JMenu Estadisticas;
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JPanel Fondo;
    private javax.swing.JComboBox<String> GS;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Limpiar;
    private javax.swing.JMenu Menu;
    private javax.swing.JButton Modificar;
    private javax.swing.JMenu Movimientos;
    private javax.swing.JTextField Nombre;
    private javax.swing.JMenu Salir;
    private javax.swing.JTable Tabla;
    private javax.swing.JTextField Telefono;
    private javax.swing.JMenu cargoemp;
    private javax.swing.JMenuItem elim;
    private javax.swing.JMenuItem historial;
    private javax.swing.JMenuItem inicemp;
    private javax.swing.JMenuItem iniciomov;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mod;
    private javax.swing.JComboBox<String> opcArea;
    private javax.swing.JComboBox<String> opcCargo;
    private javax.swing.JLabel opcareala;
    private javax.swing.JLabel opccarla;
    // End of variables declaration//GEN-END:variables

}
