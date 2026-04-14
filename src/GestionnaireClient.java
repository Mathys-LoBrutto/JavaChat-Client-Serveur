package src;

import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

public class GestionnaireClient implements Runnable {

	ClientInfo clientInfo;
	DatagramSocket socket;
	ConcurrentHashMap<String, ClientInfo> clients;


	GestionnaireClient(ClientInfo clientInfo, DatagramSocket socket, ConcurrentHashMap<String, ClientInfo> clients) {
		this.clientInfo = clientInfo;
		this.socket = socket;
		this.clients = clients;
	}

	@Override
	public void run() {
		// Envoie du message de bienvenue
		for (String pseudo : clients.keySet()) {

		}
	}
}
