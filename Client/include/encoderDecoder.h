#include <connectionHandler.h>
using std::string;

class encoderDecoder {


private:
    string _cur;
public:
    encoderDecoder(std::string cur);
    static void sendResponse;
};


#endif //CLIENT_ENCODERDECODER_H
