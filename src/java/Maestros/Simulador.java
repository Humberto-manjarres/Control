/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros;

import java.io.Serializable;

/**
 *
 * @author Humberto Manjarres
 */
public class Simulador implements Serializable{
    private String prestamo;
    private int cuotas;
    private float porcentaje;
    private String tipoCuotas;
    
    private String id;
    private String cuota;
    private String capitalPagar;
    private String fechaPago;

    public Simulador() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public String getCapitalPagar() {
        return capitalPagar;
    }

    public void setCapitalPagar(String capitalPagar) {
        this.capitalPagar = capitalPagar;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
    
    
    
    public float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(float porcentaje) {
        this.porcentaje = porcentaje;
    }
    

    public String getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(String prestamo) {
        this.prestamo = prestamo;
    }

    
    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getTipoCuotas() {
        return tipoCuotas;
    }

    public void setTipoCuotas(String tipoCuotas) {
        this.tipoCuotas = tipoCuotas;
    }
    
    
}
