package ru.mpei.brics.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlSerialization {
    public static <T> T unMarshalAny(Class<T> clazz, String outPutFileName) {
        T object = null;
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object obj = jaxbUnmarshaller.unmarshal(new File(outPutFileName));
            try {
                object = (T) obj;
            } catch (ClassCastException cce) {
                object = null;
                cce.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static <T> String marshalAny(T information, String outPutFileName) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(information.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File outFile = new File(outPutFileName);
            jaxbMarshaller.marshal(information, outFile);
            return outFile.getAbsolutePath();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during marshaling xml file from path "+outPutFileName);
        }
    }
}
