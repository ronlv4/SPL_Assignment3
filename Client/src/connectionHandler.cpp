#include <connectionHandler.h>
#include <encoderDecoder.h>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_){}
    
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
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, '\n');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(encoderDecoder::encode(line), '\n');
}
 
bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    char cur;
    bool logout = false;

    try {
		do{
			char *convert = new char[2];
            getBytes(&cur, 1);
            convert[0] = cur;
            getBytes(&cur, 1);
            convert[1] = cur;
            char* word = new char;
            //frame.append(1, ch);
            short opcode = encoderDecoder::bytesToShort(convert);
            string output = "";

            if(opcode==9){
                output+="NOTIFICATION";
                getBytes(&cur,1);
                if(cur == 0)
                    output+=" PM";
                if(cur == 1)
                    output+=" Public";
                getBytes(&cur, 1);
                int size = 0;
                string name;
                while(cur != '\0'){
                    getBytes(&cur, 1);
                    word+=cur;
                    size++;
                }
                name.assign(word, size);
                output+=name;
                size=0;
                string content;
                getBytes(&cur, 1);
                while(cur != '\0'){
                    getBytes(&cur, 1);
                    word+=cur;
                    size++;
                }
                content.assign(content, size);
                output+=" ";
                output+=content;
            }
            if(opcode == 10){
                output+="ACK";
                char *convert2 = new char[2];
                getBytes(&cur, 1);
                convert2[0] = cur;
                getBytes(&cur, 1);
                convert2[1] = cur;
                short opcode2 = encoderDecoder::bytesToShort(convert2);
                output+=" ";
                output+=opcode2;
                output+=" ";
                if(opcode2 == 7 || opcode2 == 8){
                    getBytes(&cur, 1);
                    convert2[0] = cur;
                    getBytes(&cur, 1);
                    convert2[1] = cur;
                    short numFollowing = encoderDecoder::bytesToShort(convert2);
                    getBytes(&cur, 1);
                    convert2[0] = cur;
                    getBytes(&cur, 1);
                    convert2[1] = cur;
                    short numFollowers = encoderDecoder::bytesToShort(convert2);
                    getBytes(&cur, 1);
                    convert2[0] = cur;
                    getBytes(&cur, 1);
                    convert2[1] = cur;
                    short numPosts = encoderDecoder::bytesToShort(convert2);
                    getBytes(&cur, 1);
                    convert2[0] = cur;
                    getBytes(&cur, 1);
                    convert2[1] = cur;
                    short age = encoderDecoder::bytesToShort(convert2);
                    output+=std::to_string(age)+" "+ std::to_string(numPosts)+" "+std::to_string(numFollowers)+" "+std::to_string(numFollowing)+'\n';
                }
                else if(opcode2==4){
                    output+=" ";
                    int size;
                    string content;
                    while(cur != '\0'){
                        getBytes(&cur, 1);
                        word+=cur;
                        size++;
                        content.assign(content, size);
                    }
                    output+=content;
                }
                else if(opcode2==3)
                    logout=true;
            }
            if(opcode == 11){
                output+="ERROR";
                char *convert2 = new char[2];
                getBytes(&cur, 1);
                convert2[0] = cur;
                getBytes(&cur, 1);
                convert2[1] = cur;
                short opcode2 = encoderDecoder::bytesToShort(convert2);
                output+=opcode2;
            }
            cout << output << endl;
            if(logout){
                std::terminate();
            }

        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

