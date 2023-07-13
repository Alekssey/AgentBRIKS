package ru.mpei.briks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import ru.mpei.briks.extention.agentDescription.AgentDescription;
//import ru.mpei.briks.test.services.AgentDescription;
import ru.mpei.briks.extention.agentDescription.AgentDescriptionContainer;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.test.BeanTest;
import ru.mpei.briks.test.agents.ReceiverAgent;
import ru.mpei.briks.test.agents.SenderAgent;
import ru.mpei.briks.test.services.AgentFactory;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

	public static void main(String[] args) {
		// start application
		SpringApplication.run(SpringBootApplication.class, args);

		ApplicationContext context = ApplicationContextHolder.getContext();
		String[] beanNames = context.getBeanDefinitionNames();
		log.info("Number of beans in context: {}", beanNames.length);
//		for (String beanName: beanNames) {
//			log.info("Bean from container: {}", beanName);
//		}

		createAgents();

		/** тест на scope */
//		extractTestBeansFromContext();

		/** тестирование фабрики агентов */
//		createTestAgents();

	}

	public static void createAgents() {
		ApplicationContext context = ApplicationContextHolder.getContext();
		AgentDescriptionContainer agentDescriptionContainer = (AgentDescriptionContainer) context.getBean("agentDescriptionsContainer");

		for (AgentDescription ad : agentDescriptionContainer.getAgentDescriptionList()) {
			AgentDescription adBean = (AgentDescription) context.getBean("agentDescription");
			adBean.setAgentName(ad.getAgentName());
			adBean.setAgentClass(ad.getAgentClass());
			adBean.setArgs(ad.getArgs());

			context.getBean("agentCreator");
		}


	}

	public static void extractTestBeansFromContext() {
		ApplicationContext context = ApplicationContextHolder.getContext();
		BeanTest bean1 = (BeanTest) context.getBean("testSingletonBean");
		bean1.setS("String for Bean1");
		BeanTest bean2 = (BeanTest) context.getBean("testSingletonBean");
		bean2.setS("String for Bean2");


		BeanTest bean3 = (BeanTest) context.getBean("testPrototypeBean");
		bean3.setS("String for Bean3");

		BeanTest bean4 = (BeanTest) context.getBean("testPrototypeBean");

		BeanTest bean5 = (BeanTest) context.getBean("testSingletonBean");

		BeanTest bean6 = (BeanTest) context.getBean("testPrototypeBean");


		System.out.println();

	}

	public static void createTestAgents() {
		List<ru.mpei.briks.test.services.AgentDescription> agentDescriptionList = new ArrayList<>();
		agentDescriptionList.addAll(List.of(
				new ru.mpei.briks.test.services.AgentDescription("senderAgent", SenderAgent.class),
				new ru.mpei.briks.test.services.AgentDescription("receiverAgent", ReceiverAgent.class)
		));
		AgentFactory.createAgents(agentDescriptionList);
	}

}

