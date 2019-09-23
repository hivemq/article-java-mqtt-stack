# Article-java-mqtt-stack
This repository includes all information to run the described sample from the article 
for building a full stack MQTT solution with java based open source components.

## Steps to run the full Example 

### Prerequisites
* Java 11 
* Maven  


### Message Broker
* Download the latest HiveMQ archive from [HiveMQ - Developers getting started](https://www.hivemq.com/developers/getting-started/)
* Unpack
* Run start HiveMQ

### Backend MQTT Client
* Compile and build java-mqtt-client example from the module
* Details are described in [java-mqtt-client/README.md](java-mqtt-client/README.md)
* Start Main class from java-mqtt-client via IDE or as
`java -jar java-mqtt-client/target/backend-client-1.0-SNAPSHOT.jar `

### MQTT Client Interface
* Get a package of your choice from HiveMQ-CLI via [MQTT CLI github](https://github.com/hivemq/mqtt-cli)
* Install mqtt-cli or build the jar (gradle build)
* Run the script `example-devices.sh ` 


### Further Documentation
* https://github.com/hivemq/hivemq-community-edition
* https://hivemq.github.io/hivemq-mqtt-client/
* https://hivemq.github.io/hivemq-cli/
* https://www.hivemq.com/  
* https://www.hivemq.com/docs/4.2/hivemq/introduction.html
* https://www.hivemq.com/mqtt-essentials/  
* https://www.hivemq.com/extensions/
