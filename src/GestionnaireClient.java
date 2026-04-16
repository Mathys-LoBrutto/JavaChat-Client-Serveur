package src;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
		String message = "bienvenue !";

		byte[] envoyees = message.getBytes();

		try {
			DatagramSocket socketClient = new DatagramSocket();

			for (String pseudo : clients.keySet()) {
				InetAddress adresseClient = clientInfo.adresseIP();
				int port = clientInfo.getPort();
				DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseClient, port);

				socketClient.send(messageEnvoye);
			}

			socketClient.close();
		} catch (Exception e) {
			System.err.println("Erreur lors de l'envoi du message : " + e.getMessage());
		}
	}
}
