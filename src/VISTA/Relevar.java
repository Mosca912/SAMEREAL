/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VISTA;

import CLASES.Movimientos.Cliente;
import CONEXIONES.Conexiones;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author Facuymayriver
 */
public class Relevar extends javax.swing.JDialog {

    private boolean marcado = true;
    Connection con = Conexiones.Conexion();
    ResultSet rs;
    int idChofer, idMedico, idEnfermero, idvictor;
    static int idtrip2, iduser;
    String fechaActual, fechaActual2, veri;

    private BufferedImage imagen;      // la imagen de fondo
    private BufferedImage imagenOriginal;

    private int centroX, centroY;   // centro del dial
    private double angulo = Math.toRadians(225); // ángulo inicial 
    private final int radio = 75;        // largo de la aguja
    private boolean arrastrando = false;
    //AMBULANCIA
    public class PanelDibujo extends JPanel {

        // Para guardar los círculos hechos
        private final java.util.List<Point> puntos = new java.util.ArrayList<>();

        public PanelDibujo(String ruta) throws IOException {
            imagen = ImageIO.read(getClass().getResource(ruta));
            imagenOriginal = ImageIO.read(getClass().getResource(ruta));
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image cursorImg = toolkit.getImage(getClass().getResource("/IMAGENES/lapiz.png"));

            // 📍 Crear cursor
            Point hotspot = new Point(0, 0); // Donde está la punta del lápiz
            Cursor cursorPersonalizado = toolkit.createCustomCursor(cursorImg, hotspot, "Lapiz");

            // 🔁 Aplicar cursor al panel
            this.setCursor(cursorPersonalizado);

            imagen = ImageIO.read(
                    getClass().getResource(ruta)
            );

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Graphics2D g2 = imagen.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int radioExterno = 80; // tamaño del círculo grande
                    int grosor = 5;       // grosor del borde

                    // Dibuja el borde rojo
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(grosor));
                    g2.drawOval(e.getX() - radioExterno / 2, e.getY() - radioExterno / 2, radioExterno, radioExterno);

