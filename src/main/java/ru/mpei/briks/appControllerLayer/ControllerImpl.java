package ru.mpei.briks.appControllerLayer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mpei.briks.appServiceLayer.ServiceInterface;

@Slf4j
@RestController("controller")
public class ControllerImpl implements ControllerInterface{

    @Autowired
    ServiceInterface service;

    @PostMapping("/grid/set/power")
    public String changeGridPower(@RequestParam double p, @RequestParam double q){
        return service.setPowerToGrid(p, q);
    }

    @GetMapping("/data/test")
    public boolean test(){
        return true;
    }

}
