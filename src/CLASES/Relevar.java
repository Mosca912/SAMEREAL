/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

/**
 *
 * @author Facuymayriver
 */
public class Relevar {

    public static void Insert(Connection conexion, int luzpos, int luzdir, int luzalt, int luzdest, int luzbaj, int luzfre, int luztras, int limpara, int fren, int indictemp, int testserv, int indcom, int indac, int carbat, int calair, int bal, int ant, int sir, int eqcom, int cint, int rueda, int lla, int gan, int cat, int lavadero2, int entregaypf, int liquidfreno, int refrigerante, int matafuego, String texto, String fecha, int id) {

        String sql = "INSERT INTO observaciones (idtripulacion, observacion) VALUES (?,?);";
        String sql2 = "INSERT INTO estado(idtripulacion, Matafuego, Liquido_freno, Agua_Refrigerante) VALUES (?,?,?,?)";
        String sql3 = "INSERT INTO partes (idtripulacion, cinturones, ruedaaux, llave, gancho, gato, lavuni, guardsig) VALUES (?,?,?,?,?,?,?,?)";
        String sql4 = "INSERT INTO luces (idtripulacion, luz_posicion, direccionales, alta, destalladora_emergencia, baja, freno, trasera, parabrisas) VALUES (?,?,?,?,?,?,?,?,?)";
        String sql5 = "INSERT INTO senales (idtripulacion, Frenos, temperatura_motor, testigo_service, carga_bateria, calefactor_aire, baliza, antena, sirenas, equipo_comunicacion, nivcomb, nivace) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, texto);
            ps.execute();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ps2.setInt(1, id);
            ps2.setInt(2, matafuego);
            ps2.setInt(3, liquidfreno);
            ps2.setInt(4, refrigerante);
            ps2.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
        
        try {
            PreparedStatement ps3 = conexion.prepareStatement(sql3);
            ps3.setInt(1, id);
            ps3.setInt(2, cint);
            ps3.setInt(3, rueda);
            ps3.setInt(4, lla);
            ps3.setInt(5, gan);
            ps3.setInt(6, cat);
            ps3.setInt(7, lavadero2);
            ps3.setInt(8, entregaypf);
            ps3.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
        
        try {
            PreparedStatement ps4 = conexion.prepareStatement(sql4);
            ps4.setInt(1, id);
            ps4.setInt(2, luzpos);
            ps4.setInt(3, luzdir);
            ps4.setInt(4, luzalt);
            ps4.setInt(5, luzdest);
            ps4.setInt(6, luzbaj);
            ps4.setInt(7, luzfre);
            ps4.setInt(8, luztras);
            ps4.setInt(9, limpara);
            ps4.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
        
        try {
            PreparedStatement ps5 = conexion.prepareStatement(sql5);
            ps5.setInt(1, id);
            ps5.setInt(2, fren);
            ps5.setInt(3, indictemp);
            ps5.setInt(4, testserv);
            ps5.setInt(5, carbat);
            ps5.setInt(6, calair);
            ps5.setInt(7, bal);
            ps5.setInt(8, ant);
            ps5.setInt(9, sir);
            ps5.setInt(10, eqcom);
            ps5.setInt(11, indcom);
            ps5.setInt(12, indac);
            ps5.execute();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
        
        String sql6 = "UPDATE tripulacion SET relevado=1 WHERE idtripulacion=?";
        try {
            PreparedStatement ps6 = conexion.prepareStatement(sql6);
            ps6.setInt(1, id);
            ps6.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Convierte un JPanel en BufferedImage
    private static BufferedImage panelToImage(JPanel panel) {
        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        panel.paint(g2d);
        g2d.dispose();
        return img;
    }

    // Convierte JPanel → iText Image
    private static com.itextpdf.text.Image panelToPDFImage(JPanel panel) throws IOException, DocumentException {
        BufferedImage bimg = panelToImage(panel);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bimg, "png", baos);
        return com.itextpdf.text.Image.getInstance(baos.toByteArray());
    }

    // Guardar PDF con posiciones absolutas
    public static void guardar(JPanel panelSuperior, JPanel panelIzq1, JPanel panelIzq2, JPanel panelIzqGrande, JPanel panelDerGrande, JPanel panelDerMedio, JPanel panelInferior, JPanel Marcarext, String rutaArchivo) throws Exception {

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
        document.open();

        float pageHeight = PageSize.A4.getHeight();

        // ---- Panel superior (arriba de todo, ocupa todo el ancho)
        com.itextpdf.text.Image imgSup = panelToPDFImage(panelSuperior);
        imgSup.scaleAbsolute(550, 60);
        imgSup.setAbsolutePosition(25, 765);
        document.add(imgSup);

        // ---- Panel izquierdo chico 1
        com.itextpdf.text.Image imgIzq1 = panelToPDFImage(panelIzq1);
        imgIzq1.scaleAbsolute(245, 60);
        imgIzq1.setAbsolutePosition(25, 700);
        document.add(imgIzq1);

        // ---- Panel izquierdo chico 2
        com.itextpdf.text.Image imgIzq2 = panelToPDFImage(panelIzq2);
        imgIzq2.scaleAbsolute(245, 75);
        imgIzq2.setAbsolutePosition(25, 620);
        document.add(imgIzq2);

        // ---- Panel derecho grande
        com.itextpdf.text.Image imgDerGrande = panelToPDFImage(panelDerGrande);
        imgDerGrande.scaleAbsolute(295, 220);
        imgDerGrande.setAbsolutePosition(280, 540);
        document.add(imgDerGrande);

        // ---- Panel izquierdo grande
        com.itextpdf.text.Image imgIzqGrande = panelToPDFImage(panelIzqGrande);
        imgIzqGrande.scaleAbsolute(245, 405);
        imgIzqGrande.setAbsolutePosition(25, 210);
        document.add(imgIzqGrande);

        // ---- Panel derecho medio
        com.itextpdf.text.Image imgDerMedio = panelToPDFImage(panelDerMedio);
        imgDerMedio.scaleAbsolute(295, 140);
        imgDerMedio.setAbsolutePosition(280, 210);
        document.add(imgDerMedio);

        // ---- Panel derecho abajo
        com.itextpdf.text.Image imgDerBajo = panelToPDFImage(Marcarext);
        imgDerBajo.scaleAbsolute(295, 180);
        imgDerBajo.setAbsolutePosition(280, 355);
        document.add(imgDerBajo);

        // ---- Panel inferior (abajo, ocupa todo el ancho)
        com.itextpdf.text.Image imgInferior = panelToPDFImage(panelInferior);
        imgInferior.scaleAbsolute(550, 180);
        imgInferior.setAbsolutePosition(25, 25);
        document.add(imgInferior);

        document.close();
    }

    public static void Relevo(Connection conexion, JLabel victor, JLabel anterior, JLabel saliente, JLabel entrante, JLabel kmi, JLabel kmf, JLabel turno, JLabel serie, int id) {
        String sql = "SELECT SEC_TO_TIME(TIME_TO_SEC(CASE WHEN MAX(llegada) < MIN(salida) THEN ADDTIME(MAX(llegada), '24:00:00') ELSE MAX(llegada) END) - TIME_TO_SEC(MIN(salida))) AS tiempo_total FROM movimientos WHERE idtripulacion = ? GROUP BY idtripulacion;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                turno.setText(rs.getString("tiempo_total"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql2 = "SELECT MAX(kilometrosdesalida), MIN(kilometrosdesalida) FROM movimientos WHERE idtripulacion = ? GROUP BY idtripulacion;";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                kmf.setText(rs2.getString("MAX(kilometrosdesalida)"));
                kmi.setText(rs2.getString("MIN(kilometrosdesalida)"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql3 = "SELECT ambulancia.victor from tripulacion inner join ambulancia on tripulacion.idAmbulancia=ambulancia.idAmbulancia where tripulacion.idtripulacion=?;";
        try {
            PreparedStatement ps3 = conexion.prepareStatement(sql3);
            ps3.setInt(1, id);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                victor.setText(rs3.getString("ambulancia.victor"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql4 = "SELECT ? AS id_actual, (SELECT MAX(idtripulacion) FROM movimientos WHERE idtripulacion < ?) AS id_anterior;";
        try {
            PreparedStatement ps4 = conexion.prepareStatement(sql4);
            ps4.setInt(1, id);
            ps4.setInt(2, id);
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) {
                int idact = rs4.getInt("id_actual");
                int idant = rs4.getInt("id_anterior");
                String sql5 = "SELECT CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero FROM tripulacion t JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado WHERE idtripulacion=? and e1.borrado=0 and e2.borrado=0 and e3.borrado=0;";
                try {
                    PreparedStatement ps5 = conexion.prepareStatement(sql5);
                    ps5.setInt(1, idact);
                    ResultSet rs5 = ps5.executeQuery();

                    if (rs5.next()) {
                        saliente.setText(rs5.getString("cvs") + "-" + rs5.getString("medico") + "-" + rs5.getString("enfermero"));
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
                }

                String sql6 = "SELECT CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero FROM tripulacion t JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado WHERE idtripulacion=? and e1.borrado=0 and e2.borrado=0 and e3.borrado=0;";
                try {
                    PreparedStatement ps6 = conexion.prepareStatement(sql6);
                    ps6.setInt(1, idant);
                    ResultSet rs6 = ps6.executeQuery();

                    if (rs6.next()) {
                        anterior.setText(rs6.getString("cvs") + "-" + rs6.getString("medico") + "-" + rs6.getString("enfermero"));
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql7 = "SELECT matafuego.numeroserie from tripulacion inner join ambulancia on tripulacion.idAmbulancia=ambulancia.idAmbulancia inner join matafuego on ambulancia.idMatafuego=matafuego.idMatafuego where tripulacion.idtripulacion=?;";
        try {
            PreparedStatement ps7 = conexion.prepareStatement(sql7);
            ps7.setInt(1, id);
            ResultSet rs7 = ps7.executeQuery();
            if (rs7.next()) {
                serie.setText(rs7.getString("matafuego.numeroserie"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

}
