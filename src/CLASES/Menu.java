/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.UIManager;

/**
 *
 * @author Facuymayriver
 */
public class Menu {

    
    public static void Configuracion(){
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
    
    public static void MenuConfig(JMenu Movimientos, JMenu Menu, JMenu Asistencia, JMenu Empleados, JMenu Estadisticas, JMenu Ayuda, JMenu Configuracion, JMenu Salir){
        
        Icon iconNormal = new ImageIcon(Menu.class.getResource("/IMAGENES/movimiento.png"));
        Icon iconHover = new ImageIcon(Menu.class.getResource("/IMAGENES/movimiento2.png"));

        Icon iconNormal2 = new ImageIcon(Menu.class.getResource("/IMAGENES/menui.png"));
        Icon iconHover2 = new ImageIcon(Menu.class.getResource("/IMAGENES/menui2.png"));

        Icon iconNormal3 = new ImageIcon(Menu.class.getResource("/IMAGENES/asistencia.png"));
        Icon iconHover3 = new ImageIcon(Menu.class.getResource("/IMAGENES/asistencia2.png"));

        Icon iconNormal4 = new ImageIcon(Menu.class.getResource("/IMAGENES/empleado.png"));
        Icon iconHover4 = new ImageIcon(Menu.class.getResource("/IMAGENES/empleado2.png"));

        Icon iconNormal5 = new ImageIcon(Menu.class.getResource("/IMAGENES/estats.png"));
        Icon iconHover5 = new ImageIcon(Menu.class.getResource("/IMAGENES/estats2.png"));

        Icon iconNormal6 = new ImageIcon(Menu.class.getResource("/IMAGENES/ayuda.png"));
        Icon iconHover6 = new ImageIcon(Menu.class.getResource("/IMAGENES/ayuda2.png"));

        Icon iconNormal7 = new ImageIcon(Menu.class.getResource("/IMAGENES/config.png"));
        Icon iconHover7 = new ImageIcon(Menu.class.getResource("/IMAGENES/config2.png"));

        Icon iconNormal8 = new ImageIcon(Menu.class.getResource("/IMAGENES/salir.png"));
        Icon iconHover8 = new ImageIcon(Menu.class.getResource("/IMAGENES/salir2.png"));

        Movimientos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Movimientos.setIcon(iconHover);
                Movimientos.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Menu.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Asistencia.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Empleados.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Estadisticas.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Ayuda.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Configuracion.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
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
                Salir.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Salir.setIcon(iconNormal8);
                Salir.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 121)));
            }
        });
    }
}
