#include <connectionHandler.h>
using std::string;

class encoderDecoder {


private:
    encoderDecoder();
public:
    static string encode(string message);
    static short getType(string type);
    static short bytesToShort(char *bytesArr);
    static void shortToBytes(short num, char *bytesArr);
};


