/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Facuymayriver
 */
public class Movimientos {

    static DefaultComboBoxModel<Cliente> model, model2, model3, model4;
    static DefaultComboBoxModel<Victor> victormod;
    static DefaultComboBoxModel<Trip> model5;
    static List<Integer> relevado = new ArrayList<>();
    static List<Integer> relevadont = new ArrayList<>();

    public static class Cliente {

        private final int id;
        private final String nombre;

        public Cliente(int id, String nombre) {
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

    public static class Victor {

        private final int id;
        private final String nombre;

        public Victor(int id, String nombre) {
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

    public static class Trip {

        private final int id;
        private final String nombre;

        public Trip(int id, String nombre) {
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

    public static void jChofer(Connection conexion, JComboBox<Cliente> combo1, JComboBox<Cliente> combo2, JComboBox<Cliente> combo3, JComboBox<Cliente> combo4, int band) {
        String sql = "SELECT e.id_Empleado, CONCAT(e.nombre, ' ', e.apellido) AS cvs_nombre_completo FROM empleado e WHERE e.borrado = 0 AND e.idCargo = 3 AND e.id_Empleado NOT IN (SELECT t.cvs FROM tripulacion t WHERE t.relevado = 0 AND t.borrado = 0);";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            model = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("e.id_Empleado");
                String nombreCompleto = rs.getString("cvs_nombre_completo");
                model.addElement(new Cliente(id, nombreCompleto));
            }

            combo1.setModel(model);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql2 = "SELECT e.id_Empleado, CONCAT(e.nombre, ' ', e.apellido) AS med_nombre_completo FROM empleado e WHERE e.borrado = 0 AND e.idCargo = 12 AND e.id_Empleado NOT IN (SELECT t.medico FROM tripulacion t WHERE t.relevado = 0 AND t.borrado = 0);";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            model2 = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("e.id_Empleado");
                String nombreCompleto = rs.getString("med_nombre_completo");
                model2.addElement(new Cliente(id, nombreCompleto));
            }

            combo2.setModel(model2);
            AutoCompleteDecorator.decorate(combo2);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql3 = "SELECT e.id_Empleado, CONCAT(e.nombre, ' ', e.apellido) AS enf_nombre_completo FROM empleado e WHERE e.borrado = 0 AND e.idCargo = 13 AND e.id_Empleado NOT IN (SELECT t.enfermero FROM tripulacion t WHERE t.relevado = 0 AND t.borrado = 0);";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql3);
            ResultSet rs = ps.executeQuery();
            model3 = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("id_Empleado");
                String nombreCompleto = rs.getString("enf_nombre_completo");
                model3.addElement(new Cliente(id, nombreCompleto));
            }

            combo3.setModel(model3);
            AutoCompleteDecorator.decorate(combo3);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        if (band == 0) {
            String sql4 = "SELECT a.idAmbulancia, a.Victor FROM ambulancia a LEFT JOIN tripulacion t ON a.idAmbulancia = t.idAmbulancia  AND t.relevado = 0 AND t.borrado = 0 WHERE a.borrado = 0 AND t.idAmbulancia IS NULL;";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql4);
                ResultSet rs = ps.executeQuery();
                model4 = new DefaultComboBoxModel<>();
                while (rs.next()) {
                    int id = rs.getInt("a.idAmbulancia");
                    String nombreCompleto = rs.getString("a.victor");
                    model4.addElement(new Cliente(id, nombreCompleto));
                }

                combo4.setModel(model4);
                AutoCompleteDecorator.decorate(combo4);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }
        } else if (band == 1) {
            String sql4 = "SELECT idAmbulancia, victor FROM ambulancia WHERE borrado=0;";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql4);
                ResultSet rs = ps.executeQuery();
                model4 = new DefaultComboBoxModel<>();
                while (rs.next()) {
                    int id = rs.getInt("idAmbulancia");
                    String nombreCompleto = rs.getString("victor");
                    model4.addElement(new Cliente(id, nombreCompleto));
                }

