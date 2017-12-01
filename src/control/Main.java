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
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import connection.SocketManagerServer;
import model.State;




public class Main {
	private static final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
	static boolean stop = false;
	static State piState;
	static SocketManagerServer sock;
	
	// should start on the launch of the pi
	public static void main(String[] args) {
		System.out.println("starting prog");
		Scanner scanner = new Scanner(System.in);
		piState = new State();
		
			
		
		sock = new SocketManagerServer(queue, 20009);
		sock.start();
		
		
		while(!stop){
			//String commandInput = scanner.next();
			String commandInput = "askLum";
			if(commandInput == "stop"){
				stop = true;
				sock.stopRequested = true;
			} else {
				sendCommand(commandInput);	//send "askLum" to get the lum as answer
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			sock.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
				

		
		
		System.out.println("the end");
	}
		

    public static void sendCommand(String com) {
        while (!queue.offer(com)) {
            queue.poll();
        }
    }



	public static void onReceivedCommand(String line) {
		
		piState.lightValue = Double.parseDouble(line);
		
       /* switch (line) {
        case "Lumi":
            
            break;
        case "WIFIfalse":
        	break;

        default:
        }*/
        
        
        
	}

}
