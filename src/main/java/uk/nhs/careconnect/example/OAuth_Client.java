package uk.nhs.careconnect.example;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class OAuth_Client {

	public void authenticate()
	{
		System.out.println("Authenticate");

		//Generate token
		String token =  getToken();
		
		// Access smart on fhir using the generated token
        useBearerToken(token);
	}
	
	public String getToken()
	{
		String token = "";
		try {
		
		String content = "grant_type=client_credentials";

		final String clientId = "patient-access";//clientId
		final String clientSecret = "IShTVi8mRSV7bVREuU1freiDo79y_8fLX3BBw2nf2eIpv9A_r91VlVuF2LOiK_zLZAkBQCusEXLp_o6DEIgvaQ";//client secret
		String auth = clientId + ":" + clientSecret;
		String authentication = Base64.getEncoder().encodeToString(auth.getBytes());
		
		URL url = new URL("https://data.developer.nhs.uk/ccri-auth/token");
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        System.out.println("authentication string = " + authentication);
        connection.setRequestProperty("Authorization", "Basic " + authentication);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");
        PrintStream os = new PrintStream(connection.getOutputStream());
        os.print(content);
        os.close();
        Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");
        BufferedReader  reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = null;
        StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        String response = out.toString();
        Matcher matcher = pat.matcher(response);
        if (matcher.matches() && matcher.groupCount() > 0) {
        	token = matcher.group(1);

		System.out.println("Authentication done" + token);
		}
		}
    catch (Exception e) {
       System.out.println("Error : " + e.getMessage());
		
	}
		return token;
	}
	
	
	public void useBearerToken(String bearerToken) {
	    BufferedReader reader = null;
	    try {
	        // Smart on FHIR secured server 
	    	URL url = new URL("https://data.developer-test.nhs.uk/ccri-smartonfhir/STU3/Patient?identifier=123456");
	    	
	    	// FHIR Unsecured server
	    	//URL url = new URL("https://data.developer-test.nhs.uk/ccri-fhir/STU3/Patient?identifier=12345");
	    	
	        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
	        
	        connection.setRequestProperty ("Authorization", "Bearer " + bearerToken);
	        connection.setRequestProperty("Content-Type", "application/fhir+xml");
	        connection.setRequestProperty("Accept", "application/fhir+json");
	        connection.setDoOutput(true);
	        connection.setRequestMethod("GET");
	        
	        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String line = null;
	        StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        String response = out.toString();
	        System.out.println(response);
	     
	        
	        
	    } catch (Exception e) {
	    	
	    	System.out.println("Error : " + e.getMessage());
	    }
}
}

