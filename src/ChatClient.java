import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Client class for the chat system.
 *
 * @author Dylan Drescher
 */
public class ChatClient {
    private String username;

    /**
     * Constructor
     */
    private ChatClient() {
        username = changeName();
    }

    /**
     * Gets a message that the user enters.
     *
     * @return Returns the message that the user enters
     */
    private String getMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String msg = "#DONOTSEND";
        try {
            msg = reader.readLine();
        }
        catch (IOException e) {
            System.out.println("Invalid input.");
            return msg;
        }

        return "[" + this.username + "] " + msg;
    }

    /**
     * Sets the username of a particular instance of the client.
     */
    private String changeName() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter a username.");
        try {
            return reader.readLine();
        }
        catch (IOException e) {
            System.out.println("Something happened. A boring name will be given to you.");
            return "Jeff";
        }
    }

    /**
     * Method that runs when the ChatClient is run.
     *
     * @param args Server address and port
     */
    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient();
        /*Pound sign commands:
        * #DONOTSEND - client does nothing
        * #QUIT - terminate the client*/
        String cca;
        int ccp;
        try {
            cca = args[0]; // The address to connect to
            ccp = Integer.parseInt(args[1]); // The port to connect to
        }
        catch (ArrayIndexOutOfBoundsException e) {
            cca = "127.0.0.1"; // default address
            ccp = 14001; // default port
        }
        Socket connection = new Socket(cca, ccp);
        DataOutputStream toServer = new DataOutputStream(connection.getOutputStream());
        Receiver receiver = new Receiver(connection);
        receiver.start();
        System.out.println("Ready for messages.");
        //noinspection InfiniteLoopStatement
        while (true) {
            String toSend = client.getMessage();
            if (!toSend.equals("#DONOTSEND")) { // Message will parse if it's not equal to #DONOTSEND
                toServer.writeBytes(toSend + "\n");
            }
        }
    }
}

