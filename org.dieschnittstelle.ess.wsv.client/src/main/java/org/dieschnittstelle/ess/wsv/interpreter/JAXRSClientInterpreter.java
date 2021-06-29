package org.dieschnittstelle.ess.wsv.interpreter;


import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.apache.http.client.methods.HttpDelete;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import org.dieschnittstelle.ess.utils.Http;
import org.dieschnittstelle.ess.wsv.interpreter.json.JSONObjectSerialiser;

import static org.dieschnittstelle.ess.utils.Utils.*;


/*
 * TODO WSV1: implement this class such that the crud operations declared on ITouchpointCRUDService in .ess.wsv can be successfully called from the class AccessRESTServiceWithInterpreter in the .esa.wsv.client project
 */
public class JAXRSClientInterpreter implements InvocationHandler {

    // use a logger
    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(JAXRSClientInterpreter.class);

    // declare a baseurl
    private String baseurl;

    // declare a common path segment
    private String commonPath;

    // use our own implementation JSONObjectSerialiser
    private JSONObjectSerialiser jsonSerialiser = new JSONObjectSerialiser();

    // use an attribute that holds the serviceInterface (useful, e.g. for providing a toString() method)
    private Class serviceInterface;

    // use a constructor that takes an annotated service interface and a baseurl. the implementation should read out the path annotation, we assume we produce and consume json, i.e. the @Produces and @Consumes annotations will not be considered here
    public JAXRSClientInterpreter(Class serviceInterface,String baseurl) {

        // TODO: implement the constructor!
        this.serviceInterface = serviceInterface;           // siehe notizen und aufzeihnungen 34:50 (WSV1-...)
        this.baseurl = baseurl;
        this.commonPath =   ( (Path) serviceInterface.getAnnotation(Path. class)).value();

        logger.info("<constructor>: " + serviceInterface + " / " + baseurl + " / " + commonPath);
    }

