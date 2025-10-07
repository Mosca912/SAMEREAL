package CLASES;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Asistencia {
    
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

    public static void Asistencia(Connection conexion, int id) throws SQLException {
        int ida=0;
        PreparedStatement stm5 = conexion.prepareStatement("SELECT idAsistencia from asistencia where id_Empleado=? and activo=1");
        stm5.setInt(1, id);
        ResultSet rs5 = stm5.executeQuery();

        if (rs5.next()) {
            JOptionPane.showMessageDialog(null, "YA ESTA ACTIVO EL EMPLEADO");

        } else {

            PreparedStatement stm2 = conexion.prepareStatement("INSERT INTO asistencia (id_Empleado, idBase, activo) values (?,2,1)");
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
            JOptionPane.showMessageDialog(null, "Carga completa");
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
