import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String args[]) throws IOException {
		final int portNumber;
		String ipAddress = "172.56.40.23";	// arbitrary
		int index;
		int msgCount = 0;
		String[] msg;
		String str, cmd;
		
		System.out.println("Please enter a port number\nUse a port number > 1024 if you are not the admin/root");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		portNumber = Integer.parseInt(input.readLine());
		
		System.out.println("Server listening on port " + portNumber);
		ServerSocket server = new ServerSocket(portNumber);
		
		while (true) {
			Socket socket = server.accept();
			OutputStream os = socket.getOutputStream();
			PrintWriter output = new PrintWriter(os, true);
			
			if(msgCount < 1)
			{
				output.println("Got connection from client " + ipAddress + " on port " + portNumber);
				msgCount++;
			}
			output.println("Ready");

			BufferedReader client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			msg = new String[50]; 	// holds the message from client
			index = 0;
			
			// SERVER receives the command here [UPPERCASE, LOWERCASE, REVERSE, EXIT]
			cmd = client.readLine();

			// proceed if the command is valid
			if(isValidCmd(cmd))
			{
				output.println("200 OK");
				output.println(ipAddress + " sends " + cmd.toUpperCase());
				System.out.println(ipAddress + " sends " + cmd.toUpperCase());
				if(cmd.equalsIgnoreCase("exit")) {
					break;
				}
				
				// we have the command, now collect message
				while(true)
				{
					str = client.readLine();
					if(str.equals("."))
					{
						msg[index] = str;
						break;
					}
					msg[index] = str;
					index++;
				}
				// handle (transform) the message and respond
				msg = handleMsg(msg, cmd);
				for(String i : msg)
				{
					if(i == null) { break; }
					output.println(i);
				}
			}
			else
			{
				output.println("400: Invalid Command");
			}
			socket.close();
			output.close();
		}
	}
	
	public static String[] handleMsg(String[] newMsg, String cmd)
	{
		if(cmd.equalsIgnoreCase("uppercase"))
		{
			// loop through the array and manipulate
			for(int i=0; i<newMsg.length; i++)
			{
				if(newMsg[i] == null) { break; }
				newMsg[i] = newMsg[i].toUpperCase();
			}
		}
		else if(cmd.equalsIgnoreCase("lowercase"))
		{
			// loop through the array and manipulate
			for(int i=0; i<newMsg.length; i++)
			{
				if(newMsg[i] == null) { break; }
				newMsg[i] = newMsg[i].toLowerCase();
			}
		}
		else if(cmd.equalsIgnoreCase("reverse"))
		{
			// loop through the array and manipulate
			for(int i=0; i<newMsg.length; i++)
			{
				if(newMsg[i] == null) { break; }
				newMsg[i] = newMsg[i] = reverse(newMsg[i]);
			}
		}
		return newMsg;
	}
	
	public static String reverse(String s)
	{
		String result = "";
		for(int i=s.length()-1; i>=0; i--)
		{
			result += s.charAt(i);
		}
		return result;
	}
	
	public static boolean isValidCmd(String newCommand)
	{
		if(	newCommand.equalsIgnoreCase("uppercase") || 
			newCommand.equalsIgnoreCase("lowercase") || 
			newCommand.equalsIgnoreCase("reverse")   || 
			newCommand.equalsIgnoreCase("exit")  )
		{
			return true;
		}
		return false;
	}
	
}