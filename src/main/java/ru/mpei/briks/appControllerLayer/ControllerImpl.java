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

    @Override
    @PostMapping("/grid/set/power")
    public String changeGridPower(@RequestParam double p, @RequestParam double q){
        return service.setPowerToGrid(p, q);
    }

//    @Override
    @PostMapping("/load/set/power")
    public String changeLoadPower(@RequestParam String loadName, @RequestParam double p, @RequestParam double q){
        return service.setPowerToLoad(loadName, p, q);
    }


    @GetMapping("/data/test")
    public boolean test(){
        service.getAgentFromContext("station1");
        return true;
    }



}
