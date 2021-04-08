package server;

/*
(space add robi (rect new))
(space.robi translate 130 50)
(space.robi setColor yellow)
(space add momo (oval new))
(space.momo setColor red)
(space.momo translate 80 80)
(space add pif (image new alien.gif))
(space.pif translate 100 0)
(space add hello (string new "Hello world"))
(space.hello translate 10 10)
(space.hello setColor black)
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import extra.Environment;
import extra.Reference;
import extra.TypeReference;
import extra.command.*;
import graphicLayer.GRect;
import graphicLayer.GSpace;
import stree.parser.SNode;
import stree.parser.SParser;

public class server {


	// une seule variable d'instance 
	Environment environment = new Environment();

	public server() throws Exception {
		ServerSocket server = new ServerSocket(8000);
		System.out.println("Server ROBI");
		
		//ici  space et robi sont temporaire
		GSpace space = new GSpace("Projet ROBI", new Dimension(200, 200));
		space.open();

		Reference Ref_space = new Reference(space);
		Ref_space.addCommand("setColor", new SetColor());
		Ref_space.addCommand("sleep", new Sleep());
		Ref_space.addCommand("add", new AddElement());
		Ref_space.addCommand("del", new DelElement());
		
		// enrigestrement des references dans l'environement par nom
		environment.addReference("space", Ref_space);
		TypeReference.init(environment);

		this.mainLoop(server);
	}

	private void mainLoop(ServerSocket server) throws Exception{
		String commande ="";
		String lecture="";
		PrintStream ps;
		BufferedReader br;
		Socket socket;
		
		while (true) {
			socket = server.accept();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ps = new PrintStream(socket.getOutputStream());
			commande="";
			//lecture de la socket.
			lecture=br.readLine();
			while(!lecture.equals("fin")) {
				if(lecture.equals("stop")) {	//si stop le programme s'arrete.
					server.close();
					System.exit(1);;
				}
				commande+=lecture;
				lecture=br.readLine();
			}
			System.out.println(commande);

			// creation du parser
			SParser<SNode> parser = new SParser<>();
			// compilation
			List<SNode> compiled = null;
			
			
			try {
				compiled = parser.parse(commande);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Iterator<SNode> itor = compiled.iterator();
			while (itor.hasNext()) {
				SNode expr = itor.next();
				Reference receiver = getReceiver(environment, expr);
				receiver.run(expr);
				ps.println(returnCommand(expr));
			}
			ps.println("GOOD");
		}
	}
	
	public Reference getReceiver(Environment environment, SNode next) {
		String receiverName = next.get(0).contents();
		String[] allRefNames = receiverName.split("\\.");
		Reference receiver = environment.getReferenceByName(receiverName);
		Environment refEnv = environment;
		
		for(String refName : allRefNames) {
			receiver = refEnv.getReferenceByName(refName);
			refEnv = receiver.getEnvironment();
		}
		
		return receiver;
	}

	private void run(SNode expr) {
		// quel est le nom du receiver
		String receiverName = expr.get(0).contents();
		// quel est le receiver
		Reference receiver = environment.getReferenceByName(receiverName);
		// demande au receiver d'executer la s-expression compilee
		receiver.run(expr);
	}

	public static void main(String[] args){
		try {
			new server();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String returnCommand(SNode command) {
		String res = "";
		int i = 0;
		while(i < command.size() && command.get(i).isLeaf()) {
			res += command.get(i).contents() + " ";
			i++;
		}
		return res;
	}

}


