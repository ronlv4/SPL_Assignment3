#include "readFromKeyboard.h"
#include <connectionHandler.h>

using namespace std;
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        cerr << "Usage: " << argv[0] << " host port" << std::endl << endl;
        return -1;
    }
    string host = argv[1];
    short port = atoi(argv[2]);
    std::condition_variable _cond;
    std::mutex _mutex;
    make_shared<std::mutex>();


    ConnectionHandler connectionHandler(host, port, _mutex, _cond);
    if (!connectionHandler.connect()) {
        cerr << "Cannot connect to " << host << ":" << port << endl;
        return 1;
    }

    readFromKeyboard task(connectionHandler, _mutex, _cond);

    thread th1(&readFromKeyboard::run, &task);
//    th1.join();


	//From here we will see the rest of the ehco client implementation:
    while (1) {
        string answer;
        if (!connectionHandler.getLine(answer)) {
            cout << "Disconnected. Exiting...\n" << endl;
            exit(0);
        }
		unsigned long len=answer.size();
        cout << "Reply: " << answer << " - " << len << " bytes " << endl << endl;
        if (answer == "bye") {
            cout << "Exiting...\n" << endl;
            exit(0);
        }
    }
}
