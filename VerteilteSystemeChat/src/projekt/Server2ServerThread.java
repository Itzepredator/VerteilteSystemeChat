package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server2ServerThread extends Thread {
	public ServerThread[] clients;
	public ServerSocket server;
	public Socket server2serverSocket;
	public Thread thread;
	public int clientCount;
	public ArrayList<String> history;
	public boolean historySend;

	public Server2ServerThread(ServerThread[] clients, ServerSocket server,
			Socket server2serverSocket, Thread thread, int clientCount,
			ArrayList<String> history, boolean historySend) {
		this.clients = clients;
		this.server = server;
		this.server2serverSocket = server2serverSocket;
		this.thread = thread;
		this.clientCount = clientCount;
		this.history = history;
		this.historySend = historySend;
	}

	public void run() {
		sendeMessageAnAndereServer(this.server2serverSocket, ";;;");
	}

	void sendeMessageAnAndereServer(Socket server2serverSocket, String message) {

		try {
			if (server2serverSocket != null) {
				// BufferedReader input = new BufferedReader(
				// new InputStreamReader(
				// this.server2serverSocket.getInputStream()));
				// BufferedWriter output = new BufferedWriter(
				// new OutputStreamWriter(
				// this.server2serverSocket.getOutputStream()));

				DataInputStream console = new DataInputStream(
						server2serverSocket.getInputStream());
				DataOutputStream streamOut = new DataOutputStream(
						server2serverSocket.getOutputStream());

				streamOut.writeUTF(message);
				streamOut.flush();

				// System.out.println(input.readLine());

				// output.write("Test");
				// output.flush();
			}
		} catch (IOException e) {
			System.out
					.println("Konnte keine Nachricht an zweiten Server senden!");
		}

	}
}