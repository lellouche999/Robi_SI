package extra;

import java.io.PrintStream;

import stree.parser.SNode;

public class Interpreter {
	
	private PrintStream ps;
	
	public Interpreter(PrintStream ps) {
		this.ps = ps;
	}

	public void compute(Environment environment, SNode next) {
		Reference exec;
		try {
			Reference receiver = getReceiver(environment, next);
			ps.println(recoverCommand(next) + " -- ok");
			exec = receiver.run(next);
			if(next.get(1).contents().compareTo("info") == 0)
				ps.println(exec.toString());
		} catch(NullPointerException e) {
			e.printStackTrace();
			ps.println("### ERROR(NullPointerException) ###");
			ps.println(recoverCommand(next));
			ps.println("### ERROR ###");
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			ps.println("### ERROR(IndexOutOfBoundsException) ###");
			ps.println(recoverCommand(next));
			ps.println("### ERROR ###");
		}
	}
	
	private Reference getReceiver(Environment environment, SNode next) {
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
	
	private String recoverCommand(SNode command) {
		String ret = "";
		int i = 0;
		while(i < command.size() && command.get(i).isLeaf()) {
			ret += command.get(i).contents() + " ";
			i++;
		}
		return ret;
	}
}
