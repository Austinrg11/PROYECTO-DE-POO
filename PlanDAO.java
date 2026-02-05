/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gimnasio;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author denni
 */
public class PlanDAO {
    private int Id_Plan;
    private String Nombre_plan;
    private double Costo;
    private int Duracion;
    private String Descripcion;
    ConexionBD bd;

    public PlanDAO() throws Exception{
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_Plan() {
        return Id_Plan;
    }

    public void setId_Plan(int Id_Plan) {
        this.Id_Plan = Id_Plan;
    }

    public String getNombre_plan() {
        return Nombre_plan;
    }

    public void setNombre_plan(String Nombre_plan) {
        this.Nombre_plan = Nombre_plan;
    }

    public double getCosto() {
        return Costo;
    }

    public void setCosto(double Costo) {
        this.Costo = Costo;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int Duracion) {
        this.Duracion = Duracion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    public int IncrementarPlan() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDPLAN) AS Num FROM plan;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarPlan() throws SQLException {
        String url = "INSERT INTO plan VALUES(" + IncrementarPlan() + ", '" + getNombre_plan() + "', " + getCosto() + ", " + getDuracion() + ", '" + getDescripcion() + "')";
        bd.ActualizarBD(url);
    }

    public void ActualizarPlan() {
        try {
            String url = "UPDATE plan SET " + "NOMBREPLAN = '" + getNombre_plan() + "', " + "COSTO = " + getCosto() + ", " + "DURACION = " + getDuracion() + ", " + "DESCRIPCION = '" + getDescripcion() + "' " + " WHERE IDPLAN =  " + getId_Plan();
        bd.ActualizarBD(url);
            System.out.println("PLAN ACTUALIZADO EXITOSAMENTE.");
        } catch (Exception e) {
            System.out.println("ERROR AL ACTUALIZAR PLAN." + e.toString());
        }
    }

    public void EliminarPlan() {
        try {
            String url = "DELETE FROM plan WHERE IDPLAN = " + getId_Plan();
            bd.ActualizarBD(url);
            System.out.println("PLAN ELIMINADO EXITOSAMENTE.");
        } catch (Exception e) {
            System.out.println("ERROR AL ELIMINAR PLAN." + e.toString());
        }
        
    }

    public ResultSet ConsultarPlan(String filtro) throws SQLException {
    if (filtro.equals("")) {
        return bd.ConsultaBD("SELECT * FROM plan");
    } else {
        return bd.ConsultaBD("SELECT * FROM plan WHERE NOMBREPLAN LIKE '%" + filtro + "%'");
    }
}
}
