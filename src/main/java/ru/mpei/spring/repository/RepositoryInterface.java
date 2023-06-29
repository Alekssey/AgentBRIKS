package ru.mpei.spring.repository;

import ru.mpei.spring.beans.Measurement;

import java.util.List;

public interface RepositoryInterface {

    public void save(Measurement m);

    public List<Measurement> getMeasurements(int startIndex, int endIndex);
}
