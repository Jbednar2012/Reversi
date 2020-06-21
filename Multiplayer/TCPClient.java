package jbednarski;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {
    
    public static void main(String[] args){
        try {
            Socket s = new Socket("localhost", 65500);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            InputStream is = s.getInputStream();
            ServerMove serverMove = new ServerMove(Field.WHITE, 0, 0);
            oos.writeObject(serverMove);
            oos.flush();
            System.out.println("sent");
            System.out.println(is.read());
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
