package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji će implementirati egzekuciju parsiranog stabla dokumenta.
 * 
 * @author Ivan Relić
 * 
 */
public class SmartScriptEngine {

	private DocumentNode documentNode;
	private RequestContext requestContext;
	private ObjectMultistack multistack = new ObjectMultistack();

	// stvaranje primjerka visitora koji će obavljati egzekuciju parserskog
	// stabla
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				String text = node.getText();
				requestContext.write(text);
			} catch (IOException e) {
				throw new RuntimeException(
						"Error writing on request context output stream!");
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			// dohvati vrijednosti za start value, end value i step value(ako
			// postoji)
			ValueWrapper startValue = getValue(node.getStartExpression());
			ValueWrapper endValue = getValue(node.getEndExpression());
			ValueWrapper stepValue;
			if (node.getStepExpression() == null) {
				stepValue = new ValueWrapper(new Integer(1));
			} else {
				stepValue = getValue(node.getStepExpression());
			}
			String variableName = node.getVariable().getName();
			// na multistack gurni varijablu s trenutnom vrijednosti
			multistack.push(variableName, startValue);
			// dok god trenutna vrijednost varijable nije veća od krajnje, vrti
			// se u petlji
			while (true) {
				int compareValue;
				try {
					compareValue = multistack.peek(variableName).numCompare(
							endValue.getValue());
				} catch (Exception e) {
					throw new RuntimeException("Cannot compare "
							+ startValue.getValue().toString() + " with "
							+ endValue.getValue().toString());
				}
				if (compareValue == 1) {
					break;
				}
				// prođi svom djecom for loop nodea i izvrši operacije za njih
				for (int i = 0, length = node.numberOfChildren(); i < length; i++) {
					node.getChild(i).accept(visitor);
				}
				// uvećaj varijablu iteratora za korak
				try {
					multistack.peek(variableName).increment(
							stepValue.getValue());
				} catch (Exception e) {
					throw new RuntimeException("Cannot increment "
							+ startValue.getValue().toString() + " with "
							+ stepValue.getValue().toString());
				}
			}
			// ukloni varijablu iteratora s multistacka
			multistack.remove(variableName);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<ValueWrapper> temporaryStack = new Stack<ValueWrapper>();
			Token[] tokens = node.getTokens();

			for (int i = 0, length = tokens.length; i < length; i++) {
				if (tokens[i] instanceof TokenConstantDouble
						|| tokens[i] instanceof TokenConstantInteger) {
					temporaryStack.push(new ValueWrapper(tokens[i].asText()));
				} else if (tokens[i] instanceof TokenString) {
					temporaryStack.push(new ValueWrapper(
							((TokenString) tokens[i]).getName()));
				} else if (tokens[i] instanceof TokenVariable) {
					temporaryStack.push(getVariable((TokenVariable) tokens[i]));
				} else if (tokens[i] instanceof TokenOperator) {
					executeOperator((TokenOperator) tokens[i], temporaryStack);
				} else if (tokens[i] instanceof TokenFunction) {
					executeFunction((TokenFunction) tokens[i], temporaryStack);
				} else {
					throw new RuntimeException("Unknown token!");
				}
			}
			// na pomocni stog prebaci sve s temporary stoga tako da dohvaćaš
			// vrijednosti pravim redoslijedom i zapisuj ih na output stream
			Stack<Object> helpStack = new Stack<Object>();
			while (!temporaryStack.isEmpty()) {
				helpStack.push(temporaryStack.pop());
			}
			while (!helpStack.isEmpty()) {
				try {
					requestContext.write(helpStack.pop().toString());
				} catch (IOException e) {
					throw new RuntimeException(
							"Error writing to output stream!");
				}
			}
		}

		/**
		 * Dohvaća value iz izraza for loop nodea. (string prikaz integera ili
		 * doublea, integer ili double)
		 * 
		 * @param expression
		 *            izraz for loop nodea
		 * @return ValueWrapper vrijednost izraza iz for loop nodea
		 */
		private ValueWrapper getValue(Token expression) {
			if (expression == null) {
				throw new RuntimeException(
						"For loop node expression should not be null!");
			}
			if (expression instanceof TokenString) {
				return new ValueWrapper(((TokenString) expression).getName());
			} else if (expression instanceof TokenConstantInteger) {
				return new ValueWrapper(
						((TokenConstantInteger) expression).getName());
			} else if (expression instanceof TokenConstantDouble) {
				return new ValueWrapper(
						((TokenConstantDouble) expression).getName());
			} else {
				throw new RuntimeException(
						"Unknown object in ForLoopNode expression: "
								+ expression.getClass().toString());
			}
		}

		/**
		 * Prima funkciju i stog na kojem treba izvršiti funkciju.
		 * 
		 * @param tokenFunction
		 *            funkcija
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeFunction(TokenFunction function,
				Stack<ValueWrapper> temporaryStack) {
			String functionName = function.getName();
			if (functionName.equals("sin")) {
				executeSinFunction(temporaryStack);
			} else if (functionName.equals("decfmt")) {
				executeDecfmtFunction(temporaryStack);
			} else if (functionName.equals("dup")) {
				executeDupFunction(temporaryStack);
			} else if (functionName.equals("swap")) {
				executeSwapFunction(temporaryStack);
			} else if (functionName.equals("setMimeType")) {
				executeSetMimeTypeFunction(temporaryStack);
			} else if (functionName.equals("paramGet")) {
				executeParamGetFunction(temporaryStack);
			} else if (functionName.equals("pparamSet")) {
				executePParamSetFunction(temporaryStack);
			} else if (functionName.equals("pparamGet")) {
				executePParamGetFunction(temporaryStack);
			} else if (functionName.equals("pparamDel")) {
				executePParamDelFunction(temporaryStack);
			} else if (functionName.equals("tparamSet")) {
				executeTParamSetFunction(temporaryStack);
			} else if (functionName.equals("tparamGet")) {
				executeTParamGetFunction(temporaryStack);
			} else if (functionName.equals("tparamDel")) {
				executeTParamDelFunction(temporaryStack);
			} else {
				throw new RuntimeException("Unsupported function: "
						+ functionName);
			}
		}

		/**
		 * Izvršava tparamGet funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeTParamGetFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value = getParameterValueFromMap(
					popFromStack(temporaryStack), popFromStack(temporaryStack),
					requestContext.getTemporaryParameters());
			temporaryStack.push(value);
		}

		/**
		 * Izvršava tparamSet funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeTParamSetFunction(Stack<ValueWrapper> temporaryStack) {
			setParameterValueToMap(popFromStack(temporaryStack),
					popFromStack(temporaryStack),
					requestContext.getTemporaryParameters());
		}

		/**
		 * Izvršava tparamDel funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeTParamDelFunction(Stack<ValueWrapper> temporaryStack) {
			delParameterValueFromMap(popFromStack(temporaryStack),
					requestContext.getTemporaryParameters());
		}

		/**
		 * Izvršava pparamGet funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executePParamGetFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value = getParameterValueFromMap(
					popFromStack(temporaryStack), popFromStack(temporaryStack),
					requestContext.getPersistentParameters());
			temporaryStack.push(value);
		}

		/**
		 * Izvršava pparamSet funkciju
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executePParamSetFunction(Stack<ValueWrapper> temporaryStack) {
			setParameterValueToMap(popFromStack(temporaryStack),
					popFromStack(temporaryStack),
					requestContext.getPersistentParameters());
		}

		/**
		 * Izvršava pparamDel funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executePParamDelFunction(Stack<ValueWrapper> temporaryStack) {
			delParameterValueFromMap(popFromStack(temporaryStack),
					requestContext.getPersistentParameters());
		}

		/**
		 * Izvršava paramGet funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeParamGetFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value = getParameterValueFromMap(
					popFromStack(temporaryStack), popFromStack(temporaryStack),
					requestContext.getParameters());
			temporaryStack.push(value);
		}

		/**
		 * Dohvaća vrijednost iz mape parametara
		 * 
		 * @param defaultValue
		 *            vrijednost koja će se upisati ako nema parametra pod
		 *            predanim imenom
		 * @param name
		 *            ime parametra
		 * @return vrijednost parametra
		 */
		private ValueWrapper getParameterValueFromMap(
				ValueWrapper defaultValue, ValueWrapper name,
				Map<String, String> map) {
			if (!(name.getValue() instanceof String)) {
				throw new RuntimeException("Cannot search map with object: "
						+ name.getClass().toString());
			}
			String value = map.get(name.getValue());
			return (value == null ? defaultValue : new ValueWrapper(value));
		}

		/**
		 * Postavlja vrijednost u mape parametara
		 * 
		 * @param value
		 *            vrijednost koja se stavlja u mapu
		 * @param name
		 *            ime parametra
		 */
		private void setParameterValueToMap(ValueWrapper name,
				ValueWrapper value, Map<String, String> map) {
			if (!(name.getValue() instanceof String)) {
				throw new RuntimeException("Cannot map with key class: "
						+ name.getValue().getClass().toString());
			}
			map.put((String) name.getValue(), value.getValue().toString());
		}

		/**
		 * Briše ključ name iz predane mape parametara.
		 * 
		 * @param name
		 *            ključ
		 * @param map
		 *            mapa za brisanje
		 */
		private void delParameterValueFromMap(ValueWrapper name,
				Map<String, String> map) {
			if (!(name.getValue() instanceof String)) {
				throw new RuntimeException(
						"Cannot remove association with object: "
								+ name.getClass().toString());
			}
			try {
				map.remove(name.getValue());
			} catch (Exception e) {
				throw new RuntimeException("Key " + name.toString()
						+ " doesn't exist in map!");
			}
		}

		/**
		 * Izvršava setMimeType funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeSetMimeTypeFunction(
				Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value = popFromStack(temporaryStack);
			if (!(value.getValue() instanceof String)) {
				throw new RuntimeException("Cannot set mime type with object: "
						+ value.getValue().getClass().toString());
			}
			try {
				requestContext.setMimeType((String) value.getValue());
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot set mime type because header was already generated!");
			}
		}

		/**
		 * Izvršava swap funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeSwapFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value1 = popFromStack(temporaryStack);
			ValueWrapper value2 = popFromStack(temporaryStack);
			temporaryStack.push(value1);
			temporaryStack.push(value2);
		}

		/**
		 * Izvršava dup funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeDupFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value = popFromStack(temporaryStack);
			temporaryStack.push(value);
			// pushaj kopiju vrijednosti skinute sa stoga (jedna vrijednost ne
			// smije imati veze s drugom)
			temporaryStack.push(new ValueWrapper(value.getValue()));
		}

		/**
		 * Skida element s vrha stoga (ako postoji).
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacija
		 * @return objekt s vrha stoga
		 */
		private ValueWrapper popFromStack(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value;
			try {
				value = temporaryStack.pop();
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot pop from stack! It is empty!");
			}
			return value;
		}

		/**
		 * Izvršava decfmt funkciju
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeDecfmtFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper formatValue = null;
			DecimalFormat f;
			// pokušaj dohvatiti formatter
			try {
				formatValue = popFromStack(temporaryStack);
				f = new DecimalFormat((String) formatValue.getValue());
			} catch (Exception e) {
				throw new RuntimeException(
						"Unsupported object for decimal format: "
								+ formatValue.getClass().toString());
			}
			// pokušaj dohvatiti double vrijednost broja iz wrappera
			ValueWrapper value = popFromStack(temporaryStack);
			double doubleValue = getDoubleValueFromWrapper(value);
			// formatiraj ga i vrati string prikaz tog broja na stog
			String formattedValue = f.format(doubleValue);
			temporaryStack.push(new ValueWrapper(formattedValue));
		}

		/**
		 * Izvršava sinus funkciju.
		 * 
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeSinFunction(Stack<ValueWrapper> temporaryStack) {
			ValueWrapper value = popFromStack(temporaryStack);
			double doubleValue = getDoubleValueFromWrapper(value);
			value.setValue(Math.sin(Math.toRadians(doubleValue)));
			temporaryStack.push(value);
		}

		/**
		 * Vraća double vrijednost iz wrappera (ukoliko je to moguće).
		 * 
		 * @param value
		 *            ValueWrapper vrijednost
		 * @return double vrijednost iz Wrappera, ako ju je moguće dohvatiti
		 */
		private double getDoubleValueFromWrapper(ValueWrapper value) {
			double doubleValue;
			if (value.getValue() instanceof String) {
				try {
					doubleValue = Double.parseDouble((String) value.getValue());
				} catch (Exception e) {
					throw new RuntimeException(
							"Unknown number value for object: "
									+ value.getValue().toString());
				}
			} else {
				try {
					doubleValue = ((Number) value.getValue()).doubleValue();
				} catch (Exception e) {
					throw new RuntimeException(
							"Unknown number value for object: "
									+ value.getValue().getClass().toString());
				}
			}
			return doubleValue;
		}

		/**
		 * Prima pročitani operator i stog na kojem izvršava operacije.
		 * 
		 * @param token
		 *            operator
		 * @param temporaryStack
		 *            stog za izvršavanje i pohranjivanje rezultata operacije
		 */
		private void executeOperator(TokenOperator operator,
				Stack<ValueWrapper> temporaryStack) {
			// pročitaj operande sa stoga
			ValueWrapper operand2 = popFromStack(temporaryStack);
			ValueWrapper operand1 = popFromStack(temporaryStack);
			if (operator.getName().equals("+")) {
				try {
					operand1.increment(operand2.getValue());
					temporaryStack.push(operand1);
				} catch (Exception e) {
					throw new RuntimeException("Cannot increment "
							+ operand1.getValue().toString() + " with "
							+ operand2.getValue().toString() + "!");
				}
			}
			if (operator.getName().equals("-")) {
				try {
					operand1.decrement(operand2.getValue());
					temporaryStack.push(operand1);
				} catch (Exception e) {
					throw new RuntimeException("Cannot decrement "
							+ operand1.getValue().toString() + " with "
							+ operand2.getValue().toString() + "!");
				}
			}
			if (operator.getName().equals("*")) {
				try {
					operand1.multiply(operand2.getValue());
					temporaryStack.push(operand1);
				} catch (Exception e) {
					throw new RuntimeException("Cannot multiply "
							+ operand1.getValue().toString() + " with "
							+ operand2.getValue().toString() + "!");
				}
			}
			if (operator.getName().equals("/")) {
				try {
					operand1.divide(operand2.getValue());
					temporaryStack.push(operand1);
				} catch (Exception e) {
					throw new RuntimeException("Cannot divide "
							+ operand1.getValue().toString() + " with "
							+ operand2.getValue().toString() + "!");
				}
			}
		}

		/**
		 * Dohvaća vrijednost varijable i vraća je u ValueWrapper obliku (ako
		 * postoji u multistacku).
		 * 
		 * @param variable
		 *            varijabla
		 * @return vrijednost varijable
		 */
		private ValueWrapper getVariable(TokenVariable variable) {
			try {
				ValueWrapper variableValue = multistack
						.peek(variable.getName());
				return new ValueWrapper(variableValue.getValue());
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot read variable with that name!");
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, length = node.numberOfChildren(); i < length; i++) {
				node.getChild(i).accept(visitor);
			}
		}
	};

	/**
	 * Konstruktor. Prima korijen parserskog stabla i primjerak razreda
	 * RequestContext s kojim radi.
	 * 
	 * @param documentNode
	 *            korijen parserskog stabla
	 * @param requestContext
	 *            primjerak request context razreda
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		if (documentNode == null) {
			throw new IllegalArgumentException(
					"Document node should not be null!");
		}
		if (requestContext == null) {
			throw new IllegalArgumentException(
					"Document node should not be null!");
		}
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Metoda izvršava naredbe iz parserskog stabla.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
