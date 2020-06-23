/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Conexion;
import Maestros.Perfil;
import Maestros.Usuario;
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
public class DaoUsuarios extends Conexion implements Serializable {

    Connection con = null;
    CallableStatement call = null;
    ResultSet rs = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    List<Usuario> listaUsuario;
    Usuario usuario;
    List<String> listaPerfilesForAsignar;
    private List<Perfil> listaPerfiles;

    public List<Usuario> getUsuarios(String usu, String patron) throws SQLException {
        listaUsuario = new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetusuarios(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, usu);
            call.setString(4, patron);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            System.out.println("respuesta getUsuario -> "+call.getString(5));
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                if (rs.next()) {
                    usuario = new Usuario();
                    System.out.println("nombre-->" + rs.getString("nombre") + " estado-->" + rs.getString("estado") + " sexo-->" + rs.getString("sexo"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setEstado(rs.getString("estado"));
                    usuario.setSexo(rs.getString("sexo"));
                    usuario.setError(call.getString(5));
                    listaUsuario.add(usuario);
                }
            }else{
                usuario = new Usuario();
                usuario.setError(call.getString(5));
                listaUsuario.add(usuario);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error DaoUsuario getUsuarios -> " + ex);
            throw new SQLException(ex);
        } finally {
            cerrarConexion();
        }
        return listaUsuario;
    }
    
    ////////////////////////////////////////////
    public List<String> getPerfilesNoAsignados() throws SQLException {
        try {
            con = conectar();
            listaPerfilesForAsignar = new ArrayList<>();
            DaoPerfiles daoPerfil = new DaoPerfiles();
            listaPerfiles = daoPerfil.get_perfilDialog("", "%");
            for (Perfil listaP : listaPerfiles) {
                listaPerfilesForAsignar.add(listaP.getNombre() + " - " + listaP.getId());
                System.out.println("datos de perfiles--->" + listaP.getNombre() + " - " + listaP.getId());
            }
        } catch (Exception e) {
            System.out.println("ERROR get_perfilesAsignados-->" + e);
        } finally {
            cerrarConexion();
        }
        return listaPerfilesForAsignar;
    }

    public List<String> getPerfilesAsignados(String usu) throws SQLException {
        listaPerfilesForAsignar = new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetperfilesusuarios(?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, usu);
            call.registerOutParameter(4, Types.VARCHAR);
            call.execute();
            System.out.println("prespuesta-->" + call.getString(4));
            if (call.getString(4).equals("0|")) {
                rs = call.getResultSet();
                if (rs.next()) {
                    listaPerfilesForAsignar.add(rs.getString("nombre") + " - " + rs.getString("perfil"));
                    System.out.println("datos de perfiles--->" + rs.getString("nombre") + " - " + rs.getString("perfil"));

                }
            }
        } catch (Exception e) {
            System.out.println("Error getPerfilesAsignados-->" + e);
        } finally {
            cerrarConexion();
        }

        return listaPerfilesForAsignar;
    }
    
    public String guardarUsuario(String usu,String nom,String ape,String contra,String estado,String accion,String trama,String sexo) throws SQLException {
        String respuesta = "no resgistrado";
        try {
            con = conectar();
            call = con.prepareCall("call _spregusuarios(?,?,?,?,?,?,?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, usu);
            call.setString(4, nom);
            call.setString(5, ape);
            call.setString(6, contra);
            call.setString(7, estado);
            call.setString(8, accion);
            call.setString(9, trama);
            call.setString(10, sexo);
            call.registerOutParameter(11, Types.VARCHAR);
            call.execute();
            System.out.println("prespuesta-->" + call.getString(11));
            if (call.getString(11).equals("0|")) {
                respuesta="registrado";
            }
        } catch (Exception e) {
            System.out.println("ERROR guardarUsuario-->"+e);
            respuesta=e+"";
        }finally{
            cerrarConexion();
        }
            
        return respuesta;
    }
    
    public List<Usuario> getUsuarioDialog(String datoUsu,String accion) throws SQLException{
        List<Usuario> listaUsuariosDialog=new ArrayList<>();
        try {
            con=conectar();
            call = con.prepareCall("call _spgetusuarios(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, datoUsu);
            call.setString(4, accion);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    usuario=new Usuario();
                    System.out.println("nombre-->" + rs.getString("nombre"));
                    usuario.setUsuario(rs.getString("usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setEstado(rs.getString("estado"));
                    usuario.setSexo(rs.getString("sexo"));
                    listaUsuariosDialog.add(usuario);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("ERROR getUsuarioDialog-->"+e);
        }finally{
            cerrarConexion();
        }
        
        return listaUsuariosDialog;
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
