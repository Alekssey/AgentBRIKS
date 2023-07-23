package ru.mpei.brics.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BeanTest {
    public BeanTest() {
        log.info("Test Bean was created");
    }

    private int i;
    private String s;
    private double d;
}
