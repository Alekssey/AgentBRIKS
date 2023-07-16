package ru.mpei.briks.extention.helpers;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;

public class KieConfigurator {
    private KieServices kieServices = KieServices.Factory.get();
    private String rulesFilePath;

    private KieFileSystem getKieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(this.rulesFilePath));
        return kieFileSystem;
    }

    private KieContainer getKieContainer() throws IOException {
//        System.out.println("Container created");
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

    public KieSession getKieSession(String rulesFilePath) throws IOException {
//        System.out.println("Session created");
        this.rulesFilePath = rulesFilePath;
        return getKieContainer().newKieSession();
    }
}

