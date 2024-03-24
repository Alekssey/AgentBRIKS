package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours.regulators;

import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;

@Slf4j
public class LoadRegulator extends RegulateFrequency {
    private int stepPositionBeforeRegulating;
    private long prevCommandTime;
    private double startFrequency;
    public LoadRegulator(NetworkElementAgent a, long period) {
        super(a, period);
    }

    @Override
    public void onStart() {
        this.stepPositionBeforeRegulating = this.cfg.getCurrentStep();
        this.startFrequency = this.cfg.getF();
        this.startTime = System.currentTimeMillis();
        log.warn("Agent {} start regulation task", myAgent.getLocalName());
    }

    @Override
    protected void onTick() {
        log.error("try do exequte onTick method");
        if(isAllSamplesInNormalRange()) {
            this.behaviourResult = 1;
            log.warn("all samples are in normal range");
            this.stop();
        }
        if (crossSetpoint()) {
            this.behaviourResult = 1;
            log.warn("Load changing enough");
            this.stop();
        }
        boolean canRegulate = cfg.getKnowledgeBaseCommunicator().getAllowSignal();
        boolean isInTimeBounds = checkRegulatingTime();
        boolean freqIsChange = checkFrequencyChanging();

        if(canRegulate && freqIsChange && isInTimeBounds && timeAfterPreviousCommand() > 5.0) {
            log.warn("Frequency for calculations: {}", cfg.getF());

//            double setpoint = regulator.getSetpoint(cfg.getTargetFreq(), cfg.getF());
            int newStep = this.cfg.getCurrentStep();
            if (this.cfg.getF() > this.cfg.getTargetFreq()) {
                newStep++;
            } else {
                newStep--;
            }

            if(newStep > cfg.getLoadSteps().size() - 1) {
                log.warn(" Calculated step is greater than max step");
//                newStep = cfg.getLoadSteps().size() - 1;
                this.behaviourResult = 2;
                stop();
            } else if (newStep < 0) {
                log.warn("Calculated step is less than min step");
//                newStep = 0;
                this.behaviourResult = 2;
                stop();
            }

            double setpoint = this.cfg.getMaxP() * this.cfg.getLoadSteps().get(newStep) / 100;
            if (sendCommandToModel(setpoint)) {
                this.cfg.setCurrentStep(newStep);
                this.cfg.setCurrentP(setpoint);
                this.prevCommandTime = System.currentTimeMillis();
            }

        } else if (!canRegulate) {
            this.behaviourResult = 2;
            log.warn("Forbidden signal from knowledge base");
            this.stop();
        } else if (!checkFrequencyChanging()) {
            log.warn("Frequency value does not change");
            behaviourResult = 2;
            double setpoint = this.cfg.getMaxP() * this.cfg.getLoadSteps().get(this.stepPositionBeforeRegulating) / 100;
            if (sendCommandToModel(setpoint)) {
                this.cfg.setCurrentStep(this.stepPositionBeforeRegulating);
                this.cfg.setCurrentP(setpoint);
            }
            this.stop();
        } else if (!isInTimeBounds) {
            this.behaviourResult = 2;
            log.warn("Permissible regulation time has been exceeded");
            this.stop();
        }
    }

    private double timeAfterPreviousCommand() {
        if (prevCommandTime == 0) {
            this.prevCommandTime = System.currentTimeMillis();
            return 100.0;
        }
        return (System.currentTimeMillis() - this.prevCommandTime) / 1000.0;
    }

    private boolean crossSetpoint() {
        return ((this.cfg.getTargetFreq() - this.startFrequency) * (this.cfg.getTargetFreq() - this.cfg.getF())) < 0;
    }
}
