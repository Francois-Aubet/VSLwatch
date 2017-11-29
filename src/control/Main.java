package control;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import model.State;




public class Main {
	private static final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
	static boolean stop = false;
	static State piState;
	
	// should start on the launch of the pi
	public static void main(String[] args) {
		System.out.println("starting prog");
		
		piState = new State();
		
			
		
		//sock = new SocketManager(queue,ipAddress, 20009);
		//sock.start();
		
		
		while(!stop){
		
			try {
				aConnectionLoop();
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		System.out.println("the end");
	}
	
	
	
	public static void aConnectionLoop() throws IOException{
		boolean stoploop = false;
		ServerSocket socketserver  ;
		Socket socketduserveur ;
        BufferedReader in;
        PrintWriter out;
		socketserver = new ServerSocket(20009);
		socketduserveur = socketserver.accept(); 
		System.out.println("connection!");
		
        in = new BufferedReader (new InputStreamReader (socketduserveur.getInputStream()));
		out = new PrintWriter(socketduserveur.getOutputStream());
		

        out.println("pi listening!");
        out.flush();
        
        while(!stoploop){
            String command = in.readLine();
            System.out.println(command);
            
            if(command == null){ break; }
            
            switch (command) {
            case "startWIFI":
            	
            	
                break;
         
                
            case "Sunday":
                break;
            default:
            
        }
        }
        
        

	    socketserver.close();
        socketduserveur.close();
	}
	
	
	
	

    private static void sendCommand(String com) {
        while (!queue.offer(com)) {
            queue.poll();
        }
    }



	public static void onReceivedCommand(String line) {
        switch (line) {
        case "Lumi":
            
            break;
        case "WIFIfalse":
        	break;

        default:
    }
	}

}
