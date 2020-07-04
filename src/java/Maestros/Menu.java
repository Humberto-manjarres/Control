/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "Menu")
@ViewScoped
public class Menu extends Conexion implements Serializable {

    private MenuModel model;
    DefaultSubMenu submenu;
    DefaultMenuItem item;
    CallableStatement call = null;
    ResultSet rs = null;
    Connection con = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    PrimeFaces instance = PrimeFaces.current();
    
    public Menu() throws SQLException, ClassNotFoundException, IOException {
        cargar_menu();
    }

    public void cargar_menu() throws SQLException, ClassNotFoundException, IOException {
        System.out.println("sesion id ->" + session.getAttribute("idSession"));
        try {
            model = new DefaultMenuModel();
            con = conectar();
            call = obtener_menu(call);
            System.out.println("Respuesta MYSQL -> "+call.getString(3));
            if (!call.getString(3).equals("0|")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            }
            if (call.getString(3).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    if (rs.getString("Padre") == null) {
                        System.out.println("nombre -> " + rs.getString("nombre"));
                        if (!rs.isFirst()) {
                            model.getElements().add(submenu);
                        }
                        submenu = new DefaultSubMenu();
                        submenu.setLabel(rs.getString("Nombre"));
                        
                    } else {
                        item = new DefaultMenuItem();
                        item.setValue(rs.getString("Nombre"));
                        System.out.println(""+rs.getString("Nombre"));
                        item.setOnclick("cargar('" + rs.getString("Formulario") + "','" + rs.getString("Nombre") + "','" + rs.getString("Icono") + "','" + rs.getString("tarea") + "', '')");
                        item.setIcon(rs.getString("Icono"));
                        item.setUrl("#");
                        submenu.getElements().add(item);
                        if (rs.isLast()) {
                            model.getElements().add(submenu);
                        }
                    }

                }

            }
        } catch (SQLException e) {
            System.out.println("error desde menu --> " + e);
        } finally {
            cerrarConexion();
            conectar().close();
        }
    }

    private CallableStatement obtener_menu(CallableStatement call) throws SQLException {
        call = con.prepareCall("{call _spgetmenu(?,?,?)}");
        call.setString(1, session.getAttribute("usuario") + "");
        call.setString(2, session.getAttribute("idSession") + "");
        call.registerOutParameter(3, Types.VARCHAR);
        call.execute();
        return call;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
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
