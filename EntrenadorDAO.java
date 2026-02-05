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
public class EntrenadorDAO {

    private int Id_Entrenador;
    private String Nombres;
    private String Especialidad;
    private String Celular;
    ConexionBD bd;

    public EntrenadorDAO() throws Exception {
        bd = new ConexionBD("localhost", "root", "", "gimnasio");
        bd.ConectarBD();
    }

    public int getId_Entrenador() {
        return Id_Entrenador;
    }

    public void setId_Entrenador(int Id_Entrenador) {
        this.Id_Entrenador = Id_Entrenador;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getEspecialidad() {
        return Especialidad;
    }

    public void setEspecialidad(String Especialidad) {
        this.Especialidad = Especialidad;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public int IncrementarEntrenador() throws SQLException {
        int incremento = 0;
        ResultSet rs;
        rs = bd.ConsultaBD("SELECT MAX(IDENTRENADOR) AS Num FROM entrenador;");
        if (rs.next()) {
            incremento = rs.getInt(1) + 1;
        } else {
            incremento = 1;
        }
        return incremento;
    }

    public void InsertarEntrenador() throws SQLException {
        String url = "INSERT INTO entrenador VALUES(" + IncrementarEntrenador() + ", '" + getNombres() + "', '" + getEspecialidad() + "', '" + getCelular() + "')";
        bd.ActualizarBD(url);
    }

    public void ActualizarEntrenador() {
        try {
            String url = "UPDATE entrenador SET " + "NOMBRES = '" + getNombres() + "', " + "ESPECIALIDAD = '" + getEspecialidad() + "', " + "CELULAR = '" + getCelular() + "' " + "WHERE IDENTRENADOR =  " + getId_Entrenador();
            bd.ActualizarBD(url);
            System.out.println("ENTRENADOR ACTUALIZADO EXITOSAMENTE.");
        } catch (Exception e) {
            System.out.println("ERROR AL ACTUALIZAR ENTRENADOR." + e.toString());
        }
    }

    public void EliminarEntrenador() {
        try {
            String url = "DELETE FROM entrenador WHERE IDENTRENADOR = " + getId_Entrenador();
            bd.ActualizarBD(url);
            System.out.println("ENTRENADOR ELIMINADO EXITOSAMENTE.");
        } catch (Exception e) {
            System.out.println("ERROR AL ELIMINAR ENTRENADOR." + e.toString());
        }

    }

   public ResultSet ConsultarEntrenador(String filtro) throws SQLException {
    if (filtro.equals("")) {
        return bd.ConsultaBD("SELECT * FROM entrenador");
    } else {
        return bd.ConsultaBD("SELECT * FROM entrenador WHERE NOMBRES LIKE '%" + filtro + "%'");
    }
}
   
    
     public ResultSet Buscar(String texto) throws SQLException {
        return bd.ConsultaBD("SELECT * FROM entrenador WHERE NOMBRES LIKE '%" + texto + "%'");
    }

}
