/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author Facuymayriver
 */
public class Menu1 extends javax.swing.JFrame {

    class FondoInicio extends JPanel {

        private Image imagen5;

        public FondoInicio(String rutaImagen) {
            imagen5 = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        }

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

    public Menu1() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // usa el clásico Metal
            UIManager.put("MenuBar.background", new Color(52, 170, 121));
            UIManager.put("Menu.foreground", Color.WHITE);
            UIManager.put("MenuItem.background", new Color(52, 170, 121));
            UIManager.put("MenuItem.foreground", Color.WHITE);
            UIManager.put("Menu.selectionBackground", new Color(52, 170, 121));
            UIManager.put("Menu.selectionForeground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        this.setLocationRelativeTo(null);
        Icon iconNormal = new ImageIcon(getClass().getResource("/IMAGENES/movimiento.png"));
        Icon iconHover = new ImageIcon(getClass().getResource("/IMAGENES/movimiento2.png"));

        Icon iconNormal2 = new ImageIcon(getClass().getResource("/IMAGENES/menui.png"));
        Icon iconHover2 = new ImageIcon(getClass().getResource("/IMAGENES/menui2.png"));

        Icon iconNormal3 = new ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"));
        Icon iconHover3 = new ImageIcon(getClass().getResource("/IMAGENES/asistencia2.png"));

        Icon iconNormal4 = new ImageIcon(getClass().getResource("/IMAGENES/empleado.png"));
        Icon iconHover4 = new ImageIcon(getClass().getResource("/IMAGENES/empleado2.png"));

        Icon iconNormal5 = new ImageIcon(getClass().getResource("/IMAGENES/estats.png"));
        Icon iconHover5 = new ImageIcon(getClass().getResource("/IMAGENES/estats2.png"));

        Icon iconNormal6 = new ImageIcon(getClass().getResource("/IMAGENES/ayuda.png"));
        Icon iconHover6 = new ImageIcon(getClass().getResource("/IMAGENES/ayuda2.png"));

        Icon iconNormal7 = new ImageIcon(getClass().getResource("/IMAGENES/config.png"));
        Icon iconHover7 = new ImageIcon(getClass().getResource("/IMAGENES/config2.png"));

        Icon iconNormal8 = new ImageIcon(getClass().getResource("/IMAGENES/salir.png"));
        Icon iconHover8 = new ImageIcon(getClass().getResource("/IMAGENES/salir2.png"));

        Movimientos.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
        Menu.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
        Asistencia.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));

