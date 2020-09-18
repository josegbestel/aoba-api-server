package com.redeaoba.api.model.enums;

import com.redeaoba.api.exception.DomainException;

public enum DiaSemana {

    SEG,
    TER,
    QUA,
    QUI,
    SEX,
    SAB,
    DOM;

    public String dia;

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public static DiaSemana getByDayOfWeek(int dayOfWeek){
        switch (dayOfWeek){
            case 1:
                return SEG;
            case 2:
                return TER;
            case 3:
                return QUA;
            case 4:
                return QUI;
            case 5:
                return SEX;
            case 6:
                return SAB;
            default:
                return DOM;
        }
    }

    private static int getDayInt(DiaSemana diaSemana){
        if(diaSemana == SEG){
            return 1;
        }else if (diaSemana == TER){
            return 2;
        }else if (diaSemana == QUA){
            return 3;
        }else if (diaSemana == QUI){
            return 4;
        }else if (diaSemana == SEX){
            return 5;
        }else if(diaSemana == SAB){
            return 6;
        }else {
            return 7;
        }
    }

    public static int daysBetween(DiaSemana de, DiaSemana ate){
        int deInt = getDayInt(de);
        int ateInt = getDayInt(ate);

//        return Math.abs(ateInt - deInt);
        return ateInt - deInt;
    }
}
