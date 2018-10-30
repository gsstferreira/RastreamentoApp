package br.usp.pcs2018.rastreamentopacotesapp.Models;

import java.util.Date;

public class Localizacao {

    private double Latitude;
    private double Longitude;
    private Date HorarioAmostra;

    public Localizacao() {}

    public Date getHorarioAmostra() {
        return HorarioAmostra;
    }

    public void setHorarioAmostra(Date horarioAmostra) {
        HorarioAmostra = horarioAmostra;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
