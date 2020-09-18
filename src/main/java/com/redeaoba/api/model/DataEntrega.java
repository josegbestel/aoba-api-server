package com.redeaoba.api.model;

import com.redeaoba.api.model.enums.DiaSemana;

import java.io.Serializable;
import java.time.LocalDate;

public class DataEntrega implements Comparable<DataEntrega> {

    private DiaSemana diaSemana;
    private LocalDate data;

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public static DataEntrega toLocalDate(LocalDate dt, DiaSemana diaSemana) {
        DataEntrega dataEntrega = new DataEntrega();
        dataEntrega.setData(dt);
        dataEntrega.setDiaSemana(diaSemana);

        return dataEntrega;
    }

    public boolean compare(DataEntrega dt){

        if(this.diaSemana == dt.getDiaSemana()
                && this.data.getDayOfMonth() == dt.getData().getDayOfMonth()
                && this.data.getMonth().getValue() == dt.getData().getMonth().getValue()
                && this.data.getYear() == dt.getData().getYear()){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(DataEntrega o) {
        return this.getData().compareTo(o.getData());
    }
}
