/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Conexion;
import Maestros.Login;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Humberto Manjarres
 */
public class DaoLogin extends Conexion implements Serializable {

    Connection con = null;
    CallableStatement call = null;
    ResultSet rs = null;
    Usuario usu;
    
    public List<Usuario> login(String usuario, String clave, String idsesion) throws SQLException, ClassNotFoundException {
        List<Usuario> listaUsuario = new ArrayList<>();
        try {
            con=conectar();
            call = con.prepareCall("{call _splogin(?,?,?,?,?)}");
            call.setString(1, usuario);
            call.setString(2, clave);
            call.setString(3, ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr());
            call.setString(4, idsesion);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            System.out.println("prespuesta ->"+call.getString(5));
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                if (rs.next()) {
                    usu = new Usuario();
                    usu.setUsuario(rs.getString("usuario"));
                    usu.setNombre(rs.getString("nombre"));
                    usu.setSexo(rs.getString("sexo"));
                    listaUsuario.add(usu);
                    System.out.println("USUARIO -> "+rs.getString("usuario"));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error desde Dao-> "+e);
            throw new SQLException(e);
        }finally{
            cerrarConexion();
        }
        return listaUsuario;
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
