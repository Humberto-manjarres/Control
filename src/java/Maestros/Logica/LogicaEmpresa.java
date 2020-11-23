/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoEmpresa;
import Maestros.Empresa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaEmpresa")
@ViewScoped
public class LogicaEmpresa implements Serializable{
    private Empresa empresa = new Empresa();
    private List<Empresa> listaEmpresa;
    DaoEmpresa daoEmpresas;
    
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
    public void get_Empresa(){
        System.out.println("codigo Empresa --> "+empresa.getCodigo());
        daoEmpresas = new DaoEmpresa();
        listaEmpresa = new ArrayList<>();
        try {
            listaEmpresa = daoEmpresas.getEmpresa(empresa.getCodigo(),"");
            
        } catch (Exception e) {
        }
    }
    
}
