/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Conexion;
import Maestros.Sesiones;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Humberto Manjarres
 */
public class DaoSesiones extends Conexion implements Serializable {
    Connection con = null;
    CallableStatement call = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    ResultSet rs = null;
    List<Sesiones> listaSesiones = new ArrayList<>();
    Sesiones sesion;
    
    
    public List<Sesiones> getSesiones() throws SQLException {
        listaSesiones = new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetsesiones(?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.registerOutParameter(3, Types.VARCHAR);
            call.execute();
            if (call.getString(3).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    sesion = new Sesiones();
                    sesion.setOrden(rs.getString("orden"));
                    sesion.setUsuario(rs.getString("usuario"));
                    sesion.setSesion(rs.getString("sesion"));
                    sesion.setIp(rs.getString("ip"));
                    sesion.setFecha(rs.getString("fechasys").substring(0, 19));
                    listaSesiones.add(sesion);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR getSesiones-->" + e);
        }finally{
            cerrarConexion();
        }

        return listaSesiones;
    }
    
    public String removeSesion(String sesion, String usu) throws SQLException {
        String eliminado = "no eliminado";
        try {
            con = conectar();
            call = con.prepareCall("call _splogout(?,?,?)");
            call.setString(1, sesion);
            call.setString(2, usu);
            call.registerOutParameter(3, Types.VARCHAR);
            call.execute();
            System.out.println("respuesta -> "+call.getString(3));
            if (call.getString(3).equals("0|")) {
                eliminado="eliminado";
            }
        } catch (Exception e) {
            eliminado=e+"";
        }finally{
            cerrarConexion();
        }

        return eliminado;
    }
    
    
    public void cerrarConexion() throws SQLException {
        if (rs != null) {
            rs.close();
            rs = null;
        }

        if (call != null) {
            call.close();
        }

        if (con != null) {
            con.close();
            con = null;
        }
    }
    
}
