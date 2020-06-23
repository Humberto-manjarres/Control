/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoSesiones;
import Maestros.Sesiones;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaSesiones")
@ViewScoped
public class LogicaSesiones {
    private List<Sesiones> listaSesiones=new ArrayList<>();
    DaoSesiones daoSesiones=new DaoSesiones();
    private int totalSesiones=0;
    private Sesiones sessiones;
    PrimeFaces instance = PrimeFaces.current();
    
    public int getTotalSesiones() {
        return totalSesiones;
    }

    public void setTotalSesiones(int totalSesiones) {
        this.totalSesiones = totalSesiones;
    }
    

    public Sesiones getSessiones() {
        return sessiones;
    }

    public void setSessiones(Sesiones sessiones) {
        this.sessiones = sessiones;
    }
    

    public LogicaSesiones() throws SQLException {
        get_Sesiones();
    }
    
    public List<Sesiones> getListaSesiones() {
        return listaSesiones;
    }

    public void setListaSesiones(List<Sesiones> listaSesiones) {
        this.listaSesiones = listaSesiones;
    }
    
     public void get_Sesiones() throws SQLException{
        try {
            listaSesiones.clear();
            listaSesiones=daoSesiones.getSesiones();
            totalSesiones=0;
            for (Sesiones listaSesione : listaSesiones) {
                totalSesiones=totalSesiones+1;
            }
            
        } catch (Exception e) {
           
        }
        PrimeFaces.current().ajax().update("formSesion");
       //RequestContext.getCurrentInstance().update("formulario"); 
    }
    
     public void eliminarSesion() throws SQLException{
        System.out.println("SESION ELIMINADA");
        String respuestaDelete="";
        try {
            System.out.println("sesion a enviar-->"+sessiones.getSesion()+" usuario a enviar-->"+sessiones.getUsuario());
            respuestaDelete=daoSesiones.removeSesion(sessiones.getSesion(),sessiones.getUsuario());
            get_Sesiones();
        } catch (Exception e) {
            //RequestContext.getCurrentInstance().execute("cualquierErrorBD('"+e+"');");
        }
        //RequestContext.getCurrentInstance().update("formulario");
    }
    
    
}
