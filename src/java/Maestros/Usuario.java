/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros;

/**
 *
 * @author Humberto Manjarres
 */
public class Usuario {

    private String nombre = "";
    private String apellidos = "";
    private String usuario = "";
    private String clave = "";
    private String clave2 = "";
    private String clave3 = "";
    private String estado = "";
    private String marcaAgua = "";
    private String sexo="";
    private String nombreDgl;
    private String error;

    public Usuario() {
        
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    

    public String getNombreDgl() {
        return nombreDgl;
    }

    public void setNombreDgl(String nombreDgl) {
        this.nombreDgl = nombreDgl;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getClave2() {
        return clave2;
    }

    public void setClave2(String clave2) {
        this.clave2 = clave2;
    }

    public String getClave3() {
        return clave3;
    }

    public void setClave3(String clave3) {
        this.clave3 = clave3;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMarcaAgua() {
        return marcaAgua;
    }

    public void setMarcaAgua(String marcaAgua) {
        this.marcaAgua = marcaAgua;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    
    
}
