package src;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Classe représentant un client, permettant de se connecter au serveur, d'envoyer et de recevoir des messages.
 */
public class ClientChatUDP {

	/**
	 * Méthode principale du client.
	 * - Demande à l'utilisateur de choisir son pseudo.
	 * - Envoie le message de join au serveur.
	 * - Attend la réponse du serveur pour le port attribué.
	 * - Démarre un thread d'écoute pour les messages entrants.
	 * - Envoie au serveur les messages de l'utilisateur.
	 * - Ferme la socket quand l'utilisateur envoie exit.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);

			// Demande à l'utilisateur son pseudo
			System.out.println("Veuillez saisir votre pseudo :");
			String pseudo = sc.nextLine();

			DatagramSocket socketClient = new DatagramSocket();

			// Récupération de l'adresse du serveur
			InetAddress adresseServeur = InetAddress.getByName("localhost");

			// Envoie du message JOIN:<pseudo> au serveur (port 9000)
			String messageClient = "JOIN:" + pseudo;
			byte[] envoyees = messageClient.getBytes();
			DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseServeur, 9000);
			socketClient.send(messageEnvoye);

			System.out.println("Demande de connexion envoyée...");


			byte[] recues = new byte[1024];

			// Réception du message par le serveur
			DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
			socketClient.receive(paquetRecu);
			String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
			System.out.println("Serveur : " + messageRecu);

			// récupération du port dédié renvoyé par le serveur
			String[] reponseDecoupe = messageRecu.split(":");
			int portDedie = Integer.parseInt(reponseDecoupe[1]);

			// Création d'un Thread d'écoute pour écouter et envoyer des messages en meme temps (le while est bloquant)
			Thread Ecoute = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							// réception d'un message
							DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
							socketClient.receive(paquetRecu);

							String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
							System.out.println(messageRecu);
						}
					} catch (Exception e) {
						System.out.print("Fin de la reception des messages"); //car la fermeture du socket va créer une erreur.
					}
				}
			});
			Ecoute.start();

			// Création d'une boucle de saisie
			while (true) {
				String message = sc.nextLine();

				envoyees = message.getBytes();
				messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseServeur, portDedie);
				socketClient.send(messageEnvoye);

				// Le client veut se déconnecter
				if (message.equals("exit")) {
					socketClient.close();
					break;
				}
			}
			System.out.println("Déconnexion du chat...");
		} catch (Exception e) {
			System.err.println("Erreur côté client : " + e.getMessage());
		}
	}
}
