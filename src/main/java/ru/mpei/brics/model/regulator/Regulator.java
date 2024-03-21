package ru.mpei.brics.model.regulator;

public interface Regulator {
    double getSetpoint(double targetValue, double currentValue);
}
