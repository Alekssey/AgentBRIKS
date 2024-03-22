package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.regulators;

import jade.core.behaviours.TickerBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.model.NetworkElementConfiguration;
import ru.mpei.brics.model.regulator.PiRegulator;
import ru.mpei.brics.model.regulator.Regulator;
import ru.mpei.brics.utils.CommunicationWithContext;
import ru.mpei.brics.utils.CommunicatorWith104Service;
import ru.mpei.brics.utils.knowledgeBase.DroolsCommunicator;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.Optional;

@Slf4j
public class RegulateFrequency extends TickerBehaviour {

    protected final NetworkElementConfiguration cfg;
    protected final Regulator regulator;
    protected CommunicatorWith104Service communicatorWith104;
    protected final double[] previousFrequencyValues = new double[]{-1.0, -1.0, -1.0, -1.0};
    protected double powerBeforeRegulating;
    protected int behaviourResult;
    protected long startTime;
    protected int outOfRangeCommandsCounter = 0;

    public RegulateFrequency(NetworkElementAgent a, long period) {
        super(a, period);
        this.cfg = a.getCfg();
        Optional<CommunicatorWith104Service> opCommunicator = CommunicationWithContext.getBean(CommunicatorWith104Service.class);
        opCommunicator.ifPresent(communicatorWith104Service -> this.communicatorWith104 = communicatorWith104Service);
        this.regulator = new PiRegulator(cfg.getKp(), cfg.getKi(), cfg.getCurrentP());
        if(this.cfg.getKnowledgeBaseCommunicator() == null) {
            try {
                DroolsCommunicator communicator = new DroolsCommunicator((String) a.getArguments()[2], this.cfg);
                this.cfg.setKnowledgeBaseCommunicator(communicator);
            } catch (FileNotFoundException e) {
                log.error("Error occurred while initializing knowledge base");
                a.getADetector().terminate();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onStart() {
        this.powerBeforeRegulating = this.cfg.getCurrentP();
        this.startTime = System.currentTimeMillis();
        log.warn("Agent {} start regulation task", myAgent.getLocalName());
    }

    @Override
    protected void onTick() {
        if(isAllSamplesInNormalRange()) {
            this.behaviourResult = 1;
            log.warn("all samples are in normal range");
            this.stop();
        }

        boolean canRegulate = cfg.getKnowledgeBaseCommunicator().getAllowSignal();
        boolean isInTimeBounds = checkRegulatingTime();

        if(canRegulate && checkFrequencyChanging() && isInTimeBounds) {
            log.warn("Frequency for calculations: {}", cfg.getF());
            double setpoint = regulator.getSetpoint(cfg.getTargetFreq(), cfg.getF());

            if(setpoint >= cfg.getMaxP()) {
                log.warn(" Calculated setpoint value is greater than max_P");
                setpoint = cfg.getMaxP();
                this.behaviourResult = 2;
                checkStop();
            } else if (setpoint <= cfg.getMinP()) {
                log.warn("Calculated setpoint value is less than min_P");
                setpoint = cfg.getMinP();
                this.behaviourResult = 2;
                checkStop();
            } else {
                if (this.outOfRangeCommandsCounter != 0) this.outOfRangeCommandsCounter = 0;
            }

            if (sendCommandToModel(setpoint)) this.cfg.setCurrentP(setpoint);

        } else if (!canRegulate) {
            this.behaviourResult = 2;
            log.warn("Forbidden signal from knowledge base");
            this.stop();
        } else if (!checkFrequencyChanging()) {
            log.warn("Frequency value does not change");
            behaviourResult = 2;
            if (sendCommandToModel(this.powerBeforeRegulating)) this.cfg.setCurrentP(this.powerBeforeRegulating);
            this.stop();
        } else if (!isInTimeBounds) {
            this.behaviourResult = 2;
            log.warn("Permissible regulation time has been exceeded");
            this.stop();
        }
    }

    protected boolean checkRegulatingTime() {
        return (System.currentTimeMillis() - this.startTime) <= this.cfg.getMaxRegulationTime();
    }

    private boolean checkFrequencyChanging() {
        for (int i = 0; i < previousFrequencyValues.length; i++) {
            if (cfg.getF() != previousFrequencyValues[i]) {
                int j = 0;
                while (j <= previousFrequencyValues.length - 2) {
                    previousFrequencyValues[j] = previousFrequencyValues[j + 1];
                    j++;
                }
                previousFrequencyValues[previousFrequencyValues.length - 1] = cfg.getF();
                return true;
            }
        }
        return false;
    }

    private boolean isAllSamplesInNormalRange() {
        for (double f : previousFrequencyValues) {
            if (f < cfg.getTargetFreq() - cfg.getDeltaFreq()
                    || f > cfg.getTargetFreq() + cfg.getDeltaFreq()) {
                return false;
            }
        }
        return true;
    }

    private boolean sendCommandToModel(Double setpoint) {
        try {
            this.communicatorWith104.sendCommand(this.cfg.getPCommandName(), setpoint);
            log.warn("send setpoint: {}", setpoint);
            return true;
        } catch (ResourceAccessException e) {
            log.error("ResourceAccessException occurred while sending new setpoint to the model");
            return false;
        } catch (ConnectException e) {
            log.error("ConnectException occurred while sending new setpoint to the model");
            return false;
        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException occurred while sending new setpoint to the model");
            return false;
        }
    }

    private void checkStop() {
        if (this.outOfRangeCommandsCounter >= 3) {
            this.stop();
        } else {
            this.outOfRangeCommandsCounter++;
        }
    }

    @Override
    public int onEnd() {
        return this.behaviourResult;
    }
}
