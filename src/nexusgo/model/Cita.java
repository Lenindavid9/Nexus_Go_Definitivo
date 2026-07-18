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
    private String cliente;
    private String servicio;
    private double precio;
    private String diaSemana;
    private String horaInicio;

    // Constructor vacío por si necesito instanciar una cita sin datos iniciales
    public Cita() {
    }

    // Constructor lleno para cuando recupero los datos completos desde la base de datos
    public Cita(int idCita, String cliente, String servicio, double precio, String diaSemana, String horaInicio) {
        this.idCita = idCita;
        this.cliente = cliente;
        this.servicio = servicio;
        this.precio = precio;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
    }

    // --- Mis métodos Getter y Setter para acceder de forma segura a las variables ---
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
}
