package rarus.chat.server._2_createConnection.creator;

import java.io.PrintWriter;
import java.util.Scanner;

public interface Connection {

    PrintWriter getOutMessage();
    Scanner getInMessage();

}
