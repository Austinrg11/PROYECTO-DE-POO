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
public class Cliente_PlanDAO {

    private int Id_Cliente_Plan;
    private int Id_Cliente;
    private int Id_Plan;
    private String Fecha_Inicio;
    private String Fecha_Final;
    private String Estado;
    ConexionBD bd;

    public Cliente_PlanDAO() throws Exception {
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_Cliente_Plan() {
        return Id_Cliente_Plan;
    }

    public void setId_Cliente_Plan(int Id_Cliente_Plan) {
        this.Id_Cliente_Plan = Id_Cliente_Plan;
    }

    public int getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(int Id_Cliente) {
        this.Id_Cliente = Id_Cliente;
    }

    public int getId_Plan() {
        return Id_Plan;
    }

    public void setId_Plan(int Id_Plan) {
        this.Id_Plan = Id_Plan;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String Fecha_Inicio) {
        this.Fecha_Inicio = Fecha_Inicio;
    }

    public String getFecha_Final() {
        return Fecha_Final;
    }

    public void setFecha_Final(String Fecha_Final) {
        this.Fecha_Final = Fecha_Final;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public int IncrementarCliente_Plan() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDCLIENTEPLAN) AS Num FROM cliente_plan;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarClientePlan() throws SQLException, Exception {
        String sql = "INSERT INTO cliente_plan (IDCLIENTEPLAN, IDCLIENTE, IDPLAN, FECHAINICIO, FECHAFIN, ESTADO) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, IncrementarCliente_Plan());
        ps.setInt(2, getId_Cliente());
        ps.setInt(3, getId_Plan());
        ps.setString(4, getFecha_Inicio());
        ps.setString(5, getFecha_Final());
        ps.setString(6, getEstado());
        ps.executeUpdate();
    }

    public void ActualizarClientePlan() throws SQLException, Exception {
        String sql = "UPDATE cliente_plan SET IDCLIENTE=?, IDPLAN=?, FECHAINICIO=?, FECHAFIN=?, ESTADO=? WHERE IDCLIENTEPLAN=?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Cliente());
        ps.setInt(2, getId_Plan());
        ps.setString(3, getFecha_Inicio());
        ps.setString(4, getFecha_Final());
        ps.setString(5, getEstado());
        ps.setInt(6, getId_Cliente_Plan());
        ps.executeUpdate();
        System.out.println("PLAN DE CLIENTE ACTUALIZADO EXITOSAMENTE.");
    }

    public void EliminarClientePlan() throws SQLException, Exception {
        String sql = "DELETE FROM cliente_plan WHERE IDCLIENTEPLAN = ?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Cliente_Plan());
        ps.executeUpdate();
        System.out.println("PLAN DE CLIENTE ELIMINADO EXITOSAMENTE.");
    }

    public ResultSet ConsultarClientePlan(String filtro) throws Exception {
        String sql = "SELECT cp.IDCLIENTEPLAN, cp.IDCLIENTE, "
                + "CONCAT(c.NOMBRES,' ',c.APELLIDOS) AS CLIENTE, "
                + "cp.IDPLAN, p.NOMBREPLAN AS PLAN, "
                + "cp.FECHAINICIO, cp.FECHAFIN, cp.ESTADO "
                + "FROM cliente_plan cp "
                + "LEFT JOIN cliente c ON cp.IDCLIENTE = c.IDCLIENTE "
                + 
                "LEFT JOIN plan p ON cp.IDPLAN = p.IDPLAN ";             

        if (!filtro.equals("")) {
            sql += "WHERE c.NOMBRES LIKE '%" + filtro + "%' OR c.APELLIDOS LIKE '%" + filtro + "%'";
        }

        return bd.ConsultaBD(sql);
    }

}
