package com.hivemq.mqttclient.example;

import com.hivemq.mqttclient.example.climate.ClimateBackend;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

import static com.hivemq.mqttclient.example.common.Utils.idle;

public class ExampleMain {

    public static void main(String[] args) throws Exception {
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .formatPattern("Backend Client: {message}")
                .level(Level.INFO)
                .activate();

        Logger.info("START CLIMATE CONTROLLER BACKEND");

        //Backend Service
        (new ClimateBackend()).startClimateControllerBackend();

        //do not exit
        idle(3);
    }


}
