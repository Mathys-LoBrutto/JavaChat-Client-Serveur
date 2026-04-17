package src;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gère la communication avec un client.
 * Cette classe implémente Runnable pour être exécutée dans un thread séparé.
 */
public class GestionnaireClient implements Runnable {

	/** Informations du client géré */
	private final ClientInfo clientInfo;

	/** Socket dédiée à la communication avec ce client */
	private final DatagramSocket socket;

	/** Map des clients connectés sur le serveur */
	private final ConcurrentHashMap<String, ClientInfo> clients;

	/**
	 * Constructeur de la classe.
	 *
	 * @param clientInfo Les informations du client associé à ce gestionnaire
	 * @param socket     La socket dédiée à la communication
	 * @param clients    La map des clients connectés au serveur
	 */
	public GestionnaireClient(ClientInfo clientInfo, DatagramSocket socket, ConcurrentHashMap<String, ClientInfo> clients) {
		this.clientInfo = clientInfo;
		this.socket = socket;
		this.clients = clients;
	}

	/**
	 * Diffuse un message à tous les clients connectés sauf l'expéditeur du message (le client associé au gestionnaire).
	 *
	 * @param message Le message à envoyer aux autres utilisateurs
	 */
	private void envoyerMessageAutresUtilisateurs(String message) {
		try {
			byte[] envoyees = message.getBytes();

			// Parcours de tous les clients
			for (ClientInfo clientDestinataire : clients.values()) {
				// Pas d'envoi vers le client associé au gestionnaire
				if (clientDestinataire.getPseudo().equals(clientInfo.getPseudo())) {
					continue;
				}

				InetAddress adresseClient = clientDestinataire.getAdresseIP();
				int port = clientDestinataire.getPort();
				DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseClient, port);

				socket.send(messageEnvoye);
			}
		} catch (Exception e) {
			System.err.println("Erreur lors de l'envoi d'un message à tous les utilisateurs : " + e.getMessage());
		}
	}

	/**
	 * Méthode principale du thread.
	 * - Diffuse un message de bienvenue à tous les autres utilisateurs connectés.
	 * - Reçoit les messages envoyés par le client associé et les transmets aux autres.
	 * - Déconnecte le client lors de la réception du message "exit".
	 */
	@Override
	public void run() {
		try {
			// Envoi du message de bienvenue
			String message = "Serveur : Bienvenue " + clientInfo.getPseudo() + " !";
			envoyerMessageAutresUtilisateurs(message);

			byte[] recues = new byte[1024];

			// Réception des paquets en boucle
			while (true) {
				DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
				socket.receive(paquetRecu);

				String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());

				// Sortie de la boucle si c'est le message de déconnexion
				if (messageRecu.trim().equalsIgnoreCase("exit")) break;

				// Envoi du message recu à tous les utilisateurs
				envoyerMessageAutresUtilisateurs(clientInfo.getPseudo() +" : " + messageRecu);
			}

			// Envoie du message de déconnexion aux autres utilisateurs
			message = "Serveur : " + clientInfo.getPseudo() + " a quitté le chat.";
			envoyerMessageAutresUtilisateurs(message);

			// On retire le client de la map
			clients.remove(clientInfo.getPseudo());

			// On ferme la socket dédiée
			socket.close();
		} catch (Exception e) {
			System.err.println("Erreur de communication avec " + clientInfo.getPseudo() + " : " + e.getMessage());
			}
	}
}
