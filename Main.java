import java.io.*;
import java.net.*;
import java.lang.*;

class Main {

	public static void main (String[] args) throws java.lang.Exception {
		System.out.println("Starting patu web server");
		final int port = 8080;
		ServerSocket socket = new ServerSocket(port);
		System.out.println("patu listening on " + port + "...");
		while(true) {
			System.out.println("Listening again for a client");
	 		final Socket client = socket.accept();
	 		new Thread(new Runnable() {
	 			public void run() {
	 					try {
						System.out.println("Started new thread to handle connection: " + Thread.currentThread().getName());
						System.out.println("zzzzz....");
				 		Thread.sleep(10000);
				 		System.out.println("I'm awake again");
						System.out.println("Connection came in from: " + client.getRemoteSocketAddress());
						OutputStream output = client.getOutputStream();
						output.write("\nhello\n\n".getBytes());
						System.out.println("done handling client");
						client.close();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
	 			}
	 			
	 		}).start();
		}
	}
}


