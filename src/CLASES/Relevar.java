/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

/**
 *
 * @author Facuymayriver
 */
public class Relevar {

    /*
    // Convierte un JPanel a BufferedImage
    private static BufferedImage panelToImage(JPanel panel) {
        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        panel.paint(g2d);
        g2d.dispose();
        return img;
    }

    // Convierte JPanel → byte[] (PNG)
    private static byte[] toBytes(JPanel panel) throws IOException {
        BufferedImage bimg = panelToImage(panel);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bimg, "png", baos);
        return baos.toByteArray();
    }

    public static void guardar(JPanel superior, JPanel izq1, JPanel izq2, JPanel izqgrande, JPanel dergrande, JPanel dermedio, JPanel inferior, String direccion) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(direccion));
        document.open();

        // 📌 Tabla principal con 2 columnas
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);

        // ---- Panel Superior (ocupa 2 columnas)
        PdfPCell cellSuperior = new PdfPCell(Image.getInstance(toBytes(superior)), true);
        cellSuperior.setColspan(2);
        tabla.addCell(cellSuperior);

        // ---- Panel izquierdo chico 1 + Panel derecho grande
        tabla.addCell(Image.getInstance(toBytes(izq1)));
        PdfPCell cellDerGrande = new PdfPCell(Image.getInstance(toBytes(dergrande)), true);
        cellDerGrande.setRowspan(2); // ocupa dos filas
        tabla.addCell(cellDerGrande);

        // ---- Panel izquierdo chico 2
        tabla.addCell(Image.getInstance(toBytes(izq2)));

        // ---- Panel izquierdo grande + Panel derecho medio
        tabla.addCell(Image.getInstance(toBytes(izqgrande)));
        tabla.addCell(Image.getInstance(toBytes(dermedio)));

        // ---- Panel inferior (ocupa 2 columnas)
        PdfPCell cellInferior = new PdfPCell(Image.getInstance(toBytes(inferior)), true);
        cellInferior.setColspan(2);
        tabla.addCell(cellInferior);

        // Agregar tabla al documento
        document.add(tabla);
        document.close();
    }
     */

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
    public static void guardar(JPanel panelSuperior, JPanel panelIzq1, JPanel panelIzq2, JPanel panelIzqGrande, JPanel panelDerGrande, JPanel panelDerMedio, JPanel panelInferior, String rutaArchivo) throws Exception {

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
        document.open();

        float pageHeight = PageSize.A4.getHeight();

        // ---- Panel superior (arriba de todo, ocupa todo el ancho)
        com.itextpdf.text.Image imgSup = panelToPDFImage(panelSuperior);
        imgSup.scaleAbsolute(panelSuperior.getWidth(), panelSuperior.getHeight());
        imgSup.setAbsolutePosition(50, pageHeight - panelSuperior.getHeight() - 50);
        document.add(imgSup);

        // ---- Panel izquierdo chico 1
        com.itextpdf.text.Image imgIzq1 = panelToPDFImage(panelIzq1);
        imgIzq1.scaleAbsolute(panelIzq1.getWidth(), panelIzq1.getHeight());
        imgIzq1.setAbsolutePosition(50, pageHeight - panelSuperior.getHeight() - panelIzq1.getHeight() - 70);
        document.add(imgIzq1);

        // ---- Panel izquierdo chico 2
        com.itextpdf.text.Image imgIzq2 = panelToPDFImage(panelIzq2);
        imgIzq2.scaleAbsolute(panelIzq2.getWidth(), panelIzq2.getHeight());
        imgIzq2.setAbsolutePosition(50, pageHeight - panelSuperior.getHeight() - panelIzq1.getHeight() - panelIzq2.getHeight() - 90);
        document.add(imgIzq2);

        // ---- Panel derecho grande
        com.itextpdf.text.Image imgDerGrande = panelToPDFImage(panelDerGrande);
        imgDerGrande.scaleAbsolute(panelDerGrande.getWidth(), panelDerGrande.getHeight());
        imgDerGrande.setAbsolutePosition(250, pageHeight - panelSuperior.getHeight() - imgDerGrande.getScaledHeight() - 70);
        document.add(imgDerGrande);

        // ---- Panel izquierdo grande
        com.itextpdf.text.Image imgIzqGrande = panelToPDFImage(panelIzqGrande);
        imgIzqGrande.scaleAbsolute(panelIzqGrande.getWidth(), panelIzqGrande.getHeight());
        imgIzqGrande.setAbsolutePosition(50, 150);
        document.add(imgIzqGrande);

        // ---- Panel derecho medio
        com.itextpdf.text.Image imgDerMedio = panelToPDFImage(panelDerMedio);
        imgDerMedio.scaleAbsolute(panelDerMedio.getWidth(), panelDerMedio.getHeight());
        imgDerMedio.setAbsolutePosition(250, 150);
        document.add(imgDerMedio);

        // ---- Panel inferior (abajo, ocupa todo el ancho)
        com.itextpdf.text.Image imgInferior = panelToPDFImage(panelInferior);
        imgInferior.scaleAbsolute(panelInferior.getWidth(), panelInferior.getHeight());
        imgInferior.setAbsolutePosition(50, 50);
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
