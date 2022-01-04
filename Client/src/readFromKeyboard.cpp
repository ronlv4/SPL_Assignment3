#include <stdlib.h>
#include <mutex>
#include <connectionHandler.h>

class readFromKeyboard{
private:
    static const short bufsize = 1024;
    char buf[bufsize];
    int _id;
    std::mutex & _mutex;
    ConnectionHandler *_connectionHandler;
public:
    readFromKeyboard (int id, std::mutex& mutex, ConnectionHandler &connectionHandler) : _id(id), _mutex(mutex), _connectionHandler(&connectionHandler){}

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



