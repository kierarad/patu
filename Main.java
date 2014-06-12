import java.io.*;
import java.net.*;

class Main {

	public static void main (String[] args) throws java.lang.Exception {
		System.out.println("Starting patu web server");
		// s = new ServerSocket(8080);
		// client = s.select
		// output = client.openOutputStream
		// output.write("Hello")
		// client.close
		final int port = 8080;
		ServerSocket socket = new ServerSocket(port);
		System.out.println("patu listening on " + port + "...");
		Socket client = socket.accept();
		System.out.println("Connection came in from: " + client.getRemoteSocketAddress());
		OutputStream output = client.getOutputStream();
		output.write("\nhello\n\n".getBytes());
		System.out.println("done handling client.  shutting down");
	}
}


