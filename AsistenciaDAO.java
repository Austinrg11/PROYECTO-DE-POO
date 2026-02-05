/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gimnasio;

/**
 *
 * @author denni
 */
import java.sql.*;

public class AsistenciaDAO {

    private int Id_Asistencia;
    private int Id_Cliente;
    private String Fecha;
    private String Hora_Entrada;
    private String Hora_Salida;
    ConexionBD bd;

    public AsistenciaDAO() throws Exception {
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    // Getters y Setters
    public int getId_Asistencia() {
        return Id_Asistencia;
    }

    public void setId_Asistencia(int Id_Asistencia) {
        this.Id_Asistencia = Id_Asistencia;
    }

    public int getId_Cliente() {
        return Id_Cliente;
    }

    public void setId_Cliente(int Id_Cliente) {
        this.Id_Cliente = Id_Cliente;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getHora_Entrada() {
        return Hora_Entrada;
    }

    public void setHora_Entrada(String Hora_Entrada) {
        this.Hora_Entrada = Hora_Entrada;
    }

    public String getHora_Salida() {
        return Hora_Salida;
    }

    public void setHora_Salida(String Hora_Salida) {
        this.Hora_Salida = Hora_Salida;
    }

    public int IncrementarAsistencia() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDASISTENCIA) AS Num FROM asistencia;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarAsistencia() throws Exception {
        String sql = "INSERT INTO asistencia (IDASISTENCIA, IDCLIENTE, FECHA, HORAENTRADA, HORASALIDA) VALUES (?, ?, ?, ?, ?)";

        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, IncrementarAsistencia());
        ps.setInt(2, getId_Cliente());
        ps.setString(3, getFecha());
        ps.setString(4, getHora_Entrada());
        ps.setString(5, getHora_Salida());

        ps.executeUpdate();
    }

    public void ActualizarAsistencia() throws SQLException, Exception {
        String sql = "UPDATE asistencia SET IDCLIENTE=?, FECHA=?, HORAENTRADA=?, HORASALIDA=? WHERE IDASISTENCIA=?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Cliente());
        ps.setString(2, getFecha());
        ps.setString(3, getHora_Entrada());
        ps.setString(4, getHora_Salida());
        ps.setInt(5, getId_Asistencia());
        ps.executeUpdate();
        System.out.println("ASISTENCIA ACTUALIZADA EXITOSAMENTE.");
    }

    public void EliminarAsistencia() throws SQLException, Exception {
        String sql = "DELETE FROM asistencia WHERE IDASISTENCIA = ?";
        Connection con = bd.ConectarBD();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, getId_Asistencia());
        ps.executeUpdate();
        System.out.println("ASISTENCIA ELIMINADA EXITOSAMENTE.");
    }

    // MÉTODO NUEVO: Consulta que muestra TODOS los clientes (con o sin asistencia)
    public ResultSet ConsultarAsistencia(String filtro) throws SQLException {
        String sql = "SELECT "
                + "COALESCE(a.IDASISTENCIA, 0) AS IDASISTENCIA, "
                + "c.IDCLIENTE, "
                + "CONCAT(c.NOMBRES, ' ', c.APELLIDOS) AS CLIENTE, "
                + "COALESCE(a.FECHA, c.FECHAREGISTRO) AS FECHA, "
                + "a.HORAENTRADA, "
                + "a.HORASALIDA "
                + "FROM cliente c "
                + "LEFT JOIN asistencia a ON c.IDCLIENTE = a.IDCLIENTE ";

        // Si hay filtro, agregamos WHERE
        if (filtro != null && !filtro.trim().equals("")) {
            sql += "WHERE (c.NOMBRES LIKE '%" + filtro + "%' OR c.APELLIDOS LIKE '%" + filtro + "%') ";
        }

        // Ordenar: primero los que tienen asistencia, luego por nombre
        sql += "ORDER BY COALESCE(a.FECHA, c.FECHAREGISTRO) DESC, c.NOMBRES ASC";

        return bd.ConsultaBD(sql);
    }

    public int obtenerIdClientePorNombre(String nombre) {
        int id = -1;
        try {
            String sql = "SELECT IDCLIENTE FROM cliente WHERE CONCAT(NOMBRES, ' ', APELLIDOS) = '" + nombre + "' LIMIT 1";
            ResultSet rs = bd.ConsultaBD(sql);
            if (rs.next()) {
                id = rs.getInt("IDCLIENTE");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener ID del cliente: " + e.toString());
        }
        return id;
    }
}
