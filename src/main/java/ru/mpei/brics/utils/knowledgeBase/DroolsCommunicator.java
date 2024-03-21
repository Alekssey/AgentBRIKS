package ru.mpei.brics.utils.knowledgeBase;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import ru.mpei.brics.model.KnowledgeBaseAllowDto;
import ru.mpei.brics.model.KnowledgeBaseFitnessDto;
import ru.mpei.brics.model.NetworkElementConfiguration;
import ru.mpei.brics.utils.CommunicationWithContext;

import java.io.FileNotFoundException;
import java.util.Optional;


@Slf4j
public class DroolsCommunicator {
    private KieSession kieSession = null;
    private NetworkElementConfiguration cfg;

    public DroolsCommunicator(String rulesFileName, NetworkElementConfiguration cfg) throws FileNotFoundException {
        Optional<KieConfigurator> opConfigurator = CommunicationWithContext.getBean(KieConfigurator.class);
        if (opConfigurator.isPresent()) {
            this.kieSession = opConfigurator.get().getKieSession(rulesFileName);
        }
        this.cfg = cfg;

        log.debug("kie session: {}", this.kieSession);

    }

    public double getFitnessValue() {
        KnowledgeBaseFitnessDto dto = new KnowledgeBaseFitnessDto(
                cfg.getMaxP(), cfg.getMinP(), cfg.getCurrentP(), cfg.getF());
        this.kieSession.insert(dto);
        this.kieSession.fireAllRules();
        return dto.getFitnessVal();
    }

    public boolean getAllowSignal() {
        KnowledgeBaseAllowDto dto = new KnowledgeBaseAllowDto(
                cfg.getMaxP(), cfg.getMinP(), cfg.getCurrentP(), cfg.getF());
        this.kieSession.insert(dto);
        this.kieSession.fireAllRules();
        return dto.isAllow();
    }
}
