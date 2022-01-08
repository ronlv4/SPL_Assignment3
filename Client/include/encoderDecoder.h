#include <connectionHandler.h>
using std::string;

class encoderDecoder {


private:
    encoderDecoder();
public:
    static string encode(string message);
    static short getType(string type);
    static void shortToBytes(short num, char *bytesArr);
    static short bytesToShort(char *bytesArr);
};


