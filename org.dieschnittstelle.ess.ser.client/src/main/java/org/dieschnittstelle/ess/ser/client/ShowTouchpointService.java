package org.dieschnittstelle.ess.ser.client;

import java.io.*;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Address;
import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.ess.utils.Http;

import static org.dieschnittstelle.ess.utils.Utils.*;

public class ShowTouchpointService {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(ShowTouchpointService.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShowTouchpointService service = new ShowTouchpointService();
		service.run();
	}

	/**
	 * the http client that can be used for accessing the service on tomcat - note that we are usying an async client here
	 */
	private CloseableHttpAsyncClient client;
	
	/**
	 * the attribute that controls whether we are running through (when called from the junit test) or not
	 */
	private boolean stepwise = true;

	/**
	 * constructor
	 */
	public ShowTouchpointService() {

	}

	/*
	 * create the http client - this will be done for each request
	 */
	public void createClient() {
		if (client != null && client.isRunning()) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		client = Http.createAsyncClient();
		client.start();
	}

	/**
	 * run
	 */
	public void run() {

		// 1) read out all touchpoints
		List<AbstractTouchpoint> touchpoints = readAllTouchpoints();

		// 2) delete the touchpoint after next console input
		if (touchpoints != null && touchpoints.size() > 0) {
			if (stepwise)
				step();

			deleteTouchpoint(touchpoints.get(0));
		}

		// 3) wait for input and create a new touchpoint
		if (stepwise) {
			step();
		}

		Address addr = new Address("Luxemburger Strasse", "100", "13353",
				"Berlin");
		StationaryTouchpoint tp = new StationaryTouchpoint(-1,
				"BHT Verkaufsstand", addr);

		createNewTouchpoint(tp);

		try {
			client.close();
		}
		catch (IOException ioe) {
			logger.error("got IOException trying to close client: " + ioe,ioe);
		}

		show("TestTouchpointService: done.\n");
	}

