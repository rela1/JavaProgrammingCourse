package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radnik koji ispisuje sve predane parametre u HTTP zahtjevu.
 * 
 * @author Ivan Relić
 * 
 */
public class EchoWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		Map<String, String> params = context.getParameters();
		context.setMimeType("text/plain");
		for (Entry<String, String> entry : params.entrySet()) {
			String formattedOutput = String.format("%s = %s\r\n",
					entry.getKey(), entry.getValue());
			try {
				context.write(formattedOutput);
			} catch (IOException e) {
				System.out.println("Error writing on ouput stream!");
				System.exit(0);
			}
		}
	}

}
