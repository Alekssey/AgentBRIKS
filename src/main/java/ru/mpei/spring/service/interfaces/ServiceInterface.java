package ru.mpei.spring.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ServiceInterface {

    void saveFile(MultipartFile file);
    String checkKZ(int begin, int end);
    void setTriggerValue(double trigVal);

}
