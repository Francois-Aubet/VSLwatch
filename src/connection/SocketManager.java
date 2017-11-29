package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import control.Main;


public class SocketManager  extends Thread {


	private final BlockingQueue<String> queue;
	private final String hostIP;
	private Socket socket = null;
	private final int port;
	private Output appOutput;	//the class is here, in the RobotSocketManager class, look there to understand
	private Input appInput;		//the class is here, in the RobotSocketManager class, look there to understand
	public boolean connected = false;	// leting know if the connection is active
	private BufferedReader in;
	private BufferedWriter out;

	public SocketManager(BlockingQueue<String> queue, String hostIP, int port) {
		this.queue = queue;
		this.hostIP = hostIP;
		this.port = port;
	}

@Override
	public void run() {

		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(hostIP, port), 5000);

            in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
			appInput = new Input(in);
			appInput.start();

			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			appOutput = new Output(queue,out);
			appOutput.start();


			appInput.join();
			appOutput.join();


		} catch (Exception e ) { //| InterruptedException
			e.printStackTrace();
		} finally {
			try {
				if (socket != null)
					socket.close();
					connected = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	private static class Output extends Thread {
		private final BlockingQueue<String> queue;
		private final BufferedWriter output;

		public Output(BlockingQueue<String> queue, BufferedWriter output) {
			this.queue = queue;
			this.output = output;
		}

		boolean stopRequested = false;
		@Override
		public void run() {
			while (!stopRequested) {
				try {
					final String command = queue.take();
					output.write(command + "\n");
					output.flush();
					System.out.println(command);
				} catch (Exception e) {
					System.out.println("                                fail");
					stopRequested = true;
				}
				//try {	output.flush();	} catch ( IOException e) {	System.out.println("fail2");}
				//System.out.println("out");
			}
			//output.close();
			queue.clear();
		}


	}


	private static class Input extends Thread {
		//TODO: reaction to the answers -> actualize state
		private BufferedReader input;

		public Input(BufferedReader inputstream) {
			this.input = inputstream;
		}

		boolean stopRequested = false;

		@Override
		public void run() {
			while (!stopRequested) {
				//System.out.println("in0");
				String line = "error";
				try{
					line = input.readLine();
				}
				catch (Exception e) { //
					e.printStackTrace();
					stopRequested = true;
				}
				if(line != null){		//if a message is received:
					System.out.println("rep:" + line);
					Main.onReceivedCommand(line);
				}
                try {
                    Thread.sleep(100);
                }catch(InterruptedException e){}
				//System.out.println("in");


			}

		}

	}



}

