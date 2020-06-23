/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "Login")
@ViewScoped
public class Login implements Serializable{
    private String usuario;
    private String clave;
    private String catpcha;

    public Login() {
    }

    public Login(String usuario, String clave, String catpcha) {
        this.usuario = usuario;
        this.clave = clave;
        this.catpcha = catpcha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCatpcha() {
        return catpcha;
    }

    public void setCatpcha(String catpcha) {
        this.catpcha = catpcha;
    }
    
    
    
    
    
}
