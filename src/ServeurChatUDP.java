package src;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class ServeurChatUDP {

	

	/** Map des clients connectés au serveur */
	private static ConcurrentHashMap<String, ClientInfo> clients = new ConcurrentHashMap<>();



	/**
	 * Recherche le premier port disponible entre deux bornes.
	 * @param portMin la borne min
	 * @param portMax la borne max
	 * @return port UDP libre
	 */
	public static int recherchePortUDPDisponible (int portMin, int portMax) {
		DatagramSocket socket = null;
		for (int port = portMin; port <= portMax; port++) {
			try {
				socket = new DatagramSocket(port);
			} catch (SocketException e) {
			}
		}
		return socket.getLocalPort();
	}

	/**
	 *
	 */
	public static void main(String[] args) {
		try {
			/** Port associé au serveur */
			final int PORT_SERVEUR = 9000;

			// Création de la socket principale
			DatagramSocket socketServeur = new DatagramSocket(PORT_SERVEUR);

			byte[] recues = new byte[1024];

			// Réception des paquets en boucle
			while (true) {
				DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
				socketServeur.receive(paquetRecu);

				String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
				String[] messageRecuDecoupe = messageRecu.split(":");

				if (messageRecuDecoupe.length == 2 && messageRecuDecoupe[0].equals("JOIN")) {

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
					ClientInfo nouvelleUtilisateur = new ClientInfo(messageRecuDecoupe[1], paquetRecu.getAddress(), paquetRecu.getPort() );

					// enregistrement du client dans le hashmap
					clients.put(nouvelleUtilisateur.getPseudo(), nouvelleUtilisateur);
					
					//démarrer un nouveau gestionnaireClient
					new Thread(new GestionnaireClient(nouvelleUtilisateur, newSocket, clients)).start();

					
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
