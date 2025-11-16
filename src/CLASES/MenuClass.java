/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import CONEXIONES.Conexiones;
import VISTA.Menu;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    Connection con = Conexiones.Conexion();
    static int ventana = 0;
    static int ventanaTheme = 0;

    private JFrame ventanaAnterior;

    public static void Configuracion() {
        try {
            // 1. Usa el L&F del sistema para mejor integración, o sigue usando Metal (CrossPlatform)
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // Ya lo tenías

            // CONFIGURACIÓN DEL FONDO DEL MENÚ Y LA BARRA
            Color menuBg = new Color(52, 170, 121);
            Color menuFg = Color.WHITE;
            Color menuHighlight = new Color(75, 192, 145); // Un verde ligeramente más claro para el hover

            // --- BARRA DE MENÚ PRINCIPAL (Empleados, etc.) ---
            UIManager.put("MenuBar.background", menuBg);

            // --- CONTENEDOR DESPLEGABLE (El fondo que baja) ---
            // ESTE ES EL AJUSTE CRÍTICO: El fondo del menú desplegable.
            UIManager.put("PopupMenu.background", menuBg);

            // --- EL MENÚ EN SÍ (Ej: el elemento 'Empleados') ---
            UIManager.put("Menu.background", menuBg);
            UIManager.put("Menu.foreground", menuFg);
            UIManager.put("Menu.selectionBackground", menuHighlight); // Color cuando seleccionas el menú
            UIManager.put("Menu.selectionForeground", menuFg);

            // --- LOS ELEMENTOS DENTRO DEL MENÚ (Inicio, Cargo) ---
            UIManager.put("MenuItem.background", menuBg);
            UIManager.put("MenuItem.foreground", menuFg);

            // ESTE ES EL OTRO AJUSTE CRÍTICO: Color al pasar el ratón (hover/selección)
            UIManager.put("MenuItem.selectionBackground", menuHighlight); // Fondo de la celda resaltada
            UIManager.put("MenuItem.selectionForeground", Color.BLACK); // Texto del elemento resaltado (opcional)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void abrirPDF(String rutaRecurso) {
        try (InputStream inputStream = CLASES.Movimientos.class.getResourceAsStream(rutaRecurso)) {

            if (inputStream == null) {
                JOptionPane.showMessageDialog(null, "Error: No se pudo encontrar el recurso interno: " + rutaRecurso);
                return;
            }

            File tempFile = File.createTempFile("temp_pdf_", ".pdf");
            tempFile.deleteOnExit();

            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                JOptionPane.showMessageDialog(null, "Desktop no está soportado en este sistema.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al procesar/abrir el PDF: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }

    public static int Ventana() {
        return ventana;
    }

    public static void VentanaOpc(int band) {
        ventana = band;
    }

    public static void VentanaOpcTheme(int band) {
        ventanaTheme = band;
    }

    public static int VentanaOpcThemeRet() {
        return ventanaTheme;
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

    private boolean isBlocked2() {
        // Chequea si la ventanaAnterior es una IBlockableFrame
        if (ventanaAnterior instanceof IBlockableFrame) {
            // Si lo es, llama al método que debe existir en la interfaz.
            return ((IBlockableFrame) ventanaAnterior).getBlockStateSalir() == 1;
        }
        // Si la ventana no implementa la interfaz, asumimos que no hay bloqueo
        return false;
    }

    public void MenuConfig(JMenu Movimientos, JMenu Menu, JMenu Asistencia, JMenu Empleados, JMenu Estadisticas, JMenu Ayuda, JMenu Configuracion, JMenu Salir, JFrame ventanaAnterior, String ruta) {

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

            @Override
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

            @Override
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

            @Override
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

            @Override
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

            @Override
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

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked2()) {
                        String direccion1 = "/RECURSOS/" + ruta + ".pdf";
                        MenuClass.abrirPDF(direccion1);
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

            @Override
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

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!isBlocked2()) {
                        int valid = CLASES.Usuario.verificacion();
                        if (valid == 0) {
                            System.exit(0);
                        } else {
                            int opcion = JOptionPane.showConfirmDialog(
                                    null,
                                    "¿Deseás Cerrar sesión?",
                                    "Confirmar acción",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (opcion == JOptionPane.OK_OPTION) {
                                try {
                                    CLASES.Usuario.CerrarSesion2(con);
                                } catch (SQLException ex) {
                                    Logger.getLogger(MenuClass.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Menu ventana = new Menu();
                                ventana.setVisible(true);
                                if (ventanaAnterior != null) {
                                    ventanaAnterior.dispose();
                                }
                            } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                                System.out.println("Cancelado");
                            }
                        }
                    }
                }
            }
        });
    }
}
