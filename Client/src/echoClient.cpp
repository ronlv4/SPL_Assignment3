#include "readFromKeyboard.h"
#include <connectionHandler.h>
#include <boost/thread.hpp>

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

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        cerr << "Cannot connect to " << host << ":" << port << endl;
        return 1;
    }

    readFromKeyboard task(connectionHandler);

    boost::thread th1(&readFromKeyboard::run, &task);

	//From here we will see the rest of the ehco client implementation:
    while (1) {
        string answer;
        if (!connectionHandler.getLine(answer)) {
            cout << "Disconnected. Exiting...\n" << endl;
            exit(0);
        }
        cout << answer << endl;
        if (answer == "bye") {
            cout << "Exiting...\n" << endl;
            exit(0);
        }
    }
}
