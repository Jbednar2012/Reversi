package jbednarski;

import com.wyklad.networking.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
    
    public static void main(String[] args){
        Socket s = null;
        ServerSocket server = null;
        try {
            server = new ServerSocket(65500);
            s = server.accept();
            System.out.println("accepted");
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ServerMove o = (ServerMove)ois.readObject();
            System.out.println(o);
            OutputStream os = s.getOutputStream();
            os.write(1);
            System.out.println("ok");
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
	//	} catch (ClassNotFoundException ex) {
        //  Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                s.close();
                server.close();
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
