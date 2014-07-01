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
				 		System.out.println("Connection came in from: " + client.getRemoteSocketAddress());
				 		InputStream input = client.getInputStream();
				 		System.out.println("Input Stream received" + input);
				 		StringBuilder sb = new StringBuilder();
				 		byte[] buffer = new byte[8];
				 		int bytesRead = 0;
				 		int totalRead = 0;
				 		boolean endOfMessage = false;
				 		while(bytesRead != -1 && !endOfMessage) {
				 			bytesRead = input.read(buffer, totalRead, buffer.length - totalRead);
				 			totalRead += bytesRead;
				 			System.out.println("Read " + bytesRead + "bytes, read " + totalRead + " so far");
				 			endOfMessage = buffer[totalRead-1] == "\n".getBytes()[0];
				 			if (totalRead >= buffer.length || endOfMessage) {
				 				sb.append(new String(buffer));
				 				buffer = new byte[8];
				 				totalRead = 0;
				 				bytesRead = 0;
				 			}

						}
						String clientMessage = sb.toString().trim();
				 		if (clientMessage.startsWith("GET / ")) {
							System.out.println("Index requested");
						} else {
							System.out.println("Received from client: " + clientMessage);
						}
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