	/**
	 * read all touchpoints
	 * 
	 * @return
	 */
	public List<AbstractTouchpoint> readAllTouchpoints() {

		logger.info("readAllTouchpoints()");

		createClient();

		logger.debug("client running: {}",client.isRunning());

		// demonstrate access to the asynchronously running servlet (client-side access is asynchronous in any case)
		boolean async = false;

		try {

			// create a GetMethod

			// UE SER1: Aendern Sie die URL von api->gui
			HttpGet get = new HttpGet(
					"http://localhost:8080/api/" + (async ? "async/touchpoints" : "touchpoints"));

			logger.info("readAllTouchpoints(): about to execute request: " + get);

			// mittels der <request>.setHeader() Methode koennen Header-Felder
			// gesetzt werden

			// execute the method and obtain the response - for AsyncClient this will be a future from
			// which the response object can be obtained synchronously calling get() - alternatively, a FutureCallback can
			// be passed to the execute() method
			Future<HttpResponse> responseFuture = client.execute(get, null);
			logger.info("readAllTouchpoints(): received response future...");

			HttpResponse response = responseFuture.get();
			logger.info("readAllTouchpoints(): received response value");

			// check the response status
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				// try to read out an object from the response entity
				ObjectInputStream ois = new ObjectInputStream(response
						.getEntity().getContent());

				List<AbstractTouchpoint> touchpoints = (List<AbstractTouchpoint>) ois
						.readObject();

				logger.info("read touchpoints: " + touchpoints);

				return touchpoints;

			} else {
				String err = "could not successfully execute request. Got status code: "
						+ response.getStatusLine().getStatusCode();
				logger.error(err);
				throw new RuntimeException(err);
			}

		} catch (Exception e) {
			String err = "got exception: " + e;
			logger.error(err, e);
			throw new RuntimeException(e);
		}
	}


	/**
	 * TODO SER3
	 * 
	 * fuer das Schreiben des zu erzeugenden Objekts als Request Body siehe die
	 * Hinweise auf:
	 * http://stackoverflow.com/questions/10146692/how-do-i-write-to
	 * -an-outpustream-using-defaulthttpclient
	 * 
	 * @param tp
	 */
	public AbstractTouchpoint createNewTouchpoint(AbstractTouchpoint tp) {
		logger.info("createNewTouchpoint(): will create: " + tp);

		createClient();

		logger.debug("client running: {}",client.isRunning());

		try {

			// create post request for the api/touchpoints uri
			HttpPost request = new HttpPost("http://localhost:8080/api/touchpoints");
																			// Resource touchpoints dem wir ein eine weitere hinzufügen woller
			// Also die Menge aller touchpoints, die auf Serverseite bekannt sind.
			// Technisch werden wir den zugriff auf die resource durch ein servlet umsetzene, welches mit dieser URL assoziert ist.
			// Also ist quasi in der web.xml konfiguriert, dass das Servelet (TouchpointServiceServelet) mit allen request
			// associert wird, die mit der Url /api/touchpoints/* kommen (Siehe web.xml dern unterste eintrag bei servlet mapping)



			// create an ObjectOutputStream from a ByteArrayOutputStream - the
			// latter must be accessible via a variable
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(out); // Zum umwandeln von tp in ein übertragbares format zwischen zwei JVM's


			// write the object to the output stream
			objOut.writeObject(tp); // Daten liegen jetzt in "out"

			byte[] transportableObjectData = out.toByteArray();	// fürs bessere verständniss

			// create a ByteArrayEntity and pass it the byte array from the
			// output stream
			ByteArrayEntity requestBodyAndHeaderInformation = new ByteArrayEntity(transportableObjectData);
			// Das zwischenglied .... Headerinformationen

			// set the entity on the request
			request.setEntity(requestBodyAndHeaderInformation);
			// Paket ist geschnürt!! Jetzt muss es nur noch abgeschickt werden!

			// Packet abschicken!
			// execute the request, which will return a Future<HttpResponse> object
			Future<HttpResponse> responseFuture = client.execute(request,null);
			// (Versprechen) irgendwann enthält Future ein Objekt welches den typ hat, den ich im typparameter angebe.
			// Irgendwann in unserem fall, wenn der server auf den ich zugreife, eine antwort geliefert hat.
			// Ich glaube das ist ein asychchroner aufruf

			// get the response from the Future object
			HttpResponse response = responseFuture.get();		// wir warten mit get bis ein rüchgabe da ist
			// get ist so implementiert, das es warte. sobald was von get kommt, dann wissen wir, das was vom server kam

			// log the status line
			logger.info("entity is set and request is executed");
			logger.info("response status: " + response.getStatusLine()); // gibt den status vom server zurückkommende status zurück
			// z.b 200 = OK, 405 =  Method not Allowed usw

			// Wir bekommen am anfang ein 405 (Method not allowed)
			// Sieplregeln des Framworks! Damit ein Servelet ein Request bearbeiten kann, brauch es eine bearbeitungsmethode!
			// Das Servelet muss, wenn es den Request vom clinet entgegennimmt, eine gegeignete Methode habe, die der Http-methode
			// des requests entspricht! in diesem FALL !!POST!!
			//  DAFÜR EINFACH AUF SERVER_SEITE DIE DO POST METHODE WIEDER EINKOMMENTIEREN! (TouchpointServiceServlet.java)
			// NICHT VERGESSEN! SERVER NEUSTART!!

			// evaluate the result using getStatusLine(), use constants in
			// HttpStatus

				// KOMMT VOM SERVER ZURÜCK
			/* if successful: */
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {

				// create an object input stream using getContent() from the
				// response entity (accessible via getEntity())
				InputStream responseBody = response.getEntity().getContent(); // die client API ist sehr verschachtelt!
				ObjectInputStream responseBodyReader = new ObjectInputStream(responseBody);

				// read the touchpoint object from the input stream
				AbstractTouchpoint receivedTouchpoint = (AbstractTouchpoint) responseBodyReader.readObject();

				// return the object that you have read from the response
				show("received touchpoint: %s", receivedTouchpoint);
				// received == tp -> false        received.equals(tp) -> true
				return receivedTouchpoint;

			}
			return null;			// Für die tests, den tpuchpoint, der ampfangen wurde mus returned werden!!

		} catch (Exception e) {
			logger.error("got exception: " + e, e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * TODO SER4
	 * 	19.05.2021
	 * 	Muss ähnlich sein wie das create nur, dass jetzt ein HtttpDelete eingesetzt werden müsste.
	 * 	Die redundanz in der URI kann ich mit der tp.getID() machen.
	 * 	wir bracuhen keine streams und können dirket in den respons TRUE oder FALS hineinschreiben
	 * 	fürs löschen den CRUDececutor nutzen.
	 * @param tp
	 */
	public void deleteTouchpoint(AbstractTouchpoint tp) {
		logger.info("deleteTouchpoint(): will delete: " + tp);

		createClient();

		logger.debug("client running: {}",client.isRunning());

		try {

			// create delete request for the api/touchpoints/id uri
			HttpDelete request = new HttpDelete("http://localhost:8080/api/touchpoints/" + tp.getId());

			// Packet abschicken!
			// execute the request, which will return a Future<HttpResponse> object
			Future<HttpResponse> responseFuture = client.execute(request,null);

			// get the response from the Future object
			HttpResponse response = responseFuture.get();		// wir warten mit get bis ein rüchgabe da ist
			// get ist so implementiert, das es warte. sobald was von get kommt, dann wissen wir, das was vom server kam

			// log the status line
			logger.info("entity is set and request is executed");
			logger.info("response status: " + response.getStatusLine()); // gibt den status vom server zurückkommende status zurück

			// KOMMT VOM SERVER ZURÜCK
			/* if successful: */
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
				logger.info("Delete Request on Server accepted and doDelete() is running on server because !die Regeln des Frameworks!");
				logger.info("Touchpoint with ID " + tp.getId()+ " was deleted");
			}
			else
			{
				logger.info("Something goes wrong");
				logger.info("response status: " + response.getStatusLine());
			}

		} catch (Exception e) {
			logger.error("got exception: " + e, e);
			throw new RuntimeException(e);
		}

	}


	/**
	 * 
	 * @param stepwise
	 */
	public void setStepwise(boolean stepwise) {
		this.stepwise = stepwise;
	}

}
