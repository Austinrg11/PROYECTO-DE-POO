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
public class EquipoDAO {

    private int Id_Equipo;
    private int Id_Entrenador;
    private String Nombre_Equipo;
    private String Tipo;
    private String Estado;
    private String Descripcion;
    ConexionBD bd;

    public EquipoDAO() throws Exception {
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_Equipo() {
        return Id_Equipo;
    }

    public void setId_Equipo(int Id_Equipo) {
        this.Id_Equipo = Id_Equipo;
    }

    public int getId_Entrenador() {
        return Id_Entrenador;
    }

    public void setId_Entrenador(int Id_Entrenador) {
        this.Id_Entrenador = Id_Entrenador;
    }

    public String getNombre_Equipo() {
        return Nombre_Equipo;
    }

    public void setNombre_Equipo(String Nombre_Equipo) {
        this.Nombre_Equipo = Nombre_Equipo;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int IncrementarEquipo() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDEQUIPO) AS Num FROM equipo;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarEquipo() throws SQLException, Exception {
        String sql = "INSERT INTO equipo (IDEQUIPO, IDENTRENADOR, NOMBREEQUIPO, TIPO, ESTADO, DESCRIPCION) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, IncrementarEquipo());
        ps.setInt(2, getId_Entrenador());
        ps.setString(3, getNombre_Equipo());
        ps.setString(4, getTipo());
        ps.setString(5, getEstado());
        ps.setString(6, getDescripcion());
        ps.executeUpdate();
    }

    public void ActualizarEquipo() throws SQLException, Exception {
        String sql = "UPDATE equipo SET IDENTRENADOR=?, NOMBREEQUIPO=?, TIPO=?, ESTADO=?, DESCRIPCION=? WHERE IDEQUIPO=?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Entrenador());
        ps.setString(2, getNombre_Equipo());
        ps.setString(3, getTipo());
        ps.setString(4, getEstado());
        ps.setString(5, getDescripcion());
        ps.setInt(6, getId_Equipo());
        ps.executeUpdate();
        System.out.println("EQUIPO ACTUALIZADO EXITOSAMENTE.");
    }

    public void EliminarEquipo() throws SQLException, Exception {
        String sql = "DELETE FROM equipo WHERE IDEQUIPO = ?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Equipo());
        ps.executeUpdate();
        System.out.println("EQUIPO ELIMINADO EXITOSAMENTE.");
    }

    public ResultSet ConsultarEquipo(String filtro) throws SQLException {
        String sql = "SELECT e.IDEQUIPO, e.IDENTRENADOR, "
                + "en.NOMBRES AS ENTRENADOR, "
                + "e.NOMBREEQUIPO, e.TIPO, e.ESTADO, e.DESCRIPCION "
                + "FROM equipo e "
                + "LEFT JOIN entrenador en ON e.IDENTRENADOR = en.IDENTRENADOR"; 

        if (!filtro.equals("")) {
            sql += " WHERE e.NOMBREEQUIPO LIKE '%" + filtro + "%'";
        }

        return bd.ConsultaBD(sql);
    }

}
