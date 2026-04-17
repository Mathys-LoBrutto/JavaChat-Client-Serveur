package src;

import java.net.InetAddress;

/**
 * Classe contenant toutes les informations de connexion d'un utilisateur
 */
public class ClientInfo {

	/**
	 * Pseudo unique du client.
	 */
	private final String pseudo;

	/**
	 * Adresse IP du client.
	 */
	private final InetAddress adresseIP;

	/**
	 * Port utilisé par le client.
	 */
	private final int port;

	/**
	 * Constructeur de la classe.
	 *
	 * @param pseudo    pseudo du client
	 * @param adresseIP adresse IP du client
	 * @param port      port du client
	 */
	public ClientInfo(String pseudo, InetAddress adresseIP, int port) {
		this.pseudo = pseudo;
		this.adresseIP = adresseIP;
		this.port = port;

	}

	/**
	 * Accesseur pour le pseudo du client
	 *
	 * @return le pseudo du client
	 */
	public String getPseudo() {
		return this.pseudo;
	}

	/**
	 * Accesseur pour l'adresse IP du client
	 *
	 * @return l'adresse IP
	 */
	public InetAddress getAdresseIP() {
		return this.adresseIP;
	}

	/**
	 * Accesseur pour le port du client
	 *
	 * @return le port du client
	 */
	public int getPort() {
		return this.port;
	}
}
