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

    // Atributos privados (Variables básicas de la tabla)
    private int idMantenimiento;
    private int idHerramienta;
    private String tipoMantenimiento;
    private Date fechaProgramada;
    private String evidenciaNotas;
    private int idTecnicoResponsable;

    // 1. Constructor vacío (Obligatorio en Java para inicializaciones limpias)
    public Mantenimiento() {
    }

    // 2. Constructor lleno (Te permite empaquetar los datos rápidamente en el Controlador)
    public Mantenimiento(int idHerramienta, String tipoMantenimiento, Date fechaProgramada, String evidenciaNotas, int idTecnicoResponsable) {
        this.idHerramienta = idHerramienta;
        this.tipoMantenimiento = tipoMantenimiento;
        this.fechaProgramada = fechaProgramada;
        this.evidenciaNotas = evidenciaNotas;
        this.idTecnicoResponsable = idTecnicoResponsable;
    }

    // 3. Métodos Getters y Setters normales para leer y escribir las propiedades
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
}
