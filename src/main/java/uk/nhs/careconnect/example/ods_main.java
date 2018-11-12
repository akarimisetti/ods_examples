package uk.nhs.careconnect.example;

import org.hl7.fhir.dstu3.model.Bundle;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class ods_main {
	public static void main(String [] args)
	{
		FhirContext ctx = FhirContext.forDstu3();
        IParser parser = ctx.newXmlParser();
        Bundle results;

        
// finding organisation by id        
        System.out.println("Finding organisations for RR8");
		Organisation organisation = new Organisation();
		results = organisation.findOrganisationById ("RR8");
		System.out.println(parser.setPrettyPrint(true).encodeResourceToString(results));
		
		
// finding organisation by name		
		System.out.println("Finding organisations by name CLWYD");
		results = organisation.findOrganisationByName("CLWYD");
		
		System.out.println(parser.setPrettyPrint(true).encodeResourceToString(results));
		
// finding practitioner by name		
		ODSPractitioner practitioner = new ODSPractitioner();
		results = practitioner.findPractitionerbyid("G8133438");
		System.out.println("Finding practitioner by id G8133438");
		System.out.println(parser.setPrettyPrint(true).encodeResourceToString(results));

// Creating new Patient in CCRI		
		ODSPatient odsPatient = new ODSPatient() ;
		odsPatient.createPatient();
		
		OAuth_Client auth = new OAuth_Client();
		auth.authenticate();
		
	}
	
	
}
