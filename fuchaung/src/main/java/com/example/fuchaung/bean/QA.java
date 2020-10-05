package com.example.fuchaung.bean;

public class QA {
    String PossibleQ;//可能问
    String StandardQ;//标准问

    public QA(String possibleQ, String standardQ) {
        PossibleQ = possibleQ;
        StandardQ = standardQ;
    }

    public String getPossibleQ() {
        return PossibleQ;
    }

    public void setPossibleQ(String possibleQ) {
        PossibleQ = possibleQ;
    }

    public String getStandardQ() {
        return StandardQ;
    }

    public void setStandardQ(String standardQ) {
        StandardQ = standardQ;
    }
}
