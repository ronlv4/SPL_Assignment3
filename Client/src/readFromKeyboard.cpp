#include <readFromKeyboard.h>

readFromKeyboard::readFromKeyboard(ConnectionHandler &connectionHandler, std::mutex & mutex, std::condition_variable &cond) : _connectionHandler(&connectionHandler), readMutex(mutex), readCond(cond){
}

void readFromKeyboard::run() {
    std::unique_lock<std::mutex> lk(readMutex);
    do {
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int len = line.length();
        if (!_connectionHandler->sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
        std::cout << "starting to wait" << std::endl;
        readCond.wait(lk);
        std::cout << "stopped waiting" << std::endl;
    } while (!_connectionHandler->shouldTerminate());
    std::cout << "Ended readFromKeyboard Thread" << std::endl;
}
