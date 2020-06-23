/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaPlantilla")
@ViewScoped
public class LogicaPlantilla {
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    
    public void logoutPagina() throws IOException{
        session.invalidate();
        System.out.println("sesion ivalidate");
    }
    
    public void logout() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
    
    public void logouTodo() throws IOException{
        session.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
    
}