                    g2.dispose();
                    repaint();
                }
            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // primero dibujás la imagen de fondo
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);

            // después dibujás los círculos encima
            g.setColor(Color.RED);
            puntos.forEach((p) -> {
                int radio = 10;  // radio del círculo, podés cambiar
                int cx = p.x - radio / 2;
                int cy = p.y - radio / 2;
                g.fillOval(cx, cy, radio, radio);
            });
        }
    }

    //AMBULANCIA
    public class PanelImagen extends JPanel {

        private final BufferedImage imagen2;

        public PanelImagen(String ruta) throws IOException {
            imagen2 = ImageIO.read(
                    getClass().getResource(ruta)
            );
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (imagen2 != null) {
                // Dibujar imagen ajustada al tamaño actual del panel
                g.drawImage(imagen2, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public class AgujaInteractiva extends JPanel {

        public AgujaInteractiva(String ruta) throws IOException {

            // MOUSE LISTENER para click y arrastre
            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    arrastrando = true;
                    actualizarAngulo(e.getX(), e.getY());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    arrastrando = false;
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (arrastrando) {
                        actualizarAngulo(e.getX(), e.getY());
                    }
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        private void actualizarAngulo(int x, int y) {
            double nuevoAngulo = Math.atan2(y - centroY, x - centroX);
            if (nuevoAngulo < 0) {
                nuevoAngulo += 2 * Math.PI; // normalizamos
            }
            // Limitar dentro del rango visual (225° a 315°)
            double angMin = Math.toRadians(185);
            double angMax = Math.toRadians(350);

            if (nuevoAngulo < angMin) {
                nuevoAngulo = angMin;
            }
            if (nuevoAngulo > angMax) {
                nuevoAngulo = angMax;
            }

            angulo = nuevoAngulo;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Suavizado
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Centro del panel
            centroX = nivelcomb.getWidth() / 2;
            centroY = nivelcomb.getHeight() - 40;

            // 🖼️ Fondo del medidor
            Image fondo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/IMAGENES/nivel_1.png"));
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

            // 🔴 Dibujar aguja
            g2.setStroke(new BasicStroke(4f));
            g2.setColor(Color.RED);

            int x2 = (int) (centroX + radio * Math.cos(angulo));
            int y2 = (int) (centroY + radio * Math.sin(angulo));
            g2.drawLine(centroX, centroY, x2, y2);

            // Centro del eje
            g2.fillOval(centroX - 5, centroY - 5, 10, 10);
        }
    }

    class Fondo extends JPanel {

        private Image imagen5;

        public void paint(Graphics g) {
            imagen5 = new ImageIcon(getClass().getResource("/IMAGENES/nivel.png")).getImage();
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

    public Relevar(int idtrip, JFrame ventanaPrincipal) {
        super(ventanaPrincipal,true);
        this.idtrip2 = idtrip;
        initComponents();
        this.setLocationRelativeTo(null);
        setSize(1570, 860);
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        fechaActual2 = hoy.format(formato);
        fechaActual = hoy.format(formato2);
        CLASES.Relevar.Relevo(con, Victor, anterior1, saliente1, kmi1, kmf, turno, serie, idtrip);
        Fecha1.setText(fechaActual2);
        
        iduser = CLASES.Usuario.iduser();

        // Le quitamos los ticks y labels si querés
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setOpaque(false);
                
        // Personalizamos la UI
        slider.setUI(new BasicSliderUI(slider) {
            @Override
            public void paintThumb(Graphics g) {
                // Convierte a Graphics2D para usar características de dibujo avanzadas como el grosor de línea
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(Color.RED);

                // Define el grosor de la línea (por ejemplo, 5 píxeles)
                int grosor = 7;
                g2d.setStroke(new BasicStroke(grosor));

                // Calcula el punto central 'x' del área del thumbRect
                int x = thumbRect.x + thumbRect.width / 2;

                // Dibuja la línea con el nuevo grosor
                // Si quieres que la línea sea vertical y visible, su altura debe ser adecuada
                g2d.drawLine(x, thumbRect.y, x, thumbRect.y + thumbRect.height);
            }

            @Override
            public void paintTrack(Graphics g) {
                // No pintamos nada para que no se vea la barra
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

        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jToggleButton4 = new javax.swing.JToggleButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jToggleButton6 = new javax.swing.JToggleButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jToggleButton7 = new javax.swing.JToggleButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jToggleButton8 = new javax.swing.JToggleButton();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jToggleButton9 = new javax.swing.JToggleButton();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jToggleButton10 = new javax.swing.JToggleButton();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jToggleButton11 = new javax.swing.JToggleButton();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jToggleButton12 = new javax.swing.JToggleButton();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jToggleButton13 = new javax.swing.JToggleButton();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jToggleButton14 = new javax.swing.JToggleButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jToggleButton15 = new javax.swing.JToggleButton();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jToggleButton16 = new javax.swing.JToggleButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jToggleButton17 = new javax.swing.JToggleButton();
        jLabel66 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Fecha = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        victor = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        kmi = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        anterior = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        saliente = new javax.swing.JLabel();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        jLabel176 = new javax.swing.JLabel();
        entrante = new javax.swing.JLabel();
        Fondo = new javax.swing.JPanel();
        Grande = new javax.swing.JPanel();
        jLabel177 = new javax.swing.JLabel();
        Fecha1 = new javax.swing.JLabel();
        jLabel178 = new javax.swing.JLabel();
        jLabel179 = new javax.swing.JLabel();
        kmi1 = new javax.swing.JLabel();
        jLabel180 = new javax.swing.JLabel();
        kmf = new javax.swing.JLabel();
        jLabel182 = new javax.swing.JLabel();
        anterior1 = new javax.swing.JLabel();
        jLabel183 = new javax.swing.JLabel();
        saliente1 = new javax.swing.JLabel();
        jLabel185 = new javax.swing.JLabel();
        turno = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        Victor = new javax.swing.JLabel();
        Marcarext = new javax.swing.JPanel();
        try {
            Marca = new PanelImagen("/IMAGENES/Marca.png");
            cinturones = new javax.swing.JToggleButton();
            ruedaaux = new javax.swing.JToggleButton();
            llave = new javax.swing.JToggleButton();
            gato = new javax.swing.JToggleButton();
            balizas = new javax.swing.JToggleButton();
            antena = new javax.swing.JToggleButton();
            sirena = new javax.swing.JToggleButton();
            equipcom = new javax.swing.JToggleButton();
            gancho = new javax.swing.JToggleButton();
            jLabel8 = new javax.swing.JLabel();
            MarcarNiv = new javax.swing.JPanel();
            try {
                extint = new PanelImagen("/IMAGENES/marcanivel.png");
                jTextField2 = new javax.swing.JTextField();
                jLabel16 = new javax.swing.JLabel();
                jLabel17 = new javax.swing.JLabel();
                serie = new javax.swing.JLabel();
                matafue = new javax.swing.JToggleButton();
                liquidfren = new javax.swing.JToggleButton();
                refr = new javax.swing.JToggleButton();
                try {
                    nivelcomb = new AgujaInteractiva("/IMAGENES/nivel_1.png");
                    jLabel10 = new javax.swing.JLabel();
                    nivelac = new Fondo();
                    slider = new javax.swing.JSlider();
                    MarcarAb = new javax.swing.JPanel();
                    jLabel9 = new javax.swing.JLabel();
                    try {
                        Ambulancia = new PanelDibujo("/IMAGENES/ambulancia.jpg");
                        Borrar = new javax.swing.JButton();
                        Observaciones = new javax.swing.JPanel();
                        jLabel1 = new javax.swing.JLabel();
                        jScrollPane1 = new javax.swing.JScrollPane();
                        observations = new javax.swing.JTextArea();
                        jPanel8 = new javax.swing.JPanel();
                        jPanel11 = new javax.swing.JPanel();
                        lavadero = new javax.swing.JPanel();
                        jLabel11 = new javax.swing.JLabel();
                        jLabel12 = new javax.swing.JLabel();
                        lavun = new javax.swing.JToggleButton();
                        ypf = new javax.swing.JPanel();
                        jLabel13 = new javax.swing.JLabel();
                        jLabel14 = new javax.swing.JLabel();
                        jTextField1 = new javax.swing.JTextField();
                        jLabel15 = new javax.swing.JLabel();
                        ypfguard = new javax.swing.JToggleButton();
                        try {
                            Funciona = new PanelImagen("/IMAGENES/Funciona.png");
                            jLabel67 = new javax.swing.JLabel();
                            CalAir = new javax.swing.JToggleButton();
                            LucesDireccionales = new javax.swing.JToggleButton();
                            LucesAltas = new javax.swing.JToggleButton();
                            LucesDestell = new javax.swing.JToggleButton();
                            LucesBajas = new javax.swing.JToggleButton();
                            LucesFreno = new javax.swing.JToggleButton();
                            LucesTras = new javax.swing.JToggleButton();
                            LimpiaPara = new javax.swing.JToggleButton();
                            Frenos = new javax.swing.JToggleButton();
                            IndicadorTemp = new javax.swing.JToggleButton();
                            TestServ = new javax.swing.JToggleButton();
                            IndNivCom = new javax.swing.JToggleButton();
                            IndNivelAc = new javax.swing.JToggleButton();
                            CarBat = new javax.swing.JToggleButton();
                            LuzPosicion = new javax.swing.JToggleButton();
                            jPanel1 = new javax.swing.JPanel();
                            jButton3 = new javax.swing.JButton();
                            jButton4 = new javax.swing.JButton();
                            activado = new javax.swing.JButton();
                            Deshabilitar = new javax.swing.JButton();

                            jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                            jLabel21.setText("Funciona");

                            jLabel22.setText("Luz Posición");

                            jLabel23.setText("Luces Direccionales");

                            jLabel24.setText("Luces Altas");

                            jLabel25.setText("Luces Destelladoras de");

                            jLabel26.setText("SI");

                            jToggleButton3.setText("NO");

                            jLabel27.setText("No");

                            jLabel28.setText("SI");

                            jToggleButton4.setText("NO");

                            jLabel29.setText("No");

                            jLabel30.setText("SI");

                            jToggleButton5.setText("NO");

                            jLabel31.setText("No");

                            jLabel32.setText("SI");

                            jToggleButton6.setText("NO");

                            jLabel33.setText("No");

                            jLabel34.setText("Luces Bajas");

                            jLabel35.setText("Luces de Freno");

                            jLabel36.setText("Luces Traseras");

                            jLabel37.setText("Limpia Parabrisas");

                            jLabel38.setText("Frenos");

                            jLabel39.setText("No");

                            jToggleButton7.setText("NO");

                            jLabel40.setText("SI");

                            jLabel41.setText("No");

                            jToggleButton8.setText("NO");

                            jLabel42.setText("SI");

                            jLabel43.setText("No");

                            jToggleButton9.setText("NO");

                            jLabel44.setText("SI");

                            jLabel45.setText("No");

                            jToggleButton10.setText("NO");

                            jLabel46.setText("SI");

                            jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                            jLabel47.setText("Indicador de Temperatura Motor");

                            jLabel48.setText("Testigo de Service");

                            jLabel49.setText("Indicador de temperatura de combustiblw");

                            jLabel50.setText("Indicador de Nivel de Combustible");

                            jLabel51.setText("Carga Bateria");

                            jLabel52.setText("Calefactor y Aire Acondicionado");

                            jLabel53.setText("No");

                            jToggleButton11.setText("NO");

                            jLabel54.setText("SI");

                            jLabel55.setText("No");

                            jToggleButton12.setText("NO");

                            jLabel56.setText("SI");

                            jLabel57.setText("No");

                            jToggleButton13.setText("NO");

                            jLabel58.setText("SI");

                            jLabel59.setText("No");

                            jToggleButton14.setText("NO");

                            jLabel60.setText("SI");

                            jLabel61.setText("No");

                            jToggleButton15.setText("NO");

                            jLabel62.setText("SI");

                            jLabel63.setText("No");

                            jToggleButton16.setText("NO");

                            jLabel64.setText("SI");

                            jLabel65.setText("No");

                            jToggleButton17.setText("NO");

                            jLabel66.setText("SI");

                            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                            jPanel5.setLayout(jPanel5Layout);
                            jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGap(26, 26, 26)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel23)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel29)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel28))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel22)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel27)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel26))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel24)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel31)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel30))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel34)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel39)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel40))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel35)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel41)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel42))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel36)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel43)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel44))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel37)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel45)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel46))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addComponent(jLabel25)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                                            .addComponent(jLabel33)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel32))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel51)
                                                .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel48)
                                                    .addComponent(jLabel38)
                                                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel53)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel54))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel55)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel56))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel57)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel58))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel59)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel60))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel61)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel62))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel63)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel64))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                                    .addComponent(jLabel65)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jToggleButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(3, 3, 3)
                                                    .addComponent(jLabel66)))))
                                    .addContainerGap())
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGap(125, 125, 125)
                                    .addComponent(jLabel21)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );
                            jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel21)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22)
                                        .addComponent(jToggleButton3)
                                        .addComponent(jLabel27)
                                        .addComponent(jLabel26))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel23)
                                        .addComponent(jToggleButton4)
                                        .addComponent(jLabel29)
                                        .addComponent(jLabel28))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel24)
                                        .addComponent(jToggleButton5)
                                        .addComponent(jLabel31)
                                        .addComponent(jLabel30))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel25)
                                        .addComponent(jToggleButton6)
                                        .addComponent(jLabel33)
                                        .addComponent(jLabel32))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel34)
                                        .addComponent(jToggleButton7)
                                        .addComponent(jLabel39)
                                        .addComponent(jLabel40))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel35)
                                        .addComponent(jToggleButton8)
                                        .addComponent(jLabel41)
                                        .addComponent(jLabel42))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel36)
                                        .addComponent(jToggleButton9)
                                        .addComponent(jLabel43)
                                        .addComponent(jLabel44))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel37)
                                        .addComponent(jToggleButton10)
                                        .addComponent(jLabel45)
                                        .addComponent(jLabel46))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton11)
                                            .addComponent(jLabel53)
                                            .addComponent(jLabel54))
                                        .addComponent(jLabel38))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton12)
                                            .addComponent(jLabel55)
                                            .addComponent(jLabel56))
                                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton13)
                                            .addComponent(jLabel57)
                                            .addComponent(jLabel58))
                                        .addComponent(jLabel48))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton14)
                                            .addComponent(jLabel59)
                                            .addComponent(jLabel60))
                                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton15)
                                            .addComponent(jLabel61)
                                            .addComponent(jLabel62))
                                        .addComponent(jLabel50))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton16)
                                            .addComponent(jLabel63)
                                            .addComponent(jLabel64))
                                        .addComponent(jLabel51))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jToggleButton17)
                                            .addComponent(jLabel65)
                                            .addComponent(jLabel66))
                                        .addComponent(jLabel52))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );

                            jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                            jLabel2.setText("Fecha:");
                            jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

                            Fecha.setText("jLabel3");
                            jPanel2.add(Fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 11, -1, -1));

                            jLabel3.setText("Victor");
                            jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 36, -1, -1));

                            jPanel2.add(victor, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 36, 133, -1));

                            jLabel4.setText("Kilometraje Inicial:");
                            jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 39, -1, -1));

                            kmi.setText("0");
                            jPanel2.add(kmi, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 39, 59, -1));

                            jLabel5.setText("Kilometraje final:");
                            jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 39, -1, -1));

                            jLabel6.setText("0");
                            jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(454, 39, 64, -1));

                            jLabel7.setText("CVS Anterior:");
                            jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 74, -1, -1));

                            anterior.setText("-");
                            jPanel2.add(anterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 74, 204, -1));

                            jLabel173.setText("CVS Saliente:");
                            jPanel2.add(jLabel173, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 74, -1, -1));

                            saliente.setText("-");
                            jPanel2.add(saliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(377, 74, 196, -1));

                            jLabel174.setText("CVS Entrante:");
                            jPanel2.add(jLabel174, new org.netbeans.lib.awtextra.AbsoluteConstraints(579, 74, -1, -1));

                            jLabel175.setText("Turno:");
                            jPanel2.add(jLabel175, new org.netbeans.lib.awtextra.AbsoluteConstraints(909, 11, -1, -1));

                            jLabel176.setText("00:00hrs");
                            jPanel2.add(jLabel176, new org.netbeans.lib.awtextra.AbsoluteConstraints(947, 11, -1, -1));

                            entrante.setText("-");
                            jPanel2.add(entrante, new org.netbeans.lib.awtextra.AbsoluteConstraints(653, 74, 262, -1));

                            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                            setTitle("Relevar");
                            setMaximumSize(new java.awt.Dimension(1550, 840));
                            setMinimumSize(new java.awt.Dimension(1550, 840));
                            setUndecorated(true);
                            setPreferredSize(new java.awt.Dimension(1550, 840));
                            setResizable(false);
                            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                            Fondo.setBackground(new java.awt.Color(204, 255, 204));
                            Fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                            Fondo.setMinimumSize(new java.awt.Dimension(1550, 860));
                            Fondo.setPreferredSize(new java.awt.Dimension(1550, 860));
                            Fondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                            Grande.setBackground(new java.awt.Color(0, 0, 0));
                            Grande.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                            jLabel177.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel177.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel177.setText(">Fecha:");
                            Grande.add(jLabel177, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

                            Fecha1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            Fecha1.setForeground(new java.awt.Color(255, 255, 255));
                            Fecha1.setText("jLabel3");
                            Grande.add(Fecha1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

                            jLabel178.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel178.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel178.setText(">Victor:");
                            Grande.add(jLabel178, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

                            jLabel179.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel179.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel179.setText(">Kilometraje Inicial:");
                            Grande.add(jLabel179, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, -1, -1));

                            kmi1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            kmi1.setForeground(new java.awt.Color(255, 255, 255));
                            kmi1.setText("0");
                            Grande.add(kmi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 110, -1));

                            jLabel180.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel180.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel180.setText(">Kilometraje final:");
                            Grande.add(jLabel180, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, 20));

                            kmf.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            kmf.setForeground(new java.awt.Color(255, 255, 255));
                            kmf.setText("0");
                            Grande.add(kmf, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, 120, -1));

                            jLabel182.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel182.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel182.setText(">CVS Anterior:");
                            Grande.add(jLabel182, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

                            anterior1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            anterior1.setForeground(new java.awt.Color(255, 255, 255));
                            anterior1.setText("-");
                            Grande.add(anterior1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 390, -1));

                            jLabel183.setBackground(new java.awt.Color(255, 255, 255));
                            jLabel183.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel183.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel183.setText(">CVS Saliente:");
                            Grande.add(jLabel183, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 70, -1, -1));

                            saliente1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            saliente1.setForeground(new java.awt.Color(255, 255, 255));
                            saliente1.setText("-");
                            Grande.add(saliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, 390, -1));

                            jLabel185.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel185.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel185.setText(">Turno:");
                            Grande.add(jLabel185, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

                            turno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            turno.setForeground(new java.awt.Color(255, 255, 255));
                            turno.setText("00:00hrs");
                            Grande.add(turno, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));

                            jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            jLabel19.setForeground(new java.awt.Color(255, 51, 51));
                            jLabel19.setText("FICHA DE RELEVO DE MOVILES");
                            Grande.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

                            Victor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
                            Victor.setForeground(new java.awt.Color(255, 255, 255));
                            Victor.setText("-");
                            Grande.add(Victor, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 40, -1));

                            Fondo.add(Grande, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 11, 1030, 108));

                            Marcarext.setBackground(new java.awt.Color(0, 0, 0));
                            Marcarext.setMaximumSize(new java.awt.Dimension(600, 281));
                            Marcarext.setMinimumSize(new java.awt.Dimension(600, 281));
                            Marcarext.setPreferredSize(new java.awt.Dimension(600, 281));

                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        Marca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                        Marca.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                        cinturones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        cinturones.setToolTipText("");
                        cinturones.setBorderPainted(false);
                        cinturones.setContentAreaFilled(false);
                        cinturones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        cinturones.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        cinturones.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        cinturones.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        cinturones.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        cinturones.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(cinturones, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 70, 30));

                        ruedaaux.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        ruedaaux.setToolTipText("");
                        ruedaaux.setBorderPainted(false);
                        ruedaaux.setContentAreaFilled(false);
                        ruedaaux.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        ruedaaux.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        ruedaaux.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        ruedaaux.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        ruedaaux.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        ruedaaux.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(ruedaaux, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 70, 30));

                        llave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        llave.setToolTipText("");
                        llave.setBorderPainted(false);
                        llave.setContentAreaFilled(false);
                        llave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        llave.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        llave.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        llave.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        llave.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        llave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(llave, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 80, 30));

                        gato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        gato.setToolTipText("");
                        gato.setBorderPainted(false);
                        gato.setContentAreaFilled(false);
                        gato.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        gato.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        gato.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        gato.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        gato.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        gato.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(gato, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 190, 70, 30));

                        balizas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        balizas.setToolTipText("");
                        balizas.setBorderPainted(false);
                        balizas.setContentAreaFilled(false);
                        balizas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        balizas.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        balizas.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        balizas.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        balizas.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        balizas.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(balizas, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 70, 30));

                        antena.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        antena.setToolTipText("");
                        antena.setBorderPainted(false);
                        antena.setContentAreaFilled(false);
                        antena.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        antena.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        antena.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        antena.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        antena.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        antena.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(antena, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 70, 30));

                        sirena.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        sirena.setToolTipText("");
                        sirena.setBorderPainted(false);
                        sirena.setContentAreaFilled(false);
                        sirena.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        sirena.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        sirena.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        sirena.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        sirena.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        sirena.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(sirena, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 70, 30));

                        equipcom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        equipcom.setToolTipText("");
                        equipcom.setBorderPainted(false);
                        equipcom.setContentAreaFilled(false);
                        equipcom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        equipcom.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        equipcom.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        equipcom.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        equipcom.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        equipcom.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(equipcom, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 70, 30));

                        gancho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        gancho.setToolTipText("");
                        gancho.setBorderPainted(false);
                        gancho.setContentAreaFilled(false);
                        gancho.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                        gancho.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        gancho.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        gancho.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                        gancho.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        gancho.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                        Marca.add(gancho, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 80, 30));

                        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
                        jLabel8.setText(">MARCAR (Extravio, deterioro, pérdida o mal funcionamiento)");

                        javax.swing.GroupLayout MarcarextLayout = new javax.swing.GroupLayout(Marcarext);
                        Marcarext.setLayout(MarcarextLayout);
                        MarcarextLayout.setHorizontalGroup(
                            MarcarextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MarcarextLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(MarcarextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Marca, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addContainerGap(16, Short.MAX_VALUE))
                        );
                        MarcarextLayout.setVerticalGroup(
                            MarcarextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MarcarextLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Marca, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        );

                        Fondo.add(Marcarext, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 560, -1, 280));

                        MarcarNiv.setBackground(new java.awt.Color(0, 0, 0));
                        MarcarNiv.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    extint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                    jLabel16.setText("Nº Serie:");

                    jLabel17.setText("Cantidad");

                    serie.setText("jLabel18");

                    matafue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                    matafue.setToolTipText("");
                    matafue.setBorderPainted(false);
                    matafue.setContentAreaFilled(false);
                    matafue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                    matafue.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    matafue.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    matafue.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                    matafue.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    matafue.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N

                    liquidfren.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                    liquidfren.setToolTipText("");
                    liquidfren.setBorderPainted(false);
                    liquidfren.setContentAreaFilled(false);
                    liquidfren.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                    liquidfren.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    liquidfren.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    liquidfren.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                    liquidfren.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    liquidfren.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N

                    refr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                    refr.setToolTipText("");
                    refr.setBorderPainted(false);
                    refr.setContentAreaFilled(false);
                    refr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                    refr.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    refr.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    refr.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
                    refr.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
                    refr.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N

                    javax.swing.GroupLayout extintLayout = new javax.swing.GroupLayout(extint);
                    extint.setLayout(extintLayout);
                    extintLayout.setHorizontalGroup(
                        extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(extintLayout.createSequentialGroup()
                            .addGap(223, 223, 223)
                            .addGroup(extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(extintLayout.createSequentialGroup()
                                    .addComponent(refr, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(matafue, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(extintLayout.createSequentialGroup()
                                    .addComponent(liquidfren, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(103, 103, 103)
                                    .addGroup(extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(extintLayout.createSequentialGroup()
                                            .addComponent(jLabel17)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(extintLayout.createSequentialGroup()
                                            .addComponent(jLabel16)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(serie)))
                                    .addGap(0, 3, Short.MAX_VALUE)))
                            .addContainerGap())
                    );
                    extintLayout.setVerticalGroup(
                        extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(extintLayout.createSequentialGroup()
                            .addGroup(extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(extintLayout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(14, 14, 14)
                                    .addGroup(extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(serie)))
                                .addGroup(extintLayout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(liquidfren, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                            .addGroup(extintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(matafue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(refr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addContainerGap())
                    );

                    MarcarNiv.add(extint, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 165, 580, -1));

                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
                }
                nivelcomb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                nivelcomb.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                nivelcomb.setMaximumSize(new java.awt.Dimension(270, 120));
                nivelcomb.setMinimumSize(new java.awt.Dimension(270, 120));

                javax.swing.GroupLayout nivelcombLayout = new javax.swing.GroupLayout(nivelcomb);
                nivelcomb.setLayout(nivelcombLayout);
                nivelcombLayout.setHorizontalGroup(
                    nivelcombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 268, Short.MAX_VALUE)
                );
                nivelcombLayout.setVerticalGroup(
                    nivelcombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 118, Short.MAX_VALUE)
                );

                MarcarNiv.add(nivelcomb, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 39, 270, -1));

                jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(255, 255, 255));
                jLabel10.setText(">MARCAR (Nivel)");
                MarcarNiv.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

                nivelac.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                slider.setBackground(new java.awt.Color(255, 204, 204));
                slider.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                nivelac.add(slider, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 16, 230, 50));

                MarcarNiv.add(nivelac, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 300, 120));

                Fondo.add(MarcarNiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 260, 600, 290));

                MarcarAb.setBackground(new java.awt.Color(0, 0, 0));
                MarcarAb.setMaximumSize(new java.awt.Dimension(600, 420));
                MarcarAb.setMinimumSize(new java.awt.Dimension(600, 420));
                MarcarAb.setPreferredSize(new java.awt.Dimension(600, 420));
                MarcarAb.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

                jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
                jLabel9.setForeground(new java.awt.Color(255, 255, 255));
                jLabel9.setText(">MARCAR (Abolladuras, toques, rallones, etc.)");
                MarcarAb.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
            }
            Ambulancia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

            javax.swing.GroupLayout AmbulanciaLayout = new javax.swing.GroupLayout(Ambulancia);
            Ambulancia.setLayout(AmbulanciaLayout);
            AmbulanciaLayout.setHorizontalGroup(
                AmbulanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            AmbulanciaLayout.setVerticalGroup(
                AmbulanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            MarcarAb.add(Ambulancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 44, 580, 360));

            Borrar.setText("Borrar");
            Borrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    BorrarActionPerformed(evt);
                }
            });
            MarcarAb.add(Borrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 90, -1));

            Fondo.add(MarcarAb, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 600, 420));

            Observaciones.setBackground(new java.awt.Color(0, 0, 0));

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
            jLabel1.setForeground(new java.awt.Color(255, 255, 255));
            jLabel1.setText(">Observaciones");

            observations.setColumns(20);
            observations.setRows(5);
            jScrollPane1.setViewportView(observations);

            javax.swing.GroupLayout ObservacionesLayout = new javax.swing.GroupLayout(Observaciones);
            Observaciones.setLayout(ObservacionesLayout);
            ObservacionesLayout.setHorizontalGroup(
                ObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ObservacionesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(ObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ObservacionesLayout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                    .addContainerGap())
            );
            ObservacionesLayout.setVerticalGroup(
                ObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ObservacionesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addContainerGap())
            );

            Fondo.add(Observaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 560, 600, 280));

            jPanel8.setBackground(new java.awt.Color(0, 0, 0));
            jPanel8.setMaximumSize(new java.awt.Dimension(600, 122));
            jPanel8.setMinimumSize(new java.awt.Dimension(600, 122));
            jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            jPanel11.setBackground(new java.awt.Color(255, 255, 255));
            jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            jPanel8.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 100));

            lavadero.setBackground(new java.awt.Color(255, 255, 255));
            lavadero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

            jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
            jLabel11.setText("LAVADERO");

            jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
            jLabel12.setText("Lavado Unidad");

            lavun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
            lavun.setToolTipText("");
            lavun.setBorderPainted(false);
            lavun.setContentAreaFilled(false);
            lavun.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            lavun.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
            lavun.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
            lavun.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
            lavun.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
            lavun.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N

            javax.swing.GroupLayout lavaderoLayout = new javax.swing.GroupLayout(lavadero);
            lavadero.setLayout(lavaderoLayout);
            lavaderoLayout.setHorizontalGroup(
                lavaderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lavaderoLayout.createSequentialGroup()
                    .addContainerGap(106, Short.MAX_VALUE)
                    .addComponent(jLabel11)
                    .addGap(105, 105, 105))
                .addGroup(lavaderoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lavun, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            lavaderoLayout.setVerticalGroup(
                lavaderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(lavaderoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel11)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(lavaderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lavun, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel8.add(lavadero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 280, 70));

            ypf.setBackground(new java.awt.Color(255, 255, 255));
            ypf.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

            jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
            jLabel13.setText("YPF RUTA");

            jLabel14.setText("Nº Tarjeta");

            jLabel15.setText("Entrega a Guarda Siguiente:");

            ypfguard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
            ypfguard.setToolTipText("");
            ypfguard.setBorderPainted(false);
            ypfguard.setContentAreaFilled(false);
            ypfguard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            ypfguard.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
            ypfguard.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
            ypfguard.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
            ypfguard.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
            ypfguard.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N

            javax.swing.GroupLayout ypfLayout = new javax.swing.GroupLayout(ypf);
            ypf.setLayout(ypfLayout);
            ypfLayout.setHorizontalGroup(
                ypfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ypfLayout.createSequentialGroup()
                    .addContainerGap(105, Short.MAX_VALUE)
                    .addComponent(jLabel13)
                    .addGap(104, 104, 104))
                .addGroup(ypfLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(ypfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ypfLayout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1))
                        .addGroup(ypfLayout.createSequentialGroup()
                            .addComponent(jLabel15)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ypfguard, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            ypfLayout.setVerticalGroup(
                ypfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ypfLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(ypfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(ypfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(ypfguard, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel8.add(ypf, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 280, 100));

            Fondo.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 130, 600, 120));

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        }
        Funciona.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Funciona.setMaximumSize(new java.awt.Dimension(304, 720));
        Funciona.setMinimumSize(new java.awt.Dimension(304, 720));
        Funciona.setPreferredSize(new java.awt.Dimension(304, 720));
        Funciona.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel67.setText("Funciona");
        Funciona.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 1, -1, 20));

        CalAir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        CalAir.setToolTipText("");
        CalAir.setBorderPainted(false);
        CalAir.setContentAreaFilled(false);
        CalAir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CalAir.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        CalAir.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        CalAir.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        CalAir.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        CalAir.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(CalAir, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 670, 70, 30));

        LucesDireccionales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesDireccionales.setToolTipText("");
        LucesDireccionales.setBorderPainted(false);
        LucesDireccionales.setContentAreaFilled(false);
        LucesDireccionales.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LucesDireccionales.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesDireccionales.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesDireccionales.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesDireccionales.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesDireccionales.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LucesDireccionales, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 70, -1));

        LucesAltas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesAltas.setToolTipText("");
        LucesAltas.setBorderPainted(false);
        LucesAltas.setContentAreaFilled(false);
        LucesAltas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LucesAltas.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesAltas.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesAltas.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesAltas.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesAltas.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LucesAltas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 70, 30));

        LucesDestell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesDestell.setToolTipText("");
        LucesDestell.setBorderPainted(false);
        LucesDestell.setContentAreaFilled(false);
        LucesDestell.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LucesDestell.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesDestell.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesDestell.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesDestell.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesDestell.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LucesDestell, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 70, 40));

        LucesBajas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesBajas.setToolTipText("");
        LucesBajas.setBorderPainted(false);
        LucesBajas.setContentAreaFilled(false);
        LucesBajas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LucesBajas.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesBajas.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesBajas.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesBajas.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesBajas.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LucesBajas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 70, 30));

        LucesFreno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesFreno.setToolTipText("");
        LucesFreno.setBorderPainted(false);
        LucesFreno.setContentAreaFilled(false);
        LucesFreno.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LucesFreno.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesFreno.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesFreno.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesFreno.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesFreno.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LucesFreno, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, 70, 40));

        LucesTras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesTras.setToolTipText("");
        LucesTras.setBorderPainted(false);
        LucesTras.setContentAreaFilled(false);
        LucesTras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LucesTras.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesTras.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesTras.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LucesTras.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LucesTras.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LucesTras, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 70, 30));

        LimpiaPara.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LimpiaPara.setToolTipText("");
        LimpiaPara.setBorderPainted(false);
        LimpiaPara.setContentAreaFilled(false);
        LimpiaPara.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LimpiaPara.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LimpiaPara.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LimpiaPara.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LimpiaPara.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LimpiaPara.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LimpiaPara, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 70, 40));

        Frenos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        Frenos.setToolTipText("");
        Frenos.setBorderPainted(false);
        Frenos.setContentAreaFilled(false);
        Frenos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Frenos.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Frenos.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Frenos.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        Frenos.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Frenos.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(Frenos, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 70, 40));

        IndicadorTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        IndicadorTemp.setToolTipText("");
        IndicadorTemp.setBorderPainted(false);
        IndicadorTemp.setContentAreaFilled(false);
        IndicadorTemp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IndicadorTemp.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndicadorTemp.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndicadorTemp.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        IndicadorTemp.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndicadorTemp.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(IndicadorTemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 440, 70, 30));

        TestServ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        TestServ.setToolTipText("");
        TestServ.setBorderPainted(false);
        TestServ.setContentAreaFilled(false);
        TestServ.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TestServ.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        TestServ.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        TestServ.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        TestServ.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        TestServ.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(TestServ, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 480, 70, 40));

        IndNivCom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        IndNivCom.setToolTipText("");
        IndNivCom.setBorderPainted(false);
        IndNivCom.setContentAreaFilled(false);
        IndNivCom.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IndNivCom.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndNivCom.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndNivCom.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        IndNivCom.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndNivCom.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(IndNivCom, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 530, 70, 30));

        IndNivelAc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        IndNivelAc.setToolTipText("");
        IndNivelAc.setBorderPainted(false);
        IndNivelAc.setContentAreaFilled(false);
        IndNivelAc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        IndNivelAc.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndNivelAc.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndNivelAc.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        IndNivelAc.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        IndNivelAc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(IndNivelAc, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 580, 70, 30));

        CarBat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        CarBat.setToolTipText("");
        CarBat.setBorderPainted(false);
        CarBat.setContentAreaFilled(false);
        CarBat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CarBat.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        CarBat.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        CarBat.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        CarBat.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        CarBat.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(CarBat, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 620, 70, 40));

        LuzPosicion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LuzPosicion.setToolTipText("");
        LuzPosicion.setBorderPainted(false);
        LuzPosicion.setContentAreaFilled(false);
        LuzPosicion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LuzPosicion.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LuzPosicion.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LuzPosicion.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/NO.png"))); // NOI18N
        LuzPosicion.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        LuzPosicion.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/SI.png"))); // NOI18N
        Funciona.add(LuzPosicion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 70, 30));

        Fondo.add(Funciona, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 710));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jButton3.setText("Guardar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Volver");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        activado.setText("Todos si");
        activado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activadoActionPerformed(evt);
            }
        });

        Deshabilitar.setText("Todos no");
        Deshabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeshabilitarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(activado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Deshabilitar, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 258, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(activado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Deshabilitar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        Fondo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 10, 480, 110));

        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1570, 880));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void BorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarActionPerformed
        // Crea una copia limpia de la imagen original
        imagen = new BufferedImage(
                imagenOriginal.getWidth(),
                imagenOriginal.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2 = imagen.createGraphics();
        g2.drawImage(imagenOriginal, 0, 0, null);
        g2.dispose();

        repaint(); // refresca el panel
    }//GEN-LAST:event_BorrarActionPerformed

    private void DeshabilitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeshabilitarActionPerformed

        marcado = !marcado;

        for (Component comp : Funciona.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(false);
            }
        }

        for (Component comp : Marca.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(false);
            }
        }

        for (Component comp : lavadero.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(false);
            }
        }

        for (Component comp : ypf.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(false);
            }
        }

        for (Component comp : extint.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(false);
            }
        }
    }//GEN-LAST:event_DeshabilitarActionPerformed

    private void activadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activadoActionPerformed
        for (Component comp : Funciona.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(true);
            }
        }

        for (Component comp : Marca.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(true);
            }
        }

        for (Component comp : lavadero.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(true);
            }
        }

        for (Component comp : ypf.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(true);
            }
        }

        for (Component comp : extint.getComponents()) {
            if (comp instanceof JToggleButton) {
                ((JToggleButton) comp).setSelected(true);
            }
        }
    }//GEN-LAST:event_activadoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String vic = Victor.getText();
        String sal = saliente1.getText();

        String direccion = "C:\\db\\Relevo _ id " + idtrip2 + "_ fecha " + fechaActual2 + "_ saliente " + sal + " _ victor " + vic + ".pdf";
        int opcion = JOptionPane.showConfirmDialog(
                null,
                "¿Deseás Terminar?",
                "Confirmar acción",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {

            //Funciona (primer panel)
            int luzpos = LuzPosicion.isSelected() ? 1 : 0;
            int luzdir = LucesDireccionales.isSelected() ? 1 : 0;
            int luzalt = LucesAltas.isSelected() ? 1 : 0;
            int luzdest = LucesDestell.isSelected() ? 1 : 0;
            int luzbaj = LucesBajas.isSelected() ? 1 : 0;
            int luzfre = LucesFreno.isSelected() ? 1 : 0;
            int luztras = LucesTras.isSelected() ? 1 : 0;
            int limpara = LimpiaPara.isSelected() ? 1 : 0;
            int fren = Frenos.isSelected() ? 1 : 0;
            int indictemp = IndicadorTemp.isSelected() ? 1 : 0;
            int testserv = TestServ.isSelected() ? 1 : 0;
            int indcom = IndNivCom.isSelected() ? 1 : 0;
            int indac = IndNivelAc.isSelected() ? 1 : 0;
            int carbat = CarBat.isSelected() ? 1 : 0;
            int calair = CalAir.isSelected() ? 1 : 0;
            //Funciona (primer panel)

            //Marcar
            int bal = balizas.isSelected() ? 1 : 0;
            int ant = antena.isSelected() ? 1 : 0;
            int sir = sirena.isSelected() ? 1 : 0;
            int eqcom = equipcom.isSelected() ? 1 : 0;
            int cint = cinturones.isSelected() ? 1 : 0;
            int rueda = ruedaaux.isSelected() ? 1 : 0;
            int lla = llave.isSelected() ? 1 : 0;
            int gan = gancho.isSelected() ? 1 : 0;
            int cat = gato.isSelected() ? 1 : 0;
            //Marcar

            int lavadero2 = lavun.isSelected() ? 1 : 0;
            int entregaypf = ypfguard.isSelected() ? 1 : 0;
            int liquidfreno = liquidfren.isSelected() ? 1 : 0;
            int refrigerante = refr.isSelected() ? 1 : 0;
            int matafuego = matafue.isSelected() ? 1 : 0;

            String texto = observations.getText();

            try {
                CLASES.Relevar.Insert(con, luzpos, luzdir, luzalt, luzdest, luzbaj, luzfre, luztras, limpara, fren, indictemp, testserv, indcom, indac, carbat, calair, bal, ant, sir, eqcom, cint, rueda, lla, gan, cat, lavadero2, entregaypf, liquidfreno, refrigerante, matafuego, texto, fechaActual, idtrip2, iduser);
            } catch (Exception ex) {
                Logger.getLogger(Relevar.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                CLASES.Relevar.guardar(Grande, lavadero, ypf, Funciona, MarcarAb, MarcarNiv, Observaciones, Marcarext, direccion, idtrip2);
            } catch (Exception ex) {
                Logger.getLogger(Relevar.class.getName()).log(Level.SEVERE, null, ex);
            }

            JOptionPane.showMessageDialog(null, "GUARDADO CORRECTAMENTE");
            this.dispose();

        } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            System.out.println("Cancelado");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Relevar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Relevar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Relevar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Relevar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Relevar(idtrip2, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Ambulancia;
    private javax.swing.JButton Borrar;
    private javax.swing.JToggleButton CalAir;
    private javax.swing.JToggleButton CarBat;
    private javax.swing.JButton Deshabilitar;
    private javax.swing.JLabel Fecha;
    private javax.swing.JLabel Fecha1;
    private javax.swing.JPanel Fondo;
    private javax.swing.JToggleButton Frenos;
    private javax.swing.JPanel Funciona;
    private javax.swing.JPanel Grande;
    private javax.swing.JToggleButton IndNivCom;
    private javax.swing.JToggleButton IndNivelAc;
    private javax.swing.JToggleButton IndicadorTemp;
    private javax.swing.JToggleButton LimpiaPara;
    private javax.swing.JToggleButton LucesAltas;
    private javax.swing.JToggleButton LucesBajas;
    private javax.swing.JToggleButton LucesDestell;
    private javax.swing.JToggleButton LucesDireccionales;
    private javax.swing.JToggleButton LucesFreno;
    private javax.swing.JToggleButton LucesTras;
    private javax.swing.JToggleButton LuzPosicion;
    private javax.swing.JPanel Marca;
    private javax.swing.JPanel MarcarAb;
    private javax.swing.JPanel MarcarNiv;
    private javax.swing.JPanel Marcarext;
    private javax.swing.JPanel Observaciones;
    private javax.swing.JToggleButton TestServ;
    private javax.swing.JLabel Victor;
    private javax.swing.JButton activado;
    private javax.swing.JToggleButton antena;
    private javax.swing.JLabel anterior;
    private javax.swing.JLabel anterior1;
    private javax.swing.JToggleButton balizas;
    private javax.swing.JToggleButton cinturones;
    private javax.swing.JLabel entrante;
    private javax.swing.JToggleButton equipcom;
    private javax.swing.JPanel extint;
    private javax.swing.JToggleButton gancho;
    private javax.swing.JToggleButton gato;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel185;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton11;
    private javax.swing.JToggleButton jToggleButton12;
    private javax.swing.JToggleButton jToggleButton13;
    private javax.swing.JToggleButton jToggleButton14;
    private javax.swing.JToggleButton jToggleButton15;
    private javax.swing.JToggleButton jToggleButton16;
    private javax.swing.JToggleButton jToggleButton17;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JLabel kmf;
    private javax.swing.JLabel kmi;
    private javax.swing.JLabel kmi1;
    private javax.swing.JPanel lavadero;
    private javax.swing.JToggleButton lavun;
    private javax.swing.JToggleButton liquidfren;
    private javax.swing.JToggleButton llave;
    private javax.swing.JToggleButton matafue;
    private javax.swing.JPanel nivelac;
    private javax.swing.JPanel nivelcomb;
    private javax.swing.JTextArea observations;
    private javax.swing.JToggleButton refr;
    private javax.swing.JToggleButton ruedaaux;
    private javax.swing.JLabel saliente;
    private javax.swing.JLabel saliente1;
    private javax.swing.JLabel serie;
    private javax.swing.JToggleButton sirena;
    private javax.swing.JSlider slider;
    private javax.swing.JLabel turno;
    private javax.swing.JComboBox<Cliente> victor;
    private javax.swing.JPanel ypf;
    private javax.swing.JToggleButton ypfguard;
    // End of variables declaration//GEN-END:variables
}
