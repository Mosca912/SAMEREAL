package CONEXIONES;

import java.sql.*;

public class Conexiones {
    public static Connection Conexion (){
        Connection conexion=null;
        String Servidor="jdbc:mysql://localhost/samerealpro";
        String Usuario="same";
        String pass="samejujuy1072025ies";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion=(Connection)DriverManager.getConnection(Servidor,Usuario,pass);
        } catch (ClassNotFoundException | SQLException ex){
        }
        
        finally {
            return conexion;
        }
    }
}
