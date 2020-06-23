/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Conexion;
import Maestros.Perfil;
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
public class DaoPerfiles extends Conexion implements Serializable{
    Connection con = null;
    CallableStatement call = null;
    ResultSet rs = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    private Perfil perfilDao = new Perfil();
    List<String> listaTareasForAsignar;
    
    public List<Perfil> getDaoPerfiles(int idPerfil, String patron) throws SQLException, Exception {
        List<Perfil> listaPeriles = new ArrayList<>();
        try {
            con=conectar();
            call = con.prepareCall("call _spgetperfiles(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setInt(3, idPerfil);
            call.setString(4, patron);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            System.out.println("respuesta-->" + call.getString(5));
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                if (rs.next()) {
                    System.out.println("nombre-->" + rs.getString("nombre"));
                    perfilDao.setId(rs.getString("perfil"));
                    perfilDao.setNombre(rs.getString("nombre"));
                    perfilDao.setEstado(rs.getString("estado"));
                    listaPeriles.add(perfilDao);
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR getDaoPerfiles-->" + e);
            throw new Exception(e);
        } finally {
            cerrarConexion();
        }
        return listaPeriles;
    }
    
    
    public List<String> get_tareasAsignadas() throws ClassNotFoundException, SQLException {
        try {
            con = conectar();
            listaTareasForAsignar = new ArrayList<>();
            call = con.prepareCall("call _spgettareas(?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.registerOutParameter(3, Types.VARCHAR);
            call.execute();
            //System.out.println("respuesta tareas-->" + call.getString(3));
            if (call.getString(3).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    //System.out.println("nombre tarea-->" + rs.getString("nombre"));
                    listaTareasForAsignar.add(rs.getString("nombre") + " - " + rs.getString("tarea"));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("ERROR get_tareasAsignadas-->" + e);
        } finally {
            cerrarConexion();
        }
        return listaTareasForAsignar;
    }
    
    public List<String> get_perfilesTareas(String perfil) throws SQLException {
        try {
            con = conectar();
            listaTareasForAsignar = new ArrayList<>();
            call = con.prepareCall("call _spgettareasperfiles(?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, perfil);
            call.registerOutParameter(4, Types.VARCHAR);
            call.execute();
            //System.out.println("respuesta tareas-->" + call.getString(4));
            if (call.getString(4).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    System.out.println("nombre tarea-->" + rs.getString("nombre"));
                    listaTareasForAsignar.add(rs.getString("nombre") + " - " + rs.getString("tarea"));
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR get_perfilesTareas-->" + e);
        } finally {
            cerrarConexion();
        }

        return listaTareasForAsignar;
    }
    
    
    public List<Perfil> get_perfilDialog(String datoId, String accion) throws SQLException {
        List<Perfil> listaPerilesDialog = new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetperfiles(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, datoId);
            call.setString(4, accion);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            //System.out.println("respuesta-->" + call.getString(5));
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    perfilDao=new Perfil();
                    System.out.println("nombre-->" + rs.getString("nombre"));
                    perfilDao.setId(rs.getString("perfil"));
                    perfilDao.setNombre(rs.getString("nombre"));
                    perfilDao.setEstado(rs.getString("estado"));
                    listaPerilesDialog.add(perfilDao);
                }
            }else{
                
            }
        } catch (Exception e) {
            System.out.println("ERROR get_perfilDialog-->"+e);
        }finally{
            cerrarConexion();
        }

        return listaPerilesDialog;
    }
    
    public boolean guardar(String id, String nombre, String estado, String trama, String accion) throws SQLException {

        boolean respuesta = false;
        try {
            con = conectar();
            call = con.prepareCall("call _spregperfiles(?,?,?,?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, id);
            call.setString(4, nombre);
            call.setString(5, estado);
            call.setString(6, trama);
            call.setString(7, accion);
            call.registerOutParameter(8, Types.VARCHAR);
            call.execute();
            System.out.println("respuesta guardar editar-->" + call.getString(8));
            if (call.getString(8).equals("0|")) {
                respuesta = true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("ERROR guardar-->" + e);
            respuesta = false;
        } finally {
            cerrarConexion();
        }

        return respuesta;
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
