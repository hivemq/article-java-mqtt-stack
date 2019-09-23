package com.hivemq.mqttclient.example.common;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import org.pmw.tinylog.Logger;

public class Utils {

    public static final String BROKER_HIVEMQ_ADR = "localhost";
    public static final int BROKER_HIVEMQ_PORT = 1883;
    public static final int KEEP_ALIVE = 30;

    public static void sleep(int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            Logger.error(e.getMessage());
        }
    }

    public static void idle(int sleepInterval) throws InterruptedException {
        while (true) {
            sleep(sleepInterval);
            System.out.print(".");
        }
    }

    static void disconnectOnExit(Mqtt5AsyncClient client) {
        if (client != null) {
            Logger.info("Disconnect Client " + client.getConfig().getClientIdentifier().get());
            client.disconnect();
        }
    }

    public static void addDisconnectOnRuntimeShutDownHock(Mqtt5AsyncClient client) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                disconnectOnExit(client);
            }
        });
    }
}
