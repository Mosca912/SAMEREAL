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
import java.io.File;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
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

        String sql = "SELECT empleado.id_Empleado, empleado.apellido, empleado.dni FROM empleado inner join cargo on empleado.idCargo=cargo.idCargo WHERE cargo.idArea=? and empleado.borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            DefaultComboBoxModel<Asistencia.Empleado> empl = new DefaultComboBoxModel();
            empl.addElement(new Asistencia.Empleado(0, "Opciones"));
            while (rs.next()) {
                int id2 = rs.getInt("empleado.id_Empleado");
                String emp = rs.getString("empleado.apellido") + ("-") + rs.getString("empleado.dni");
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
    static int dia2, mesActual, yearact;

    public static void MostrarTabla(Connection conexion, int id, JTable tabla) throws SQLException {
        mesActual = LocalDate.now().getMonthValue();
        yearact = LocalDate.now().getYear();
        dia2 = LocalDate.now().getDayOfMonth();

        PreparedStatement stm = conexion.prepareStatement(
                "SELECT DAY(entrada) as dia, entrada, salida, observacion FROM seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE asistencia.id_Empleado=? AND MONTH(entrada)=? AND asistencia.year=?;");
        stm.setInt(1, id);
        stm.setInt(2, mesActual);
        stm.setInt(3, yearact);
        ResultSet rs = stm.executeQuery();

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        // Limpiar la tabla
        modelo.setRowCount(0);

        // Primero agregás todos los días con vacío
        for (int i = 1; i <= 31; i++) {
            if (i == dia2) {
                modelo.addRow(new Object[]{i, "", "", ""});
            } else if (i < dia2) {
                modelo.addRow(new Object[]{i, "", "", "No asistió"});
            } else {
                modelo.addRow(new Object[]{i, "", "", ""});
            }
        }

        // Ahora llenás los días con datos
        while (rs.next()) {
            int dia = rs.getInt("dia");
            String entrada = rs.getString("entrada");
            String salida = rs.getString("salida");
            String observ = rs.getString("observacion");

            if (dia <= 31) {
                modelo.setValueAt(entrada, dia - 1, 1);
                modelo.setValueAt(salida, dia - 1, 2);
                modelo.setValueAt(observ, dia - 1, 3);
            }
        }
    }

    public static List<String> obtenerFechasFaltantes(Connection con, int mes, int anioActual, int id) throws SQLException {
        // Usaremos una lista de String para guardar las fechas
        List<String> fechasFaltantes = new ArrayList<>();
        PreparedStatement pstmt = con.prepareStatement("SELECT fecha, year FROM asistencia WHERE id_Empleado = ? AND relevado = 0 AND (year < ? OR (year = ? AND fecha < ?)) ORDER BY year, fecha ASC;");
        pstmt.setInt(1, id);
        pstmt.setInt(2, anioActual);
        pstmt.setInt(3, anioActual);
        pstmt.setInt(4, mes);
        ResultSet rs = pstmt.executeQuery();
        try {
            while (rs.next()) {
                int mesrel = rs.getInt("fecha");
                int anio = rs.getInt("year"); // <-- Obtenemos el año

                // Combinamos mes y año en un formato fácil de usar (ej: "11-2025")
                String fechaCompleta = mesrel + "-" + anio;
                fechasFaltantes.add(fechaCompleta);
            }
        } catch (SQLException e) {
            // En lugar de e.printStackTrace(), es mejor lanzar la excepción o manejarla
            throw new SQLException("Error al obtener fechas faltantes de la asistencia: " + e.getMessage(), e);
        } finally {
            // Asegúrate de cerrar el ResultSet
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return fechasFaltantes;
    }

    public static void Verificacion(Connection conexion, int id, int iduser) throws Exception {
        LocalDate fechaActual = LocalDate.now();
        int anioActual = fechaActual.getYear();
        String rutaCarpeta = "C:\\SAME";
        String rutaCarpeta2 = "C:\\SAME\\Asistencia";
        File carpeta = new File(rutaCarpeta);
        File carpeta2 = new File(rutaCarpeta2);

        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (creada) {
                System.out.println("✅ Carpeta creada exitosamente en: " + rutaCarpeta);
                if (!carpeta2.exists()) {
                    boolean creada2 = carpeta2.mkdirs();
                    if (creada2) {
                        System.out.println("✅ Carpeta creada exitosamente en: " + rutaCarpeta2);
                    } else {
                        System.out.println("❌ Error al intentar crear la carpeta en: " + rutaCarpeta2);
                    }
                } else {
                    System.out.println("⚠️ La carpeta ya existe en: " + rutaCarpeta2);
                }
            } else {
                System.out.println("❌ Error al intentar crear la carpeta en: " + rutaCarpeta);
            }
        } else {
            System.out.println("⚠️ La carpeta ya existe en: " + rutaCarpeta);
            if (!carpeta2.exists()) {
                boolean creada = carpeta2.mkdirs();
                if (creada) {
                    System.out.println("✅ Carpeta creada exitosamente en: " + rutaCarpeta2);
                } else {
                    System.out.println("❌ Error al intentar crear la carpeta en: " + rutaCarpeta2);
                }
            } else {
                System.out.println("⚠️ La carpeta ya existe en: " + rutaCarpeta2);
            }
        }
        int mes = LocalDate.now().getMonthValue();
        String nombre;
        String apellido;

        String sqlContar = "SELECT COUNT(T1.idasistencia), T2.nombre, T2.apellido FROM asistencia T1 INNER JOIN empleado T2 ON T1.id_Empleado = T2.id_Empleado WHERE T1.id_Empleado = ?  AND T1.relevado = 0  AND (T1.year != ? OR (T1.year = ? AND T1.fecha != ?));";

        //PreparedStatement stm = conexion.prepareStatement("SELECT COUNT(asistencia.idasistencia), empleado.nombre, empleado.apellido FROM asistencia inner join empleado on asistencia.id_Empleado=empleado.id_Empleado WHERE asistencia.fecha != ? and asistencia.id_Empleado=? and asistencia.relevado=0;");
        PreparedStatement stm = conexion.prepareStatement(sqlContar);
        stm.setInt(1, id);
        stm.setInt(2, anioActual); // T1.year < anioActual
        stm.setInt(3, anioActual); // T1.year = anioActual
        stm.setInt(4, mes);
        ResultSet rs = stm.executeQuery();
        int base = CLASES.Usuario.base();
        if (rs.next()) {
            count = rs.getInt("COUNT(T1.idasistencia)");
            nombre = rs.getString("T2.nombre");
            apellido = rs.getString("T2.apellido");
            if (count == 1) {
                JOptionPane.showMessageDialog(null, "Este empleado tiene " + count + " mes sin relevar. Se generará el PDF correspondiente");

                // Llamar al método
                List<String> misFechas = obtenerFechasFaltantes(con, mes, anioActual, id);
                int cantidad = misFechas.size();
                System.out.println("Cantidad de registros encontrados: " + cantidad);

                // Para ver cada fecha:
                for (String fecha : misFechas) {
                    System.out.println("Fecha faltante: " + fecha);
                    String[] partes = fecha.split("-");
                    int mesFaltante = Integer.parseInt(partes[0]);
                    int anioFaltante = Integer.parseInt(partes[1]);

                    Document document = new Document(PageSize.A4.rotate());
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\SAME\\Asistencia\\asistencia_mes_" + fecha + "_" + nombre + " " + apellido + ".pdf"));
                    document.open();

                    // 📸 Cargar imagen (desde carpeta del proyecto o ruta absoluta)
                    com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(Relevar.class.getResource("/IMAGENES/asistenciapdf.png"));

                    // 📏 Ajustar tamaño y posición
                    img.scaleAbsolute(809, 140);
                    img.setAbsolutePosition(16, 450);

                    // 🧩 Agregar al documento
                    document.add(img);

                    PdfContentByte canvas = writer.getDirectContent();
                    String sql = "SELECT empleado.nombre, empleado.apellido , area.area, cargo.Cargo, base.Base, empleado.dni FROM asistencia inner join empleado on asistencia.id_Empleado=empleado.id_Empleado inner join cargo on empleado.idCargo=cargo.idCargo inner join area on cargo.idArea=area.idArea inner join base on asistencia.idBase=base.idBase where asistencia.id_Empleado=? and asistencia.idBase=?;";
                    try {
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);
                        ps.setInt(2, base);
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
                    Paragraph titulo = new Paragraph("PLANILLA DE ASISTENCIA - MES " + fecha, tituloFont);
                    titulo.setAlignment(Element.ALIGN_CENTER);
                    document.add(titulo);
                    document.add(new Paragraph("\n"));

                    // 🎨 Fuente y colores
                    Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                    Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

                    // Consulta para los registros de seguimiento (stm2)
                    PreparedStatement stm2 = con.prepareStatement(
                            "SELECT DAY(sa.entrada) as dia, sa.entrada, sa.salida, sa.observacion "
                            + "FROM seguimientoasistencia sa "
                            + "INNER JOIN asistencia a ON sa.idAsistencia = a.idAsistencia "
                            + "WHERE a.id_Empleado = ? "
                            + "  AND MONTH(sa.entrada) = ? "
                            + "  AND YEAR(sa.entrada) = ?;"
                    );

                    stm2.setInt(1, id);
                    stm2.setInt(2, mesFaltante); // Mes faltante (debe ser INT)
                    stm2.setInt(3, anioFaltante); // Año faltante (debe ser INT)

                    ResultSet rs2 = stm2.executeQuery();

                    // 🗂️ Guardar los datos por día
                    Map<Integer, String[]> registros = new HashMap<>();
                    while (rs2.next()) {
                        int dia = rs2.getInt("dia");
                        String entrada = rs2.getString("entrada") != null ? rs2.getString("entrada") : "";
                        String salida = rs2.getString("salida") != null ? rs2.getString("salida") : "";
                        String observ = rs2.getString("observacion") != null ? rs2.getString("observacion") : "";
                        registros.put(dia, new String[]{entrada, salida, observ});
                    }

                    Calendar calendar = new GregorianCalendar(anioFaltante, mesFaltante - 1, 1);
                    int totalDiasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

// Generar la tabla unificada de 8 columnas
                    PdfPTable tablaUnificada = crearTablaAsistenciaUnificada(
                            registros,
                            fontHeader,
                            fontNormal,
                            totalDiasMes // <-- Le pasamos cuántos días tiene el mes
                    );

// Añadir la tabla al documento
                    document.add(tablaUnificada);

                    document.close();
                    writer.close();

                    System.out.println("✅ PDF generado correctamente en horizontal: asistencia_mes_" + mes + ".pdf");

                    PreparedStatement stm3 = conexion.prepareStatement("UPDATE asistencia SET relevado = 1 WHERE fecha= ? and id_Empleado=? and year=?;");
                    stm3.setInt(1, mesFaltante);
                    stm3.setInt(2, id);
                    stm3.setInt(3, anioFaltante);
                    try {
                        stm3.executeUpdate();
                        PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_asistencia (evento, id_empleado, id_usuario) VALUES (?, ?, ?)");
                        stm9.setString(1, "REGISTRO_ASISTENCIA_PDF");
                        stm9.setInt(2, id);
                        stm9.setInt(3, iduser);
                        try {
                            stm9.execute();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "ERROR: " + e);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "ERROR" + e);
                    }
                }
            } else if (count > 1) {
                JOptionPane.showMessageDialog(null, "Este empleado tiene " + count + " meses sin relevar. Se generará el PDF correspondiente");

                // Llamar al método
                List<String> misFechas = obtenerFechasFaltantes(con, mes, anioActual, id);
                int cantidad = misFechas.size();
                System.out.println("Cantidad de registros encontrados: " + cantidad);

                // Para ver cada fecha:
                for (String fecha : misFechas) {
                    System.out.println("Fecha faltante: " + fecha);
                    String[] partes = fecha.split("-");
                    int mesFaltante = Integer.parseInt(partes[0]);
                    int anioFaltante = Integer.parseInt(partes[1]);
                    // 📄 Documento en horizontal
                    Document document = new Document(PageSize.A4.rotate());
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\SAME\\Asistencia\\asistencia_mes_" + fecha + "_" + nombre + " " + apellido + ".pdf"));
                    document.open();

                    // 📸 Cargar imagen (desde carpeta del proyecto o ruta absoluta)
                    com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(Relevar.class.getResource("/IMAGENES/asistenciapdf.png"));

                    // 📏 Ajustar tamaño y posición
                    img.scaleAbsolute(809, 140);
                    img.setAbsolutePosition(16, 450);

                    // 🧩 Agregar al documento
                    document.add(img);

                    PdfContentByte canvas = writer.getDirectContent();
                    String sql = "SELECT empleado.nombre, empleado.apellido , area.area, cargo.Cargo, base.Base, empleado.dni FROM asistencia inner join empleado on asistencia.id_Empleado=empleado.id_Empleado inner join cargo on empleado.idCargo=cargo.idCargo inner join area on cargo.idArea=area.idArea inner join base on asistencia.idBase=base.idBase where asistencia.id_Empleado=? and asistencia.idBase=?;";
                    try {
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, id);
                        ps.setInt(2, base);
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
                    Paragraph titulo = new Paragraph("PLANILLA DE ASISTENCIA - MES " + fecha, tituloFont);
                    titulo.setAlignment(Element.ALIGN_CENTER);
                    document.add(titulo);
                    document.add(new Paragraph("\n"));

                    // 🎨 Fuente y colores
                    Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                    Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

                    // Consulta para los registros de seguimiento (stm2)
                    PreparedStatement stm2 = con.prepareStatement(
                            "SELECT DAY(sa.entrada) as dia, sa.entrada, sa.salida, sa.observacion "
                            + "FROM seguimientoasistencia sa "
                            + "INNER JOIN asistencia a ON sa.idAsistencia = a.idAsistencia "
                            + "WHERE a.id_Empleado = ? "
                            + "  AND MONTH(sa.entrada) = ? "
                            + "  AND YEAR(sa.entrada) = ?;"
                    );

                    stm2.setInt(1, id);
                    stm2.setInt(2, mesFaltante); // Mes faltante (debe ser INT)
                    stm2.setInt(3, anioFaltante); // Año faltante (debe ser INT)

                    ResultSet rs2 = stm2.executeQuery();

                    // 🗂️ Guardar los datos por día
                    Map<Integer, String[]> registros = new HashMap<>();
                    while (rs2.next()) {
                        int dia = rs2.getInt("dia");
                        String entrada = rs2.getString("entrada") != null ? rs2.getString("entrada") : "";
                        String salida = rs2.getString("salida") != null ? rs2.getString("salida") : "";
                        String observ = rs2.getString("observacion") != null ? rs2.getString("observacion") : "";
                        registros.put(dia, new String[]{entrada, salida, observ});
                    }

                    Calendar calendar = new GregorianCalendar(anioFaltante, mesFaltante - 1, 1);
                    int totalDiasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

// Generar la tabla unificada de 8 columnas
                    PdfPTable tablaUnificada = crearTablaAsistenciaUnificada(
                            registros,
                            fontHeader,
                            fontNormal,
                            totalDiasMes // <-- Le pasamos cuántos días tiene el mes
                    );

// Añadir la tabla al documento
                    document.add(tablaUnificada);

                    document.close();
                    writer.close();

                    System.out.println("✅ PDF generado correctamente en horizontal: asistencia_mes_" + mes + ".pdf");

                    PreparedStatement stm3 = conexion.prepareStatement("UPDATE asistencia SET relevado = 1 WHERE fecha= ? and id_Empleado=? and year=?;");
                    stm3.setInt(1, mesFaltante);
                    stm3.setInt(2, id);
                    stm3.setInt(3, anioFaltante);
                    try {
                        stm3.executeUpdate();
                        PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_asistencia (evento, id_empleado, id_usuario) VALUES (?, ?, ?)");
                        stm9.setString(1, "REGISTRO_ASISTENCIA_PDF");
                        stm9.setInt(2, id);
                        stm9.setInt(3, iduser);
                        try {
                            stm9.execute();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "ERROR: " + e);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "ERROR" + e);
                    }
                }
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
        //PdfPTable tablaIzq = crearTablaAsistencia(1, 15, registros, fontHeader, fontNormal);
        //PdfPTable tablaDer = crearTablaAsistencia(16, 31, registros, fontHeader, fontNormal);
        // ✅ Versión buena — usar tabla contenedora (no coordenadas absolutas)
        PdfPTable contenedor = new PdfPTable(2);
        contenedor.setWidthPercentage(105);
        //contenedor.addCell(tablaIzq);
        //contenedor.addCell(tablaDer);

        document.add(contenedor);

        document.close();
        writer.close();

        System.out.println("✅ PDF generado correctamente en horizontal: asistencia_mes_" + mes + ".pdf");
    }

    public static PdfPTable crearTablaAsistenciaUnificada(
            Map<Integer, String[]> registros, // Mapa con todos los días del mes (1-31)
            Font fontHeader,
            Font fontNormal,
            int totalDiasMes // Nuevo parámetro para manejar meses de 28, 29, 30 o 31 días
    ) {

        // 1. Crear la tabla con 8 columnas
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        // ********** AJUSTE CRÍTICO PARA EL SALTO DE PÁGINA **********
        // Permite que la tabla se divida más cerca del final de la página.
        table.setSplitLate(false);

        // Definir anchos relativos (Total debe sumar 100f)
        // Días más estrechos, Entradas/Salidas intermedias, Observación más ancha.
        float[] columnWidths = {5f, 15f, 15f, 25f, 5f, 15f, 15f, 5f}; // TOTAL: 100f
        try {
            table.setWidths(columnWidths);
        } catch (Exception e) {
            // Manejo de excepción si hay un problema con los anchos
            e.printStackTrace();
        }

        // 2. Añadir encabezados (8 columnas)
        String[] headers = {"Día", "Entrada", "Salida", "Observación"};
        BaseColor headerColor = new BaseColor(60, 60, 60); // Gris oscuro para encabezados

        for (int j = 0; j < 2; j++) { // Repite los encabezados para ambos lados
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h, fontHeader));
                header.setBackgroundColor(headerColor);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPadding(5);
                table.addCell(header);
            }
        }

        // Define el valor de padding que quieres usar
        float paddingValue = 5f;

        // 3. Llenar los datos: Iteramos por las primeras 16 filas (Día 1 al 16)
        // El máximo de filas necesarias es 16 (para cubrir día 1 y día 31)
        int filas = (totalDiasMes <= 15) ? 15 : 16;

        for (int i = 1; i <= filas; i++) {

            // --- LADO IZQUIERDO (Día 1 al 15 o 16) ---
            int diaIzquierdo = i;
            agregarFila(table, diaIzquierdo, registros, fontNormal, paddingValue);

            // --- LADO DERECHO (Día 16 al 31) ---
            int diaDerecho = i + 15;

            // Solo agregamos si el día existe en el mes
            if (diaDerecho <= totalDiasMes) {
                agregarFila(table, diaDerecho, registros, fontNormal, paddingValue);
            } else {
                // Si el día no existe (ej: después del 30 en Abril), rellenar con 4 celdas vacías
                for (int k = 0; k < 4; k++) {
                    PdfPCell emptyCell = new PdfPCell(new Phrase("", fontNormal));
                    emptyCell.setPadding(paddingValue);
                    table.addCell(emptyCell);
                }
            }
        }

        return table;
    }

