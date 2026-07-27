/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

/**
 *
 * @author INGRID
 */
public class ReporteFinanciero {
    
    private double sumaServicios;
    private double sumaPromociones;
    private double totalNeto;
    private String servicioMes;

    public double getSumaServicios() { return sumaServicios; }
    public void setSumaServicios(double sumaServicios) { this.sumaServicios = sumaServicios; }

    public double getSumaPromociones() { return sumaPromociones; }
    public void setSumaPromociones(double sumaPromociones) { this.sumaPromociones = sumaPromociones; }

    public double getTotalNeto() { return totalNeto; }
    public void setTotalNeto(double totalNeto) { this.totalNeto = totalNeto; }

    public String getServicioMes() { return servicioMes; }
    public void setServicioMes(String servicioMes) { this.servicioMes = servicioMes; }
    
}
