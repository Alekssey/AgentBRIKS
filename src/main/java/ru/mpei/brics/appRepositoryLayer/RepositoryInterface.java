package ru.mpei.brics.appRepositoryLayer;


import ru.mpei.brics.extention.dto.Measurement;

import java.util.List;

public interface RepositoryInterface {

    public void save(Measurement m);

    public List<Measurement> getMeasurements(int startIndex, int endIndex);
}
