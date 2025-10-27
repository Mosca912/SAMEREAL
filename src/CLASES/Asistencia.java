package CLASES;

import static CLASES.Relevar.con;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Asistencia {

    static int count = 0;

    public static class Area {

        private final int id;
        private final String nombre;

        public Area(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre; // lo que se muestra en el combo
        }
    }

    public static class Empleado {

        private final int id;
        private final String nombre;

        public Empleado(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre; // lo que se muestra en el combo
        }
    }

    public static void MostrarAsistencia(Connection conexion, DefaultTableModel modelo) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("SELECT asistencia.idAsistencia, empleado.apellido, cargo.cargo, base.base, seguimientoasistencia.entrada, seguimientoasistencia.salida, seguimientoasistencia.observacion FROM seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia inner join empleado on asistencia.id_Empleado=empleado.id_Empleado inner join base on asistencia.idBase=base.idBase inner join cargo on empleado.idCargo=cargo.idCargo WHERE asistencia.activo=1 AND empleado.borrado=0");
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[7];
            fila[0] = rs.getString("asistencia.idAsistencia");
            fila[1] = rs.getString("empleado.apellido");
            fila[2] = rs.getString("cargo.cargo");
            fila[3] = rs.getString("base.base");
            fila[4] = rs.getString("seguimientoasistencia.entrada");
            fila[5] = rs.getString("seguimientoasistencia.salida");
            fila[6] = rs.getString("seguimientoasistencia.observacion");
            modelo.addRow(fila);
        }
    }

    public static void jCombo(Connection conexion, JComboBox<Empleado> combo1, int id) {
        int car = 0;
        String sql3 = "SELECT idCargo FROM cargo WHERE idArea=?";

        try {

            PreparedStatement stm2 = conexion.prepareStatement(sql3);
            stm2.setInt(1, id);
            ResultSet rs2 = stm2.executeQuery();

            if (rs2.next()) {
                car = rs2.getInt("idCargo");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql = "SELECT id_Empleado, apellido, dni FROM empleado WHERE idCargo=? and borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, car);
            ResultSet rs = ps.executeQuery();
            DefaultComboBoxModel<Asistencia.Empleado> empl = new DefaultComboBoxModel();
            empl.addElement(new Asistencia.Empleado(0, "Opciones"));
            while (rs.next()) {
                int id2 = rs.getInt("id_Empleado");
                String emp = rs.getString("apellido") + ("-") + rs.getString("dni");
                empl.addElement(new Asistencia.Empleado(id2, emp));
            }

            combo1.setModel(empl);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void Datos(Connection conexion, JLabel nya, JLabel cya, JLabel d, int id) throws SQLException {
        PreparedStatement stm = conexion.prepareStatement("SELECT empleado.nombre, empleado.apellido, cargo.cargo, area.area, empleado.dni FROM empleado inner join cargo on empleado.idCargo=cargo.idCargo inner join area on cargo.idArea=area.idArea WHERE empleado.id_Empleado=?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            String nom = rs.getString("empleado.nombre");
            String ap = rs.getString("empleado.apellido");
            String car = rs.getString("cargo.cargo");
            String ar = rs.getString("area.area");
            String dni = rs.getString("empleado.dni");
            String nomap = ("Empleado: " + nom + " " + ap);
            String carar = ("Cargo: " + car + " - Area: " + ar);
            String doc = ("Documento: " + dni);

            nya.setText(nomap);
            cya.setText(carar);
            d.setText(doc);
        }
    }
    static int dia2, mesActual;

    public static void MostrarTabla(Connection conexion, int id, JTable tabla, JTable tabla2) throws SQLException {
        mesActual = LocalDate.now().getMonthValue();
        dia2 = LocalDate.now().getDayOfMonth();

        PreparedStatement stm = conexion.prepareStatement(
                "SELECT DAY(entrada) as dia, entrada, salida, observacion FROM seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE asistencia.id_Empleado=? AND MONTH(entrada)=?;");
        stm.setInt(1, id);
        stm.setInt(2, mesActual);
        ResultSet rs = stm.executeQuery();

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        DefaultTableModel modelo2 = (DefaultTableModel) tabla2.getModel();
        // Limpiar la tabla
        modelo.setRowCount(0);
        modelo2.setRowCount(0);

        // Primero agregás todos los días con vacío
        for (int i = 1; i <= 15; i++) {
            if (i == dia2) {
                modelo.addRow(new Object[]{i, "", "", ""});
            } else if (i < dia2) {
                modelo.addRow(new Object[]{i, "", "", "No asistió"});
            } else {
                modelo.addRow(new Object[]{i, "", "", ""});
            }
        }

        // Primero agregás todos los días con vacío
        for (int r = 16; r <= 31; r++) {
            if (r == dia2) {
                modelo2.addRow(new Object[]{r, "", "", ""});
            } else if (r < dia2) {
                modelo2.addRow(new Object[]{r, "", "", "No asistió"});
            } else {
                modelo2.addRow(new Object[]{r, "", "", ""});
            }
        }

        // Ahora llenás los días con datos
        while (rs.next()) {
            int dia = rs.getInt("dia");
            String entrada = rs.getString("entrada");
            String salida = rs.getString("salida");
            String observ = rs.getString("observacion");

            if (dia <= 15) {
                modelo.setValueAt(entrada, dia - 1, 1);
                modelo.setValueAt(salida, dia - 1, 2);
                modelo.setValueAt(observ, dia - 1, 3);
            } else {
                int fila = dia - 16; // Ajustar para la tabla derecha
                modelo2.setValueAt(entrada, fila, 1);
                modelo2.setValueAt(salida, fila, 2);
                modelo2.setValueAt(observ, fila, 3);
            }
        }
    }

    public static void Verificacion(Connection conexion, int id) throws Exception {
        int mes = LocalDate.now().getMonthValue();
        PreparedStatement stm = conexion.prepareStatement("SELECT COUNT(idseguimientoasistencia) FROM seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE asistencia.fecha != ? and asistencia.id_Empleado=? and asistencia.relevado=0;");
        stm.setInt(1, mes);
        stm.setInt(2, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            count = rs.getInt("COUNT(idseguimientoasistencia)");
            if (count == 1) {
                JOptionPane.showMessageDialog(null, "Este empleado tiene " + count + " mes sin relevar. Se generará el PDF correspondiente");

                // 📄 Documento en horizontal
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\db\\asistencia_mes_" + mes + ".pdf"));
                document.open();

                // 📸 Cargar imagen (desde carpeta del proyecto o ruta absoluta)
                com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(Relevar.class.getResource("/IMAGENES/asistenciapdf.png"));

                // 📏 Ajustar tamaño y posición
                img.scaleAbsolute(809, 140);
                img.setAbsolutePosition(16, 450);

                // 🧩 Agregar al documento
                document.add(img);

                PdfContentByte canvas = writer.getDirectContent();
                String sql = "SELECT empleado.nombre, empleado.apellido , area.area, cargo.Cargo, base.Base, empleado.dni FROM asistencia inner join empleado on asistencia.id_Empleado=empleado.id_Empleado inner join cargo on empleado.idCargo=cargo.idCargo inner join area on cargo.idArea=area.idArea inner join base on asistencia.idBase=base.idBase where asistencia.id_Empleado=9 and asistencia.idBase=2;";
                try {
                    PreparedStatement ps = con.prepareStatement(sql);
                    //ps.setInt(1, id);
                    ResultSet rs2 = ps.executeQuery();
                    if (rs2.next()) {
                        String emp = (rs2.getString("empleado.nombre") + (" ") + rs2.getString("empleado.apellido"));
                        String are = rs2.getString("area.area");
                        String car = rs2.getString("cargo.Cargo");
                        String bas = rs2.getString("base.Base");
                        String dni = rs2.getString("empleado.dni");

                        com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);

                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                                new Phrase(emp, font),
                                235, 460, 0);

                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                                new Phrase(are, font),
                                475, 565, 0);

                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                                new Phrase(car, font),
                                720, 515, 0);

                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                                new Phrase(bas, font),
                                580, 540, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                                new Phrase(dni, font),
                                615, 460, 0);

                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
                }

                document.add(new Paragraph("\n\n\n\n\n\n"));
                // 🧠 Título
                Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
                Paragraph titulo = new Paragraph("PLANILLA DE ASISTENCIA - MES " + mes, tituloFont);
                titulo.setAlignment(Element.ALIGN_CENTER);
                document.add(titulo);
                document.add(new Paragraph("\n"));

                // 🎨 Fuente y colores
                Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

                // ⚙️ Query → traer los registros
                PreparedStatement stm2 = con.prepareStatement("SELECT DAY(entrada) as dia, entrada, salida, observacion FROM seguimientoasistencia INNER JOIN asistencia ON seguimientoasistencia.idAsistencia = asistencia.idAsistencia WHERE asistencia.id_Empleado = 9 AND MONTH(entrada) = 10;");
                //stm.setInt(1, idEmpleado);
                //stm.setInt(2, mes);

                ResultSet rs2 = stm2.executeQuery();

                // 🗂️ Guardar los datos por día
                Map<Integer, String[]> registros = new HashMap<>();
                while (rs2.next()) {
                    int dia = rs.getInt("dia");
                    String entrada = rs2.getString("entrada") != null ? rs2.getString("entrada") : "";
                    String salida = rs2.getString("salida") != null ? rs2.getString("salida") : "";
                    String observ = rs2.getString("observacion") != null ? rs2.getString("observacion") : "";
                    registros.put(dia, new String[]{entrada, salida, observ});
                }

                // 🧱 Crear dos tablas
                PdfPTable tablaIzq = crearTablaAsistencia(1, 15, registros, fontHeader, fontNormal);
                PdfPTable tablaDer = crearTablaAsistencia(16, 31, registros, fontHeader, fontNormal);

                // ✅ Versión buena — usar tabla contenedora (no coordenadas absolutas)
                PdfPTable contenedor = new PdfPTable(2);
                contenedor.setWidthPercentage(105);
                contenedor.addCell(tablaIzq);
                contenedor.addCell(tablaDer);

                document.add(contenedor);

                document.close();
                writer.close();

                System.out.println("✅ PDF generado correctamente en horizontal: asistencia_mes_" + mes + ".pdf");

            } else if (count > 1) {
                JOptionPane.showMessageDialog(null, "Este empleado tiene " + count + " meses sin relevar. Se generará el PDF correspondiente");
            }

        }
    }

    public static void Verificacion2(Connection conexion, int id) throws Exception {
        int mes = LocalDate.now().getMonthValue();

        // 📄 Documento en horizontal
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\db\\asistencia_mes_" + mes + ".pdf"));
        document.open();

        // 📸 Cargar imagen (desde carpeta del proyecto o ruta absoluta)
        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(Relevar.class.getResource("/IMAGENES/asistenciapdf.png"));

        // 📏 Ajustar tamaño y posición
        img.scaleAbsolute(809, 140);
        img.setAbsolutePosition(16, 450);

        // 🧩 Agregar al documento
        document.add(img);

        PdfContentByte canvas = writer.getDirectContent();
        String sql = "SELECT empleado.nombre, empleado.apellido , area.area, cargo.Cargo, base.Base, empleado.dni FROM asistencia inner join empleado on asistencia.id_Empleado=empleado.id_Empleado inner join cargo on empleado.idCargo=cargo.idCargo inner join area on cargo.idArea=area.idArea inner join base on asistencia.idBase=base.idBase where asistencia.id_Empleado=9 and asistencia.idBase=2;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            //ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String emp = (rs.getString("empleado.nombre") + (" ") + rs.getString("empleado.apellido"));
                String are = rs.getString("area.area");
                String car = rs.getString("cargo.Cargo");
                String bas = rs.getString("base.Base");
                String dni = rs.getString("empleado.dni");

                com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);

                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase(emp, font),
                        235, 460, 0);

                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase(are, font),
                        475, 565, 0);

                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase(car, font),
                        720, 515, 0);

                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase(bas, font),
                        580, 540, 0);
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase(dni, font),
                        615, 460, 0);

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        document.add(new Paragraph("\n\n\n\n\n\n"));
        // 🧠 Título
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("PLANILLA DE ASISTENCIA - MES " + mes, tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph("\n"));

        // 🎨 Fuente y colores
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        // ⚙️ Query → traer los registros
        PreparedStatement stm = con.prepareStatement("SELECT DAY(entrada) as dia, entrada, salida, observacion FROM seguimientoasistencia INNER JOIN asistencia ON seguimientoasistencia.idAsistencia = asistencia.idAsistencia WHERE asistencia.id_Empleado = 9 AND MONTH(entrada) = 10;");
        //stm.setInt(1, idEmpleado);
        //stm.setInt(2, mes);

        ResultSet rs = stm.executeQuery();

        // 🗂️ Guardar los datos por día
        Map<Integer, String[]> registros = new HashMap<>();
        while (rs.next()) {
            int dia = rs.getInt("dia");
            String entrada = rs.getString("entrada") != null ? rs.getString("entrada") : "";
            String salida = rs.getString("salida") != null ? rs.getString("salida") : "";
            String observ = rs.getString("observacion") != null ? rs.getString("observacion") : "";
            registros.put(dia, new String[]{entrada, salida, observ});
        }

        // 🧱 Crear dos tablas
        PdfPTable tablaIzq = crearTablaAsistencia(1, 15, registros, fontHeader, fontNormal);
        PdfPTable tablaDer = crearTablaAsistencia(16, 31, registros, fontHeader, fontNormal);

        // ✅ Versión buena — usar tabla contenedora (no coordenadas absolutas)
        PdfPTable contenedor = new PdfPTable(2);
        contenedor.setWidthPercentage(105);
        contenedor.addCell(tablaIzq);
        contenedor.addCell(tablaDer);

        document.add(contenedor);

        document.close();
        writer.close();

        System.out.println("✅ PDF generado correctamente en horizontal: asistencia_mes_" + mes + ".pdf");
    }

    private static PdfPTable crearTablaAsistencia(int inicio, int fin, Map<Integer, String[]> registros, Font fontHeader, Font fontNormal) {
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setHeaderRows(1);

        String[] headers = {"Día", "Entrada", "Salida", "Observación"};
        for (String h : headers) {
            PdfPCell header = new PdfPCell(new Phrase(h, fontHeader));
            header.setBackgroundColor(BaseColor.DARK_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(5);
            tabla.addCell(header);
        }
// Define el valor de padding que quieres usar (ej: 5f para 5 puntos)
        float paddingValue = 5f;

        for (int i = inicio; i <= fin; i++) {
            // Celda para el valor de 'i'
            PdfPCell cellI = new PdfPCell(new Phrase(String.valueOf(i), fontNormal));
            cellI.setPadding(paddingValue); // <--- APLICA EL RELLENO AQUÍ
            tabla.addCell(cellI);

            if (registros.containsKey(i)) {
                String[] datos = registros.get(i);

                // Celda para Entrada
                PdfPCell cellEntrada = new PdfPCell(new Phrase(datos[0], fontNormal));
                cellEntrada.setPadding(paddingValue); // <--- APLICA EL RELLENO
                tabla.addCell(cellEntrada);

                // Celda para Salida
                PdfPCell cellSalida = new PdfPCell(new Phrase(datos[1], fontNormal));
                cellSalida.setPadding(paddingValue); // <--- APLICA EL RELLENO
                tabla.addCell(cellSalida);

                // Celda para Observación
                PdfPCell cellObservacion = new PdfPCell(new Phrase(datos[2], fontNormal));
                cellObservacion.setPadding(paddingValue); // <--- APLICA EL RELLENO
                tabla.addCell(cellObservacion);

            } else {
                // Celdas vacías

                PdfPCell emptyCell1 = new PdfPCell(new Phrase("", fontNormal));
                emptyCell1.setPadding(paddingValue); // <--- APLICA EL RELLENO
                tabla.addCell(emptyCell1);

                PdfPCell emptyCell2 = new PdfPCell(new Phrase("", fontNormal));
                emptyCell2.setPadding(paddingValue); // <--- APLICA EL RELLENO
                tabla.addCell(emptyCell2);

                PdfPCell emptyCell3 = new PdfPCell(new Phrase("", fontNormal));
                emptyCell3.setPadding(paddingValue); // <--- APLICA EL RELLENO
                tabla.addCell(emptyCell3);
            }
        }

        return tabla;
    }

    public static void Asistencia(Connection conexion, int id) throws SQLException {
        int ida = 0;
        PreparedStatement stm5 = conexion.prepareStatement("SELECT idAsistencia from asistencia where id_Empleado=? and activo=1");
        stm5.setInt(1, id);
        ResultSet rs5 = stm5.executeQuery();

        if (rs5.next()) {
            JOptionPane.showMessageDialog(null, "YA ESTA ACTIVO EL EMPLEADO");
        } else {

            PreparedStatement stm2 = conexion.prepareStatement("INSERT INTO asistencia (id_Empleado, idBase, activo, relevado) values (?,2,1,0)");
            stm2.setInt(1, id);
            try {
                stm2.execute();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Primer Error:" + e);
            }

            PreparedStatement stm3 = conexion.prepareStatement("SELECT idAsistencia from asistencia where id_Empleado=? and activo=1");
            stm3.setInt(1, id);
            ResultSet rs3 = stm3.executeQuery();

            if (rs3.next()) {
                ida = rs3.getInt("idAsistencia");

            }

            PreparedStatement stm4 = conexion.prepareStatement("INSERT INTO seguimientoasistencia (entrada, idAsistencia) values (NOW(),?)");
            stm4.setInt(1, ida);
            try {
                stm4.execute();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Segundo Error:" + e);
            }
        }
    }

    public static void AsistenciaReal(Connection conexion, int id) throws SQLException {
        int ida = 0;

        PreparedStatement stm5 = conexion.prepareStatement("SELECT entrada from seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE DAY(entrada)=? and MONTH(entrada)=? and asistencia.id_Empleado=?;");
        stm5.setInt(1, dia2);
        stm5.setInt(2, mesActual);
        stm5.setInt(3, id);
        ResultSet rs5 = stm5.executeQuery();

        if (rs5.next()) {
            JOptionPane.showMessageDialog(null, "Ya se cargo el empleado hoy!");
        } else {
            PreparedStatement stm6 = conexion.prepareStatement("SELECT idAsistencia from Asistencia WHERE id_Empleado=? and Activo=1 and relevado=0");
            stm6.setInt(1, id);
            ResultSet rs = stm6.executeQuery();
            if (rs.next()) {
                ida = rs.getInt("idAsistencia");
            } else {
                PreparedStatement stm2 = conexion.prepareStatement("INSERT INTO asistencia (id_Empleado, idBase, activo, relevado) values (?,2,1,0)");
                stm2.setInt(1, id);
                try {
                    stm2.execute();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Primer Error:" + e);
                }

                PreparedStatement stm3 = conexion.prepareStatement("SELECT idAsistencia from Asistencia WHERE id_Empleado=? and Activo=1 and relevado=0");
                stm3.setInt(1, id);
                ResultSet rs3 = stm3.executeQuery();

                if (rs3.next()) {
                    ida = rs3.getInt("idAsistencia");

                }
            }

            PreparedStatement stm4 = conexion.prepareStatement("INSERT INTO seguimientoasistencia (entrada, idAsistencia, Relevado) values (NOW(),?,0)");
            stm4.setInt(1, ida);
            try {
                stm4.execute();
                JOptionPane.showMessageDialog(null, "¡Carga completa!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Segundo Error:" + e);
            }
        }
    }

    public static void ActualizarAsistencia(Connection conexion, int Dia, String Entrada, String Observacion, int id) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia SET seguimientoasistencia.salida = NOW(), seguimientoasistencia.observacion = ?, seguimientoasistencia.Relevado=1 WHERE seguimientoasistencia.entrada = ? and asistencia.id_Empleado=?");
        stm.setString(1, Observacion);
        stm.setString(2, Entrada);
        stm.setInt(3, id);

        try {
            stm.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }

    }

    public static int VerificacionClick(Connection conexion, String Entrada, int id) throws SQLException {
        int veri = 0;

        PreparedStatement stm = conexion.prepareStatement("Select seguimientoasistencia.Relevado from seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE seguimientoasistencia.entrada=? and asistencia.id_Empleado=?;");
        stm.setString(1, Entrada);
        stm.setInt(2, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            int relevo = rs.getInt("seguimientoasistencia.Relevado");
            if (relevo == 1) {
                veri = 1;
            }
        }
        return veri;

    }

    public static void GenerarPDF(Connection conexion, int idEmpleado, int mesSeleccionado, String rutaGuardado) throws SQLException, IOException, DocumentException {
        for (int i = 0; i < count; i++) {
            String rutaPlantilla = "/RECURSOS/planilla.pdf";

            PdfReader reader = new PdfReader(rutaPlantilla);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(rutaGuardado)); // 🔹 acá se guarda el archivo
            PdfContentByte cb = stamper.getOverContent(1); // usamos la página 1

            BaseFont font = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
            cb.setFontAndSize(font, 10);
            cb.setColorFill(BaseColor.BLACK);

            PreparedStatement stm = conexion.prepareStatement(
                    "SELECT entrada, salida, observacion FROM seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE asistencia.id_Empleado=? AND MONTH(entrada)=?"
            );
            stm.setInt(1, idEmpleado);
            stm.setInt(2, mesSeleccionado);
            ResultSet rs = stm.executeQuery();

            float yBase = 630;  // posición vertical del día 1
            float salto = 18;   // separación entre filas (depende de tu plantilla)

            while (rs.next()) {
                LocalDateTime entrada = rs.getTimestamp("entrada").toLocalDateTime();
                LocalDateTime salida = rs.getTimestamp("salida") != null ? rs.getTimestamp("salida").toLocalDateTime() : null;
                String obs = rs.getString("observacion");
                int dia = entrada.getDayOfMonth();

                float yPos = yBase - (dia - 1) * salto;

                cb.beginText();
                cb.showTextAligned(Element.ALIGN_LEFT, String.valueOf(dia), 45, yPos, 0);
                cb.showTextAligned(Element.ALIGN_LEFT, entrada.toLocalTime().toString(), 85, yPos, 0);
                if (salida != null) {
                    cb.showTextAligned(Element.ALIGN_LEFT, salida.toLocalTime().toString(), 140, yPos, 0);
                }
                if (obs != null) {
                    cb.showTextAligned(Element.ALIGN_LEFT, obs, 220, yPos, 0);
                }
                cb.endText();
            }

            stamper.close();
            reader.close();

            System.out.println("✅ PDF generado correctamente en: " + rutaGuardado);
        }
    }

    private static Timer timer = null;

    public static void iniciarContador(Connection conexion, LocalDateTime entrada, JLabel destinoLabel) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(1000, (ActionEvent e) -> {
            LocalDateTime ahora = LocalDateTime.now();
            long minutosTotales = ChronoUnit.MINUTES.between(entrada, ahora);
            long horas = minutosTotales / 60;
            long minutos = minutosTotales % 60;

            if (horas < 8) {
                destinoLabel.setText("Transcurrido: " + horas + "h " + minutos + "min");
            } else {
                // 🚨 Alcanzó 8 horas
                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "Se alcanzaron las 8 horas. ¿Querés finalizar el turno?",
                        "Confirmar fin de turno",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    destinoLabel.setText("Turno finalizado");
                    ((Timer) e.getSource()).stop();
                    try {
                        PreparedStatement stm = conexion.prepareStatement("UPDATE asistencia SET activo=0");
                        stm.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(Asistencia.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    destinoLabel.setText("Superadas 8h (esperando cierre manual)");
                    ((Timer) e.getSource()).stop(); // ❌ lo detengo igual
                }
            }
        });

        timer.start(); // ⏯️ Comienza la cuenta
    }

    public static void Select(Connection conexion, JComboBox<Area> combo1) {

        String sql = "SELECT idArea, area FROM area WHERE borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DefaultComboBoxModel<Asistencia.Area> area = new DefaultComboBoxModel();
            area.addElement(new Asistencia.Area(0, "Opciones"));
            while (rs.next()) {
                int id = rs.getInt("idArea");
                String tripulacion = rs.getString("area");
                area.addElement(new Asistencia.Area(id, tripulacion));
            }

            combo1.setModel(area);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

    }

}
