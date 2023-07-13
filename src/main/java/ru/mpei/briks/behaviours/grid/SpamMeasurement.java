package ru.mpei.briks.behaviours.grid;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.briks.appServiceLayer.ServiceInterface;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.extention.configirationClasses.GridConfiguration;
import ru.mpei.briks.extention.dto.Measurement;

@Slf4j
public class SpamMeasurement extends TickerBehaviour {
    GridConfiguration cfg;
    long startTime = System.currentTimeMillis();
    int measurementIndex = 1;
    ServiceInterface service = ApplicationContextHolder.getContext().getBean(ServiceInterface.class);
    public SpamMeasurement(Agent a, long period, GridConfiguration cfg) {
        super(a, period);
        this.cfg = cfg;
    }

    @Override
    protected void onTick() {

        double frequency = 50 + 0.01 * (cfg.getGeneratedP() - cfg.getNecessaryP());
        cfg.setF(frequency);
        log.debug("{} spam measurement: f={}, nP={}, nQ={}, gP={}, gQ={}", myAgent.getLocalName(),
                cfg.getF(), cfg.getNecessaryP(), cfg.getNecessaryQ(), cfg.getGeneratedP(), cfg.getGeneratedQ());

        service.saveMeasurementInDB(new Measurement(measurementIndex, (System.currentTimeMillis() - startTime) / 1000L, frequency, 0, 0, 0));
        measurementIndex++;

    }
}
