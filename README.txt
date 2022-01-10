1) How to run your code:

from the server folder:
mvn clean
mvn compile
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="7777"



// for reactor:
mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="7777 5"


cd back into Client folder:

make
./bin/BGSClient <Server-IP> 7777

where <Server-IP> is printed to console when running the server

1.2)

REGISTER ron 12345 28-04-1994
LOGIN ron 12345 1
FOLLOW 0 aviv
STAT ron|aviv
LOGSTAT
BLOCK aviv
PM ron Hello how are you?
POST Hi everyone I'm new here

2) Filtered words are stored in "src/main/java/bgu/spl/net/impl/bidi/BGSService.java"
hard coded as a class member.

