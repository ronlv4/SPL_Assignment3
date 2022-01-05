#include <encoderDecoder.h>

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

encoderDecoder::encoderDecoder(){}

std::string encoderDecoder::encode(std::string message){
    string encodedMessage;
    int till = message.find_first_of(" ");
    string type = message.substr(0, till);
    message = message.substr(till+1);
    short typeInShort = getType(type);
    char* bytesArr = new char;
    shortToBytes(typeInShort, bytesArr);
    encodedMessage += bytesArr[0];
    encodedMessage += bytesArr[1];
    if(typeInShort==1){
        till = message.find_first_of(" ");
        string user = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += user;
        encodedMessage += '0';
        till = message.find_first_of(" ");
        string password = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += password;
        encodedMessage += '0';
        string birthday = message;
        encodedMessage += birthday;
        encodedMessage += '0';
    }
    else if(typeInShort==2){
        till = message.find_first_of(" ");
        string user = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += user;
        encodedMessage += '0';
        till = message.find_first_of(" ");
        string password = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += password;
        encodedMessage += '0';
        string captcha = message;
        encodedMessage += captcha;
    }
    else if(typeInShort==5 || typeInShort==8 || typeInShort==12){
        string content = message;
        encodedMessage += content;
        encodedMessage += '0';
    }
    else if(typeInShort==6){
        till = message.find_first_of(" ");
        string user = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += user;
        encodedMessage += '0';
        till = message.find_first_of(" ");
        string content = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += content;
        encodedMessage += '0';
        string data = message;
        encodedMessage += data;
        encodedMessage += '0';
    }
    else if(typeInShort==9){
        till = message.find_first_of(" ");
        string user = message.substr(0,till);
        message = message.substr(till+1);
        encodedMessage += user;
        encodedMessage += '0';
        string content = message;
        encodedMessage += content;
        encodedMessage += '0';
    }
    else{
        encodedMessage+=message;
    }
    encodedMessage += ';';
    return encodedMessage;
}

short encoderDecoder::getType(std::string type){
    if(type == "REGISTER")
        return 1;
    else if(type == "LOGIN")
        return 2;
    else if(type == "LOGOUT")
        return 3;
    else if(type == "FOLLOW")
        return 4;
    else if(type == "POST")
        return 5;
    else if(type == "PM")
        return 6;
    else if(type == "LOGSTAT")
        return 7;
    else if(type == "STAT")
        return 8;
    else if(type == "NOTIFICATION")
        return 9;
    else if(type == "BLOCK")
        return 12;
    return 0;
};

short encoderDecoder::bytesToShort(char* bytesArr){
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void encoderDecoder::shortToBytes(short num, char* bytesArr){
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}