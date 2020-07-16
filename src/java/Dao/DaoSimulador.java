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
import java.sql.Types;
import java.text.DecimalFormat;
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
    Simulador simulador;
    List<Simulador> listaSimulacro;
    DecimalFormat formatea;

    public List<Simulador> getSimulacro(int prestamo,int ncuotas, String tipoC, int cuotaPagar, int capital) {
        listaSimulacro = new ArrayList<>();
        try {
            con = conectar();
            call = con.prepareCall("call _spgetsimulador(?,?,?,?,?,?,?,?)");
            call.setString(1, session.getAttribute("idSession") + "");
            call.setString(2, session.getAttribute("usuario") + "");
            call.setInt(3, prestamo);
            call.setInt(4, ncuotas);
            call.setString(5, tipoC);
            call.setInt(6, cuotaPagar);
            call.setInt(7, capital);
            call.registerOutParameter(8, Types.VARCHAR);
            call.execute();
            System.out.println("respuesta getSimulador -> "+call.getString(8));
            if (call.getString(8).equals("0|")) {
                rs = call.getResultSet();
                while (rs.next()) {
                    formatea = new DecimalFormat("###,###.##");
                    simulador = new Simulador();
                    simulador.setId(rs.getString("id"));
                    simulador.setCuota(formatea.format(Integer.parseInt(rs.getString("cuota"))));
                    simulador.setCapitalPagar(formatea.format(Integer.parseInt(rs.getString("capitalPagar"))));
                    simulador.setFechaPago(rs.getString("fechaPago"));
                    
                    listaSimulacro.add(simulador);
                }
                
            }
        } catch (Exception e) {
            System.out.println("error getSimulacro -> "+e);
        }
        return listaSimulacro;
    }

}
