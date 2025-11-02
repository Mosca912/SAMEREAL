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
import javax.swing.JOptionPane;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Facuymayriver
 */
public class Usuario {

    static DefaultComboBoxModel<Usuario.Rango> model;

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

    public static void jRango(Connection conexion, JComboBox<Usuario.Rango> combo1) {
        String sql = "SELECT idRango, rango FROM rango WHERE borrado=0";
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
    }

    public static void AgregarNuevoUsuario(Connection conexion, String nombre, String apellido, String dni, String email, String contrasena, int id) throws SQLException {
        String contrasenaHasheada = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        String sql2 = "INSERT into usuario (nombre, apellido, correo, contrasena, rango, dni) VALUES (?,?,?,?,?,?) ";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql2);
            ps2.setString(1, nombre);
            ps2.setString(2, apellido);
            ps2.setString(3, email);
            ps2.setString(4, contrasenaHasheada);
            ps2.setInt(5, id);
            ps2.setString(6, dni);
            ps2.execute();
            JOptionPane.showMessageDialog(null, "Guardado");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR " + e);
        }
    }
    public static void CambiarCont(Connection conexion, String dni, String email, String contrasena) throws SQLException {
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
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR " + e);
        }
    }

    static int valid;
    static int rango;

    public static int IniciarSesion(Connection conexion, String dni, String contrasena) throws SQLException {
        valid = 0;
        String contrasenadb;
        String nomb;
        String sql = "SELECT contrasena, idRango nombre FROM usuario WHERE dni = ?";
        try {
            PreparedStatement ps2 = conexion.prepareStatement(sql);
            ps2.setString(1, dni);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                contrasenadb = rs.getString("contrasena");
                nomb = rs.getString("nombre");
                rango = rs.getInt("idRango");
                if (BCrypt.checkpw(contrasena, contrasenadb)) {
                    valid = 1;
                    JOptionPane.showMessageDialog(null, "INICIO DE SESION CORRECTO, " + nomb);
                } else {
                    valid = 0;
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
    
    public static int rango(){
        return rango;
    }

    public static int verificacion() {
        return valid;
    }

}
