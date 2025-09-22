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
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Facuymayriver
 */
public class Movimientos {

    static DefaultComboBoxModel<Cliente> model, model2, model3, model4;
    static DefaultComboBoxModel<Trip> model5;

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

    public static void jChofer(Connection conexion, JComboBox<Cliente> combo1, JComboBox<Cliente> combo2, JComboBox<Cliente> combo3, JComboBox<Cliente> combo4) {
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

        String sql4 = "SELECT idAmbulancia, victor FROM ambulancia";
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

    public static void Select(Connection conexion, JComboBox<Trip> combo1, String fecha) {

        String sql = "SELECT t.idtripulacion, CONCAT(e1.nombre, ' ', e1.apellido) AS cvs, CONCAT(e2.nombre, ' ', e2.apellido) AS medico, CONCAT(e3.nombre, ' ', e3.apellido) AS enfermero, t.Fecha FROM tripulacion t JOIN empleado e1 ON t.cvs = e1.id_Empleado JOIN empleado e2 ON t.medico = e2.id_Empleado JOIN empleado e3 ON t.enfermero = e3.id_Empleado WHERE relevado=0 and e1.borrado=0 and e2.borrado=0 and e3.borrado=0;";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DefaultComboBoxModel<Trip> trip = new DefaultComboBoxModel();
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

    public static void Carga(Connection conexion, int id, int id2, int id3, int id4, String fecha) {

        String sql = "INSERT into tripulacion (cvs, medico, enfermero, idAmbulancia, Fecha, relevado) VALUES (?,?,?,?,?,0) ";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, id2);
            ps.setInt(3, id3);
            ps.setInt(4, id4);
            ps.setString(5, fecha);
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }

    }

    public static void ActualizarTrip(Connection conexion, int id, int id2, int id3, int id4, int id5) {

        String sql = "UPDATE tripulacion SET cvs=?, medico=?, enfermero=?, idAmbulancia=? WHERE idtripulacion=?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, id2);
            ps.setInt(3, id3);
            ps.setInt(4, id4);
            ps.setInt(5, id5);
            ps.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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

    public static void movimientos(Connection conexion, int cod, String Salida, String Llegada, String Km, String Destino, String NumServicio, int band, int id, String fecha) throws SQLException {
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

        PreparedStatement stm3 = conexion.prepareStatement("SELECT COUNT(idMovimientos) FROM samerealpro.movimientos WHERE idtripulacion=? AND fecha=? AND borrado=0");
        stm3.setInt(1, id);
        stm3.setString(2, fecha);
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

    public static void EliminarMovimiento(Connection conexion, int Codigo) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE movimientos SET borrado= ? WHERE idMovimientos = ?");
        stm.setInt(1, 1);
        stm.setInt(2, Codigo);

        try {
            stm.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }

    public static void EditarTrip(Connection conexion, int id, JComboBox<Cliente> combo1, JComboBox<Cliente> combo2, JComboBox<Cliente> combo3, JComboBox<Cliente> combo4) {
        int cvs, med, enf, amb;
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
}
