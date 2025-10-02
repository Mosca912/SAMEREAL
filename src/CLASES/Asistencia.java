package CLASES;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class Asistencia {

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

    public static void jCombo(Connection conexion, JComboBox<String> combo1, int veri, String area) {

        int a = 0;
        int car = 0;
        if (veri == 0) {
            String sql = "SELECT area FROM area ORDER BY idArea";

            try {
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String ar = rs.getString("area");
                    combo1.addItem(ar);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }
        }

        if (veri == 1) {
            String sql2 = "SELECT idArea FROM area WHERE area=?";

            try {

                PreparedStatement stm3 = conexion.prepareStatement(sql2);
                stm3.setString(1, area);
                ResultSet rs2 = stm3.executeQuery();

                if (rs2.next()) {
                    a = rs2.getInt("idArea");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }

            String sql3 = "SELECT idCargo FROM cargo WHERE idArea=?";

            try {

                PreparedStatement stm2 = conexion.prepareStatement(sql3);
                stm2.setInt(1, a);
                ResultSet rs2 = stm2.executeQuery();

                if (rs2.next()) {
                    car = rs2.getInt("idCargo");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }

            String sql = "SELECT apellido, dni FROM empleado WHERE idCargo=? and borrado=0";

            try {
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, car);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String ap = rs.getString("apellido");
                    String dni = rs.getString("dni");
                    String emp = (ap + "-" + dni);
                    combo1.addItem(emp);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
            }

        }
    }
    static String docu;

    public static void Datos(Connection conexion, String dni, JLabel nya, JLabel cya, JLabel d) throws SQLException {
        docu = dni;
        PreparedStatement stm = conexion.prepareStatement("SELECT empleado.nombre, empleado.apellido, cargo.cargo, area.area FROM empleado inner join cargo on empleado.idCargo=cargo.idCargo inner join area on cargo.idArea=area.idArea WHERE empleado.dni=?");
        stm.setString(1, dni);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            String nom = rs.getString("empleado.nombre");
            String ap = rs.getString("empleado.apellido");
            String car = rs.getString("cargo.cargo");
            String ar = rs.getString("area.area");
            String nomap = ("Empleado: " + nom + " " + ap);
            String carar = ("Cargo: " + car + " - Area: " + ar);
            String doc = ("Documento: " + dni);

            nya.setText(nomap);
            cya.setText(carar);
            d.setText(doc);
        }
    }

    public static void Asistencia(Connection conexion) throws SQLException {
        int id = 0;
        int ida = 0;
        PreparedStatement stm = conexion.prepareStatement("SELECT id_Empleado from empleado where dni=?");
        stm.setString(1, docu);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            id = rs.getInt("id_Empleado");
        }

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

}
