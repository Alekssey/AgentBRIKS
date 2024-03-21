package ru.mpei.brics.behaviours.activePowerImbalanceFsmSubbehaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.mpei.brics.agent.NetworkElementAgent;
import ru.mpei.brics.behaviours.AnalyzeMeasurements;
import ru.mpei.brics.model.AgentsCommunicationProtocols;
import ru.mpei.brics.model.NetworkElementConfiguration;

@Slf4j
public class EndRegulation extends OneShotBehaviour {

    private NetworkElementConfiguration cfg;
    private AnalyzeMeasurements analyzeBehaviour;

    public EndRegulation(NetworkElementAgent a) {
        super(a);
        this.cfg = a.getCfg();
        this.analyzeBehaviour = new AnalyzeMeasurements(a, this.cfg.getUdpMonitoringPeriod());
    }

    @Override
    public void action() {
        cleanUnhandledMessages(MessageTemplate.MatchProtocol(AgentsCommunicationProtocols.FITNESS_TRADES.getValue()));
        cleanUnhandledMessages(MessageTemplate.MatchProtocol(AgentsCommunicationProtocols.NOTIFICATIONS.getValue()));
        cfg.getAgentsQueue().clear();
        myAgent.addBehaviour(this.analyzeBehaviour);
    }

    @SneakyThrows
    @Override
    public int onEnd() {
        Thread.sleep(5_000);
        log.warn("Agent {} end regulation process. Regulating time: {} seconds",
                myAgent.getLocalName(),
                (System.currentTimeMillis() - this.cfg.getRegulationStartTime()) / 1000.0);
        return 0;
    }

    private void cleanUnhandledMessages(MessageTemplate mt) {
        ACLMessage msg = myAgent.receive(mt);
        while (msg != null) {
            msg = myAgent.receive(mt);
        }
    }
}
