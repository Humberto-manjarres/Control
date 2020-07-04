/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoSimulador;
import Maestros.Simulador;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaSimulador")
@ViewScoped
public class LogicaSimulador implements Serializable{
    private String foco = "formSimulador:prestamo";
    Simulador simulador = new Simulador();
    List<Simulador> listaSimulacro;
    DaoSimulador daoSimulador;

    public String getFoco() {
        return foco;
    }

    public void setFoco(String foco) {
        this.foco = foco;
    }
    
    public Simulador getSimulador() {
        return simulador;
    }

    public void setSimulador(Simulador simulador) {
        this.simulador = simulador;
    }

    public List<Simulador> getListaSimulacro() {
        return listaSimulacro;
    }

    public void setListaSimulacro(List<Simulador> listaSimulacro) {
        this.listaSimulacro = listaSimulacro;
    }
    
    public void calcular(){
        listaSimulacro = new ArrayList<>();
        daoSimulador = new DaoSimulador();
        try {
            System.out.println("prestamo -> "+simulador.getPrestamo());
            String replace = simulador.getPrestamo().replaceAll("\\.","");
            System.out.println("replace--> "+replace);
            listaSimulacro = daoSimulador.getSimulacro(simulador.getPrestamo(),simulador.getCuotas());
        } catch (Exception e) {
        }
    }
    
}
