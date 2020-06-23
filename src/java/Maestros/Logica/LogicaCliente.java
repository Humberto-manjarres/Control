/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoClientes;
import Maestros.Cliente;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaCliente")
@ViewScoped
public class LogicaCliente implements Serializable{
    private Cliente cliente = new Cliente();
    private String foco = "formCliente:identificacion";
    PrimeFaces instance = PrimeFaces.current();
    DaoClientes daoCliente;
    List<Cliente> listaCliente;
    private List<Cliente> listaClientesConsultados = new ArrayList<>();
    private Cliente seleccionarCliente;
    private String labelBtnGuardar = "Guardar";
    private boolean disableBtnGuardar = true;
    private String iconoBtnGuardar = "fa fa-save";
    private boolean disableIdentificacion = false;

    public boolean isDisableIdentificacion() {
        return disableIdentificacion;
    }

    public void setDisableIdentificacion(boolean disableIdentificacion) {
        this.disableIdentificacion = disableIdentificacion;
    }
    

    public String getIconoBtnGuardar() {
        return iconoBtnGuardar;
    }

    public void setIconoBtnGuardar(String iconoBtnGuardar) {
        this.iconoBtnGuardar = iconoBtnGuardar;
    }

    public boolean isDisableBtnGuardar() {
        return disableBtnGuardar;
    }

    public void setDisableBtnGuardar(boolean disableBtnGuardar) {
        this.disableBtnGuardar = disableBtnGuardar;
    }

    public String getLabelBtnGuardar() {
        return labelBtnGuardar;
    }

    public void setLabelBtnGuardar(String labelBtnGuardar) {
        this.labelBtnGuardar = labelBtnGuardar;
    }

    public Cliente getSeleccionarCliente() {
        return seleccionarCliente;
    }

    public void setSeleccionarCliente(Cliente seleccionarCliente) {
        this.seleccionarCliente = seleccionarCliente;
    }
    
    public List<Cliente> getListaClientesConsultados() {
        return listaClientesConsultados;
    }

    public void setListaClientesConsultados(List<Cliente> listaClientesConsultados) {
        this.listaClientesConsultados = listaClientesConsultados;
    }
    
    public String getFoco() {
        return foco;
    }

    public void setFoco(String foco) {
        this.foco = foco;
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void buscarCliente(){
        listaClientesConsultados.clear();
        cliente.setClienteDgl("");
        foco = "formularioBuscarClientes:clienteDgl";
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formularioBuscarClientes");
        instance.executeScript("PF('dlgBuscarClientew').show()");
    }
    
    public void get_cliente(){
        daoCliente = new DaoClientes();
        listaCliente = new ArrayList<>();
        String dato = "";
        try {
            listaCliente = daoCliente.getClientes(cliente.getIdentificacion(),"");
            for (Cliente cliente1 : listaCliente) {
                dato = cliente1.getError();
                System.out.println("dato -> "+dato);
            }
            if (dato.equals("0|")) {
                if (!listaCliente.isEmpty()) {
                    for (Cliente cliente1 : listaCliente) {
                        cliente.setEmpresa(cliente1.getEmpresa());
                        cliente.setNombre(cliente1.getNombre());
                        cliente.setApellidos(cliente1.getApellidos());
                        cliente.setTelefono(cliente1.getTelefono());
                        cliente.setDireccion(cliente1.getDireccion());
                    }
                }
                disableIdentificacion = true;
                labelBtnGuardar = "Editar";
                disableBtnGuardar = false;
                iconoBtnGuardar = "fas fa-edit";
            }else if(dato.equals("")){
                instance.executeScript("crearClientes();");
            }else{
                limpiar();
                instance.executeScript("sesion()");
            }
            
            PrimeFaces.current().ajax().update("formCliente");
        } catch (SQLException e) {
        }
    }
    
    
    public void get_clientesLista(){
        daoCliente = new DaoClientes();
        if (cliente.getClienteDgl().equals("")) {
            listaClientesConsultados = daoCliente.getClienteDialog("","%");
        }else{
            listaClientesConsultados = daoCliente.getClienteDialog("","%"+cliente.getClienteDgl()+"%");
        }
        
        if (listaClientesConsultados.isEmpty()) {
            System.out.println("lista Clientes dialogo está vacia");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontraron Clientes con los parametros de busqueda"));
        }

        PrimeFaces.current().ajax().update("formularioBuscarClientes");
        
    }
    
    
    public void seleccionCliente(SelectEvent<Cliente> even){
        cliente.setIdentificacion(even.getObject().getIdentificacion());
        get_cliente();
        instance.executeScript("PF('dlgBuscarClientew').hide()");
        
    }
    
    public void limpiar(){
        cliente.setIdentificacion("");
        cliente.setEmpresa("");
        cliente.setNombre("");
        cliente.setApellidos("");
        cliente.setTelefono("");
        cliente.setDireccion("");
        disableIdentificacion = false;
        disableBtnGuardar = true;
        labelBtnGuardar = "Guardar";
        iconoBtnGuardar = "fa fa-save";
        foco = "formCliente:identificacion";
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formCliente");
    }
    
}
