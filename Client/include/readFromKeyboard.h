#ifndef CLIENT_READFROMKEYBOARD_H
#define CLIENT_READFROMKEYBOARD_H
#include <stdlib.h>
#include <mutex>
#include <connectionHandler.h>


class readFromKeyboard {
private:
    static const short bufsize = 1024;
    char buf[bufsize];
    int _id;
    std::mutex & _mutex;
    ConnectionHandler connectionHandler;
public:
    readFromKeyboard (int id, std::mutex& mutex, ConnectionHandler &connectionHandler);
    void run(){};
};


#endif //CLIENT_READFROMKEYBOARD_H
