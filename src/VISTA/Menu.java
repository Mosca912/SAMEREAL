/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CONEXIONES.Conexiones;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Facuymayriver
 */
public final class Menu extends javax.swing.JFrame implements CLASES.IBlockableFrame {

    public int block, block2 = 0;
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int id = 0, valid, rango = 0;
    String veri = "Opciones", fechaFormateada;
    private final char caracterEchoPredeterminado;
    String ayuda2 = "Menu";

    public void validado() {
        DNI.setVisible(false);
        contrasena.setVisible(false);
        iniciarsesion1.setVisible(false);
        cambiarcont.setVisible(false);
        cerrarsesion.setVisible(true);
        MostOcPass.setVisible(false);
    }

    public void cerrado() {
        DNI.setVisible(true);
        contrasena.setVisible(true);
        labelogin.setVisible(true);
        iniciarsesion1.setVisible(true);
        cambiarcont.setVisible(true);
        cerrarsesion.setVisible(true);
        MostOcPass.setVisible(true);
    }

    public void botones() {
        Barra.setEnabled(true);
        Menu.setEnabled(true);
        Movimientos.setEnabled(true);
        Asistencia.setEnabled(true);
        Empleados.setEnabled(true);
        Estadisticas.setEnabled(true);
        Ayuda.setEnabled(true);
        Configuracion.setEnabled(true);
        Salir.setEnabled(true);
        block = 0;
    }

    public void botonesblock() {
        block = 1;
        Barra.setEnabled(false);
        Menu.setEnabled(false);
        Movimientos.setEnabled(false);
        Asistencia.setEnabled(false);
        Empleados.setEnabled(false);
        Estadisticas.setEnabled(false);
        Configuracion.setEnabled(false);
        cerrarsesion.setVisible(false);
        registrarse.setVisible(false);
        modificaruser.setVisible(false);

        DNI.setVisible(true);
        contrasena.setVisible(true);
        labelogin.setVisible(true);
        iniciarsesion1.setVisible(true);
        cambiarcont.setVisible(true);
        cerrarsesion.setVisible(false);
        eliminaruser.setVisible(false);
        MostOcPass.setVisible(true);
    }

    @Override
    public int getBlockState() {
        return this.block;
    }

    @Override
    public int getBlockStateSalir() {
        return this.block2;
    }

    class FondoInicio extends JPanel {

        private final Image imagen5;

