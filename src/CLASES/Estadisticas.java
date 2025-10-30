/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Facuymayriver
 */
public class Estadisticas {

    static DefaultComboBoxModel<Victor1> model;

    public static class Victor1 {

        private final int id;
        private final String nombre;

        public Victor1(int id, String nombre) {
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

    public static void victor(Connection conexion, JComboBox<Estadisticas.Victor1> combo) {

        String sql4 = "SELECT idAmbulancia, victor FROM ambulancia";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql4);
            ResultSet rs = ps.executeQuery();
            model = new DefaultComboBoxModel<>();
            model.addElement(new Estadisticas.Victor1(0, "Opciones"));
            while (rs.next()) {
                int id = rs.getInt("idAmbulancia");
                String nombreCompleto = rs.getString("victor");
                model.addElement(new Estadisticas.Victor1(id, nombreCompleto));
            }

            combo.setModel(model);
            AutoCompleteDecorator.decorate(combo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void estadisticageneral(Connection conexion, JPanel panelEst) {
        // Listas para guardar los IDs y nombres (o "victor" en tu caso)
        List<Integer> ambulancias = new ArrayList<>();
        List<Integer> victor = new ArrayList<>();

        // 1️⃣ Obtener todos los idAmbulancia y victor
        String sqlAmbulancias = "SELECT idAmbulancia, victor FROM ambulancia;";
        try (PreparedStatement ps = conexion.prepareStatement(sqlAmbulancias);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ambulancias.add(rs.getInt("idAmbulancia"));
                victor.add(rs.getInt("victor"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener ambulancias: " + ex.getMessage());
            return;
        }

        // Dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 2️⃣ Recorrer cada ambulancia y contar sus movimientos por mes
        String sqlMovimientos = "SELECT MONTH(movimientos.fecha) AS mes, COUNT(*) AS cantidad_movimientos "
                + "FROM movimientos "
                + "INNER JOIN tripulacion ON movimientos.idtripulacion = tripulacion.idtripulacion "
                + "WHERE tripulacion.idAmbulancia = ? "
                + "GROUP BY MONTH(fecha) ORDER BY mes;";

        for (int i = 0; i < ambulancias.size(); i++) {
            int idAmb = ambulancias.get(i);
            int victorName = victor.get(i);

            try (PreparedStatement ps2 = conexion.prepareStatement(sqlMovimientos)) {
                ps2.setInt(1, idAmb);
                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    int cantidadMov = rs2.getInt("cantidad_movimientos");
                    int mes = rs2.getInt("mes");

                    // Agregar datos al dataset (valor, serie, categoría)
                    dataset.addValue(cantidadMov, "Ambulancia " + victorName, "Mes " + mes);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al contar movimientos de la ambulancia " + idAmb + ": " + ex.getMessage());
            }
        }

        // 3️⃣ Crear gráfico de columnas (bar chart)
        JFreeChart chart = ChartFactory.createBarChart(
                "Movimientos por Mes", // título
                "Mes", // eje X
                "Cantidad de Movimientos", // eje Y
                dataset, // datos
                PlotOrientation.VERTICAL, // orientación
                true, // leyenda
                true, // tooltips
                false // URLs
        );

        // 4️⃣ Personalizar apariencia
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.GRAY);

        // 🔹 Cambiar color de barras y grosor
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setMaximumBarWidth(0.10); // Ajusta el grosor de la barra (0.10 = 10% del espacio)
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // Azul
        renderer.setSeriesPaint(1, new Color(192, 80, 77));  // Rojo
        renderer.setSeriesPaint(2, new Color(155, 187, 89)); // Verde

        // 5️⃣ Mostrar el gráfico en el panel
        panelEst.removeAll();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(panelEst.getWidth(), panelEst.getHeight()));
        panelEst.setLayout(new BorderLayout());
        panelEst.add(chartPanel, BorderLayout.CENTER);
        panelEst.revalidate();
        panelEst.repaint();
    }

    public static void estadisticavictor(Connection conexion, JPanel panelEst, int id, String victor,
            JLabel vict, JLabel kmtl, JLabel relevol, JLabel nmovl, JLabel dominio) {

        // Dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Query principal: cantidad de movimientos por mes
        String sqlMovimientos
                = "SELECT MONTH(movimientos.fecha) AS mes, COUNT(*) AS cantidad_movimientos "
                + "FROM movimientos "
                + "INNER JOIN tripulacion ON movimientos.idtripulacion = tripulacion.idtripulacion "
                + "WHERE tripulacion.idAmbulancia = ? AND tripulacion.relevado = 1 "
                + "GROUP BY MONTH(fecha) ORDER BY mes;";

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlMovimientos)) {
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                int cantidadMov = rs2.getInt("cantidad_movimientos");
                int mes = rs2.getInt("mes");

                // Agregar valor: (valor, serie, categoría)
                dataset.addValue(cantidadMov, "Ambulancia: " + victor, "Mes " + mes);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al contar movimientos de la ambulancia " + id + ": " + ex.getMessage());
        }

        // 3️⃣ Crear gráfico de columnas
        JFreeChart chart = ChartFactory.createBarChart(
                "Movimientos por Mes - Ambulancia " + victor, // título
                "Mes", // eje X
                "Cantidad de Movimientos", // eje Y
                dataset, // datos
                PlotOrientation.VERTICAL, // orientación
                true, // leyenda
                true, // tooltips
                false // URLs
        );

        // 4️⃣ Personalizar apariencia
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.GRAY);

