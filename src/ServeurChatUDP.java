package src;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class ServeurChatUDP {

	/** Port associé au serveur */
	private final static int PORT_SERVEUR = 9000 ;

	/** Map des clients connectés au serveur */
	private ConcurrentHashMap<String, ClientInfo> clients;

	/**
	 * Constructeur de la classe.
	 */
	public ServeurChatUDP() {
		this.clients = new ConcurrentHashMap<>();
	}

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
	public static void main() {
		try {
			// Création de la socket principale
			DatagramSocket socketServeur = new DatagramSocket(PORT_SERVEUR);

			byte[] recues = new byte[1024];

			// Réception des paquets en boucle
			while (true) {
				DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
				socketServeur.receive(paquetRecu);

				String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
				String[] messageRecuDecoupe = messageRecu.split(":");

				if (messageRecuDecoupe.length == 2 && messageRecuDecoupe[1].equals("JOIN")) {
					// Création d'un nouvel utilisateur

					// Recherche d'un port libre
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
