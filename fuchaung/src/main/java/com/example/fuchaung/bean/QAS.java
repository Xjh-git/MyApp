package com.example.fuchaung.bean;

public class QAS implements Comparable<QAS> {
    QA qa;
    double sim;

    public QAS(QA qa, double sim) {
        this.qa = qa;
        this.sim = sim;
    }

    public void setQa(QA qa) {
        this.qa = qa;
    }

    public void setSim(double sim) {
        this.sim = sim;
    }

    public QA getQa() {
        return qa;
    }

    public double getSim() {
        return sim;
    }

    @Override
    public int compareTo(QAS o) {
        return (int) ((o.getSim() - this.sim) * 100);
    }
}
