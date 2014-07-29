package com.tw.kiera.patu;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.regex.*;

class Main {

	private boolean isRunning;

	public void startAsync() {
		new Thread(new Runnable(){
			public void run() {
				startSync();
			}
		}).start();
	}

	public void startSync() {
		try {
			System.out.println("Starting patu web server");
			final int port = 8080;
			ServerSocket socket = new ServerSocket(port);
			System.out.println("patu listening on " + port + "...");
			isRunning = true;
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
							OutputStream output = client.getOutputStream();
							System.out.println("Output is: " + output);
							String[] lines = sb.toString().split("\n");
							String clientMessage = lines[0].trim();
							System.out.println(String.format("Received from client: (Length: %d) '%s'", clientMessage.length(), clientMessage));


							System.out.println("");
							System.out.println("");
							System.out.println("");

							Pattern pattern = Pattern.compile("GET /([\\w\\.]*) HTTP.*");
							Matcher matcher = pattern.matcher(clientMessage);
							if (matcher.matches()) {
								System.out.println("match! " + matcher);
								String requestedFile = "index.html";
								System.out.println(String.format("Match: '%s'", matcher.group(1)));
								if (!matcher.group(1).equals("")) {
										requestedFile = matcher.group(1);
								}

								System.out.println("");
								InputStream fileContents = new FileInputStream("/Users/ThoughtWorker/Sites/" + requestedFile);
								buffer = new byte[124];
								int currentReadLength = 0;
								int bufferPosition = 0;

								while(true) {
									if (bufferPosition >= buffer.length) {
										buffer = new byte[124];
										bufferPosition = 0;
										currentReadLength = 0;
									}

									currentReadLength = fileContents.read(buffer, bufferPosition, buffer.length - bufferPosition);
									if (currentReadLength == -1) {
										break;
									}
									output.write(buffer, bufferPosition, currentReadLength);
									bufferPosition += currentReadLength;
								}

							} else {
								output.write("\n???? no match\n\n".getBytes());
							}

							System.out.println("done handling client");
							client.close();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}

				}).start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public static void main (String... args) throws java.lang.Exception {
		new Main().startAsync();
	}
}
