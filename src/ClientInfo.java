package src;

import java.net.InetAddress;

public class ClientInfo {

    private String pseudo;
    private InetAddress adresseIP;
    private int port;

    public ClientInfo(String pseudo, InetAddress adresseIP, int port)
    {
        this.pseudo = pseudo;
        this.adresseIP = adresseIP;
        this.port = port;

    }
    

    public String getPseudo()
    {
        return this.pseudo;
    }

    public InetAddress adresseIP()
    {
        return this.adresseIP;
    }

    public int getPort()
    {
        return this.port;
    }
}
