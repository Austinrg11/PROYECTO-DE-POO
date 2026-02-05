/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gimnasio;

import java.sql.Date;

/**
 *
 * @author denni
 */
import java.sql.*;

public class ClienteDAO {

    private int Id_Cliente;
    private String Nombres;
    private String Apellidos;
    private String Celular;
    private String Direccion;
    private String Correo;
    private String Fecha_Registro;
    ConexionBD bd;

    public ClienteDAO() throws Exception {
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(int Id_Cliente) {
        this.Id_Cliente = Id_Cliente;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.Apellidos = Apellidos;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public String getFecha_Registro() {
        return Fecha_Registro;
    }

    public void setFecha_Registro(String Fecha_Registro) {
        this.Fecha_Registro = Fecha_Registro;
    }

    public int IncrementarCliente() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDCLIENTE) AS Num FROM cliente;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarCliente() throws SQLException {
        String url = "INSERT INTO cliente VALUES(" + IncrementarCliente() + ", '" + getNombres() + "', '" + getApellidos() + "', '" + getCelular() + "', '" + getDireccion() + "', '" + getCorreo() + "', '" + getFecha_Registro() + "')";
        bd.ActualizarBD(url);
    }

    public void ActualizarCliente() throws Exception {
        // 1. SQL Seguro con signos de interrogación
        String sql = "UPDATE cliente SET NOMBRES=?, APELLIDOS=?, CELULAR=?, DIRECCION=?, CORREO=?, FECHAREGISTRO=? WHERE IDCLIENTE=?";
        
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        
        // 2. Rellenamos los datos en orden
        ps.setString(1, getNombres());
        ps.setString(2, getApellidos());
        ps.setString(3, getCelular());
        ps.setString(4, getDireccion());
        ps.setString(5, getCorreo());
        ps.setString(6, getFecha_Registro());
        ps.setInt(7, getId_Cliente()); // El ID es el último ? (el del WHERE)
        
        // 3. Ejecutamos
        ps.executeUpdate();
    }

    public void EliminarCliente() throws Exception {
        String sql = "DELETE FROM cliente WHERE IDCLIENTE = ?";

        Connection con = bd.ConectarBD();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Cliente());
        
        ps.executeUpdate();
       
    }

    public void ConsultarCliente() throws SQLException{
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT * FROM cliente");
        System.out.println("ID\tNOMBRES\tAPELLIDOS\tCELULAR\tDIRECCION\tCORREO\tFECHA DE REGISTRO"); 
        while (rs.next()) {            
            System.out.print(rs.getInt(1)+ "\t");
            System.out.print(rs.getString(2)+ "\t");
            System.out.print(rs.getString(3)+ "\t");
            System.out.print(rs.getString(4)+ "\t");
            System.out.print(rs.getString(5)+ "\t");
            System.out.print(rs.getString(6)+ "\t");
            System.out.print(rs.getDate(7)+ "\t");
            System.out.println();
        }

    }

}
