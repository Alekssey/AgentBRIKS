package ru.mpei.briks;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mpei.briks.configuration.AgentConfigurationInterface;
import ru.mpei.briks.configuration.AgentsConfigurationImpl;
import ru.mpei.briks.extention.AgentDescription;
import ru.mpei.briks.extention.AgentDescriptionContainer;
import ru.mpei.briks.extention.AgentWrapper;
import ru.mpei.briks.extention.ApplicationContextHolder;
import ru.mpei.briks.test.BeanTest;

import java.lang.reflect.Field;
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

		/** тест на scope */
//		extractTestBeansFromContext();

		/** тест на извлечение агента */
//		extractAgentFromContext();



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

/*	public static void extractAgentFromContext() {
		ApplicationContext context = ApplicationContextHolder.getContext();
		AgentContainer mainContainer = (AgentContainer) context.getBean("mainContainer");

		try {
			AgentController agentController = mainContainer.getAgent("grid");
			log.info("I get agentController {} from mainContainer", agentController.getName());
			log.info("Class of agentController: {}", agentController.getClass());
			log.info("Agent fields: {}", List.of(agentController.getClass().getFields()));
			log.info("Agent declared fields: {}", List.of(agentController.getClass().getDeclaredFields()));

			try {
				Field agentAidField = agentController.getClass().getDeclaredField("agentID");
				agentAidField.setAccessible(true);

				Field agentContainerField = agentController.getClass().getDeclaredField("myContainer");
				agentContainerField.setAccessible(true);

				AID agentAID = (AID) agentAidField.get(agentController);
				jade.core.AgentContainer agentContainer = (jade.core.AgentContainer) agentContainerField.get(agentController);

				Agent a = agentContainer.acquireLocalAgent(agentAID);

				log.info("Getted agen!!!: {}", a);


//				log.info("извлеченное поле: {}", agentAidField.get(agentController));
//				log.info("Class of mainContainer: {}", mainContainer.getClass());
//				log.info("MainContainer declared fields: {}", List.of(mainContainer.getClass().getDeclaredMethods()));


			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

		} catch (ControllerException e) {
			throw new RuntimeException(e);
		}
	}*/

}

