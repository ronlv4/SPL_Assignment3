#include <connectionHandler.h>
#include <encoderDecoder.h>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port) : host_(host), port_(port), io_service_(),
                                                                socket_(io_service_) {}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception &e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp) {
            tmp += socket_.read_some(boost::asio::buffer(bytes + tmp, bytesToRead - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string &line) {
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string &line) {
    return sendFrameAscii(encoderDecoder::encode(line), ';');
}

bool ConnectionHandler::getFrameAscii(std::string &frame, char delimiter) {
    char ch;
    int i = 0;
    char * bytesArray = new char [1 << 10];
    try {
        do {
            getBytes(&ch, 1);
            strncpy(bytesArray + i,&ch,1);
            i++;
        } while (delimiter != ch);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    short opCode = encoderDecoder::bytesToShort(bytesArray);
    bool logout = false;
    short opCode2;
    if (opCode == 9) {
        frame += "NOTIFICATION";
        if (bytesArray[2] == 0)
            frame += " PM";
        if (bytesArray[2] == 1)
            frame += " Public";
        string userName;
        i = 3;
        while (bytesArray[i] != '\0') {
            userName.append(&bytesArray[i]);
            i++;
        }
        string content;
        while (bytesArray[i] != '\0') {
            content.append(&bytesArray[i]);
            i++;
        }
        frame += userName + " " + content;
    }
    if (opCode == 10) {
        frame += "ACK";
        opCode2 = encoderDecoder::bytesToShort(bytesArray + 2);
        frame += " " + std::to_string(opCode2);
        if (opCode2 == 7 || opCode2 == 8) {
            short numFollowing = encoderDecoder::bytesToShort(bytesArray + 4);
            short numFollowers = encoderDecoder::bytesToShort(bytesArray + 6);
            short numPosts = encoderDecoder::bytesToShort(bytesArray+ 8);
            short age = encoderDecoder::bytesToShort(bytesArray + 10);
            frame += " " + std::to_string(age) + " " + std::to_string(numPosts) + " " + std::to_string(numFollowers) +
                     " " + std::to_string(numFollowing) + '\n';
        } else if (opCode2 == 4) {
            string userName;
            int i = 4;
            while (bytesArray[i] != '\0'){
                userName.append(&bytesArray[i]);
                i++;
            }
            frame += " " + userName;
        } else if (opCode2 == 3)
            logout = true;
    }
    if (opCode == 11) {
        frame += "ERROR";
        opCode2 = encoderDecoder::bytesToShort(bytesArray + 2);
        frame += " " + std::to_string(opCode2);
    }
    try {
        if (logout) {
            close();
            frame = "bye";
        }
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendFrameAscii(const std::string &frame, char delimiter) {
    bool result = sendBytes(frame.c_str(), frame.length());
    if (!result) return false;
    return sendBytes(&delimiter, 1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try {
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

