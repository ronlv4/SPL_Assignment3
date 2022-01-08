#include <readFromKeyboard.h>

readFromKeyboard::readFromKeyboard(ConnectionHandler &connectionHandler): _connectionHandler(&connectionHandler) {

}

void readFromKeyboard::run() {
    while (1) {
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            int len = line.length();
            if (!_connectionHandler->sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
        }
}