                combo4.setModel(model4);
                AutoCompleteDecorator.decorate(combo4);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }
        }

    }

    public static void jChoferAct(Connection conexion, JComboBox<Cliente> combo1, JComboBox<Cliente> combo2, JComboBox<Cliente> combo3, JComboBox<Cliente> combo4, int band) {
        String sql = "SELECT id_Empleado, nombre, apellido FROM empleado WHERE borrado=0 AND idCargo=3";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            model = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("id_Empleado");
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                model.addElement(new Cliente(id, nombreCompleto));
            }

            combo1.setModel(model);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql2 = "SELECT id_Empleado, nombre, apellido FROM empleado WHERE borrado=0 AND idCargo=12";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            model2 = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("id_Empleado");
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                model2.addElement(new Cliente(id, nombreCompleto));
            }

            combo2.setModel(model2);
            AutoCompleteDecorator.decorate(combo2);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql3 = "SELECT id_Empleado, nombre, apellido FROM empleado WHERE borrado=0 AND idCargo=13";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql3);
            ResultSet rs = ps.executeQuery();
            model3 = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("id_Empleado");
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                model3.addElement(new Cliente(id, nombreCompleto));
            }

            combo3.setModel(model3);
            AutoCompleteDecorator.decorate(combo3);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        if (band == 0) {
            String sql4 = "SELECT a.idAmbulancia, a.Victor FROM ambulancia a LEFT JOIN tripulacion t ON a.idAmbulancia = t.idAmbulancia AND t.relevado = 0 WHERE t.idAmbulancia IS NULL AND a.borrado=0;";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql4);
                ResultSet rs = ps.executeQuery();
                model4 = new DefaultComboBoxModel<>();
                while (rs.next()) {
                    int id = rs.getInt("a.idAmbulancia");
                    String nombreCompleto = rs.getString("a.victor");
                    model4.addElement(new Cliente(id, nombreCompleto));
                }

                combo4.setModel(model4);
                AutoCompleteDecorator.decorate(combo4);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }
        } else if (band == 1) {
            String sql4 = "SELECT idAmbulancia, victor FROM ambulancia WHERE borrado=0;";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql4);
                ResultSet rs = ps.executeQuery();
                model4 = new DefaultComboBoxModel<>();
                while (rs.next()) {
                    int id = rs.getInt("idAmbulancia");
                    String nombreCompleto = rs.getString("victor");
                    model4.addElement(new Cliente(id, nombreCompleto));
                }

                combo4.setModel(model4);
                AutoCompleteDecorator.decorate(combo4);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }
        }

    }

    public static void victorcomb(Connection conexion, JComboBox<Victor> combo1) {
        String sql4 = "SELECT idAmbulancia, victor FROM ambulancia WHERE borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql4);
            ResultSet rs = ps.executeQuery();
            victormod = new DefaultComboBoxModel<>();
            while (rs.next()) {
                int id = rs.getInt("idAmbulancia");
                String nombreCompleto = rs.getString("victor");
                victormod.addElement(new Victor(id, nombreCompleto));
            }

            combo1.setModel(victormod);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void Select(Connection conexion, JComboBox<Trip> combo1, String fecha) {

        String sql = "SELECT t.idtripulacion, CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero, t.Fecha FROM tripulacion t JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado WHERE relevado=0 and t.borrado=0 and e1.borrado=0 and e2.borrado=0 and e3.borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DefaultComboBoxModel<Trip> trip = new DefaultComboBoxModel();
            trip.addElement(new Trip(0, "Opciones"));
            while (rs.next()) {
                String fechatr = rs.getString("t.Fecha");
                int id = rs.getInt("t.idtripulacion");
                String tripulacion = rs.getString("cvs") + "-" + rs.getString("medico") + "-" + rs.getString("enfermero");

                if (fechatr.equalsIgnoreCase(fecha)) {
                    trip.addElement(new Trip(id, tripulacion));
                } else {
                    String tripulacionsin = (tripulacion + " (¡Sin Relevar!) ");
                    trip.addElement(new Trip(id, tripulacionsin));
                }
            }

            combo1.setModel(trip);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

    }

    public static void Carga(Connection conexion, int id, int id2, int id3, int id4, String fecha, int iduser) throws SQLException {
        String sql2 = "SELECT idtripulacion from Tripulacion WHERE idAmbulancia=? AND relevado=0 AND Fecha AND DATE(Fecha) = CURDATE();";
        PreparedStatement stm = conexion.prepareStatement(sql2);
        stm.setInt(1, id4);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "Ya hay una tripulación con este victor!");
        } else {
            String sql = "INSERT into tripulacion (cvs, medico, enfermero, idAmbulancia, Fecha, relevado, borrado) VALUES (?,?,?,?,?,0,0) ";
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, id);
            ps.setInt(2, id2);
            ps.setInt(3, id3);
            ps.setInt(4, id4);
            ps.setString(5, fecha);
            try {
                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    try (java.sql.ResultSet rs2 = ps.getGeneratedKeys()) {
                        if (rs2.next()) {
                            int nuevoIdTrip = rs2.getInt(1);
                            PreparedStatement stm4 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
                            stm4.setString(1, "NUEVA_TRIPULACION");
                            stm4.setInt(2, nuevoIdTrip);
                            stm4.setInt(3, iduser);
                            try {
                                stm4.execute();
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "ERROR12" + e);
                            }
                        } else {
                            System.out.println("⚠️ Insertado, pero no se pudo obtener el ID.");
                        }
                    }
                } else {
                    System.out.println("❌ Error: No se insertó ninguna fila.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR12");
            }
        }

    }

    public static void ActualizarTrip(Connection conexion, int id, int id2, int id3, int id4, int id5, int iduser) throws SQLException {
        String sql2 = "Select idAmbulancia, idtripulacion from tripulacion WHERE idAmbulancia=? and relevado=0 and borrado=0;";
        PreparedStatement stm = conexion.prepareStatement(sql2);
        stm.setInt(1, id4);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "Se hara el cambio del vehiculo, ya que este esta activo en otra tripulación");
            int idtrip = rs.getInt("idtripulacion");
            String sql = "UPDATE tripulacion SET cvs=?, medico=?, enfermero=?, idAmbulancia=? WHERE idtripulacion=?";
            String sql3 = "UPDATE tripulacion SET idAmbulancia=? WHERE idtripulacion = ?";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, id2);
                ps.setInt(3, id3);
                ps.setInt(4, id4);
                ps.setInt(5, id5);
                ps.execute();
                PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
                stm9.setString(1, "ACTUALIZACIÓN_TRIPULACION");
                stm9.setInt(2, id5);
                stm9.setInt(3, iduser);
                try {
                    stm9.execute();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + e);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

            try {
                PreparedStatement ps = conexion.prepareStatement(sql3);
                ps.setInt(1, amb);
                ps.setInt(2, idtrip);
                ps.execute();
                PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
                stm9.setString(1, "ACTUALIZACIÓN_TRIPULACION");
                stm9.setInt(2, idtrip);
                stm9.setInt(3, iduser);
                try {
                    stm9.execute();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + e);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            String sql = "UPDATE tripulacion SET cvs=?, medico=?, enfermero=?, idAmbulancia=? WHERE idtripulacion=?";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setInt(2, id2);
                ps.setInt(3, id3);
                ps.setInt(4, id4);
                ps.setInt(5, id5);
                ps.execute();
                PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
                stm9.setString(1, "ACTUALIZACIÓN_TRIPULACION");
                stm9.setInt(2, id5);
                stm9.setInt(3, iduser);
                try {
                    stm9.execute();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + e);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public static void CargaVic(Connection conexion, String victor, String modelo, String patente, String marca, String matafuego) throws SQLException {
        int nuevoId = 0;
        String sql = "INSERT INTO matafuego (NumeroSerie) VALUES (?)";
        PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, matafuego);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            nuevoId = rs.getInt(1);
        }

        String sql2 = "INSERT into ambulancia (patente, victor, modelo, marca, idMatafuego) VALUES (?,?,?,?,?) ";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ps2.setString(1, patente);
            ps2.setString(2, victor);
            ps2.setString(3, modelo);
            ps2.setString(4, marca);
            ps2.setInt(5, nuevoId);
            ps2.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }
    static int idmat;

    public static void EditarVic(Connection conexion, JTextField VictorT, JTextField Modelo, JTextField Patente, JTextField Marca, JTextField matafuego, int id) throws SQLException {
        PreparedStatement stm = conexion.prepareStatement("SELECT Victor, Modelo, Marca, matafuego.NumeroSerie, Patente, matafuego.idMatafuego from ambulancia inner join matafuego on ambulancia.idMatafuego=matafuego.idMatafuego WHERE ambulancia.borrado=0 and ambulancia.idAmbulancia=?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            String Vic = rs.getString("Victor");
            String Mod = rs.getString("Modelo");
            String Nser = rs.getString("matafuego.NumeroSerie");
            String Mar = rs.getString("Marca");
            String Pat = rs.getString("Patente");
            idmat = rs.getInt("matafuego.idMatafuego");
            VictorT.setText(Vic);
            Modelo.setText(Mod);
            Patente.setText(Pat);
            Marca.setText(Mar);
            matafuego.setText(Nser);
        }
    }

    public static void actVic(Connection conexion, String victor, String modelo, String patente, String marca, String matafuego, int id) throws SQLException {
        String sql = "UPDATE ambulancia SET Victor=?, Modelo=?, Marca=?, Patente=? WHERE idAmbulancia=?";
        String sql2 = "UPDATE matafuego set NumeroSerie=? WHERE idMatafuego=?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, victor);
            ps.setString(2, modelo);
            ps.setString(3, patente);
            ps.setString(4, marca);
            ps.setInt(5, id);
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        try {
            PreparedStatement ps = conexion.prepareStatement(sql2);
            ps.setString(1, matafuego);
            ps.setInt(2, idmat);
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static void MostrarMov(Connection conexion, DefaultTableModel modelo, String fechaActual, int id) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("SELECT movimientos.idMovimientos, movimientos.salida, movimientos.llegada, movimientos.kilometrosdesalida, movimientos.destino, movimientos.num_servicio FROM movimientos WHERE idtripulacion=? AND borrado=0");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[6];
            fila[0] = rs.getString("movimientos.idMovimientos");
            fila[1] = rs.getString("movimientos.salida");
            fila[2] = rs.getString("movimientos.llegada");
            fila[3] = rs.getString("movimientos.kilometrosdesalida");
            fila[4] = rs.getString("movimientos.destino");
            fila[5] = rs.getString("movimientos.num_servicio");
            modelo.addRow(fila);
        }
    }

    public static void movimientos(Connection conexion, int cod, String Salida, String Llegada, String Km, String Destino, String NumServicio, int band, int id, String fecha, int iduser) throws SQLException {
        if (band == 0) {
            String sql = "INSERT INTO movimientos (idtripulacion, salida, llegada, kilometrosdesalida, destino, num_servicio, fecha, borrado) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
            try {
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, Salida);
                ps.setString(3, Llegada);
                ps.setString(4, Km);
                ps.setString(5, Destino);
                ps.setString(6, NumServicio);
                ps.setString(7, fecha);
                ps.execute();
                PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
                stm9.setString(1, "NUEVO_MOVIMIENTO");
                stm9.setInt(2, id);
                stm9.setInt(3, iduser);
                try {
                    stm9.execute();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR: " + e);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else if (band == 1) {
            PreparedStatement stm2 = conexion.prepareStatement("Select salida, llegada, kilometrosdesalida, destino, num_servicio from movimientos where idMovimientos=?");
            stm2.setInt(1, cod);
            ResultSet rs = stm2.executeQuery();

            if (rs.next()) {
                String sa = rs.getString("salida");
                String ll = rs.getString("llegada");
                String k = rs.getString("kilometrosdesalida");
                String De = rs.getString("destino");
                String ns = rs.getString("num_servicio");
                if (!Salida.equals(sa) || !Llegada.equals(ll) || !Km.equals(k) || !Destino.equals(De) || !NumServicio.equals(ns)) {

                    PreparedStatement stm = conexion.prepareStatement("UPDATE movimientos SET salida = ?, llegada = ?, kilometrosdesalida = ?, destino = ?, num_servicio= ? WHERE idMovimientos = ?");
                    stm.setString(1, Salida);
                    stm.setString(2, Llegada);
                    stm.setString(3, Km);
                    stm.setString(4, Destino);
                    stm.setString(5, NumServicio);
                    stm.setInt(6, cod);

                    try {
                        stm.executeUpdate();
                        PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
                        stm9.setString(1, "ACTUALIZACIÓN_MOVIMIENTO_Nº" + cod);
                        stm9.setInt(2, id);
                        stm9.setInt(3, iduser);
                        try {
                            stm9.execute();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "ERROR: " + e);
                        }

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "ERROR12");
                    }
                }
            }
        }
    }

    public static void Datos(Connection conexion, JLabel chofer, JLabel medico, JLabel enfermero, JLabel victor, JLabel patente, JLabel modelo, JLabel marca, int id, JLabel cantidad, String fecha) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("SELECT CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero FROM tripulacion t JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado where idtripulacion=?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            chofer.setText(rs.getString("cvs"));
            medico.setText(rs.getString("medico"));
            enfermero.setText(rs.getString("enfermero"));
        }

        String sql = "SELECT ambulancia.patente, ambulancia.Victor, ambulancia.modelo, ambulancia.marca FROM tripulacion inner join ambulancia on tripulacion.idAmbulancia=ambulancia.idAmbulancia WHERE idtripulacion=?";
        try {
            PreparedStatement stm2 = conexion.prepareStatement(sql);
            stm2.setInt(1, id);
            ResultSet rs2 = stm2.executeQuery();

            if (rs2.next()) {
                patente.setText(rs2.getString("ambulancia.patente"));
                victor.setText(rs2.getString("ambulancia.victor"));
                modelo.setText(rs2.getString("ambulancia.modelo"));
                marca.setText(rs2.getString("ambulancia.marca"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        PreparedStatement stm3 = conexion.prepareStatement("SELECT COUNT(idMovimientos) FROM samerealpro.movimientos WHERE idtripulacion=? AND borrado=0");
        stm3.setInt(1, id);
        ResultSet rs3 = stm3.executeQuery();
        if (rs3.next()) {
            cantidad.setText(rs3.getString("COUNT(idMovimientos)"));
        }
    }

    public static void Contador(Connection conexion, int id, JLabel cantidad, String fecha) throws SQLException {

        PreparedStatement stm3 = conexion.prepareStatement("SELECT COUNT(idMovimientos) FROM samerealpro.movimientos WHERE idtripulacion=? AND fecha=? AND borrado=0");
        stm3.setInt(1, id);
        stm3.setString(2, fecha);
        ResultSet rs3 = stm3.executeQuery();
        if (rs3.next()) {
            cantidad.setText(rs3.getString("COUNT(idMovimientos)"));
        }
    }

    public static void EliminarMovimiento(Connection conexion, int Codigo, int iduser, int id) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE movimientos SET borrado= ? WHERE idMovimientos = ?");
        stm.setInt(1, 1);
        stm.setInt(2, Codigo);

        try {
            stm.executeUpdate();
            PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria_movimientos (evento, id_tripulacion, id_usuario) VALUES (?, ?, ?)");
            stm9.setString(1, "BORRADO_MOVIMIENTO_Nº" + Codigo);
            stm9.setInt(2, id);
            stm9.setInt(3, iduser);
            try {
                stm9.execute();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }
    static int amb;

    public static void EditarTrip(Connection conexion, int id, JComboBox<Cliente> combo1, JComboBox<Cliente> combo2, JComboBox<Cliente> combo3, JComboBox<Cliente> combo4) {
        int cvs, med, enf;
        String sql = "SELECT cvs, medico, enfermero, idAmbulancia FROM tripulacion where idtripulacion=?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cvs = rs.getInt("cvs");
                med = rs.getInt("medico");
                enf = rs.getInt("enfermero");
                amb = rs.getInt("idAmbulancia");
                int i = 0;
                while (i < model.getSize()) {
                    Object value = model.getElementAt(i);
                    if (value instanceof Cliente) {
                        Cliente dat = (Cliente) value;
                        int idcvs = dat.getId();
                        if (idcvs == cvs) { // ejemplo comparar ID
                            combo1.setSelectedIndex(i); // <-- selecciona por índice
                            break; // cortamos el bucle si encontramos
                        }
                        i++;
                    }
                }
                i = 0;
                while (i < model2.getSize()) {
                    Object value = model2.getElementAt(i);
                    if (value instanceof Cliente) {
                        Cliente dat = (Cliente) value;
                        int idmedico = dat.getId();
                        if (idmedico == med) {
                            combo2.setSelectedIndex(i);
                            break;
                        }
                        i++;
                    }
                }

                i = 0;
                while (i < model3.getSize()) {
                    Object value = model3.getElementAt(i);
                    if (value instanceof Cliente) {
                        Cliente dat = (Cliente) value;
                        int idenfermero = dat.getId();
                        if (idenfermero == enf) {
                            combo3.setSelectedIndex(i);
                            break;
                        }
                        i++;
                    }
                }

                i = 0;
                while (i < model4.getSize()) {
                    Object value = model4.getElementAt(i);
                    if (value instanceof Cliente) {
                        Cliente dat = (Cliente) value;
                        int idambu = dat.getId();
                        if (idambu == amb) {
                            combo4.setSelectedIndex(i);
                            break;
                        }
                        i++;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void abrirPDF(String rutaArchivo) {
        try {
            File file = new File(rutaArchivo);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(null, "El archivo no existe en: " + rutaArchivo);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el PDF: " + e.getMessage());
        }
    }

    //Historial
    public static void MostrarHist(Connection conexion, DefaultTableModel modelo, JTable tabla) throws SQLException {
        //BASE

        PreparedStatement stm = conexion.prepareStatement("SELECT t.idtripulacion, CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero, a.victor, t.fecharelevado, COUNT(m.idMovimientos) AS cantidad_movimientos FROM tripulacion t JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado INNER JOIN ambulancia a ON t.idAmbulancia = a.idAmbulancia LEFT JOIN movimientos m ON t.idtripulacion = m.idtripulacion AND m.borrado = 0 WHERE e1.borrado = 0 AND e2.borrado = 0 AND e3.borrado = 0 AND t.borrado = 0 GROUP BY t.idtripulacion, cvs, medico, enfermero, a.victor, t.fecharelevado ORDER BY fecharelevado;");
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            Object[] fila = new Object[5];
            fila[0] = rs.getInt("t.idtripulacion");
            fila[1] = rs.getString("cvs") + "-" + rs.getString("medico") + "-" + rs.getString("enfermero");
            fila[2] = rs.getString("a.victor");
            LocalDate fecha = rs.getObject("t.fecharelevado", LocalDate.class);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fechaActual;
            if (fecha != null) {
                fechaActual = fecha.format(formato);
            } else {
                fechaActual = "S/F";
            }
            fila[3] = fechaActual;
            fila[4] = rs.getString("cantidad_movimientos");
            modelo.addRow(fila);
        }

        // 🚩 Después de cargar, aplicamos el renderer UNA sola vez
        DefaultTableCellRenderer renderizador;
        renderizador = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String idfiado = table.getValueAt(row, 0).toString();

                int idF = Integer.parseInt(idfiado);

                try {
                    PreparedStatement stm2 = conexion.prepareStatement("SELECT relevado FROM tripulacion WHERE idtripulacion=?");
                    stm2.setInt(1, idF);
                    ResultSet rs2 = stm2.executeQuery();
                    if (rs2.next()) {
                        int relevado = rs2.getInt("relevado");
                        if (relevado == 0) {
                            c.setBackground(new Color(255, 153, 153));
                            c.setForeground(Color.BLACK);
                        } else if (relevado == 1) {
                            c.setBackground(new Color(144, 238, 144));
                            c.setForeground(Color.BLACK);
                        }
                    }
                } catch (NumberFormatException e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                } catch (SQLException ex) {

                }

                // Mantener selección visible
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                return c;
            }

        };
        // Aplicamos el render a todas las columnas
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
    }

    public static void tablaTrip(Connection conexion, DefaultTableModel modelo) {

        String sql = "SELECT t.idtripulacion, CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero, ambulancia.victor FROM tripulacion t inner join ambulancia on t.idAmbulancia=ambulancia.idAmbulancia JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado WHERE relevado=0 and t.borrado=0 and e1.borrado=0 and e2.borrado=0 and e3.borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("t.idtripulacion");
                fila[1] = rs.getString("cvs") + "-" + rs.getString("medico") + "-" + rs.getString("enfermero");
                fila[2] = rs.getString("ambulancia.victor");
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

    }

    public static void tablaVic(Connection conexion, DefaultTableModel modelo) {

        String sql = "SELECT idAmbulancia, victor, Patente from Ambulancia WHERE borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("idAmbulancia");
                fila[1] = rs.getString("victor");
                fila[2] = rs.getString("Patente");
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

    }

    public static void EliminarTrip(Connection conexion, int Codigo) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE tripulacion SET borrado= 1 WHERE idtripulacion = ?");
        stm.setInt(1, Codigo);

        try {
            stm.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }

    public static int ValidacionVic(Connection conexion, int Codigo) throws SQLException {
        int resultado = 0;
        PreparedStatement stm = conexion.prepareStatement("Select idtripulacion from tripulacion where idAmbulancia=? and relevado=0");
        stm.setInt(1, Codigo);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseás Eliminar todos la tripulacion con este victor?",
                    "Confirmar acción",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.OK_OPTION) {
                PreparedStatement stm2 = conexion.prepareStatement("UPDATE tripulacion SET borrado= 1 WHERE idAmbulancia = ?");
                stm2.setInt(1, Codigo);
                try {
                    stm2.executeUpdate();
                    resultado = 1;
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR12");
                }
            } else if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                // El usuario eligió "Cancelar" o cerró la ventana
                System.out.println("Cancelado");
                resultado = 2;
            }
        } else {
            resultado = 0;
        }
        return resultado;
    }

    public static void EliminarVic(Connection conexion, int Codigo) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE ambulancia SET borrado= 1 WHERE idAmbulancia = ?");
        stm.setInt(1, Codigo);

        try {
            stm.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }
}
