package com.hivemq.mqttclient.example.climate;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import org.pmw.tinylog.Logger;

import java.nio.ByteBuffer;

import static com.hivemq.mqttclient.example.common.Utils.*;

public class ClimateBackend {

    private static final String CLIMATE_CONTROLLER_BACKEND = "ClimateControllerBackend";
    private static final String TOPIC_ROOMS_TEMP = "room/+/temperature";
    private static final String DECREASE = "DOWN";
    private static final String INCREASE = "UP";

    private static String temperatureControl(Integer val) {
        return (val < 15) ? INCREASE : (val > 30) ? DECREASE : null;
    }

    public void startClimateControllerBackend() {
        final Mqtt5BlockingClient client = MqttClient.builder()
                .serverHost(BROKER_HIVEMQ_ADR)
                .serverPort(BROKER_HIVEMQ_PORT)
                .useMqttVersion5()
                .identifier(CLIMATE_CONTROLLER_BACKEND)
                .buildBlocking();

        Mqtt5ConnAck ack = client.connect();

        if( ack.getReasonCode().isError() ) {
            //do error handling
        }
        doSubscribeToRooms(client.toAsync());

        addDisconnectOnRuntimeShutDownHock(client.toAsync());
    }

    private void doSubscribeToRooms(Mqtt5AsyncClient client) {

        Logger.info("Start subscribing to rooms temperature topic");

        final Mqtt5Subscription subscriptionTemperature = Mqtt5Subscription.builder()
                .topicFilter(TOPIC_ROOMS_TEMP)
                .qos(MqttQos.AT_LEAST_ONCE).build();

        client.subscribeWith()
                .addSubscription(subscriptionTemperature)
                .callback(publish -> {
                    Logger.info("Message received on topic: {} with payload: {} and with QoS: {}" ,publish.getTopic(), new String(publish.getPayloadAsBytes()), publish.getQos().getCode());
                    doPublishCommand(client, publish);
                })
                .send()
                .whenComplete((subAck, throwable) -> {
                    if (throwable != null) {
                        Logger.error("Subscription to topics failed. " , throwable.getMessage());
                    } else {
                        Logger.info("Successful subscribed to topics: {} ", TOPIC_ROOMS_TEMP);
                    }
                });
    }

    private void doPublishCommand(Mqtt5AsyncClient client, Mqtt5Publish publish) {

        final Integer val = getIntValueFromPayload(publish);
        Logger.info("Try publish to topic: {}/command ", publish.getTopic() );

        if (MqttTopicFilter.of(TOPIC_ROOMS_TEMP).matches(publish.getTopic())) {
            if (val != null) {
                final String result = temperatureControl(val);
                if( result != null) {
                    publishCommand(client, result, publish.getTopic().toString());
                } else {
                    Logger.info("Temperature ok");
                }
            }
        } else {
            Logger.warn("Topic not match to command filter: {} ", publish.getTopic().toString());
        }
    }

    private Integer getIntValueFromPayload(Mqtt5Publish publish) {
        ByteBuffer buffer = (publish.getPayload().isPresent()) ? publish.getPayload().get() : null;
        if (buffer != null) {
            byte[] dst = new byte[buffer.limit()];
            try {
                // deep copy necessary
                buffer.get(dst);
                return Integer.valueOf(new String(dst));
            } catch (Exception any) {
                Logger.error("Cannot read integer value from Payload, error: ", any.getMessage());
            }
        }
        return null;
    }

    private void publishCommand(Mqtt5AsyncClient client, String payload, String topic) {
        if (payload != null) {
            final Mqtt5Publish command = Mqtt5Publish.builder()
                    .topic(topic + "/command")
                    .qos(MqttQos.AT_LEAST_ONCE)
                    .contentType("TEXT")
                    .payload(payload.getBytes())
                    .build();

            client.publish(command).whenComplete((publishResult, throwable) -> {
                if (throwable != null) {
                    Logger.error("Publish to topic: {} failed, ", topic , throwable.getMessage());
                } else {
                    Logger.info("Publish msg '{}' to topic: {}/command" ,payload, topic);
                }
            });
        }
    }

}
