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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Facuymayriver
 */
public class Usuario {

    static DefaultComboBoxModel<Usuario.Rango> model;
    static DefaultComboBoxModel<Usuario.Base> model2;
    static DefaultComboBoxModel<Usuario.UsuarioMod> model3;

    public static class Rango {

        private final int id;
        private final String nombre;

        public Rango(int id, String nombre) {
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

    public static class Base {

        private final int id;
        private final String nombre;

        public Base(int id, String nombre) {
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

    public static class UsuarioMod {

        private final int id;
        private final String nombre;

        public UsuarioMod(int id, String nombre) {
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

    public static void jComboUser(Connection conexion, JComboBox<Usuario.UsuarioMod> combo1) {
        String sql = "SELECT idUsuario, DNI, Nombre FROM usuario WHERE borrado=0";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            model3 = new DefaultComboBoxModel<>();
            model3.addElement(new Usuario.UsuarioMod(0, "Opciones"));
            while (rs.next()) {
                int id = rs.getInt("idUsuario");
                String nombreCompleto = (rs.getString("DNI") + "-" + rs.getString("Nombre"));
                model3.addElement(new Usuario.UsuarioMod(id, nombreCompleto));
            }
            combo1.setModel(model3);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static void jCombo(Connection conexion, JComboBox<Usuario.Rango> combo1, JComboBox<Usuario.Base> combo2) {
        String sql = "SELECT idRango, rango FROM rango WHERE borrado=0";
        String sql2 = "SELECT idBase, Base from Base";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            model = new DefaultComboBoxModel<>();
            model.addElement(new Usuario.Rango(0, "Opciones"));
            while (rs.next()) {
                int id = rs.getInt("idRango");
                String nombreCompleto = rs.getString("rango");
                model.addElement(new Usuario.Rango(id, nombreCompleto));
            }
            combo1.setModel(model);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }

        try {
            PreparedStatement ps = conexion.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();
            model2 = new DefaultComboBoxModel<>();
            model2.addElement(new Usuario.Base(0, "Opciones"));
            while (rs.next()) {
                int id = rs.getInt("idBase");
                String nombreCompleto = rs.getString("Base");
                model2.addElement(new Usuario.Base(id, nombreCompleto));
            }
            combo2.setModel(model2);
            AutoCompleteDecorator.decorate(combo1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

    public static int AgregarNuevoUsuario(Connection conexion, String nombre, String apellido, String dni, String email, String contrasena, int id, int idbase, int band) throws SQLException {
        int valid = 0;
        if (band == 0) {
            String contrasenaHasheada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
            String sql2 = "INSERT into usuario (nombre, apellido, correo, contrasena, rango, dni, base, borrado) VALUES (?,?,?,?,?,?,?, 0) ";
            try {
                PreparedStatement ps2 = conexion.prepareStatement(sql2);
                ps2.setString(1, nombre);
                ps2.setString(2, apellido);
                ps2.setString(3, email);
                ps2.setString(4, contrasenaHasheada);
                ps2.setInt(5, 1);
                ps2.setString(6, dni);
                ps2.setInt(7, idbase);
                ps2.execute();
                JOptionPane.showMessageDialog(null, "Guardado");
                valid = 1;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "ERROR " + e);
            }
        } else if (band == 1) {
            String contrasenaHasheada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
            String sql2 = "INSERT into usuario (nombre, apellido, correo, contrasena, rango, dni, base, borrado) VALUES (?,?,?,?,?,?,?, 0) ";
            try {
                PreparedStatement ps2 = conexion.prepareStatement(sql2);
                ps2.setString(1, nombre);
                ps2.setString(2, apellido);
                ps2.setString(3, email);
                ps2.setString(4, contrasenaHasheada);
                ps2.setInt(5, id);
                ps2.setString(6, dni);
                ps2.setInt(7, base);
                ps2.execute();
                JOptionPane.showMessageDialog(null, "Guardado");
                valid = 1;
            } catch (SQLException e) {
                int errorCode = e.getErrorCode();
                if (errorCode == 1062) {
                    // Mensaje Personalizado para DNI/Correo Repetido
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Error: El DNI o el correo electrónico que intentás ingresar ya existen en el sistema. Deben ser únicos.",
                            "Error de Registro Duplicado", // Título de la ventana
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Si es cualquier otro error de SQL, mostramos el mensaje genérico
                    JOptionPane.showMessageDialog(null, "ERROR SQL no esperado: " + e.getMessage());
                }
            }
        }
        return valid;
    }

    public static void TraerUsuario(Connection conexion, int id, JTextField nombre, JTextField apellido, JTextField correo, JTextField dni) throws SQLException {
        String sql2 = "SELECT nombre, apellido, correo, dni, rango FROM usuario WHERE idUsuario = ?;";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ps2.setInt(1, id);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nombre");
                String ape = rs.getString("apellido");
                String cor = rs.getString("correo");
                String dnis = rs.getString("dni");
                int ran = rs.getInt("rango");
                nombre.setText(nom);
                apellido.setText(ape);
                correo.setText(cor);
                dni.setText(dnis);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR " + e);
        }
    }

    public static int CambiarCont(Connection conexion, String dni, String email, String contrasena) throws SQLException {
        int veri = 0;
        String contrasenaHasheada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        int idu;
        String sql2 = "SELECT idUsuario from usuario where dni=? and correo=?";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ps2.setString(1, dni);
            ps2.setString(2, email);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                idu = rs.getInt("idUsuario");
                String sql = "UPDATE usuario SET contrasena=? WHERE idUsuario=?";
                try {
                    PreparedStatement ps = conexion.prepareStatement(sql);
                    ps.setString(1, contrasenaHasheada);
                    ps.setInt(2, idu);
                    ps.execute();
                    JOptionPane.showMessageDialog(null, "Cambio de contraseña exitosa!");
                    veri = 1;
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR " + e);
        }
        return veri;
    }

    public static int ActualizarUser(Connection conexion, String nombre, String apellido, String dni, String em, int id1, int id) throws SQLException {
        System.out.println(id1);
        String sql = "UPDATE usuario SET nombre=?, apellido=?, correo=?, rango = ?, dni = ? WHERE idUsuario=?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, em);
            ps.setInt(4, id1);
            ps.setString(5, dni);
            ps.setInt(6, id);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Actualización de usuario exitosa");
            valid=1;
            PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria (evento, usuario_afectado) VALUES (?, ?)");
            stm9.setString(1, "MODIFICACION_USUARIO");
            stm9.setString(2, em);
            try {
                stm9.execute();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR: " + e);
            }
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 1062) {
                // Mensaje Personalizado para DNI/Correo Repetido
                JOptionPane.showMessageDialog(null,
                        "⚠️ Error: El DNI o el correo electrónico que intentás ingresar ya existen en el sistema. Deben ser únicos.",
                        "Error de Registro Duplicado", // Título de la ventana
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Si es cualquier otro error de SQL, mostramos el mensaje genérico
                JOptionPane.showMessageDialog(null, "ERROR SQL no esperado: " + e.getMessage());
            }
        }
        return valid;
    }

    public static int Verificacion(Connection conexion) {
        int veri = 0;
        String sql2 = "SELECT idUsuario from usuario";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                veri = 1;
            } else {
                veri = 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR " + e);
        }
        return veri;
    }

    static int valid;
    static int rango;
    static int iduser;
    static String nomb;
    static int base;
    static String correo, basenomb;

    public static int IniciarSesion(Connection conexion, String dni, String contrasena, JLabel login, JLabel datos) throws SQLException {
        valid = 0;
        String contrasenadb;
        String sql = "SELECT contrasena, nombre, rango, correo, idUsuario, usuario.base, base.Base FROM usuario inner join base on usuario.base=base.idBase WHERE dni = ?";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql);
            ps2.setString(1, dni);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                contrasenadb = rs.getString("contrasena");
                nomb = rs.getString("nombre");
                rango = rs.getInt("rango");
                iduser = rs.getInt("idUsuario");
                base = rs.getInt("base");
                correo = rs.getString("correo");
                basenomb = rs.getString("base.Base");
                if (BCrypt.checkpw(contrasena, contrasenadb)) {
                    valid = 1;
                    login.setText("Bienvenido/a " + nomb);
                    datos.setText(basenomb);
                    JOptionPane.showMessageDialog(null, "INICIO DE SESION CORRECTO, " + nomb);
                    PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria (evento, usuario_afectado) VALUES (?, ?)");
                    stm9.setString(1, "LOGIN_USUARIO");
                    stm9.setString(2, correo);
                    try {
                        stm9.execute();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "ERROR: " + e);
                    }
                } else {
                    valid = 0;
                    rango = 0;
                    JOptionPane.showMessageDialog(null, "Contraseña pifiada");
                }
            } else {
                JOptionPane.showMessageDialog(null, "DNI Incorrecto!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR " + e);
        }
        return valid;
    }

    public static void CerrarSesion(Connection conexion, JLabel login, JLabel datos) throws SQLException {
        login.setText("Login");
        datos.setText("");
        valid = 0;
        rango = 0;
        iduser = 0;
        base = 0;
        PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria (evento, usuario_afectado) VALUES (?, ?)");
        stm9.setString(1, "LOGOUT_USUARIO");
        stm9.setString(2, correo);
        try {
            stm9.execute();
            correo = "";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        }
    }

    public static void CerrarSesion2(Connection conexion) throws SQLException {
        valid = 0;
        rango = 0;
        iduser = 0;
        base = 0;
        PreparedStatement stm9 = conexion.prepareStatement("INSERT INTO auditoria (evento, usuario_afectado) VALUES (?, ?)");
        stm9.setString(1, "LOGOUT_USUARIO");
        stm9.setString(2, correo);
        try {
            stm9.execute();
            correo = "";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + e);
        }
    }

    public static int iduser() {
        return iduser;
    }

    public static int base() {
        return base;
    }

    public static int rango() {
        return rango;
    }

    public static void nombre(JLabel login, JLabel base) {
        login.setText("Bienvenido/a " + nomb);
        base.setText(basenomb);
    }

    public static int verificacion() {
        return valid;
    }

}
