/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Empleados {

    public static void MostrarEmpleados(Connection conexion, DefaultTableModel modelo) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("SELECT empleado.id_Empleado, empleado.nombre, empleado.apellido, empleado.dni, empleado.telefono, cargo.Cargo from empleado inner join cargo on empleado.idCargo=cargo.idCargo where empleado.borrado=0");
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[6];
            fila[0] = rs.getString("empleado.id_Empleado");
            fila[1] = rs.getString("empleado.nombre");
            fila[2] = rs.getString("empleado.apellido");
            fila[3] = rs.getString("empleado.dni");
            fila[4] = rs.getString("empleado.telefono");
            fila[5] = rs.getString("cargo.Cargo");
            modelo.addRow(fila);
        }
    }

    public static void MostrarCargo(Connection conexion, DefaultTableModel modelo) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("SELECT cargo.idCargo, cargo.Cargo, area.area from cargo inner join area on cargo.idArea=area.idArea where cargo.borrado=0;");
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            Object[] fila = new Object[4];
            fila[0] = rs.getString("cargo.idCargo");
            fila[1] = rs.getString("cargo.Cargo");
            fila[2] = rs.getString("area.area");
            modelo.addRow(fila);
        }
    }

    public static void jCombo(Connection conexion, JComboBox<String> combo1, JComboBox<String> combo2) {

        String sql = "SELECT GrupoSanguineo FROM gruposanguineo ORDER BY idGrupoSanguineo";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String gs = rs.getString("GrupoSanguineo");
                combo1.addItem(gs);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        String sql2 = "SELECT Cargo FROM Cargo ORDER BY idCargo";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql2);
            ResultSet rs2 = ps.executeQuery();

            while (rs2.next()) {
                String cargo = rs2.getString("Cargo");
                combo2.addItem(cargo);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void jCombo2(Connection conexion, JComboBox<String> combo3) {

        String sql3 = "SELECT area FROM area ORDER BY idArea";

        try {
            PreparedStatement ps3 = conexion.prepareStatement(sql3);
            ResultSet rs3 = ps3.executeQuery();

            while (rs3.next()) {
                String area = rs3.getString("area");
                combo3.addItem(area);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void ActjCombo(Connection conexion, JComboBox<String> combo1) {

        String sql2 = "SELECT Cargo FROM Cargo ORDER BY idCargo";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql2);
            ResultSet rs2 = ps.executeQuery();

            while (rs2.next()) {
                String cargo = rs2.getString("Cargo");
                combo1.addItem(cargo);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

    }

    public static void AgregarEmpleados(Connection conexion, String Nombre, String Apellido, String DNI, String Domicilio, String Email, String Telefono, String Fecha, String Grupo, String Cargo, int iduser) throws SQLException {

        int gs = 0;
        int car = 0;

        PreparedStatement stm3 = conexion.prepareStatement("SELECT idGrupoSanguineo from gruposanguineo where GrupoSanguineo = ?");
        stm3.setString(1, Grupo);
        ResultSet rs2 = stm3.executeQuery();
        if (rs2.next()) {
            gs = rs2.getInt("idGrupoSanguineo");
        }

        PreparedStatement stm2 = conexion.prepareStatement("SELECT idCargo from cargo where Cargo = ?");
        stm2.setString(1, Cargo);
        ResultSet rs3 = stm2.executeQuery();
        if (rs3.next()) {
            car = rs3.getInt("idCargo");
        }

        PreparedStatement stm = conexion.prepareStatement(
                "INSERT INTO empleado (nombre, apellido, dni, domicilio, email, telefono, fecha_nac, borrado, idGrupoSanguineo, idCargo, idUsuario) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                // Usa la constante RETURN_GENERATED_KEYS
                Statement.RETURN_GENERATED_KEYS
        );
        stm.setString(1, Nombre);
        stm.setString(2, Apellido);
        stm.setString(3, DNI);
        stm.setString(4, Domicilio);
        stm.setString(5, Email);
        stm.setString(6, Telefono);
        stm.setString(7, Fecha);
        stm.setInt(8, 0);
        stm.setInt(9, gs);
        stm.setInt(10, car);
        stm.setInt(11, iduser);
        int nuevoId = 0;
        try {
            int filasAfectadas = stm.executeUpdate();
            if (filasAfectadas > 0) {
                try (java.sql.ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        int nuevoIdEmpleado = rs.getInt(1);
                        PreparedStatement stm4 = conexion.prepareStatement("INSERT INTO auditoria_empleado (evento, id_empleado, id_usuario) VALUES (?, ?, ?)");
                        stm4.setString(1, "NUEVO_USUARIO");
                        stm4.setInt(2, nuevoIdEmpleado);
                        stm4.setInt(3, iduser);
                        try {
                            stm4.execute();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "ERROR12" + e);
                        }
                    } else {
                        System.out.println("⚠️ Insertado, pero no se pudo obtener el ID.");
                    }
                }
            } else {
                System.out.println("❌ Error: No se insertó ninguna fila.");
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la inserción: " + e.getMessage());
        }
    }

    public static void EliminarEmpleados(Connection conexion, int Codigo, int borrado, int iduser) throws SQLException {

        boolean autoCommit = conexion.getAutoCommit();
        conexion.setAutoCommit(false);

        String sqlUpdate = "UPDATE empleado SET Borrado = ? WHERE id_Empleado = ?";
        String sqlAuditoria = "INSERT INTO auditoria_empleado (evento, id_empleado, id_usuario) VALUES (?, ?, ?)";

        try (PreparedStatement stm = conexion.prepareStatement(sqlUpdate);
                PreparedStatement psAudit = conexion.prepareStatement(sqlAuditoria)) {
            stm.setInt(1, borrado);
            stm.setInt(2, Codigo);
            int filasActualizadas = stm.executeUpdate();

            if (filasActualizadas > 0) {

                psAudit.setString(1, "BORRADO_LOGICO");
                psAudit.setInt(2, Codigo);
                psAudit.setInt(3, iduser);
                psAudit.executeUpdate();

                // 3. Confirmar la Transacción solo si AMBAS operaciones fueron exitosas
                conexion.commit();
                JOptionPane.showMessageDialog(null, "✅ Borrado lógico y auditoría registrados con éxito.");
            } else {
                // Si no se actualizó ninguna fila (el empleado no existía), hacemos rollback y notificamos.
                conexion.rollback();
                JOptionPane.showMessageDialog(null, "⚠️ No se encontró al empleado para el borrado.");
            }

        } catch (SQLException e) {
            // 4. Si algo falla (el UPDATE o el INSERT), deshacer todo
            conexion.rollback();
            JOptionPane.showMessageDialog(null, "❌ Error al procesar la operación. Transacción deshecha: " + e.getMessage());
            throw e;

        } finally {
            conexion.setAutoCommit(autoCommit);
        }
    }

    public static void EliminarCargo(Connection conexion, int Codigo, int borrado) throws SQLException {

        PreparedStatement stm = conexion.prepareStatement("UPDATE cargo SET Borrado= ? WHERE idCargo = ?");
        stm.setInt(1, borrado);
        stm.setInt(2, Codigo);

        try {
            stm.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }

    public static void AgregarCargo(Connection conexion, String Cargo, String area, int Borrado) throws SQLException {

        int ar = 0;

        PreparedStatement stm3 = conexion.prepareStatement("SELECT idArea from area where area = ?");
        stm3.setString(1, area);
        ResultSet rs = stm3.executeQuery();
        if (rs.next()) {
            ar = rs.getInt("idArea");
        }

        PreparedStatement stm = conexion.prepareStatement("INSERT INTO cargo (Cargo, idArea, borrado) VALUES (?,?,0)");
        stm.setString(1, Cargo);
        stm.setInt(2, ar);

        try {
            stm.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12");
        }
    }

    public static void SacarEmpleados(Connection conexion, int Codigo, JTextField nombre, JTextField apellido, JTextField dni, JTextField domicilio, JTextField email, JTextField telefono, JDateChooser fecha, JComboBox<String> combo1, JComboBox<String> combo2) throws SQLException {
        String nom;
        String ap;
        String doc;
        String dom;
        String em;
        String tel;
        int gs;
        String car;
        String valor;
        Date fe;

        PreparedStatement stm = conexion.prepareStatement("SELECT empleado.nombre, empleado.apellido, empleado.dni, empleado.domicilio, empleado.email, empleado.telefono, empleado.fecha_nac, empleado.idGrupoSanguineo, cargo.Cargo from empleado inner join cargo on empleado.idCargo=cargo.idCargo WHERE id_Empleado=?");
        stm.setInt(1, Codigo);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            nom = rs.getString("empleado.nombre");
            ap = rs.getString("empleado.apellido");
            doc = rs.getString("empleado.dni");
            dom = rs.getString("empleado.domicilio");
            em = rs.getString("empleado.email");
            tel = rs.getString("empleado.telefono");
            fe = rs.getDate("empleado.fecha_nac");
            gs = rs.getInt("empleado.idGrupoSanguineo");
            car = rs.getString("cargo.Cargo");

            nombre.setText(nom);
            apellido.setText(ap);
            dni.setText(doc);
            domicilio.setText(dom);
            email.setText(em);
            telefono.setText(tel);
            fecha.setDate(fe);
            combo1.setSelectedIndex(gs);

            for (int i = 1; i < combo2.getItemCount(); i++) {
                valor = combo2.getItemAt(i);
                if (valor.equals(car)) {
                    combo2.setSelectedIndex(i);
                    break;
                }
            }
        }
        try {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12" + e);
        }
    }

    public static void ModificarEmpleados(Connection conexion, int id, String Nombre, String Apellido, String DNI, String Domicilio, String Email, String Telefono, String Fecha, String Grupo, String Cargo, int iduser) throws SQLException {

        int gs = 0;
        int car = 0;

        PreparedStatement stm3 = conexion.prepareStatement("SELECT idGrupoSanguineo from gruposanguineo where GrupoSanguineo = ?");
        stm3.setString(1, Grupo);
        ResultSet rs2 = stm3.executeQuery();
        if (rs2.next()) {
            gs = rs2.getInt("idGrupoSanguineo");
        }

        PreparedStatement stm2 = conexion.prepareStatement("SELECT idCargo from cargo where Cargo = ?");
        stm2.setString(1, Cargo);
        ResultSet rs3 = stm2.executeQuery();
        if (rs3.next()) {
            car = rs3.getInt("idCargo");
        }

        PreparedStatement stm = conexion.prepareStatement("UPDATE empleado SET nombre=?, apellido=?, dni=?, domicilio=?, email=?, telefono=?, fecha_nac=?, idGrupoSanguineo=?, idCargo=? WHERE id_Empleado = ?");
        stm.setString(1, Nombre);
        stm.setString(2, Apellido);
        stm.setString(3, DNI);
        stm.setString(4, Domicilio);
        stm.setString(5, Email);
        stm.setString(6, Telefono);
        stm.setString(7, Fecha);
        stm.setInt(8, gs);
        stm.setInt(9, car);
        stm.setInt(10, id);

        try {
            stm.execute();
            PreparedStatement stm4 = conexion.prepareStatement("INSERT INTO auditoria_empleado (evento, id_empleado, id_usuario) VALUES (?, ?, ?)");
            stm4.setString(1, "ACTUALIZACIÓN_USUARIO");
            stm4.setInt(2, id);
            stm4.setInt(3, iduser);
            try {
                stm4.execute();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR12" + e);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR12" + e);
        }
    }

    public static int Validacion(Connection conexion, int Codigo) throws SQLException {
        int resultado = 0;
        PreparedStatement stm = conexion.prepareStatement("Select empleado.id_Empleado from empleado where idCargo=?");
        stm.setInt(1, Codigo);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseás Eliminar todos los clientes con esta Cargo?",
                    "Confirmar acción",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (opcion == JOptionPane.OK_OPTION) {
                PreparedStatement stm2 = conexion.prepareStatement("UPDATE empleado SET borrado=1 WHERE idCargo=?");
                stm2.setInt(1, Codigo);
                try {
                    stm2.executeUpdate();
                    resultado = 1;
                } catch (Exception e) {
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

    public static void ActualizarCargo(Connection conexion, int Codigo, String Cargo) throws SQLException {

        PreparedStatement stm2 = conexion.prepareStatement("Select cargo.Cargo from cargo where cargo.idCargo=?");
        stm2.setInt(1, Codigo);
        ResultSet rs = stm2.executeQuery();

        if (rs.next()) {
            String car = rs.getString("cargo.Cargo");
            if (!Cargo.equals(car)) {

                PreparedStatement stm = conexion.prepareStatement("UPDATE cargo SET Cargo = ? WHERE idCargo = ?");
                stm.setString(1, Cargo);
                stm.setInt(2, Codigo);

                try {
                    stm.executeUpdate();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR12");
                }
            }
        }

    }

}
