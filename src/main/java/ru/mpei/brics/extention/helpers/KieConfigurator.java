package ru.mpei.brics.extention.helpers;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;

@Slf4j
public class KieConfigurator {
    private KieServices kieServices = KieServices.Factory.get();
    private String rulesFilePath;

    public KieSession getKieSession(String rulesFilePath) throws IOException {
//        System.out.println("Session created");
        this.rulesFilePath = rulesFilePath;
        KieContainer kieContainer = getKieContainer(rulesFilePath);
//        log.error("Kie Base names: {}", kieContainer.getKieBaseNames());
        KieSession kieSession = getKieContainer(rulesFilePath).newKieSession();
//        log.error("KIE SESSION: {}", kieSession.getKieBase());
        return kieSession;
    }

    public KieContainer getKieContainer(String rulesFilePath) throws IOException {
//        System.out.println("Container created");
        getKieRepository();
        this.rulesFilePath = rulesFilePath;
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kContainer;
    }

    private void getKieRepository() {
        KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
    }

    private KieFileSystem getKieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
//        log.error("{}", rulesFilePath);
        kieFileSystem.write(ResourceFactory.newClassPathResource(this.rulesFilePath));
        return kieFileSystem;
    }
}