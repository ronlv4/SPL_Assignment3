#ifndef CLIENT_READFROMKEYBOARD_H
#define CLIENT_READFROMKEYBOARD_H
#include <stdlib.h>
#include <mutex>
#include <connectionHandler.h>


class readFromKeyboard {
private:
    static const short bufsize = 1024;
    char buf[bufsize];
    ConnectionHandler *_connectionHandler;
    std::mutex & readMutex;
    std::condition_variable &readCond;
    bool shouldTerminate = false;
public:
    readFromKeyboard (ConnectionHandler &connectionHandler, std::mutex &mutex, std::condition_variable &cond);
    void run();
    void terminate();
};

#endif //CLIENT_READFROMKEYBOARD_H
