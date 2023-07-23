package ru.mpei.brics.behaviours.grid;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.appServiceLayer.ServiceInterface;
import ru.mpei.brics.extention.ApplicationContextHolder;
import ru.mpei.brics.extention.configirationClasses.GridConfiguration;
import ru.mpei.brics.extention.dto.Measurement;

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
        double deltaP = 0;
        for (AID aid : cfg.getActivePowerData().keySet()) {
            deltaP += cfg.getActivePowerData().get(aid);
        }
        log.info("Sum of powers: {}", deltaP);

        double frequency = 50 + 0.01 * (deltaP);
        cfg.setF(frequency);
        log.info("{} spam measurement: f={}, nP={}, gP={}, nQ={}, gQ={}", myAgent.getLocalName(),
                cfg.getF(), cfg.getNecessaryP(), cfg.getGeneratedP(), cfg.getNecessaryQ(), cfg.getGeneratedQ());


        service.saveMeasurementInDB(new Measurement(measurementIndex,
                (System.currentTimeMillis() - startTime) / 1000L,
                frequency,
                cfg.getNecessaryP(),
                cfg.getGeneratedP(),
                cfg.getNecessaryQ(),
                cfg.getGeneratedQ()));
        measurementIndex++;

    }
}
