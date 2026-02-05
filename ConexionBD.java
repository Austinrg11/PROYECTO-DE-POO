/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gimnasio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author denni
 */
public class ConexionBD {

    private final String host;
    private final String user;
    private final String pass;
    private final String bd;

    private Connection conexion;

    public ConexionBD(String host, String user, String pass, String bd) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.bd = bd;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getBd() {
        return bd;
    }

    public Connection ConectarBD() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://" + host + "/" + bd
                    + "?useSSL=false&serverTimezone=UTC";

            conexion = DriverManager.getConnection(url, user, pass);

            System.out.println("CONEXIÓN EXITOSA A LA BASE DE DATOS.");
            return conexion;

        } catch (SQLException e) {
            System.out.println("ERROR AL MOMENTO DE CONECTAR CON LA BASE DE DATOS: " + e.toString());
            return null;

        }
    }

    public void ActualizarBD(String sql) throws SQLException {
        try {

            Statement stm = conexion.createStatement();
            stm.executeUpdate(sql);
            System.out.println("TRANSACCION REALIZADA EXITOSAMENTE.....");
        } catch (SQLException e) {
            System.out.println("TRANSACCION NO REALIZADA....." + e.toString());
        }

    }

    public ResultSet ConsultaBD(String sql) throws SQLException {
        ResultSet rs;
        Statement stm = conexion.createStatement();
        rs = stm.executeQuery(sql);
        return rs;
    }

    public void CerrarBD() throws SQLException{
        conexion.close();
        
    }

}