        Movimientos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Movimientos.setIcon(iconHover);
                Movimientos.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Movimientos.setIcon(iconNormal);
                Movimientos.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Menu.setIcon(iconHover2);
                Menu.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Menu.setIcon(iconNormal2);
                Menu.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Asistencia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Asistencia.setIcon(iconHover3);
                Asistencia.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Asistencia.setIcon(iconNormal3);
                Asistencia.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Empleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Empleados.setIcon(iconHover4);
                Empleados.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Empleados.setIcon(iconNormal4);
                Empleados.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Estadisticas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Estadisticas.setIcon(iconHover5);
                Estadisticas.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Estadisticas.setIcon(iconNormal5);
                Estadisticas.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Ayuda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Ayuda.setIcon(iconHover6);
                Ayuda.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Ayuda.setIcon(iconNormal6);
                Ayuda.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Configuracion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Configuracion.setIcon(iconHover7);
                Configuracion.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Configuracion.setIcon(iconNormal7);
                Configuracion.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });

        Salir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Salir.setIcon(iconHover8);
                Salir.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Salir.setIcon(iconNormal8);
                Salir.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
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

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        Fondo = new FondoInicio("/IMAGENES/fondoamb.png");
        menuinicio = new FondoInicio("/IMAGENES/iniciosesion.png");
        jSeparator1 = new javax.swing.JSeparator();
        Usuario = new javax.swing.JTextField();
        Contraseña = new javax.swing.JPasswordField();
        registrarse = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        iniciarsesion1 = new javax.swing.JButton();
        olvide = new javax.swing.JLabel();
        Ayudin = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Barra = new javax.swing.JMenuBar();
        Menu = new javax.swing.JMenu();
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
        nuev = new javax.swing.JMenuItem();
        mod = new javax.swing.JMenuItem();
        elim = new javax.swing.JMenuItem();
        Estadisticas = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        Ayuda = new javax.swing.JMenu();
        iniayu = new javax.swing.JMenuItem();
        Configuracion = new javax.swing.JMenu();
        iniconf = new javax.swing.JMenuItem();
        Salir = new javax.swing.JMenu();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu7.setText("jMenu7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bienvenido al sistema de SAME 107");

        menuinicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        menuinicio.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 20, 250));

        Usuario.setBackground(new java.awt.Color(204, 204, 204));
        Usuario.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Usuario", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Usuario.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        menuinicio.add(Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 270, 40));

        Contraseña.setBackground(new java.awt.Color(204, 204, 204));
        Contraseña.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Contraseña", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        Contraseña.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        menuinicio.add(Contraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 270, 40));

        registrarse.setBackground(new java.awt.Color(52, 170, 121));
        registrarse.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        registrarse.setForeground(new java.awt.Color(255, 255, 255));
        registrarse.setText("Registrarse");
        registrarse.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        registrarse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuinicio.add(registrarse, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 120, 40));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/asistencia.png"))); // NOI18N
        jLabel1.setText("Login");
        menuinicio.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 100, 50));

        iniciarsesion1.setBackground(new java.awt.Color(52, 170, 121));
        iniciarsesion1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        iniciarsesion1.setForeground(new java.awt.Color(255, 255, 255));
        iniciarsesion1.setText("Iniciar Sesión");
        iniciarsesion1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        iniciarsesion1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuinicio.add(iniciarsesion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 120, 40));

        olvide.setBackground(new java.awt.Color(52, 170, 121));
        olvide.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        olvide.setForeground(new java.awt.Color(52, 170, 121));
        olvide.setText("¿Olvidaste la contraseña?");
        menuinicio.add(olvide, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 180, 50));

        Ayudin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Ayudin.setForeground(new java.awt.Color(52, 170, 121));
        Ayudin.setText("Ayuda");
        menuinicio.add(Ayudin, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 290, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/empleado.png"))); // NOI18N
        jLabel2.setText("Nuevo Usuario");
        menuinicio.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 90, 150, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setText("SAME TRANSPORTE");

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addContainerGap(290, Short.MAX_VALUE)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                        .addComponent(menuinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(254, Short.MAX_VALUE))))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoLayout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuinicio, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(152, Short.MAX_VALUE))
        );

        Barra.setBackground(new java.awt.Color(52, 170, 121));

        Menu.setBackground(new java.awt.Color(204, 255, 204));
        Menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/menui.png"))); // NOI18N
        Menu.setText("Menu");
        Menu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Menu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

        nuev.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nuev.setText("Nuevo");
        cargoemp.add(nuev);

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

        iniayu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        iniayu.setText("Ayuda");
        iniayu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniayuActionPerformed(evt);
            }
        });
        Ayuda.add(iniayu);

        Barra.add(Ayuda);

        Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/config.png"))); // NOI18N
        Configuracion.setText("Configuración");
        Configuracion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Configuracion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        iniconf.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        iniconf.setText("Inicio");
        iniconf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniconfActionPerformed(evt);
            }
        });
        Configuracion.add(iniconf);

        Barra.add(Configuracion);

        Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/salir.png"))); // NOI18N
        Salir.setText("Salir");
        Salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Salir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

    private void nuevtripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevtripActionPerformed
        AddTri ventana = new AddTri(0);
        ventana.setVisible(true);
    }//GEN-LAST:event_nuevtripActionPerformed

    private void historialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_historialActionPerformed

    private void nuevovicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevovicActionPerformed
        AddVic ventana = new AddVic();
        ventana.setVisible(true);
    }//GEN-LAST:event_nuevovicActionPerformed

    private void modActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(1);
        ventana.setVisible(true);
    }//GEN-LAST:event_modActionPerformed

    private void AsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AsistenciaActionPerformed
        Asistencia1 ventana=new Asistencia1();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AsistenciaActionPerformed

    private void inicempActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicempActionPerformed
        Empleados ventana=new Empleados();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_inicempActionPerformed

    private void elimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimActionPerformed
        ModElimCargo1 ventana = new ModElimCargo1(0);
        ventana.setVisible(true);
    }//GEN-LAST:event_elimActionPerformed

    private void inicioasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioasActionPerformed
        Asistencia1 ventana=new Asistencia1();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_inicioasActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Estadisticas ventana=new Estadisticas();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void iniayuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniayuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_iniayuActionPerformed

    private void iniconfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniconfActionPerformed
        Configuracion ventana = new Configuracion();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_iniconfActionPerformed

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
            java.util.logging.Logger.getLogger(Menu1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Asistencia;
    private javax.swing.JMenu Ayuda;
    private javax.swing.JLabel Ayudin;
    private javax.swing.JMenuBar Barra;
    private javax.swing.JMenu Configuracion;
    private javax.swing.JPasswordField Contraseña;
    private javax.swing.JMenu Empleados;
    private javax.swing.JMenu Estadisticas;
    private javax.swing.JPanel Fondo;
    private javax.swing.JMenu Menu;
    private javax.swing.JMenu Movimientos;
    private javax.swing.JMenu Salir;
    private javax.swing.JTextField Usuario;
    private javax.swing.JMenu cargoemp;
    private javax.swing.JMenuItem elim;
    private javax.swing.JMenuItem historial;
    private javax.swing.JMenuItem iniayu;
    private javax.swing.JMenuItem inicemp;
    private javax.swing.JButton iniciarsesion1;
    private javax.swing.JMenuItem inicioas;
    private javax.swing.JMenuItem iniciomov;
    private javax.swing.JMenuItem iniconf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel menuinicio;
    private javax.swing.JMenuItem mod;
    private javax.swing.JMenuItem nuev;
    private javax.swing.JMenuItem nuevovic;
    private javax.swing.JMenuItem nuevtrip;
    private javax.swing.JLabel olvide;
    private javax.swing.JButton registrarse;
    // End of variables declaration//GEN-END:variables
}