        public FondoInicio(String rutaImagen) {
            imagen5 = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(imagen5, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dibuja la imagen escalada al tamaño del JPanel
            g.drawImage(imagen5, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public Menu() {

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

        block = 1;
        Barra.setEnabled(false);
        Menu.setEnabled(false);
        Movimientos.setEnabled(false);
        Asistencia.setEnabled(false);
        Empleados.setEnabled(false);
        Estadisticas.setEnabled(false);
        Configuracion.setEnabled(false);
        cerrarsesion.setVisible(false);
        modificaruser.setVisible(false);
        eliminaruser.setVisible(false);
        Datos.setText("");

        registrarse.setVisible(false);

        caracterEchoPredeterminado = contrasena.getEchoChar();

        this.setLocationRelativeTo(null);
        CLASES.MenuClass menuHelper = new CLASES.MenuClass();
        menuHelper.MenuConfig(Movimientos, Menu, Asistencia, Empleados, Estadisticas, Ayuda, Configuracion, Salir, this, ayuda2);

        int ventana = CLASES.MenuClass.Ventana();
        if (ventana == 0) {
            this.setExtendedState(NORMAL);
        } else if (ventana == 1) {
            this.setExtendedState(MAXIMIZED_BOTH);
        }

        valid = CLASES.Usuario.verificacion();
        if (valid == 1) {
            botones();
            validado();
            CLASES.Usuario.nombre(labelogin, Datos);
            Salir.setText("Cerrar Sesión");
        }

        rango = CLASES.Usuario.rango();
        if (rango == 1) {
            registrarse.setVisible(true);
            modificaruser.setVisible(true);
            eliminaruser.setVisible(true);
        }

        int newuser = CLASES.Usuario.Verificacion(con);

        if (newuser == 0) {
            JOptionPane.showMessageDialog(null, "No hay usuarios!, se procedera a crear un registro nuevo");
            RegistrarFrm ventana2 = new RegistrarFrm(this, 0);
            ventana2.setVisible(true);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        Fondo = new FondoInicio("/IMAGENES/fondoamb.png");
        menuinicio = new FondoInicio("/IMAGENES/iniciosesion.png");
        jSeparator1 = new javax.swing.JSeparator();
        DNI = new javax.swing.JTextField();
        contrasena = new javax.swing.JPasswordField();
        registrarse = new javax.swing.JButton();
        labelogin = new javax.swing.JLabel();
        iniciarsesion1 = new javax.swing.JButton();
        cambiarcont = new javax.swing.JButton();
        cerrarsesion = new javax.swing.JButton();
        MostOcPass = new javax.swing.JButton();
        Datos = new javax.swing.JLabel();
        modificaruser = new javax.swing.JButton();
        eliminaruser = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
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

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu7.setText("jMenu7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bienvenido al sistema de SAME 107");
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1200, 700));
        setResizable(false);

        Fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        menuinicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        menuinicio.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 20, 250));

        DNI.setBackground(new java.awt.Color(204, 204, 204));
        DNI.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        DNI.setToolTipText("Ingrese su DNI");
        DNI.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), "DNI", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        DNI.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
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
        menuinicio.add(DNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 100, 40));

