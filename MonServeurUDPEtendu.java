import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MonServeurUDPEtendu {

    public static void main(String[] args) {
        try {
            // Création du canal 
            DatagramSocket socketServeur = new DatagramSocket(null);

            // Réservation du port
            InetSocketAddress adresse = new InetSocketAddress("localhost", 5555);
            socketServeur.bind(adresse);

            while (true) {
                byte[] recues = new byte[1024];
                byte[] envoyees;

                // Réception du paquet
                DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                socketServeur.receive(paquetRecu);
                String message = new String(paquetRecu.getData(), 0, paquetRecu.getLength());

                
                if (message.equals("exit")) {
                    InetAddress adrClient = paquetRecu.getAddress();
                    int prtClient = paquetRecu.getPort();
                    System.out.println("Nouveau client : " + adrClient + ":" + prtClient);

                    String reponse = "à la prochaine";
                    envoyees = reponse.getBytes();
                    DatagramPacket paquetEnvoye = new DatagramPacket(envoyees, envoyees.length, adrClient, prtClient);
                    socketServeur.send(paquetEnvoye);

                    break;
                }

                InetAddress adrClient = paquetRecu.getAddress();
                int prtClient = paquetRecu.getPort();
                System.out.println("Nouveau client : " + adrClient + ":" + prtClient);

                // Emission d'une réponse
                String reponse = "200 Server ok";
                envoyees = reponse.getBytes();
                DatagramPacket paquetEnvoye = new DatagramPacket(envoyees, envoyees.length, adrClient, prtClient);
                socketServeur.send(paquetEnvoye);
            }



            
            socketServeur.close();
            System.out.println("Serveur arrêté.");

        } catch (Exception e) {
            System.err.println(e);
        }
        
    }
    
}
