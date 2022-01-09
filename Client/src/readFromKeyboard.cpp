#include <readFromKeyboard.h>

readFromKeyboard::readFromKeyboard(ConnectionHandler &connectionHandler) : _connectionHandler(&connectionHandler){
}

void readFromKeyboard::run() {
    while (true){
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        if (!_connectionHandler->sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
}
