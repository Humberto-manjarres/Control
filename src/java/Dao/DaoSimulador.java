/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Maestros.Conexion;
import Maestros.Simulador;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Humberto Manjarres
 */
public class DaoSimulador extends Conexion implements Serializable {

    Connection con = null;
    CallableStatement call = null;
    ResultSet rs = null;
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    Simulador simulacro;
    List<Simulador> listaSimulacro;

    public List<Simulador> getSimulacro(String prestamo,int ncuotas) {
        listaSimulacro = new ArrayList<>();
        try {
            
        } catch (Exception e) {
        }
        return listaSimulacro;
    }

}
