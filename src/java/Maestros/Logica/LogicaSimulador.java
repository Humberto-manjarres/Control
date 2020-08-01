/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Maestros.Logica;

import Dao.DaoSimulador;
import Maestros.Simulador;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Humberto Manjarres
 */
@ManagedBean(name = "LogicaSimulador")
@ViewScoped
public class LogicaSimulador implements Serializable {

    private String foco = "formSimulador:prestamo";
    Simulador simulador = new Simulador();
    List<Simulador> listaSimulacro = new ArrayList<>();
    DaoSimulador daoSimulador;
    PrimeFaces instance = PrimeFaces.current();

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

    public void calcular() throws ParseException {
        //listaSimulacro = new ArrayList<>();
        daoSimulador = new DaoSimulador();
        float valorCuota = 0;
        int interesTotal = 0, capital = 0, numeroCuotas = 0, cuotaPagar = 0;
        try {
            String replace = simulador.getPrestamo().replaceAll("\\.", "");
            int prestamo = Integer.parseInt(replace);
            valorCuota = prestamo * simulador.getPorcentaje() / 100;
            System.out.println("---------------------------------------------");
            System.out.println("prestamo -> " + prestamo);
            System.out.println("cuotas prestamos sin interes-> " + valorCuota);

            if (simulador.getTipoCuotas().equals("2")) {

                if (simulador.getCuotas() % 2 != 0) {
                    if (simulador.getCuotas() == 1) {
                        int b = Math.round(valorCuota);//valor cuota sin interes
                        capital = prestamo + b;
                        System.out.println("capitalizado una cuota --> " + capital);
                    } else {
                        int b = Math.round(valorCuota);//valor cuota sin interes
                        int resta = simulador.getCuotas() - 1;
                        numeroCuotas = resta / 2;//la mitad de las cuotas sin 1
                        interesTotal = b * numeroCuotas;
                        int interesesIndividuales = interesTotal / numeroCuotas;//interes individual x mes
                        int interesRestante = interesesIndividuales / 2;//interes restante de -1
                        int totalInteresesImpares = interesTotal + interesRestante;
                        //System.out.println("intereses individuales -> "+ interesesIndividuales +" interes restante -1 -> "+interesRestante);
                        System.out.println("interes total impares -> " + totalInteresesImpares);
                        capital = prestamo + totalInteresesImpares;
                        System.out.println("capitalizado -> " + capital);
                        cuotaPagar = capital / simulador.getCuotas();
                        System.out.println("N° de cuotas quincenales -> " + simulador.getCuotas() + " de -> " + cuotaPagar);
                    }
                } else if (simulador.getCuotas() % 2 == 0) {
                    int b = Math.round(valorCuota);//valor cuota sin interes
                    numeroCuotas = simulador.getCuotas() / 2;// numero de cuotas llevadas a mes
                    interesTotal = b * numeroCuotas;// interes total
                    capital = prestamo + interesTotal;

                    System.out.println("Interes -> " + interesTotal);
                    System.out.println("capitalizado -> " + capital);
                    System.out.println("N° de cuotas -> " + simulador.getCuotas() + " valor -> " + capital / simulador.getCuotas());
                }
                cuotaPagar = capital / simulador.getCuotas();
            } else if(simulador.getTipoCuotas().equals("3")) {
                System.out.println("mensual");
                
            }

            /*
            Calendar fecha = new GregorianCalendar();
            int año = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH);
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            System.out.println("Fecha Actual: " + dia + "/" + (mes+1) + "/" + año);
             */
 /*
            if (simulador.getTipoCuotas().equals("2")) {
                System.out.println("15");
                float v = valorCuota / 15;
                int redondeo = Math.round(v);
                System.out.println("intereses quincenales -> " + v + " redondeo -> " + redondeo);
            } else {
                System.out.println("30");
            }
             */
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            System.out.println("fecha pago --> "+sdf.format(simulador.getDiaPago()));
            String d = sdf.format(simulador.getDiaPago());
            listaSimulacro = daoSimulador.getSimulacro(prestamo, simulador.getCuotas(), simulador.getTipoCuotas(), cuotaPagar, capital,d);
            PrimeFaces.current().ajax().update("formSimulador:tablaSimulador");
            
            
            
            DecimalFormat formatea = new DecimalFormat("###,###.##");
            System.out.println("formateando cuota  -> "+formatea.format(1000));
        } catch (NumberFormatException e) {
            System.out.println("error -> " + e);
        }
    }
    
    public void cancelar(){
        simulador.setPrestamo("");
        simulador.setTipoCuotas("");
        simulador.setDiaPago(null);
        foco="formSimulador:prestamo";
        listaSimulacro.clear();
        instance.executeScript("limpiarCampos()");
        PrimeFaces.current().ajax().update("foco");
        PrimeFaces.current().ajax().update("formSimulador");
    }

}
