/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.util.Date;
import javax.swing.JFrame;
import java.sql.Time;

/**
 *
 * @author HOME
 */
public class Cita {

    private int idCita;
    private int idCliente;
    private int idProfesional;
    private int idServicio;
    private String fechaHoraProgramada; // Formato "yyyy-MM-dd HH:mm:ss"
    private String estado;
    private Date fechaCita;
    private Time horaCita;
    private String nombreCliente;
    private String nombreServicio;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public Cita() {
        this.estado = "PENDIENTE";
    }

    public Cita(int idCliente, int idProfesional, int idServicio, String fechaHoraProgramada) {
        this.idCliente = idCliente;
        this.idProfesional = idProfesional;
        this.idServicio = idServicio;
        this.fechaHoraProgramada = fechaHoraProgramada;
        this.estado = "PENDIENTE";
    }

    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getFechaHoraProgramada() {
        return fechaHoraProgramada;
    }

    public void setFechaHoraProgramada(String fechaHoraProgramada) {
        this.fechaHoraProgramada = fechaHoraProgramada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }
}