        // Configurar estilo de barras
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setMaximumBarWidth(0.12); // grosor de las barras
        renderer.setBarPainter(new StandardBarPainter()); // evita gradientes raros
        renderer.setSeriesPaint(0, new Color(33, 150, 243)); // Azul moderno

        // Bordes suaves
        plot.setOutlinePaint(Color.GRAY);
        // 5️⃣ Mostrar el gráfico en el panel
        panelEst.removeAll();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(panelEst.getWidth(), panelEst.getHeight()));
        panelEst.setLayout(new BorderLayout());
        panelEst.add(chartPanel, BorderLayout.CENTER);
        panelEst.revalidate();
        panelEst.repaint();

        // =============================
        //  📊 LABELS DE INFORMACIÓN
        // =============================
        // Kilometraje total
        String sqlLabel = "SELECT movimientos.kilometrosdesalida FROM movimientos "
                + "INNER JOIN tripulacion ON movimientos.idtripulacion = tripulacion.idtripulacion "
                + "WHERE tripulacion.idAmbulancia=? ORDER BY idmovimientos DESC LIMIT 1;";

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLabel)) {
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String kms = String.valueOf(rs2.getInt("movimientos.kilometrosdesalida"));
                kmtl.setText("Kilometraje total: " + kms);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error obteniendo kilometraje: " + ex.getMessage());
        }

        // Relevos + Dominio + Marca
        String sqlLabel2 = "SELECT COUNT(tripulacion.idtripulacion) AS cantidad, ambulancia.patente, ambulancia.marca "
                + "FROM tripulacion "
                + "INNER JOIN ambulancia ON tripulacion.idAmbulancia = ambulancia.idAmbulancia "
                + "WHERE tripulacion.idAmbulancia=? AND tripulacion.relevado=1;";

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLabel2)) {
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String cant = String.valueOf(rs2.getInt("cantidad"));
                String pat = rs2.getString("ambulancia.patente");
                String mar = rs2.getString("ambulancia.marca");
                relevol.setText("Relevos: " + cant);
                dominio.setText("Dominio: " + pat);
                vict.setText("Marca: " + mar);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error obteniendo información del vehículo: " + ex.getMessage());
        }

        // Cantidad total de movimientos
        String sqlLabel3 = "SELECT COUNT(movimientos.idMovimientos) AS cantidad "
                + "FROM movimientos "
                + "INNER JOIN tripulacion ON movimientos.idtripulacion = tripulacion.idtripulacion "
                + "WHERE tripulacion.idAmbulancia=? AND tripulacion.relevado=1;";

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLabel3)) {
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String cant = String.valueOf(rs2.getInt("cantidad"));
                nmovl.setText("Nº de Movimientos: " + cant);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al contar movimientos totales: " + ex.getMessage());
        }
    }

    public static void estadisticavictormes(Connection conexion, JPanel panelEst, int id, String victor, int indice, JLabel vict, JLabel kmtl, JLabel relevol, JLabel nmovl, JLabel dominio) {

        // Dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 2️⃣ Recorrer cada ambulancia y contar sus movimientos por mes
        String sqlMovimientos = "SELECT DAY(movimientos.fecha) AS dia, COUNT(*) AS cantidad_movimientos FROM movimientos INNER JOIN tripulacion ON movimientos.idtripulacion = tripulacion.idtripulacion WHERE tripulacion.idAmbulancia = ? AND MONTH(movimientos.fecha) = ? and tripulacion.relevado=1 GROUP BY DAY(movimientos.fecha) ORDER BY dia;";

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlMovimientos)) {
            ps2.setInt(1, id);
            ps2.setInt(2, indice);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                int cantidadMov = rs2.getInt("cantidad_movimientos");
                int dia = rs2.getInt("dia");

                // 🔹 Agregar valor: (valor, serie, categoría)
                dataset.addValue(cantidadMov, "Victor: " + victor, "Dia: " + dia);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al contar movimientos de la ambulancia " + id + ": " + ex.getMessage());
        }

        // 3️⃣ Crear gráfico fuera del bucle
        JFreeChart chart = ChartFactory.createLineChart(
                "Movimientos del mes", // título
                "Día", // eje X
                "Cantidad de Movimientos", // eje Y
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        // 🔹 Cambiar el estilo del punto (círculo)
        Shape punto = new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6);
        renderer.setSeriesShape(0, punto); // 0 = primera serie
        renderer.setSeriesShapesVisible(0, true);

        // 🔹 Colores opcionales
        renderer.setSeriesPaint(0, Color.BLUE);

        // 🔹 Fondo y grilla
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);

        // 4️⃣ Mostrar el gráfico en el panel
        panelEst.removeAll();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(panelEst.getWidth(), panelEst.getHeight()));
        panelEst.setLayout(new BorderLayout());
        panelEst.add(chartPanel, BorderLayout.CENTER);
        panelEst.revalidate();
        panelEst.repaint();

        String sqlLabel = "SELECT movimientos.kilometrosdesalida FROM movimientos inner join tripulacion on movimientos.idtripulacion=tripulacion.idtripulacion where tripulacion.idAmbulancia=? and MONTH(movimientos.fecha)=? ORDER BY idmovimientos DESC LIMIT 1;";
        String sqlLabel2 = "SELECT COUNT(tripulacion.idtripulacion) as cantidad, ambulancia.patente, ambulancia.marca from tripulacion inner join ambulancia on tripulacion.idAmbulancia=ambulancia.idAmbulancia where tripulacion.idAmbulancia=? and MONTH(tripulacion.Fecha)=? and tripulacion.relevado=1;";
        String sqlLabel3 = "SELECT Count(movimientos.idMovimientos) as cantidad from movimientos inner join tripulacion on movimientos.idtripulacion=tripulacion.idtripulacion WHERE tripulacion.idAmbulancia=? and MONTH(movimientos.fecha)=? and tripulacion.relevado=1;";

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLabel)) {
            ps2.setInt(1, id);
            ps2.setInt(2, indice);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String kms = String.valueOf(rs2.getInt("movimientos.kilometrosdesalida"));
                kmtl.setText("Kilometraje total: " + kms);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al contar movimientos de la ambulancia " + id + ": " + ex.getMessage());
        }

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLabel2)) {
            ps2.setInt(1, id);
            ps2.setInt(2, indice);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String cant = String.valueOf(rs2.getInt("cantidad"));
                String pat = rs2.getString("ambulancia.patente");
                String mar = rs2.getString("ambulancia.marca");
                relevol.setText("Relevos: " + cant);
                dominio.setText("Dominio: " + pat);
                vict.setText("Marca: " + mar);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al contar movimientos de la ambulancia " + id + ": " + ex.getMessage());
        }

        try (PreparedStatement ps2 = conexion.prepareStatement(sqlLabel3)) {
            ps2.setInt(1, id);
            ps2.setInt(2, indice);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                String cant = String.valueOf(rs2.getInt("cantidad"));
                nmovl.setText("Nº De Movimientos: " + cant);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al contar movimientos de la ambulancia " + id + ": " + ex.getMessage());
        }

    }
}
