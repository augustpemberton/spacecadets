import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
		Interpreter interpreter= new Interpreter("C:\\Users\\user\\SpaceCadets\\BarebonesInterpreter\\source.barebones");
		interpreter.interpretAllLines();
    }
}

class Interpreter{
	private List<String> sourceCode = new ArrayList<String>();
	private String path;

	private int currentLine = 0;
	private Stack<Integer> lastWhile = new Stack<>();

	private HashMap<String, Integer> variables = new HashMap<>();

	Interpreter(String path){
		this.path = path;
		this.loadCode();
	}

	void interpretAllLines(){
		while(true){
			interpretLine(currentLine);
		}

	}

	void throwException(String exceptionType, String error){
		System.out.println("EXCEPTION: Type " + exceptionType);
		System.out.println(error);
		System.exit(0);
	}

	//interpret from line number
	void interpretLine(int lineNumber){
		if(lineNumber >= this.sourceCode.size()) {
			throwException("EOFException", "Unexpected end of file.");
		}
		//get, trim and split line
		String line = this.sourceCode.get(lineNumber).trim();
		System.out.println(this.sourceCode.get(lineNumber));
		String[] splittedLine = line.split("\\s+");

		//interpret line
		interpret(splittedLine, lineNumber);
	}

	//interpret string directly
	void interpretLine(String line){
		String[] splittedLine = line.split("\\s+");
		interpret(splittedLine, 0);
	}

	//do the actual interpreting
	private void interpret(String[] splittedLine, int lineNumber) {
		switch(splittedLine[0]){
			case "incr":
				this.modifyVariable(splittedLine[1], "+");
				break;
			case "decr":
				this.modifyVariable(splittedLine[1], "-");
				break;
			case "clear":
				this.modifyVariable(splittedLine[1], "_");
				break;
			case "while":
				this.lastWhile.push(lineNumber);
				break;
			case "end":
				if(this.getValueFromWhile(lastWhile.peek()) != 0){ 	//if currently in a while loop
					this.currentLine = this.lastWhile.peek();
				} else {
					if(this.lastWhile.size() > 1){ 					//if nested in another loop
						this.lastWhile.pop();
					} else {										//if reached end of file
						System.out.println("Program complete.");
						this.outputVariables();
						System.exit(0);
					}

				}
			default:
				//throw error,
				break;
		}
		this.currentLine++; //increment to the next line
	}

	//get the value of the variable specified in the last while loop
	private int getValueFromWhile(int lineNumber){
		String line = this.sourceCode.get(lineNumber);

		String[] splittedLine = line.trim().split("\\s+");
		if(!this.variables.containsKey(splittedLine[1])){
			this.variables.put(splittedLine[1], 0);
		}
		return this.variables.get(splittedLine[1]);
	}


	private void modifyVariable(String variable, String operator){
		//check if variable exists yet
		if(!this.variables.containsKey(variable)){
			this.variables.put(variable, 0);
		}

		switch(operator){
			case "+":
				this.variables.put(variable, this.variables.get(variable) + 1);
				break;
			case "-":
				this.variables.put(variable, this.variables.get(variable) - 1);
				break;
			case "_":
				this.variables.put(variable, 0);
				break;
		}
	}





	private void loadCode(){
		try (BufferedReader br = new BufferedReader(new FileReader(this.path)))
		{

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.replace(";", "");
				this.sourceCode.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void outputVariables(){
		Iterator it = this.variables.entrySet().iterator();
		if (!it.hasNext()){
			System.out.println("No variables.");
		}
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
		if(this.lastWhile.size() > 0){
			System.out.println("Last While: " + this.lastWhile.peek());
		}

	}
}
