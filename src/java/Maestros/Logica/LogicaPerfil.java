/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoPerfiles;
import Maestros.Perfil;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaPerfil")
@ViewScoped
public class LogicaPerfil implements Serializable {

    private String foco = "formulario:idePerfil";
    private Perfil perfil = new Perfil();
    DaoPerfiles daoPerfil;
    List<Perfil> listaPerfiles;
    private List<String> listaTareasAsignadas = new ArrayList<>();
    private List<String> listaTareasNoAsignadas = new ArrayList<>();
    private DualListModel<String> listaTareas = new DualListModel<>(listaTareasNoAsignadas, listaTareasAsignadas);
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    List<Perfil> listaPerfilesConsultados = new ArrayList<>();
    private boolean disableIdPerfil = false;
    private Perfil seleccionPerfil;
    PrimeFaces instance = PrimeFaces.current();
    private boolean disableBtnGuardar = true;
    private String labelBtnGuardar = "Guardar";
    private String iconoBtnGuardar = "fa fa-save";

    public String getIconoBtnGuardar() {
        return iconoBtnGuardar;
    }

    public void setIconoBtnGuardar(String iconoBtnGuardar) {
        this.iconoBtnGuardar = iconoBtnGuardar;
    }

    public String getLabelBtnGuardar() {
        return labelBtnGuardar;
    }

    public void setLabelBtnGuardar(String labelBtnGuardar) {
        this.labelBtnGuardar = labelBtnGuardar;
    }

    public boolean isDisableBtnGuardar() {
        return disableBtnGuardar;
    }

    public void setDisableBtnGuardar(boolean disableBtnGuardar) {
        this.disableBtnGuardar = disableBtnGuardar;
    }

    public boolean isDisableIdPerfil() {
        return disableIdPerfil;
    }

    public void setDisableIdPerfil(boolean disableIdPerfil) {
        this.disableIdPerfil = disableIdPerfil;
    }

    public Perfil getSeleccionPerfil() {
        return seleccionPerfil;
    }

    public void setSeleccionPerfil(Perfil seleccionPerfil) {
        this.seleccionPerfil = seleccionPerfil;
    }

    public List<Perfil> getListaPerfilesConsultados() {
        return listaPerfilesConsultados;
    }

    public void setListaPerfilesConsultados(List<Perfil> listaPerfilesConsultados) {
        this.listaPerfilesConsultados = listaPerfilesConsultados;
    }

    public DualListModel<String> getListaTareas() {
        return listaTareas;
    }

