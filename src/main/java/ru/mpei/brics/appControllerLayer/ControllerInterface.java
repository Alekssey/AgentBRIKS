package ru.mpei.brics.appControllerLayer;

import org.springframework.web.bind.annotation.RequestParam;

public interface ControllerInterface {
    String changeGridPower(double p, double q);
    String changeLoadPower(String loadName, double p, double q);
}
