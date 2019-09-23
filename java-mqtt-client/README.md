## EXAMPLE to ensure optimal temperature

**Automated control, to ensure optimal temperature in rooms.**

* Temperature sensors periodically send values to broker on topics 'room/sensor_id/temperature' 

**Backend - Java Client**
* in the backend connect a service that listens to 'room/+/temperature' 
* It checks every incoming message. 
  > temperature has to be between 15 and 20
* whenever these value are outside the range, 
  > the service will determine which room and device the value was coming from and send a command "up" or "down" to the topic 'room/+/temperature/command' 


**Sensors - Simulated** 
* Use a simple CLI to simulate sensors
* connect and subscribe and listen to the topics
* Periodically send random values to the related topic

##Software & Tool Requirements:

**Prerequisites** 
* Java 11 

**Client Library**
* HiveMQ MQTT Client Library 

**Message Broker**
* HiveMQ4 CE

**MQTT Client Tools**
* HiveMQ-CLI
