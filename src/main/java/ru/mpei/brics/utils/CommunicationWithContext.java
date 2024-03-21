package ru.mpei.brics.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CommunicationWithContext {
    @Getter
    private static ApplicationContext context;

    public CommunicationWithContext() {
        log.info("Application context holder created");
    }

    @Autowired
    public CommunicationWithContext(ApplicationContext applicationContext) {
        CommunicationWithContext.context = applicationContext;
    }

    public static  <T> Optional<T> getBean(Class<T> beanClass) {
        Optional<T> optionalBean = Optional.empty();
        try {
            T bean = context.getBean(beanClass);
            optionalBean = Optional.of(bean);
        } catch (BeansException e) {
            log.error("Unable get bean for {} class from context", beanClass);
        }
        return optionalBean;
    }

}
