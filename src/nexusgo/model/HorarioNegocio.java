/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class HorarioNegocio {
    
  
 
    public static class RangoOcupado {
        public final LocalTime inicio;
        public final LocalTime fin;

        public RangoOcupado(LocalTime inicio, LocalTime fin) {
            this.inicio = inicio;
            this.fin = fin;
        }
    }

    /**
     * Clase DTO para representar un slot individual en la grilla de la UI.
     * Se retiró 'final' de 'disponible' para permitir filtrados según la hora actual.
     */
    public static class SlotDisponibilidad {
        public LocalTime hora;
        public boolean disponible; // Sin 'final' para modificar su estado en ejecución

        public SlotDisponibilidad(LocalTime hora, boolean disponible) {
            this.hora = hora;
            this.disponible = disponible;
        }
    }

    /**
     * Determina si la fecha corresponde a fin de semana (Sábado o Domingo).
     */
    public static boolean esFinDeSemanaOFestivo(LocalDate fecha) {
        if (fecha == null) return false;
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    /**
     * Genera la lista de bloques/slots de tiempo para un día específico
     * considerando la duración del servicio y los rangos ya ocupados.
     */
    public static List<SlotDisponibilidad> generarSlotsDelDia(LocalDate fecha, int duracionMinutos, List<RangoOcupado> rangosOcupados) {
        List<SlotDisponibilidad> slots = new ArrayList<>();

        if (fecha == null || duracionMinutos <= 0) {
            return slots;
        }

        // Definición del horario según el día
        LocalTime horaApertura = LocalTime.of(6, 0);
        LocalTime horaCierre = esFinDeSemanaOFestivo(fecha) ? LocalTime.of(23, 0) : LocalTime.of(21, 0);

        LocalTime actual = horaApertura;

        // Generar intervalos basados en la duración del servicio seleccionado
        while (actual.plusMinutes(duracionMinutos).isBefore(horaCierre) || actual.plusMinutes(duracionMinutos).equals(horaCierre)) {
            LocalTime inicioSlot = actual;
            LocalTime finSlot = actual.plusMinutes(duracionMinutos);

            boolean ocupado = false;

            if (rangosOcupados != null) {
                for (RangoOcupado rango : rangosOcupados) {
                    // Verificación de traslape de rangos
                    if (inicioSlot.isBefore(rango.fin) && finSlot.isAfter(rango.inicio)) {
                        ocupado = true;
                        break;
                    }
                }
            }

            slots.add(new SlotDisponibilidad(inicioSlot, !ocupado));
            
            // Avanzar al siguiente slot
            actual = actual.plusMinutes(duracionMinutos);
        }

        return slots;
    }
}
