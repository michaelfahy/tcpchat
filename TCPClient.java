/**
*	TCP Client Program
*	Connects to a TCP Server
*	Receives a line of input from the keyboard and sends it to the server
*	Receives a response from the server and displays it.
*
*	@author: Michael Fahy
@	version: 2.1
*/

import java.io.*;
import java.net.*;
class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        String message = "Hello Red";
        String response;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = null;
        int state = 0;

		try
		{
			clientSocket = new Socket("localhost", 6789);
		}

		catch(Exception e)
		{
			System.out.println("Failed to open socket connection");
			System.exit(0);
		}

        PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
		    BufferedReader inFromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (state < 3) {

            switch (state) {
              case 0:                   // send initial message to server and wait for response
                System.out.println(state);
                outToServer.println(message);

                response = inFromServer.readLine();
                response = response.trim();
                System.out.println("FROM SERVER:" + response );
                if (response.substring(0,3).equals("100")) {
                  state = 1;    //You are first client.  wait for second client to connect
                }
                else if (response.substring(0,3).equals("200")){
                  state = 2;  //you are second client.  Wait for message from first client
                }
                break;

              case 1:
                System.out.println(state);

                response = inFromServer.readLine();
                response = response.trim();
                System.out.println("FROM Server:" + response);
                if ((response.length() >= 7) && (response.substring(0,7).equals("Goodbye"))) {
                  state = 3;    //YGo to Goodbye state
                  break;
                }
                //If not goodbye, then second client is ready to Chat
                System.out.print("Your turn: ");
                message = inFromUser.readLine();
                outToServer.println(message);

                state = 2;
                break;

              case 2: //Chat mode
                System.out.println(state);

                response = inFromServer.readLine();
                response = response.trim();
                System.out.println("FROM Other Client:" + response);
                if ((response.length() >= 7) && (response.substring(0,7).equals("Goodbye"))) {
                  state = 3;    //YSay goodbye
                  break;
                }
                //If not goodbye, then second client is ready to Chat
                System.out.print("Your turn: ");
                message = inFromUser.readLine();
                outToServer.println(message);

                break;

              }
        }
        clientSocket.close();
    }
}
