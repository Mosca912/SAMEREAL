package CONEXIONES;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexiones {
    public static Connection Conexion (){
        Connection conexion=null;
        String Servidor="jdbc:mysql://localhost/gemo_same107_db_2025";
        String Usuario="same";
        String pass="samejujuy1072025ies";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion=(Connection)DriverManager.getConnection(Servidor,Usuario,pass);
        } catch (ClassNotFoundException | SQLException ex){
            JOptionPane.showMessageDialog(null, "CONEXION CAIDA, POR FAVOR INTENTELO DE VUELTA");
        }
        
        finally {
            return conexion;
        }
    }
}
