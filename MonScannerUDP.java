import java.net.DatagramSocket;
import java.net.SocketException;



public class MonScannerUDP {

    public MonScannerUDP(){
        
    }


    public static void scanUDPPorts(int portMin, int portMax)
    {
        
            for (int port = portMin; port <= portMax; port++) 
            {
                try {
                    DatagramSocket socket = new DatagramSocket(port);
                    System.out.println("Port " + port + " est ouvert");
                    socket.close();
                } catch (SocketException e) {
                    System.out.println("Port " + port + " est occupé");
                }
            }
    }

    
}