        contrasena.setBackground(new java.awt.Color(204, 204, 204));
        contrasena.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        contrasena.setToolTipText("Ingrese su contraseña");
        contrasena.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Contraseña", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        contrasena.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        contrasena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contrasenaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                contrasenaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                contrasenaKeyTyped(evt);
            }
        });
        menuinicio.add(contrasena, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 190, 40));

        registrarse.setBackground(new java.awt.Color(52, 170, 121));
        registrarse.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        registrarse.setForeground(new java.awt.Color(255, 255, 255));
        registrarse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado.png"))); // NOI18N
        registrarse.setText("Registrarse nuevo usuario");
        registrarse.setToolTipText("Registrar un nuevo usuario");
        registrarse.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        registrarse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registrarse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        registrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrarseActionPerformed(evt);
            }
        });
        menuinicio.add(registrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 250, 60));

        labelogin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"))); // NOI18N
        labelogin.setText("Login");
        menuinicio.add(labelogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 260, 50));

        iniciarsesion1.setBackground(new java.awt.Color(52, 170, 121));
        iniciarsesion1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        iniciarsesion1.setForeground(new java.awt.Color(255, 255, 255));
        iniciarsesion1.setText("Iniciar Sesión");
        iniciarsesion1.setToolTipText("Iniciar sesión");
        iniciarsesion1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        iniciarsesion1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iniciarsesion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarsesion1ActionPerformed(evt);
            }
        });
        menuinicio.add(iniciarsesion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 130, 40));

        cambiarcont.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cambiarcont.setForeground(new java.awt.Color(52, 170, 121));
        cambiarcont.setText("¿Olvidaste la contraseña?");
        cambiarcont.setToolTipText("");
        cambiarcont.setBorder(null);
        cambiarcont.setContentAreaFilled(false);
        cambiarcont.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cambiarcont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarcontActionPerformed(evt);
            }
        });
        menuinicio.add(cambiarcont, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 170, -1));

        cerrarsesion.setBackground(new java.awt.Color(52, 170, 121));
        cerrarsesion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cerrarsesion.setForeground(new java.awt.Color(255, 255, 255));
        cerrarsesion.setText("Cerrar Sesión");
        cerrarsesion.setToolTipText("Cerrar sesión!");
        cerrarsesion.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cerrarsesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cerrarsesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarsesionActionPerformed(evt);
            }
        });
        menuinicio.add(cerrarsesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, 120, 40));

        MostOcPass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/ojoabierto.png"))); // NOI18N
        MostOcPass.setToolTipText("Ver/Ocultar contraseña");
        MostOcPass.setBorder(null);
        MostOcPass.setContentAreaFilled(false);
        MostOcPass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MostOcPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MostOcPassActionPerformed(evt);
            }
        });
        menuinicio.add(MostOcPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 40, 40));

        Datos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Datos.setText("jLabel1");
        menuinicio.add(Datos, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 150, 30));

        modificaruser.setBackground(new java.awt.Color(52, 170, 121));
        modificaruser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        modificaruser.setForeground(new java.awt.Color(255, 255, 255));
        modificaruser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"))); // NOI18N
        modificaruser.setText("Modificar Usuario");
        modificaruser.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        modificaruser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        modificaruser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        modificaruser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificaruserActionPerformed(evt);
            }
        });
        menuinicio.add(modificaruser, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, 250, 50));

        eliminaruser.setBackground(new java.awt.Color(52, 170, 121));
        eliminaruser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        eliminaruser.setForeground(new java.awt.Color(255, 255, 255));
        eliminaruser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/x.png"))); // NOI18N
        eliminaruser.setText("Eliminar");
        eliminaruser.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        eliminaruser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eliminaruser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        eliminaruser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminaruserActionPerformed(evt);
            }
        });
        menuinicio.add(eliminaruser, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 250, 50));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setText("SAME TRANSPORTE");

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap(237, Short.MAX_VALUE)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(menuinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(307, Short.MAX_VALUE))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iniciomovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciomovActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_iniciomovActionPerformed

    private void historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialActionPerformed
        Historial ventana = new Historial(this);
        ventana.setVisible(true);
    }//GEN-LAST:event_historialActionPerformed

    private void modActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(1, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_modActionPerformed

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

    private void elimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_elimActionPerformed

    private void MovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MovimientosActionPerformed
        Movimiento ventana = new Movimiento();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MovimientosActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SalirActionPerformed

    private void registrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrarseActionPerformed
        RegistrarFrm ventana = new RegistrarFrm(this, 1);
        ventana.setVisible(true);
    }//GEN-LAST:event_registrarseActionPerformed

    private void iniciarsesion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarsesion1ActionPerformed
        String dni = DNI.getText();
        String cont = contrasena.getText();
        if (!dni.trim().isEmpty() && !cont.trim().isEmpty()) {
            try {
                valid = CLASES.Usuario.IniciarSesion(con, dni, cont, labelogin, Datos);
            } catch (SQLException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (valid == 1) {
                botones();
                validado();
                DNI.setText("");
                contrasena.setText("");
                Salir.setText("Cerrar Sesión");
            }
            rango = CLASES.Usuario.rango();
            if (rango == 1) {
                registrarse.setVisible(true);
                modificaruser.setVisible(true);
                eliminaruser.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(null, "¡HAY CAMPOS VACIOS! Por favor, revise");
        }
    }//GEN-LAST:event_iniciarsesion1ActionPerformed

    private void cambiarcontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiarcontActionPerformed
        CambCont ventana = new CambCont(this);
        ventana.setVisible(true);
    }//GEN-LAST:event_cambiarcontActionPerformed

    private void MostOcPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MostOcPassActionPerformed
        ImageIcon icon1 = new ImageIcon(getClass().getResource("/IMAGENES/ojocerrado.png"));
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/IMAGENES/ojoabierto.png"));
        char caracterActual = contrasena.getEchoChar();
        if (caracterActual != (char) 0) {
            contrasena.setEchoChar((char) 0);
            MostOcPass.setIcon(icon1);
        } else {
            contrasena.setEchoChar(caracterEchoPredeterminado);
            MostOcPass.setIcon(icon2);
        }
    }//GEN-LAST:event_MostOcPassActionPerformed

    private void cerrarsesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarsesionActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                null,
                "¿Desea cerrar sesión?",
                "Confirmar acción",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                CLASES.Usuario.CerrarSesion(con, labelogin, Datos);
            } catch (SQLException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            valid = CLASES.Usuario.verificacion();
            rango = CLASES.Usuario.rango();
            botonesblock();
            Salir.setText("Salir");

        } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            System.out.println("Cancelado");
        }
    }//GEN-LAST:event_cerrarsesionActionPerformed

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

        if (DNI.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_DNIKeyTyped

    private void AyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AyudaActionPerformed
        String direccion1 = "\\RECURSOS\\Menu.pdf";
        CLASES.Movimientos.abrirPDF(direccion1);
    }//GEN-LAST:event_AyudaActionPerformed

    private void modificaruserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificaruserActionPerformed
        RegistrarFrm ventana = new RegistrarFrm(this, 2);
        ventana.setVisible(true);
    }//GEN-LAST:event_modificaruserActionPerformed

    private void DNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DNIActionPerformed

    private void DNIKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DNIKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            contrasena.requestFocus();
        }
    }//GEN-LAST:event_DNIKeyReleased

    private void contrasenaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contrasenaKeyReleased
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            iniciarsesion1.requestFocus();
        }
    }//GEN-LAST:event_contrasenaKeyReleased

    private void contrasenaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contrasenaKeyTyped
        if (contrasena.getText().length() >= 99) {
            evt.consume();
        }
    }//GEN-LAST:event_contrasenaKeyTyped

    private void contrasenaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contrasenaKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            iniciarsesion1.doClick();
        }
    }//GEN-LAST:event_contrasenaKeyPressed

    private void eliminaruserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminaruserActionPerformed
        AddDelUser ventana = new AddDelUser(this, 1);
        ventana.setVisible(true);
    }//GEN-LAST:event_eliminaruserActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        RegistrarFrm ventana = new RegistrarFrm(this, 1);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        int ban = 1;
        AddDelVicTrip ventana = new AddDelVicTrip(this, ban);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        AddTri ventana = new AddTri(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

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

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        AddTri ventana = new AddTri(1, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        AddAreaCargo ventana3 = new AddAreaCargo(this, 1);
        if (rango == 1) {
            ventana3.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        ModElimArea1 ventana = new ModElimArea1(1, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }

    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        AddAreaCargo ventana3 = new AddAreaCargo(this, 0);
        if (rango == 1) {
            ventana3.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ModElimArea1 ventana = new ModElimArea1(0, this);
        if (rango == 1) {
            ventana.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Es un usuario lector!");
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

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
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Asistencia;
    private javax.swing.JMenu Ayuda;
    private javax.swing.JMenuBar Barra;
    private javax.swing.JMenu Configuracion;
    private javax.swing.JTextField DNI;
    private javax.swing.JLabel Datos;
    private javax.swing.JMenu Empleados;
    private javax.swing.JMenu Estadisticas;
    private javax.swing.JPanel Fondo;
    private javax.swing.JMenu Menu;
    private javax.swing.JButton MostOcPass;
    private javax.swing.JMenu Movimientos;
    private javax.swing.JMenu Salir;
    private javax.swing.JButton cambiarcont;
    private javax.swing.JMenu cargoemp;
    private javax.swing.JButton cerrarsesion;
    private javax.swing.JPasswordField contrasena;
    private javax.swing.JMenuItem elim;
    private javax.swing.JButton eliminaruser;
    private javax.swing.JMenuItem historial;
    private javax.swing.JMenuItem inicemp;
    private javax.swing.JButton iniciarsesion1;
    private javax.swing.JMenuItem iniciomov;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelogin;
    private javax.swing.JPanel menuinicio;
    private javax.swing.JMenuItem mod;
    private javax.swing.JButton modificaruser;
    private javax.swing.JButton registrarse;
    // End of variables declaration//GEN-END:variables
}