    public void setListaTareas(DualListModel<String> listaTareas) {
        this.listaTareas = listaTareas;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getFoco() {
        return foco;
    }

    public void setFoco(String foco) {
        this.foco = foco;
    }

    public void get_Perfil() {
        int id = Integer.parseInt(perfil.getId());
        daoPerfil = new DaoPerfiles();
        listaPerfiles = new ArrayList<>();

        try {
            listaPerfiles = daoPerfil.getDaoPerfiles(id, "");
            if (!listaPerfiles.isEmpty()) {
                for (Perfil listaPerfile : listaPerfiles) {
                    perfil.setEstado(listaPerfile.getEstado());
                    perfil.setNombre(listaPerfile.getNombre());
                }
                disableIdPerfil = true;
                labelBtnGuardar = "Editar";
                iconoBtnGuardar = "fas fa-edit";
                disableBtnGuardar = false;
                get_tareas();
            } else {
                //agregar foco a nombre aquí
                foco = "formPer:nombre";
                PrimeFaces.current().ajax().update("foco");
                System.out.println("Perfil no existe");
                instance.executeScript("crearPerfil()");

            }

            PrimeFaces.current().ajax().update("formPer");
        } catch (Exception ex) {
            System.out.println("exception -> " + ex);
        }

    }

    public void get_tareas() throws ClassNotFoundException, SQLException {

        listaTareasAsignadas.clear();
        listaTareasNoAsignadas.clear();
        daoPerfil = new DaoPerfiles();
        this.listaTareasNoAsignadas = daoPerfil.get_tareasAsignadas();
        this.listaTareasAsignadas = daoPerfil.get_perfilesTareas(perfil.getId());

        for (String tarea : listaTareasAsignadas) {
            for (String tarea2 : listaTareasNoAsignadas) {
                if (tarea2.equals(tarea)) {
                    listaTareasNoAsignadas.remove(tarea2);
                    break;
                }
            }
        }
        listaTareas = new DualListModel<>(listaTareasNoAsignadas, listaTareasAsignadas);
        for (String listaPerfile : listaTareas.getTarget()) {
            System.out.println("listaTareas -> " + listaPerfile);
        }

    }

    public void get_perfilLista() throws SQLException {
        listaPerfilesConsultados.clear();
        daoPerfil = new DaoPerfiles();
        if (perfil.getNombre().equals("")) {
            listaPerfilesConsultados = daoPerfil.get_perfilDialog("", "%");
        } else {
            listaPerfilesConsultados = daoPerfil.get_perfilDialog("", "%" + perfil.getNombre() + "%");
        }

        if (listaPerfilesConsultados.isEmpty()) {
            System.out.println("lista perfiles dialogo está vacia");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontraron Perfiles con los parametros de busqueda"));
        }

        PrimeFaces.current().ajax().update("formBusPer");
    }

    public void seleccionP(SelectEvent<Perfil> even) {
        System.out.println("Id seleccionado -> " + even.getObject().getId());
        getPerfilDialogo(even.getObject().getId());

    }

    public void getPerfilDialogo(String id) {
        perfil.setId(id);
        get_Perfil();
        instance.executeScript("PF('dlgPerw').hide()");
    }

    public void cleanerTable(boolean control) {

        if (control) {
            listaPerfilesConsultados.clear();
            perfil.setNombre("");
            PrimeFaces.current().ajax().update("formBusPer");
        } else {
            instance.executeScript("PF('dlgPerw').hide()");
        }
    }

    public void limpiar() {
        perfil.setId("");
        perfil.setNombre("");
        perfil.setEstado("1");
        foco = "formulario:idePerfil";
        labelBtnGuardar = "Guardar";
        disableBtnGuardar = true;
        listaTareasAsignadas.clear();
        listaTareasNoAsignadas.clear();
        disableIdPerfil = false;
        listaTareas = new DualListModel<>(listaTareasNoAsignadas, listaTareasAsignadas);
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formPer");
    }

    public void cargarTareas() {
        daoPerfil = new DaoPerfiles();
        this.listaTareasAsignadas.clear();
        try {
            this.listaTareasNoAsignadas = daoPerfil.get_tareasAsignadas();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error Cargar para new perfil " + e);
        }
        listaTareas = new DualListModel<>(listaTareasNoAsignadas, listaTareasAsignadas);
        perfil.setEstado("2");
        
        perfil.setNombre("");
        disableIdPerfil = true;
        disableBtnGuardar = false;

        //PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formPer");
    }

    public void guardarEditar() {
        String trama = "";
        daoPerfil = new DaoPerfiles();
        boolean respuestaGuardar = false;
        if (labelBtnGuardar.equals("Guardar")) {
            System.out.println("Guardar perfil");
            for (String tareas : listaTareas.getTarget()) {
                trama += tareas.substring(tareas.indexOf("-") + 2, tareas.length()) + "|";
            }
            if (trama.equals("")) {
                instance.executeScript("trama();");
            } else {
                try {
                    respuestaGuardar = daoPerfil.guardar(perfil.getId(), perfil.getNombre().toUpperCase().trim(), perfil.getEstado(), trama, "I");
                    if (respuestaGuardar) {
                        limpiar();
                        String e = "Perfil Agregado";
                        instance.executeScript("ok('" + e + "');");
                    } else {
                        String e = "Nombre ya existe en el sistema";
                        instance.executeScript("errores('" + e + "');");
                    }
                } catch (SQLException ex) {

                }
            }
            System.out.println("trama -> " + trama);
        } else if (labelBtnGuardar.equals("Editar")) {
            System.out.println("Editar perfil");
            for (String tareas : listaTareas.getTarget()) {
                trama += tareas.substring(tareas.indexOf("-") + 2, tareas.length()) + "|";
            }
            try {
                respuestaGuardar = daoPerfil.guardar(perfil.getId(), perfil.getNombre().toUpperCase().trim(), perfil.getEstado(), trama, "U");
                if (respuestaGuardar) {
                    limpiar();
                    String e = "Perfil Actualizado";
                    instance.executeScript("ok('" + e + "');");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

}
