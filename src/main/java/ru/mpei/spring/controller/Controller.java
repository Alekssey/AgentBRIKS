package ru.mpei.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.spring.service.classes.ServiceImpl;

@Slf4j
@RestController
public class Controller {

    @Autowired
    private ServiceImpl service;

    @PostMapping("/data/upload")
    public void receiveFile(@RequestBody MultipartFile file){
        service.saveFile(file);
    }

    @GetMapping("/data/findFault")
    public String checkKZ(@RequestParam int startIndex, @RequestParam int endIndex){
        return service.checkKZ(startIndex, endIndex);
    }

    @GetMapping("/saveSetPoint")
    public void setTriggerValue(@RequestParam double setPoint){
        service.setTriggerValue(setPoint);
    }

}
