package mx.ipn.escom.agendaeventosapp.beans;

import io.realm.RealmObject;

public class Evento extends RealmObject {
    private String tipoEvento;
    private String descripcion;
    private String fecha, hora;
    private String statusEvento;

    public Evento() {
    }

    public Evento(String tipoEvento, String descripcion, String fecha, String hora, String statusEvento) {
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.statusEvento = statusEvento;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getStatusEvento() {
        return statusEvento;
    }

    public void setStatusEvento(String statusEvento) {
        this.statusEvento = statusEvento;
    }
}
