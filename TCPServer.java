/**
*	TCP Server Program
*	Listens on a TCP port
*	Receives a line of input from a TCP client
*	Returns an upper case version of the line to the client
*
*	@author: Michael Fahy
@	version: 2.0
*/
import java.io.*;
import java.net.*;

class TCPServer {

  public static void main(String argv[]) throws Exception
    {
    String message = "";
    String response = "";
    int state = 0;
    BufferedReader inFromClient1 = null, inFromClient2 = null;
    PrintWriter outToClient1 = null, outToClient2 = null;
		ServerSocket welcomeSocket = null;
    Socket connectionSocket1 = null, connectionSocket2 = null;

		try
		{
			welcomeSocket = new ServerSocket(6789);
		}

		catch(Exception e)
		{
			System.out.println("Failed to open socket connection");
			System.exit(0);
		}





    while (state < 4) {


        switch (state) {

          case 0:
            System.out.println(state);

            connectionSocket1 = welcomeSocket.accept();
            inFromClient1 = new BufferedReader(new InputStreamReader(connectionSocket1.getInputStream()));
            outToClient1 = new PrintWriter(connectionSocket1.getOutputStream(),true);

            message = inFromClient1.readLine();
            message = message.trim();
            System.out.println(message);

            //reply to first client
            response = "100 Message Received.  Waiting for second client to connect.";
            outToClient1.println(response);

            state = 1;
            break;


          case 1:

              // state 1" wait for second client to connect
              System.out.println(state);
              connectionSocket2 = welcomeSocket.accept();

              inFromClient2 = new BufferedReader(new InputStreamReader(connectionSocket2.getInputStream()));
              outToClient2 = new PrintWriter(connectionSocket2.getOutputStream(),true);

              message = inFromClient2.readLine();
              message = message.trim();
              System.out.println(message);

              //reply to both clients
              response = "200 Message Received from second client.  You may BEGIN CHATTING";
              System.out.println(response);
              outToClient1.println(response);

              response = "200 You are the second client.  Wait for a message from the first client";
              System.out.println(response);
              outToClient2.println(response);
              state = 2;
              break;


          case 2:  //Chat
            System.out.println(state);

            //recieve message from Client 1
            message = inFromClient1.readLine();
            message = message.trim();
            System.out.println("Message from Client 1 in state 2: " + message);

            //If it is Goodbye, go to goodbye state

            if (message.length()>=7 && message.substring(0,7).equals("Goodbye")){
              state = 4;
              break;
            }

            //if not goodbye relay message to the client2
            System.out.println("Message for client 2: " + message);
            outToClient2.println(message);

            System.out.println("Sent to client 2, waiting for response \n");

            //recieve message from Client 2
            message = inFromClient2.readLine();
            message = message.trim();
            System.out.println("Got response from client 2 \n");
            System.out.println("Message from Client 2 in state 2: " + message);

            //If it is Goodbye, go to goodbye state

            if (message.length()>=7 && message.substring(0,7).equals("Goodbye")){
              state = 4;
              break;
            }

            //if not goodbye relay message to lient1
            outToClient1.println(message);

            break;

            case 4: //Goodbye
            System.out.println(state);

            break;
          }


        }
        response = "Goodbye";
        outToClient1.println(response);
        outToClient2.println(response);
        connectionSocket1.close();
        connectionSocket2.close();
     }
}
