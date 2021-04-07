package extra.command;

import java.util.HashMap;
import java.util.Map;

import extra.Reference;
import stree.parser.SDefaultNode;
import stree.parser.SNode;

public class RunScript implements Command {
	private int nbInstructions;
	private SNode instructions;
	private SNode arguments;
	private int nbArguments;
	private Map<String, String> map;

	public RunScript(SNode instructions) {
		this.instructions = instructions;
		this.nbInstructions = instructions.size();
		this.arguments = instructions.get(0);
		this.nbArguments = instructions.get(0).size();
		this.map = new HashMap<String, String>();
	}

	@Override
	public Reference run(Reference reference, SNode method) {
		SNode currentInstruction;
		Reference ref;
		generateMap(method);
		for(int i = 1; i < nbInstructions ; i++) {
			currentInstruction = generateIntruction(instructions.get(i), method);
			ref = getReference(reference, currentInstruction.get(0).contents());
			ref.getCommandByName(currentInstruction.get(1).contents()).run(ref, currentInstruction);
		}
		return reference;
	}
	
	private SNode generateIntruction(SNode currentNode, SNode input) {
		SNode toReturn = new SDefaultNode();
		toReturn.addChild(generateCorrectName(currentNode.get(0), input));
		for(int i = 1; i < currentNode.size(); i++) {
			toReturn.addChild(changeContent(currentNode.get(i), input));
		}
		return toReturn;
	}
	
	private SNode changeContent(SNode currentNode, SNode input) {
		SNode toReturn;
		String str = null;
		if(currentNode.isLeaf()) {
			toReturn = new SDefaultNode();
			str = map.getOrDefault(currentNode.contents(), null);
			if(str == null)
				toReturn.setContents(currentNode.contents());
			else 
				toReturn.setContents(str);
		}
		else {
			toReturn = generateIntruction(currentNode, input);
		}
		return toReturn; 
	}
	// (space addScript nr ((self name) (self add name (rect.class new)))) 
	private void generateMap(SNode method) {
		this.map.put(arguments.get(0).contents(), method.get(0).contents());
		for(int i = 1; i < this.nbArguments; i++) {
			this.map.put(arguments.get(i).contents(), method.get(i + 1).contents());
		}
	}
	
	private SNode generateCorrectName(SNode method, SNode input) {
		SNode toReturn = new SDefaultNode();
		String currentName = method.contents();
		String[] allNames = currentName.split("\\.");
		String correctName = "";
		for(String name : allNames) {
			if(map.containsKey(name)) {
				correctName += map.get(name);
			}
			else {
				correctName += name;
			}
			correctName += ".";
		}
		correctName = correctName.substring(0, correctName.length() - 1);
		toReturn.setContents(correctName);
		return toReturn;
	}
	
	private Reference getReference(Reference root, String name) {
		if(name.compareTo(map.get("self")) == 0)
			return root;
		else {
			name = name.replaceFirst(map.get("self"), "");
			String[] allNames = name.split("\\.");
			Reference ref = root;
			for(int i = 1; i < allNames.length; i++) {
				ref = ref.getEnvironment().getReferenceByName(allNames[i]);
			}
			return ref;
		}
	}
}