    // TODO: implement this method interpreting jax-rs annotations on the meth argument
    @Override
    public Object invoke(Object proxy, Method meth, Object[] args)
            throws Throwable {

        // TODO check whether we handle the toString method and give some appropriate return value#
        if ("toString".equals(meth.getName()))
        {
            return "JAX-RS Proxy for " + this.serviceInterface;
        }
        // 01:04:50 WSV 1
        show("invoke(): for method " + meth.getName() + "with args: " + Arrays.asList( args == null ? new Object[]{}: args)); ;

        // use a default http client
        HttpClient client = Http.createSyncClient();

        // TODO: create the url using baseurl and commonpath (further segments may be added if the method has an own @Path annotation)
        String url = this.baseurl + this.commonPath
                + (meth.isAnnotationPresent(Path.class)
                ? meth.getAnnotation(Path.class).value()
                : "" );
        // mithilfe von reflection bekommen wir herraus

        // TODO: check whether we have a path annotation and append the url (path params will be handled when looking at the method arguments)

        // a value that needs to be sent via the http request body
        Object bodyValue = null;

        // TODO: check whether we have method arguments - only consider pathparam annotations (if any) on the first argument here - if no args are passed, the value of args is null! if no pathparam annotation is present assume that the argument value is passed via the body of the http request
        if (args != null && args.length > 0) {
            if (meth.getParameterAnnotations()[0].length > 0 && meth.getParameterAnnotations()[0][0].annotationType() == PathParam.class) {
                // TODO: handle PathParam on the first argument - do not forget that in this case we might have a second argument providing a bodyValue
                // TODO: if we have a path param, we need to replace the corresponding pattern in the url with the parameter value


                String variableExpr;        // PathParam
                String argumentValue;

                variableExpr = meth.getParameterAnnotations()[0][0].toString();
                argumentValue = args[0].toString();

                // Nur den "value" holen komisch, dass es hierfür keine methode gibt
                show("PathParam Value to Delete : " + variableExpr);
                //  @javax.ws.rs.PathParam(value="touchpointId")
                String substring = variableExpr.replace("@javax.ws.rs.PathParam(value=\"", "");
                variableExpr = substring.replace("\")", "");
                show("Substring: " + substring);
                show("variableExpression : " + variableExpr);

                // aus touchpointId -> {touchpointId} machen, damit es richtig ersetzt wird unten in der url
                StringBuilder idString = new StringBuilder();
                idString.append("{" + variableExpr + "}" );
                variableExpr = variableExpr.replace(variableExpr, idString);



                show("Value: " + args[0] );
                show("ALTE URL: " + url);
                url = url.replace(variableExpr, argumentValue);
                show("Neue URL: " + url);



                // Video ESS_6_WSV_5
                /*
                String variableExpr;        // dafür das anschaiue was in dee Condition des if statements hier oben drinn  ist
                String argumentValue;       // variableExpr kommt aus Pathparam
                                            // argumentValue kommt aus args[] args muss gecheckt werden ob > 1 ist
                also gucken was in dem ersten args drinn ist. das ist der wert, den ich brauche.
                Beim Update fall gibt es zwei pat params und dann muss ich den bodyValue mir noch anschauen


                url = url.replace(variableExpr, argumentValue);

                durch die interpretation der annotationen, bekomme ich den dynamischen bestanteil der url herraus!
                path param sagt und wie die variable heist!
                wie greife ich jetzt auf param drauf?
                zu sehen in dern  if anweisung
                ich muss in der url die variable ersetzen

                 */
            }
            else {
                // if we do not have a path param, we assume the argument value will be sent via the body of the request
                bodyValue = args[0];
            }
        }


        // declare a HttpUriRequest variable
        HttpUriRequest request = null;
        // Das sind die REQUEST-Methoden zum Server
        // TODO: check which of the http method annotation is present and instantiate request accordingly passing the url
        if (meth.isAnnotationPresent(POST.class))
        {
            request = new HttpPost((url));          // wenn ien POST zum server gehen soll
        }
        else if (meth.isAnnotationPresent(GET.class))
        {
            request = new HttpGet(url);
        }
        else if (meth.isAnnotationPresent(DELETE.class))
        {
            // die url muss ich mir oben zusammenbauen!!!
            request = new HttpDelete((url));
        }
        else
        {   // nur für seine demo! aber ich mach das auch mal!
            throw new UnsupportedOperationException("Cannot handle other methods zurzeit! Bitte weiter Implementieren");
        }

        // FÜR DEN BODY BRAUCHEN WIR NOCHEINMAL DIESE ALTE GESCHICHTE
        // TODO: add a header on the request declaring that we accept json (for header names, you can use the constants declared in javax.ws.rs.core.HttpHeaders, for content types use the constants from javax.ws.rs.core.MediaType;)

        // if we need to send the method argument in the request body we need to declare an entity
        ByteArrayEntity bae = null;

        // if a body shall be sent, convert the bodyValue to json, create an entity from it and set it on the request
        if (bodyValue != null) {

            // TODO: use a ByteArrayOutputStream for writing json
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // TODO: write the object to the stream using the jsonSerialiser
            jsonSerialiser.writeObject(bodyValue,bos);
            // TODO: create an ByteArrayEntity from the stream's content
            bae = new ByteArrayEntity((bos.toByteArray()));
            // TODO: set the entity on the request, which must be cast to HttpEntityEnclosingRequest
            ((HttpEntityEnclosingRequest)request).setEntity(bae);

            // TODO: and add a content type header for the request
            request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }

        logger.info("invoke(): executing request: " + request);

        // then send the request to the server and get the response
        HttpResponse response = client.execute(request);

        logger.info("invoke(): received response: " + response);

        // check the response code
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            // declare a variable for the return value
            Object returnValue = null;

            // 01:00:00 WSV 1
            // TODO: convert the resonse body to a java object of an appropriate type considering the return type of the method as returned by getGenericReturnType() and set the object as value of returnValue
            Type returnType = meth.getGenericReturnType();
            returnValue = jsonSerialiser.readObject(response.getEntity().getContent(), returnType);

            // and return the return value
            logger.info("invoke(): returning value: " + returnValue);
            return returnValue;

        }
        else {
            throw new RuntimeException("Got unexpected status from server: " + response.getStatusLine());
        }
    }

}