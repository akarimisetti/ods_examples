package uk.nhs.careconnect.example;

import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;

public class Validate {
	
	public void validator(Bundle bundle) throws Throwable
	{
		FhirContext ctx = FhirContext.forDstu3();
		String body = new String(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

        final HttpClient http_client = getHttpClient();

        // in case if service running locally... then it should be triggered in command prompt using
       //java -Dtks.skipsignlogs=Y -jar C:\Projects\validator\TKW\TKW.jar -httpinterceptor C:\Projects\validator\TKW\config\FHIR_VALIDATOR_SERVER\tkw.properties
      //  final HttpPost request = new HttpPost("http://localhost:4849/$validate");
        
        // Hosted service
        final HttpPost request = new HttpPost("http://192.168.128.36/$validate");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/fhir+xml");
        request.setHeader(HttpHeaders.ACCEPT,"application/fhir+json");
        if (body != null && !body.isEmpty()) {
            request.setEntity(new StringEntity(body));
        }

        Reader reader = new InputStreamReader(http_client.execute(request).getEntity().getContent());
        
        StringBuilder builder = new StringBuilder();
        int charsRead = -1;
        char[] chars = new char[100];
        do{
            charsRead = reader.read(chars,0,chars.length);
            //if we have valid chars, append them to end of string.
            if(charsRead>0)
                builder.append(chars,0,charsRead);
        }while(charsRead>0);
        String stringReadFromReader = builder.toString();
        //System.out.println("String read = "+stringReadFromReader);
        OperationOutcome operationoutcome = null;
        operationoutcome = (OperationOutcome) ctx.newJsonParser().parseResource(stringReadFromReader);
        for (int i=0;i<operationoutcome.getIssue().size();i++)
	        {
	            String st = String.valueOf(operationoutcome.getIssue().get(i).getSeverity());
	            System.out.print(operationoutcome.getIssue().get(i).getCode() + ", " + operationoutcome.getIssue().get(i).getSeverity()  + " :  " + operationoutcome.getIssue().get(i).getDiagnostics()  + ", Location is : ");
	            if (operationoutcome.getIssue().get(i).getLocation().size()>0)
	            System.out.println(operationoutcome.getIssue().get(i).getLocation().get(0).getValue());
	        }
		}
	
		private HttpClient getHttpClient(){
	        final HttpClient httpClient = HttpClientBuilder.create().build();
	        return httpClient;
	    }

}
