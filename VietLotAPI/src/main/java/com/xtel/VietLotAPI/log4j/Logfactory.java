package com.xtel.VietLotAPI.log4j;

import java.io.File;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class Logfactory {   
    public static Logger getLogger(Class inputClass){
    	String path = new File("E:/eclipse/VietLotAPI/config/log4j.properties").getAbsolutePath();
        PropertyConfigurator.configure(path);
        return LogManager.getLogger(inputClass);
    }
}
