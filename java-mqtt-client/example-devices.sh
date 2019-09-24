#!/usr/bin/env bash
## Set device-id (A constant Identifier is needed here)
## Send randomised Payload value of 1-30 for temperature on topics 'room/roomID/temp'
## Send every 15 seconds
## Send as retained messages with QoS=1
## Rooms 1,2 and 3

while true
do
    for i in 1 2 3
    do
        VALUE=$(( $RANDOM % 29 +1))
        echo "publish temperature $VALUE of room $i"
        mqtt pub -u "dev_temp_$i" -t "room/$i/temperature" -m "$VALUE" -q 1 -r
        echo "sleep ..... "
        sleep 5
    done

done

