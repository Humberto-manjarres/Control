/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoLogin;
import Maestros.Login;
import Maestros.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LoginLogica")
@ViewScoped
public class LoginLogica implements Serializable{

    private Login login = new Login();
    CallableStatement call = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    DaoLogin daoLogin;
    List<Usuario> listaUsuario = new ArrayList<>();
    private String foco = "formLogin:usuario";
    PrimeFaces instance = PrimeFaces.current();
    private String sexo="iconomujer2";
    
    public String getFoco() {
        return foco;
    }

    public void setFoco(String foco) {
        this.foco = foco;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public LoginLogica() {
        String sistemaOperativo = System.getProperty("os.name");
        System.out.println(sistemaOperativo);
        //session.invalidate();
        if (sistemaOperativo.toLowerCase().contains("windows 10")) {
            session.setAttribute("soservidor", "windows");
        } else {
            session.setAttribute("soservidor", "linux");
        }
        System.out.println("verificar");
    }

    public void logueo() throws Exception {
        
        String usua = "", nomb = "", sex="";
        String sesionId = session.getId();
        daoLogin = new DaoLogin();
        String c = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        System.out.println("catchapt ingresado->"+login.getCatpcha());
        System.out.println("catchapt imagen->"+c);
        if (c.equals(login.getCatpcha())) {

            try {
                listaUsuario = daoLogin.login(login.getUsuario(), login.getClave(), sesionId);
            } catch (Exception e) {
                System.out.println("error LoginLogica --> " + e);
                String error = "Base de datos";
                instance.executeScript("errorBD('" + error + "');");
                return;
            }

            for (Usuario usuario : listaUsuario) {
                usua = usuario.getUsuario();
                nomb = usuario.getNombre();
                sex = usuario.getSexo();
            }
            
            if (!listaUsuario.isEmpty()) {
                session.setAttribute("usuario", usua);
                session.setAttribute("nombre", nomb);
                session.setAttribute("idSession", sesionId);
                if (sex.equals("2")) {
                    sexo = "iconohombre";
                    session.setAttribute("sexo", sexo);
                }else{
                    sexo = "iconomujer2";
                    session.setAttribute("sexo", sexo);
                }
                //instance.ajax().update("form");
                FacesContext.getCurrentInstance().getExternalContext().redirect("plantilla.xhtml");
            } else {
                login.setUsuario("");
                login.setClave("");
                instance.executeScript("erroreUsuario()");
                System.out.println("Respuesta desde loginLogica -> ");
            }

        } else {
            instance.executeScript("catchapt();");
            System.out.println("mensaje error de catchapt");
        }

    }

}
