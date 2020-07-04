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

    public Simulador() {
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
