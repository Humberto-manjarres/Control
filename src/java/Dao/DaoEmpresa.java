/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Conexion;
import Maestros.Empresa;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Humberto Manjarres
 */
public class DaoEmpresa extends Conexion{

    Connection con = null;
    CallableStatement call = null;
    ResultSet rs = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    Empresa empresa;
    List<Empresa> listaEmpresa;
    
    public List<Empresa> getEmpresa(String codEmpresa,String patron) throws ClassNotFoundException{
        listaEmpresa= new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetempresas(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, codEmpresa);
            call.setString(4, patron);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                if (rs.next()) {
                    empresa=new Empresa();
                    System.out.println("Nombre Empresa --> "+rs.getString("nombre"));
                }
            }else{}
        } catch (Exception e) {
        }
        return listaEmpresa;
        
    }

}
