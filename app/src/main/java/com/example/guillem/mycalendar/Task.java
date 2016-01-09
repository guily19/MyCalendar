package com.example.guillem.mycalendar;

import java.util.Date;

/**
 * Created by guillem on 7/1/16.
 */
public class Task {
    private String descripcio;
    private String dataIni;
    private String dataFi;
    private int teRelacio;
    private String Meeting;


    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getDataIni() {
        return dataIni;
    }

    public void setDataIni(String dataIni) {
        this.dataIni = dataIni;
    }

    public String getDataFi() {
        return dataFi;
    }

    public void setDataFi(String dataFi) {
        this.dataFi = dataFi;
    }

    public int getTeRelacio() {
        return teRelacio;
    }

    public void setTeRelacio(int teRelacio) {
        this.teRelacio = teRelacio;
    }

    public String getMeeting() {
        return Meeting;
    }

    public void setMeeting(String meeting) {
        Meeting = meeting;
    }
}
