/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.util.Date;

/**
 *
 * @author USUARIO
 */
public class Mantenimiento {

    private int idMantenimiento;
    private int idHerramienta;
    private String nombreHerramienta;
    private String marca;
    private String fechaHora;
    private String observaciones;
    private String tipoMantenimiento;
    private Date fechaProgramada;
    private int idTecnicoResponsable;

    // --- Constructor Vacío ---
    public Mantenimiento() {
    }

    // --- Constructor Parametrizado (El que estás llamando) ---
    public Mantenimiento(int idHerramienta, String tipoMantenimiento, Date fechaProgramada, String observaciones, int idTecnicoResponsable) {
        this.idHerramienta = idHerramienta;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fechaProgramada = fechaProgramada;
        this.observaciones = observaciones;
        this.idTecnicoResponsable = idTecnicoResponsable;
    }

    // --- Getters y Setters ---
    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public int getIdHerramienta() {
        return idHerramienta;
    }

    public void setIdHerramienta(int idHerramienta) {
        this.idHerramienta = idHerramienta;
    }

    public String getNombreHerramienta() {
        return nombreHerramienta;
    }

    public void setNombreHerramienta(String nombreHerramienta) {
        this.nombreHerramienta = nombreHerramienta;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public Date getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(Date fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public int getIdTecnicoResponsable() {
        return idTecnicoResponsable;
    }

    public void setIdTecnicoResponsable(int idTecnicoResponsable) {
        this.idTecnicoResponsable = idTecnicoResponsable;
    }
}
