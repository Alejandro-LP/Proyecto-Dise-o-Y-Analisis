package modelo;

import java.sql.*;

public class ConexionBD {

    public Connection conexion;
    public PreparedStatement sentencia;

    public ConexionBD() {

        String ruta = "jdbc:mysql://localhost:3306/mercadored";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(ruta, "root", "2006");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    
}
