/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import VISTA.Asistencia;
import VISTA.Menu;
import VISTA.Movimiento;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Facuymayriver
 */
public class MenuClass {

    static int ventana = 0;

    private JFrame ventanaAnterior;

    public static void Configuracion() {
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
    }

    public static int Ventana() {
        return ventana;
    }

    public static void VentanaOpc(int band) {
        ventana = band;
    }

    private boolean isBlocked() {
        // Chequea si la ventanaAnterior es una IBlockableFrame
        if (ventanaAnterior instanceof IBlockableFrame) {
            // Si lo es, llama al método que debe existir en la interfaz.
            return ((IBlockableFrame) ventanaAnterior).getBlockState() == 1;
        }
        // Si la ventana no implementa la interfaz, asumimos que no hay bloqueo
        return false;
    }

    public void MenuConfig(JMenu Movimientos, JMenu Menu, JMenu Asistencia, JMenu Empleados, JMenu Estadisticas, JMenu Ayuda, JMenu Configuracion, JMenu Salir, JFrame ventanaAnterior) {

        this.ventanaAnterior = ventanaAnterior;
        Icon iconNormal = new ImageIcon(MenuClass.class.getResource("/IMAGENES/movimiento.png"));
        Icon iconHover = new ImageIcon(MenuClass.class.getResource("/IMAGENES/movimiento2.png"));

        Icon iconNormal2 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/menui.png"));
        Icon iconHover2 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/menui2.png"));

        Icon iconNormal3 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/asistencia.png"));
        Icon iconHover3 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/asistencia2.png"));

        Icon iconNormal4 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/empleado.png"));
        Icon iconHover4 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/empleado2.png"));

        Icon iconNormal5 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/estats.png"));
        Icon iconHover5 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/estats2.png"));

        Icon iconNormal6 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/ayuda.png"));
        Icon iconHover6 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/ayuda2.png"));

        Icon iconNormal7 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/config.png"));
        Icon iconHover7 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/config2.png"));

        Icon iconNormal8 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/salir.png"));
        Icon iconHover8 = new ImageIcon(MenuClass.class.getResource("/IMAGENES/salir2.png"));

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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) {

                        VISTA.Movimiento ventana = new VISTA.Movimiento();
                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            ventana.setVisible(true);
                            if (ventanaAnterior != null) {
                                ventanaAnterior.dispose();
                            }
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    if (!isBlocked()) { 

                        VISTA.Menu ventana = new VISTA.Menu();

                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            ventana.setVisible(true);
                            if (ventanaAnterior != null) {
                                ventanaAnterior.dispose();
                            }
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) { 

                        VISTA.Asistencia ventana = new VISTA.Asistencia();

                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            ventana.setVisible(true);
                            if (ventanaAnterior != null) {
                                ventanaAnterior.dispose();
                            }
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) { 

                        VISTA.Empleados ventana = new VISTA.Empleados();

                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            ventana.setVisible(true);
                            if (ventanaAnterior != null) {
                                ventanaAnterior.dispose();
                            }
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) { 

                        VISTA.Estadisticas ventana = new VISTA.Estadisticas();

                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            ventana.setVisible(true);
                            if (ventanaAnterior != null) {
                                ventanaAnterior.dispose();
                            }
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) { 

                        VISTA.Ayuda ventana = new VISTA.Ayuda();

                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Proximamente");
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) { 

                        VISTA.Configuracion ventana = new VISTA.Configuracion();

                        if (ventanaAnterior != null && ventanaAnterior.getClass() == ventana.getClass()) {
                            System.out.println("Ya estás en la ventana actual.");
                        } else {
                            ventana.setVisible(true);
                            if (ventanaAnterior != null) {
                                ventanaAnterior.dispose();
                            }
                        }
                    }
                }
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

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked()) { 
                        System.exit(0);
                    }
                }
            }
        });
    }
}
