/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gimnasio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author denni
 */
public class UsuarioDAO {
    private int Id_User;
    private String Nombre_User;
    private String Pass;
    ConexionBD bd;
    
    public UsuarioDAO()throws Exception{
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_User() {
        return Id_User;
    }

    public void setId_User(int Id_User) {
        this.Id_User = Id_User;
    }

    public String getNombre_User() {
        return Nombre_User;
    }

    public void setNombre_User(String Nombre_User) {
        this.Nombre_User = Nombre_User;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String Pass) {
        this.Pass = Pass;
    }
    
    
     public int IncrementarUsuario() throws SQLException {
        int incremento = 0;
        ResultSet rs = bd.ConsultaBD("SELECT MAX(IDUSUARIO) AS num FROM usuario");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

   
     
    public void InsertarUsuario() throws SQLException, Exception {
        String sql = "INSERT INTO usuario (IDUSUARIO, USUARIO, PASS) VALUES (?, ?, ?)";
        
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        
        // Calculamos el ID nuevo
        ps.setInt(1, IncrementarUsuario());
        ps.setString(2, getNombre_User());
        ps.setString(3, getPass());
        
        ps.executeUpdate();
        // con.close();
    }

   
    
    public boolean ValidarUsuario(String user, String pass) throws SQLException {
        ResultSet rs = bd.ConsultaBD(
            "SELECT * FROM usuario WHERE USUARIO = '" + user 
            + "' AND PASS = '" + pass + "'"
        );

        return rs.next(); 
    }

   
    public void ConsultarUsuarios() throws SQLException {
        ResultSet rs = bd.ConsultaBD("SELECT * FROM usuario");
        System.out.println("ID\tUSUARIO\tCONTRASEÑA");

        while (rs.next()) {
            System.out.print(rs.getInt(1) + "\t");
            System.out.print(rs.getString(2) + "\t");
            System.out.print(rs.getString(3) + "\n");
        }
    }

    
    
}
