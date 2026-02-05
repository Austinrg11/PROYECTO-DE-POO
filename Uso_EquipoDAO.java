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
public class Uso_EquipoDAO {

    private int Id_Uso;
    private int Id_Cliente;
    private int Id_Equipo;
    private String Fecha;
    private int Tiempo;
    ConexionBD bd;

    public Uso_EquipoDAO() throws Exception {
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_Uso() {
        return Id_Uso;
    }

    public void setId_Uso(int Id_Uso) {
        this.Id_Uso = Id_Uso;
    }

    public int getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(int Id_Cliente) {
        this.Id_Cliente = Id_Cliente;
    }

    public int getId_Equipo() {
        return Id_Equipo;
    }

    public void setId_Equipo(int Id_Equipo) {
        this.Id_Equipo = Id_Equipo;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public int getTiempo() {
        return Tiempo;
    }

    public void setTiempo(int Tiempo) {
        this.Tiempo = Tiempo;
    }

    public int IncrementarUso_Equipo() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDUSO) AS Num FROM uso_equipo;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarUso_Equipo() throws SQLException, Exception {
        String sql = "INSERT INTO uso_equipo (IDUSO, IDCLIENTE, IDEQUIPO, FECHA, TIEMPO) VALUES (?, ?, ?, ?, ?)";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, IncrementarUso_Equipo());
        ps.setInt(2, getId_Cliente());
        ps.setInt(3, getId_Equipo());
        ps.setString(4, getFecha());
        ps.setInt(5, getTiempo());
        ps.executeUpdate();
    }

    public void ActualizarUso_Equipo() throws SQLException, Exception {
        String sql = "UPDATE uso_equipo SET IDCLIENTE=?, IDEQUIPO=?, FECHA=?, TIEMPO=? WHERE IDUSO=?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Cliente());
        ps.setInt(2, getId_Equipo());
        ps.setString(3, getFecha());
        ps.setInt(4, getTiempo());
        ps.setInt(5, getId_Uso());
        ps.executeUpdate();
        System.out.println("USO DE EQUIPO ACTUALIZADO EXITOSAMENTE.");
    }

    public void EliminarUso_Equipo() throws SQLException, Exception {
        String sql = "DELETE FROM uso_equipo WHERE IDUSO = ?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Uso());
        ps.executeUpdate();
        System.out.println("USO DE EQUIPO ELIMINADO EXITOSAMENTE.");
    }

    public ResultSet ConsultarUsoEquipo(String filtro) throws SQLException {
        String sql = "SELECT u.IDUSO, u.IDCLIENTE, "
                + "CONCAT(c.NOMBRES, ' ', c.APELLIDOS) AS CLIENTE, "
                + "u.IDEQUIPO, e.NOMBREEQUIPO AS EQUIPO, "
                + "u.FECHA, u.TIEMPO "
                + "FROM uso_equipo u "
                + "LEFT JOIN cliente c ON u.IDCLIENTE = c.IDCLIENTE " 
                + "LEFT JOIN equipo e ON u.IDEQUIPO = e.IDEQUIPO";       

        if (!filtro.equals("")) {
            sql += " WHERE c.NOMBRES LIKE '%" + filtro + "%' OR c.APELLIDOS LIKE '%" + filtro + "%'";
        }

        return bd.ConsultaBD(sql);
    }

}
