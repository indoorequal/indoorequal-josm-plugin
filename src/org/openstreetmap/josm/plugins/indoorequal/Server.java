package org.openstreetmap.josm.plugins.indoorequal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.openstreetmap.josm.tools.Logging;

public class Server extends Thread {
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	private void start_() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			HttpHandler connection = new HttpHandler(socket);

			Thread request = new Thread(connection);
			request.start();
		}
	}

	@Override
	public void run() {
		try {
			this.start_();
		} catch (IOException e) {
			Logging.error(e.getMessage());
		}
	}
}
