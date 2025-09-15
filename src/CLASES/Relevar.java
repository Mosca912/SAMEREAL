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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Facuymayriver
 */
public class Relevar {

    /*

    
    SELECT *
FROM movimientos
WHERE idtripulacion IN (
    6,
    (SELECT MAX(idtripulacion) FROM movimientos WHERE idtripulacion < 6)
);
    
    SELECT 
    idtripulacion,
    MAX(kilometrosdesalida), 
    MIN(kilometrosdesalida)
FROM movimientos
WHERE idtripulacion = 4
GROUP BY idtripulacion;
    
    SELECT 8 AS id_actual, (SELECT MAX(idtripulacion) FROM movimientos WHERE idtripulacion < 8) AS id_anterior;
    
    ;
     */
    public static void Relevo(Connection conexion, JLabel victor, JLabel anterior, JLabel saliente, JLabel entrante, JLabel kmi, JLabel kmf, JLabel turno, int id) {
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

    }
}
