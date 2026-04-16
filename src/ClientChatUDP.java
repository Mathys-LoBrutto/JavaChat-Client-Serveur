    package src;
    import java.net.DatagramPacket;
    import java.net.DatagramSocket;
    import java.net.InetAddress;
    import java.util.Scanner;


    public class ClientChatUDP {
        

        static public void main(String[] args)
        {
            try {
                Scanner sc = new Scanner(System.in);

                System.out.println("Veuillez saisir votre pseudo :");
                String pseudo = sc.nextLine();

                DatagramSocket socketClient = new DatagramSocket();

                // pour communiquer avec le serveur
                InetAddress adresseServeur = InetAddress.getByName("localhost");

                
                String messageClient = "JOIN:" + pseudo;
                byte[] envoyees = messageClient.getBytes();
                
                // On envoie à l'adresse "localhost" sur le port d'écoute principal du serveur (9000)
                DatagramPacket messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseServeur, 9000);
                socketClient.send(messageEnvoye);

                System.out.println("Demande de connexion envoyée...");

                // mise en place du systeme de réceptions 
                byte[] recues = new byte[1024];

                
                DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                socketClient.receive(paquetRecu);

                String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
                System.out.println("Réponse du serveur : " + messageRecu);

                /** récupération du port dédié renvoyé par le serveur */
                String[] reponseDecoupe = messageRecu.split(":");
                int portDedie = Integer.parseInt(reponseDecoupe[1]); 


                //*Création d'un Thread d'écoute pour ecouter et envoyer des messages en meme temps (le while est bloquant) */
                Thread Ecoute = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        try
                        {
                            while(true)
                        {
                            
                                DatagramPacket paquetRecu = new DatagramPacket(recues, recues.length);
                                socketClient.receive(paquetRecu);

                                String messageRecu = new String(paquetRecu.getData(), 0, paquetRecu.getLength());
                                System.out.println("message du serveur : " + messageRecu); 
                            
                              
                        }
                        }catch(Exception e){
                                System.out.print("Fin de la reception des messages"); //car la fermeture du socket va créer une erreur.
                            }
                        
                    }
                    
                });
                Ecoute.start();

                //*Création d'une boucle de saisie */
                while(true)
                {
                    String message = sc.nextLine();

                    envoyees = message.getBytes();
                    messageEnvoye = new DatagramPacket(envoyees, envoyees.length, adresseServeur, portDedie);
                    socketClient.send(messageEnvoye);

                    //*Le client veut se déconnecter */
                    if (message.equals("exit")) 
                    {
                        socketClient.close();
                        break; 
                    }

                }

            } catch (Exception e) {
                System.err.println("Erreur côté client : " + e.getMessage());
            }
            


        }
    }
