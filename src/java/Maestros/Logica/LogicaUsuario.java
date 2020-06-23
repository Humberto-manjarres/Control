/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoUsuarios;
import Maestros.Usuario;
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
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaUsuario")
@ViewScoped
public class LogicaUsuario implements Serializable {

    private String foco = "formUser:idUsuario";
    private Usuario usuario = new Usuario();
    DaoUsuarios daoUsuarios;
    private List<String> listaPerfilesAsignadas = new ArrayList<>();
    private List<String> listaPerfilesNoAsignadas = new ArrayList<>();
    private DualListModel<String> listaPerfiles = new DualListModel<>(listaPerfilesNoAsignadas, listaPerfilesAsignadas);
    List<Usuario> listaUsuarios;
    private boolean disableUsuario = false;
    private String labelBtnGuardar = "Guardar";
    private String iconoBtnGuardar = "fa fa-save";
    private boolean disableBtnGuardar = true;
    PrimeFaces instance = PrimeFaces.current();
    private List<Usuario> listaUsuariosConsultados = new ArrayList<>();
    private Usuario seleccionarUsuario;

    public Usuario getSeleccionarUsuario() {
        return seleccionarUsuario;
    }

    public void setSeleccionarUsuario(Usuario seleccionarUsuario) {
        this.seleccionarUsuario = seleccionarUsuario;
    }

    public List<Usuario> getListaUsuariosConsultados() {
        return listaUsuariosConsultados;
    }

