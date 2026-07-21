/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import javax.swing.JFrame;

/**
 *
 * @author HOME
 */
public class Cita extends JFrame {

    private int idCita;
    private int idCliente;
    private int idProfesional;
    private int idServicio;
    private String fechaHoraProgramada; // Formato "yyyy-MM-dd HH:mm:ss"
    private String estado;

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
}
