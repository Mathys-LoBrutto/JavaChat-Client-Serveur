import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MonClientUDPEtendu {

    public static void main(String[] args) {
        try {
            // 1 - Création du canal
            DatagramSocket socketClient = new DatagramSocket();
            InetAddress adresseClient = InetAddress.getByName("localhost");
            byte[] envoyees; //tampon d'émission
            byte[] recues = new byte[1024]; // tampon de réception

            // 2 - émettre
            Scanner sc = new Scanner(System.in);
            String message;
            do {
                System.out.println("Entrez votre message : ");
                message = sc.nextLine();

                envoyees = message.getBytes();
                DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseClient, 5555);
                socketClient.send(messageEnvoye);

                // 3 - Recevoir
                DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                socketClient.receive(paquetRecu);
                String reponse = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
                System.out.println("Depuis le serveur: " + reponse);

            } while (!message.equals("exit"));

            //4 - Libérer le canal
            socketClient.close();
        } catch (Exception e) {
            System.err.println(e);

        }
    }
}