    public void setListaUsuariosConsultados(List<Usuario> listaUsuariosConsultados) {
        this.listaUsuariosConsultados = listaUsuariosConsultados;
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

    public boolean isDisableUsuario() {
        return disableUsuario;
    }

    public void setDisableUsuario(boolean disableUsuario) {
        this.disableUsuario = disableUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFoco() {
        return foco;
    }

    public void setFoco(String foco) {
        this.foco = foco;
    }

    public DualListModel<String> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(DualListModel<String> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    public void get_Usuario() {
        String dato="";
        daoUsuarios = new DaoUsuarios();
        listaUsuarios = new ArrayList<>();
        try {
            listaUsuarios = daoUsuarios.getUsuarios(usuario.getUsuario(), "");
            for (Usuario listaUsuario : listaUsuarios) {
                dato = listaUsuario.getError();
                System.out.println("resultado-> "+listaUsuario.getError());
            }
            if (dato.equals("0|")) {
                System.out.println("TODO OK");
            }else{
                instance.executeScript("sesion()");
                System.out.println("ERROR SESION MUERTA!");
            }
            if (!listaUsuarios.isEmpty()) {
                for (Usuario listaUsuario : listaUsuarios) {
                    usuario.setNombre(listaUsuario.getNombre());
                    usuario.setApellidos(listaUsuario.getApellidos());
                    usuario.setEstado(listaUsuario.getEstado());
                    usuario.setSexo(listaUsuario.getSexo());
                    usuario.setMarcaAgua("Contraseña Registrada.");
                }
                disableUsuario = true;
                labelBtnGuardar = "Editar";
                iconoBtnGuardar = "fas fa-edit";
                disableBtnGuardar = false;
                get_Perfiles();
            } else {
                instance.executeScript("crearUsuario()");
            }
            PrimeFaces.current().ajax().update("formUser");

        } catch (SQLException e) {
            System.out.println("SQLExceptio -> " + e);
        }
    }

    public void get_Perfiles() {
        listaPerfilesAsignadas.clear();
        listaPerfilesNoAsignadas.clear();
        daoUsuarios = new DaoUsuarios();
        try {
            this.listaPerfilesNoAsignadas = daoUsuarios.getPerfilesNoAsignados();
            this.listaPerfilesAsignadas = daoUsuarios.getPerfilesAsignados(usuario.getUsuario());
        } catch (SQLException ex) {

        }

        for (String perfil : listaPerfilesAsignadas) {
            for (String tarea2 : listaPerfilesNoAsignadas) {
                if (tarea2.equals(perfil)) {
                    listaPerfilesNoAsignadas.remove(tarea2);
                    break;
                }
            }
        }
        listaPerfiles = new DualListModel<>(listaPerfilesNoAsignadas, listaPerfilesAsignadas);

    }

    public void limpiar() {
        System.out.println("limpiar usuarios");
        usuario.setUsuario("");
        usuario.setNombre("");
        usuario.setApellidos("");
        usuario.setEstado("1");
        usuario.setSexo("1");
        usuario.setClave("");
        usuario.setClave2("");
        usuario.setMarcaAgua("");
        labelBtnGuardar = "Guardar";
        listaPerfilesAsignadas.clear();
        listaPerfilesNoAsignadas.clear();
        disableUsuario = false;
        iconoBtnGuardar = "fa fa-save";
        disableBtnGuardar = true;
        listaPerfiles = new DualListModel<>(listaPerfilesNoAsignadas, listaPerfilesAsignadas);
        foco = "formUser:idUsuario";
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formUser");
    }

    public void cargarPerfiles() {
        disableBtnGuardar = false;
        disableUsuario = true;
        foco = "formUser:idUsuario";
        get_Perfiles();
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formUser");
    }

    public void guardarEditar() {
        String trama = "";
        String respuesta = "";
        if (labelBtnGuardar.equals("Guardar")) {
            for (String listaP : listaPerfiles.getTarget()) {
                trama += listaP.substring(listaP.indexOf("-") + 2, listaP.length()) + "|";
            }

            try {
                if (trama.equals("")) {
                    instance.executeScript("trama();");
                } else {
                    if (usuario.getClave().equals(usuario.getClave2())) {
                        respuesta = daoUsuarios.guardarUsuario(usuario.getUsuario(), usuario.getNombre(), usuario.getApellidos(), usuario.getClave(), usuario.getEstado(),
                                "I", trama, usuario.getSexo());
                        if (respuesta.equals("registrado")) {
                            String e = "Usuario Agregado";
                            instance.executeScript("ok('" + e + "');");
                            limpiar();
                        } else {
                            System.out.println("Error al agregar usu-> " + respuesta);
                        }
                    } else {
                        usuario.setClave("");
                        usuario.setClave2("");
                        PrimeFaces.current().ajax().update("formUser:clave");
                        PrimeFaces.current().ajax().update("formUser:clave2");
                        instance.executeScript("pass();");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error agregar usu -> " + ex);
            }
            System.out.println("trama usuarios -> " + trama);
        } else if (labelBtnGuardar.equals("Editar")) {
            for (String listaP : listaPerfiles.getTarget()) {
                trama += listaP.substring(listaP.indexOf("-") + 2, listaP.length()) + "|";
            }

            try {
                if (trama.equals("")) {
                    instance.executeScript("trama();");
                } else {
                    if (usuario.getClave().equals(usuario.getClave2())) {
                        respuesta = daoUsuarios.guardarUsuario(usuario.getUsuario(), usuario.getNombre(), usuario.getApellidos(), usuario.getClave(), usuario.getEstado(),
                                "U", trama, usuario.getSexo());
                        if (respuesta.equals("registrado")) {
                            String e = "Usuario Editado";
                            instance.executeScript("ok('" + e + "');");
                            limpiar();
                        } else {
                            System.out.println("Error al editar usu-> " + respuesta);
                        }
                    } else {
                        usuario.setClave("");
                        usuario.setClave2("");
                        PrimeFaces.current().ajax().update("formUser:clave");
                        PrimeFaces.current().ajax().update("formUser:clave2");
                        instance.executeScript("pass();");
                    }
                }

            } catch (SQLException ex) {
                System.out.println("Error Editar usu -> " + ex);
            }

        }
    }

    public void mostrarBuscarUsuarios() {
        listaUsuariosConsultados.clear();
        usuario.setNombreDgl("");
        foco = "formularioBuscarUsuario:usuarioDgl";
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formularioBuscarUsuario");
        instance.executeScript("PF('dlgBuscarUsuariow').show()");
    }

    public void get_usuarioLista() throws SQLException {
        System.out.println("find user");
        listaUsuariosConsultados.clear();
        daoUsuarios = new DaoUsuarios();
        if (usuario.getNombreDgl().equals("")) {
            listaUsuariosConsultados = daoUsuarios.getUsuarioDialog("", "%");
        } else {
            listaUsuariosConsultados = daoUsuarios.getUsuarioDialog("", "%" + usuario.getNombreDgl() + "%");
        }

        if (listaUsuariosConsultados.isEmpty()) {
            System.out.println("lista perfiles dialogo está vacia");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "No se encontraron Usuarios con los parametros de busqueda"));
        }

        PrimeFaces.current().ajax().update("formularioBuscarUsuario");
    }

    public void seleccionUsu(SelectEvent<Usuario> even) {
        System.out.println("Id seleccionado -> " + even.getObject().getUsuario());
        usuario.setUsuario(even.getObject().getUsuario());
        get_Usuario();
        instance.executeScript("PF('dlgBuscarUsuariow').hide()");

    }

}
