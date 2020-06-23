/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Cliente;
import Maestros.Conexion;
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
public class DaoClientes extends Conexion implements Serializable {

    Connection con = null;
    CallableStatement call = null;
    ResultSet rs = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    List<Cliente> listaCliente;
    Cliente cliente;

    public List<Cliente> getClientes(String id, String patron) throws SQLException {
        listaCliente = new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetclientes(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, id);
            call.setString(4, patron);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            System.out.println("respuesta getUsuario -> "+call.getString(5));
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdentificacion(rs.getString("identificacion"));
                    cliente.setNombre(rs.getString("nombre"));
                    System.out.println("nombre cliente -> "+cliente.getNombre());
                    cliente.setApellidos(rs.getString("apellidos"));
                    cliente.setEmpresa(rs.getString("empresa"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setError(call.getString(5));
                    listaCliente.add(cliente);
                }
            }else{
                cliente = new Cliente();
                cliente.setError(call.getString(5));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error DaoCliente -> "+e.getMessage());
        }finally{
            cerrarConexion();
        }
        return listaCliente;
    }
    
    public List<Cliente> getClienteDialog(String id, String accion){
        List<Cliente> listaClientesDialog=new ArrayList<>();
        try {
            con=conectar();
            call = con.prepareCall("call _spgetclientes(?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setString(3, id);
            call.setString(4, accion);
            call.registerOutParameter(5, Types.VARCHAR);
            call.execute();
            if (call.getString(5).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdentificacion(rs.getString("identificacion"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellidos(rs.getString("apellidos"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmpresa(rs.getString("empresa"));
                    cliente.setDireccion(rs.getString("direccion"));
                    System.out.println("Cliente -> "+rs.getString("nombre"));
                    listaClientesDialog.add(cliente);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error daoCliente -> "+e);
        }
        return listaClientesDialog;
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
