#include <stdlib.h>
#include <mutex>
#include <connectionHandler.h>

class readFromKeyboard{
private:
    static const short bufsize = 1024;
    char buf[bufsize];
    ConnectionHandler *_connectionHandler;
public:
    readFromKeyboard (ConnectionHandler &connectionHandler) : _connectionHandler(&connectionHandler){}

    void run(){
        while (1) {
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            int len=line.length();
            if (!_connectionHandler->sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::cout << "Sent " << len+1 << " bytes to server" << std::endl;
            break;
        }
    }
};

#include "readFromKeyboard.h"



