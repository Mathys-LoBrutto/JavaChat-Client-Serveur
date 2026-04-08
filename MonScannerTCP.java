import java.net.ServerSocket;

public class MonScannerTCP {

    public MonScannerTCP()
    {

    }

    public static void scanTCPPorts(int portMin, int portMax)
    {
        for (int port = portMin; port <= portMax; port++ )
        {
            try
            {
                ServerSocket socket = new ServerSocket(port);
                System.out.println("le port " + port + " est ouvert");
                socket.close();
            }catch(Exception e)
            {
                System.out.println("le port" + port + " est occupé");
            }
            
        }
    }
    
}
