import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String args[]) throws IOException {
		final String host = "localhost";
		final int portNumber;
		int msgCount = 0;
		String response;
		String userInput;
		String[] msg;
		int index;
		BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Please enter the port number");
		portNumber = Integer.parseInt(userInputBR.readLine());

		while (true) {
			Socket socket = new Socket(host, portNumber);
			BufferedReader server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			if(msgCount < 1)
			{
				System.out.println("Server: " + server.readLine());
				msgCount++;
			}
			System.out.println("Server: " + server.readLine()); // ready

			msg = new String[50];
			index = 0;
			
			// client inputs the command here [UPPERCASE, LOWERCASE, REVERSE, EXIT]
			userInput = userInputBR.readLine();
			
			if ("exit".equalsIgnoreCase(userInput)) {
				// send the exit msg and close the socket
				out.println(userInput);
				System.out.println("Client: sending EXIT command and exiting...");
				socket.close();
				break;
			}
			
			// CLIENT sends the command here
			out.println(userInput);
			
			// get msg from server (200 OK)
			response = server.readLine();
			if(response.equalsIgnoreCase("200 OK"))
			{
				System.out.println("Server: " + response); 			// 200 OK
				server.readLine();									// xxxx sends UPPERCASE
				while(true)
				{
					userInput = userInputBR.readLine();
					msg[index++] = userInput;
					System.out.println("Client: " + userInput);
					if(userInput.equalsIgnoreCase(".")) { break; }
				}
				// we have message in array, send to server
				for(int i=0; i< msg.length; i++)
				{
					if(msg[i] == null) { break; }
					out.println(msg[i]);
				}
				// print message from server
				while(true)
				{
					response = server.readLine();
					if(response.equalsIgnoreCase(".")) { break; }
					System.out.println("Server: " + response);
				}
			}
			else
			{
				System.out.println("Server: " + response);
			}
		}
	}
}