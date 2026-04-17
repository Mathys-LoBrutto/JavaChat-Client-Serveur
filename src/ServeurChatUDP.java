package src;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe représentant le serveur de Chat, point d'entrée pour les utilisateurs.
 * Il récupère les demandes de connexion des clients et leur attribue un thread.
 */
public class ServeurChatUDP {

	/** Map des clients connectés au serveur */
	private static ConcurrentHashMap<String, ClientInfo> clients = new ConcurrentHashMap<>();


	/**
	 * Méthode principale du serveur.
	 * - Initilialise le serveur.
	 * - Attend les demandes de connexion des utilisateurs.
	 * - Notifie le client de son port attribué.
	 * - Ajoute l'utilisateur à la map des clients et démarre un thread associé.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Démarrage du serveur...");

			// Port associé au serveur
			final int PORT_SERVEUR = 9000;

			// Création de la socket principale
			DatagramSocket socketServeur = new DatagramSocket(PORT_SERVEUR);

			byte[] recues = new byte[1024];

			System.out.println("Serveur prêt, en attente de demandes de connexion");

			// Réception des paquets en boucle
			while (true) {
				DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
				socketServeur.receive(paquetRecu);

				String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
				String[] messageRecuDecoupe = messageRecu.split(":");

				// Si c'est une demande de connexion
				if (messageRecuDecoupe.length == 2 && messageRecuDecoupe[0].equals("JOIN")) {
					System.out.println("Connexion en cours de l'utilisateur : " + messageRecuDecoupe[1] );

					// Recherche d'un port libre
					DatagramSocket newSocket = new DatagramSocket(0);

					//récupération du numéro du port récuperé.
					int newPort = newSocket.getLocalPort();

					//notifie le client du port alloué via le message PORT:<n>;
					String messagePort = "PORT:" + newPort;
					byte[] envoyees = messagePort.getBytes();
					DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, paquetRecu.getAddress(), paquetRecu.getPort());
					socketServeur.send(messageEnvoye);


					// Création d'un nouvel utilisateur
					ClientInfo nouvelleUtilisateur = new ClientInfo(messageRecuDecoupe[1], paquetRecu.getAddress(), paquetRecu.getPort());

					// enregistrement du client dans le hashmap
					clients.put(nouvelleUtilisateur.getPseudo(), nouvelleUtilisateur);

					//démarrer un nouveau gestionnaireClient
					new Thread(new GestionnaireClient(nouvelleUtilisateur, newSocket, clients)).start();

					System.out.println("Connexion réussie pour : " + nouvelleUtilisateur.getPseudo());
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
