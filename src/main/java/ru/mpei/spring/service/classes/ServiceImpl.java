package ru.mpei.spring.service.classes;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.spring.beans.Measurement;
import ru.mpei.spring.repository.RepositoryInterface;
import ru.mpei.spring.service.interfaces.ServiceInterface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ServiceImpl implements ServiceInterface {

    private double triggerValue = -1;

    @Autowired
    private RepositoryInterface repository;

    @Override
    @SneakyThrows
    public void saveFile(MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        line = bufferedReader.readLine();
        double time;
        double ia;
        double ib;
        double ic;

        while (line != null){
            String[] stringParts = line.split(",");
            if(stringParts.length > 3){
                time = Double.parseDouble(stringParts[0]);
                ia = Double.parseDouble(stringParts[1]);
                ib = Double.parseDouble(stringParts[2]);
                ic = Double.parseDouble(stringParts[3]);
                repository.save(new Measurement(0, time, ia, ib, ic));
            }
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        log.info("The file has been read successfully");
    }

    @Override
    public String checkKZ(int startIndex, int endIndex) {
        if(this.triggerValue < 0){
            return "Necessary input trigger value";
        }

        List<Double> aPhaseSignal = new ArrayList<>();
        List<Double> bPhaseSignal = new ArrayList<>();
        List<Double> cPhaseSignal = new ArrayList<>();
        String damagedPhases = "";

        List<Measurement> mList = repository.getMeasurements(startIndex,endIndex);

        mList.forEach(el -> aPhaseSignal.add(el.getIa()));
        mList.forEach(el -> bPhaseSignal.add(el.getIb()));
        mList.forEach(el -> cPhaseSignal.add(el.getIc()));

        if(filterFourier(aPhaseSignal, triggerValue)){
            damagedPhases += "A";
        }
        if(filterFourier(bPhaseSignal, triggerValue)){
            damagedPhases += "B";
        }
        if(filterFourier(cPhaseSignal, triggerValue)){
            damagedPhases += "C";
        }

        log.info("Shortcut type: {}", damagedPhases);
        return damagedPhases;
    }

    @Override
    public void setTriggerValue(double trigVal) {
        this.triggerValue = trigVal;
        log.info("Trigger value {} has been set successfully", this.triggerValue);
    }
    private boolean filterFourier(List<Double> signal, double trigger){
        List<Double> buffer = new ArrayList<>();
        boolean kzFlag = false;
        double effectiveValue = 0;
        double temp;
        int i = 0;

        double real = 0;
        double imagine = 0;

        for (int j = 0; j < 20; j++) {
            buffer.add(signal.get(j));
        }

        int n  = defineInitialN(buffer);
        buffer.clear();

        for (int j = 0; j < 20; j++) {
            temp = signal.get(j);
            buffer.add(temp);
            real += temp * Math.sin(314 * n * 0.001);
            imagine += temp * Math.cos(314 * n * 0.001);
            effectiveValue = Math.sqrt(Math.pow((2.0/20) * real, 2) + Math.pow((2.0/20) * imagine, 2));

            if (n == 19) {
                n = 0;
            } else {
                n++;
            }
            i = j;
        }

        while (!kzFlag && i < signal.size()) {
            temp = signal.get(i);
            buffer.add(temp);
            real += (temp - buffer.get(0)) * Math.sin(314 * n * 0.001);
            imagine += (temp - buffer.get(0)) * Math.cos(314 * n * 0.001);
            buffer.remove(0);
            effectiveValue = Math.sqrt(Math.pow(2 * real / 20, 2) + Math.pow(2 * imagine / 20, 2));

            if (n == 19) {
                n = 0;
            } else {
                n++;
            }
            kzFlag = effectiveValue >= trigger;

            i++;
        }
        return kzFlag;
    }



    private int defineInitialN(List<Double> list){
        double temp = list.get(0);
        int i = 1;
        while (i < 20 && temp * list.get(i) <= 0){
            i++;
        }
        return 10 - i;
    }
}
