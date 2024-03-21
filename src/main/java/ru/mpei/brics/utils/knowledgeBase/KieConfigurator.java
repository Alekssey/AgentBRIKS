package ru.mpei.brics.utils.knowledgeBase;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Slf4j
@Component
public class KieConfigurator {
    @Value("${agents.knowledge-base.folder.path}")
    private String rulesFolderPath;


    private KieServices kieServices = KieServices.Factory.get();
    private String rulesFileName;

    private KieFileSystem getKieFileSystem() throws FileNotFoundException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        try {
            kieFileSystem.write(ResourceFactory.newClassPathResource(this.rulesFolderPath + this.rulesFileName));

        } catch (RuntimeException e) {
            throw new FileNotFoundException();
        }
        return kieFileSystem;
    }

    private KieContainer getKieContainer() throws FileNotFoundException {
        getKieRepository();
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kContainer;
    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }

    public synchronized KieSession getKieSession(String rulesFileName) throws FileNotFoundException {
        this.rulesFileName = rulesFileName;
        KieSession ks = getKieContainer().newKieSession();
        return ks;
    }
}
