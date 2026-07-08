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
    private String tipoMantenimiento;
    private Date fechaProgramada;
    private String evidenciaNotas;
    private int idTecnicoResponsable;

    public Mantenimiento(int idMantenimiento, int idHerramienta, String tipoMantenimiento, Date fechaProgramada, String evidenciaNotas, int idTecnicoResponsable) {
        this.idMantenimiento = idMantenimiento;
        this.idHerramienta = idHerramienta;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fechaProgramada = fechaProgramada;
        this.evidenciaNotas = evidenciaNotas;
        this.idTecnicoResponsable = idTecnicoResponsable;
    }

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

    public String getEvidenciaNotas() {
        return evidenciaNotas;
    }

    public void setEvidenciaNotas(String evidenciaNotas) {
        this.evidenciaNotas = evidenciaNotas;
    }

    public int getIdTecnicoResponsable() {
        return idTecnicoResponsable;
    }

    public void setIdTecnicoResponsable(int idTecnicoResponsable) {
        this.idTecnicoResponsable = idTecnicoResponsable;
    }

    public Mantenimiento() {
    }

}
