import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MonClientUDP {
    
    public  static void main(String[] args)
    {
        try
        {
            // 1 - Création du canal
            DatagramSocket socketClient = new DatagramSocket();
            InetAddress adresseClient = InetAddress.getByName("localhost");
            byte[] envoyees; //tampon d'émission
            byte[] recues = new byte[1024]; // tampon de réception
            // 2 - émettre
            String message = "HELLO serveur !";
            envoyees = message.getBytes();
            DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseClient, 5555);
            socketClient.send(messageEnvoye);
            // 3 - Recevoir
            DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
            socketClient.receive(paquetRecu);
            String reponse = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
            System.out.println("Depuis le serveur: " + reponse);
            //4 - Libérer le canal
            socketClient.close();
        }
        catch(Exception e)
        {
            System.err.println(e);

        }
    }



}
