#!/bin/bash
#  Original Source: http://www.topwebhosts.org/tools/traffic-control.php
#  tc uses the following units when passed as a parameter.
#  kbps: Kilobytes per second 
#  mbps: Megabytes per second
#  kbit: Kilobits per second
#  mbit: Megabits per second
#  bps: Bytes per second 
#       Amounts of data can be specified in:
#       kb or k: Kilobytes
#       mb or m: Megabytes
#       mbit: Megabits
#       kbit: Kilobits
#  To get the byte figure from bits, divide the number by 8 bit
#

#
# Name of the traffic control command.
TC=/sbin/tc

# The network interface we're planning on limiting bandwidth.
IF=lo             # Interface

# Download limit (in mega bits)
DNLD=0.47mbit     
#DNLD=200mbit

# Upload limit (in mega bits)
#UPLD=0.06mbit      
UPLD=200mbit      

# Ping 
PING=1970ms

# Test server port 
PORT=8888

# IP address of the machine we are controlling
IP=127.0.0.1     # Host IP

# Filter options for limiting the intended interface.
U32="$TC filter add dev $IF protocol ip parent 1:0 prio 1 u32"

start() {


# Set up a Hierarchial Token Bucket (HTP) as the root qdisc
# http://luxik.cdi.cz/~devik/qos/htb/manual/userg.htm

    $TC qdisc add dev $IF root handle 1: htb default 30

# UPLOADS TO TEST SERVER:
# create a class for downloads from the test server, limited by the max 
# download rate
    $TC class add dev $IF parent 1: classid 1:1 htb rate $UPLD ceil $UPLD
   #$U32 match ip dst $IP/32 flowid 1:1
    $U32 match ip dst $IP/32 match ip dport $PORT 0xffff flowid 1:1
    $TC qdisc add dev lo parent 1:1 netem delay $PING

# DOWNLOADS FROM TEST SERVER
    $TC class add dev $IF parent 1: classid 1:2 htb rate $DNLD ceil $UPLD
   #$U32 match ip src $IP/32 flowid 1:1
    $U32 match ip src $IP/32 match ip sport $PORT 0xffff flowid 1:2
    $TC qdisc add dev lo parent 1:2 netem delay $PING

# ALL OTHER TRAFFIC (i.e. between test server and database)
# we use an unrestricted class (prio)

  #  $TC class add dev $IF parent 1: classid 1:3 htb rate 100mbit

 }

stop() {

# Stop the bandwidth shaping.
    $TC qdisc del dev $IF root

}

restart() {

# Self-explanatory.
    stop
    sleep 1
    start

}

show() {

# Display status of traffic control status.
    $TC -s qdisc ls dev $IF

}

case "$1" in

  start)

    echo -n "Starting bandwidth shaping: "
    start
    echo "done"
    ;;

  stop)

    echo -n "Stopping bandwidth shaping: "
    stop
    echo "done"
    ;;

  restart)

    echo -n "Restarting bandwidth shaping: "
    restart
    echo "done"
    ;;

  show)

    echo "Bandwidth shaping status for $IF:"
    show
    echo ""
    ;;

  *)

    pwd=$(pwd)
    echo "Usage: tc.bash {start|stop|restart|show}"
    ;;

esac

exit 0