// Función auxiliar para agregar las 4 celdas de una entrada
    private static void agregarFila(PdfPTable table, int dia, Map<Integer, String[]> registros, Font fontNormal, float paddingValue) {
        // Celda para el valor de 'Día'
        PdfPCell cellDia = new PdfPCell(new Phrase(String.valueOf(dia), fontNormal));
        cellDia.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDia.setPadding(paddingValue);
        table.addCell(cellDia);

        if (registros.containsKey(dia)) {
            String[] datos = registros.get(dia);

            // Entrada (datos[0])
            PdfPCell cellEntrada = new PdfPCell(new Phrase(datos[0], fontNormal));
            cellEntrada.setPadding(paddingValue);
            table.addCell(cellEntrada);

            // Salida (datos[1])
            PdfPCell cellSalida = new PdfPCell(new Phrase(datos[1], fontNormal));
            cellSalida.setPadding(paddingValue);
            table.addCell(cellSalida);

            // Observación (datos[2])
            PdfPCell cellObservacion = new PdfPCell(new Phrase(datos[2], fontNormal));
            cellObservacion.setPadding(paddingValue);
            // IMPORTANTE: Permitir que el texto envuelva
            cellObservacion.setNoWrap(false);
            table.addCell(cellObservacion);

        } else {
            // Celdas vacías
            for (int k = 0; k < 3; k++) {
                PdfPCell emptyCell = new PdfPCell(new Phrase("", fontNormal));
                emptyCell.setPadding(paddingValue);
                table.addCell(emptyCell);
            }
        }
    }

    public static void AsistenciaReal(Connection conexion, int id, int iduser) throws SQLException {
        mesActual = LocalDate.now().getMonthValue();
        yearact = LocalDate.now().getYear();
        int ida = 0;
        int base = CLASES.Usuario.base();

        PreparedStatement stm5 = conexion.prepareStatement("SELECT entrada from seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia WHERE DAY(entrada)=? and MONTH(entrada)=? and asistencia.id_Empleado=? and asistencia.year=?;");
        stm5.setInt(1, dia2);
        stm5.setInt(2, mesActual);
        stm5.setInt(3, id);
        stm5.setInt(4, yearact);
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
                PreparedStatement stm2 = conexion.prepareStatement("INSERT INTO asistencia (id_Empleado, idBase, activo, relevado, fecha, year) values (?,?,1,0,?,?)");
                stm2.setInt(1, id);
                stm2.setInt(2, base);
                stm2.setInt(3, mesActual);
                stm2.setInt(4, yearact);
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
                PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_asistencia (evento, id_empleado, id_usuario) VALUES (?, ?, ?)");
                stm9.setString(1, "NUEVA_ASISTENCIA");
                stm9.setInt(2, id);
                stm9.setInt(3, iduser);
                try {
                    stm9.execute();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + e);
                }
                JOptionPane.showMessageDialog(null, "¡Carga completa!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Segundo Error:" + e);
            }
        }
    }

    public static void ActualizarAsistencia(Connection conexion, int Dia, String Entrada, String Observacion, int id, int iduser) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE seguimientoasistencia inner join asistencia on seguimientoasistencia.idAsistencia=asistencia.idAsistencia SET seguimientoasistencia.salida = NOW(), seguimientoasistencia.observacion = ?, seguimientoasistencia.Relevado=1 WHERE seguimientoasistencia.entrada = ? and asistencia.id_Empleado=?");
        stm.setString(1, Observacion);
        stm.setString(2, Entrada);
        stm.setInt(3, id);

        try {
            stm.executeUpdate();
            PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_asistencia (evento, id_empleado, id_usuario) VALUES (?, ?, ?)");
            stm9.setString(1, "ACTUALIZACIÓN_ASISTENCIA");
            stm9.setInt(2, id);
            stm9.setInt(3, iduser);
            try {
                stm9.execute();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e);
            }
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
