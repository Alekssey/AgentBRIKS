package ru.mpei.briks.appRepositoryLayer;


import ru.mpei.briks.extention.dto.Measurement;

import java.util.List;

public interface RepositoryInterface {

    public void save(Measurement m);

    public List<Measurement> getMeasurements(int startIndex, int endIndex);
}
