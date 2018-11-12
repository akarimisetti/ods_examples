package uk.nhs.careconnect.example;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Organization;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
//import ca.uhn.fhir.model.dstu2.resource.Patient;

public class Organisation {
	
	public Bundle findOrganisationById(String identifier)
	{
		FhirContext ctx = FhirContext.forDstu3();
        IGenericClient client = ctx.newRestfulGenericClient("https://directory.spineservices.nhs.uk/STU3/");
        Bundle results = client
                .search()
                .forResource(Organization.class)
                .where(Organization.IDENTIFIER.exactly().systemAndCode("https://fhir.nhs.uk/Id/ods-organization-code", identifier))
                .returnBundle(Bundle.class)
                .execute();
        
        
        Patient patient = new Patient();
		patient.addIdentifier()
		   .setSystem("http://acme.org/mrns")
		   .setValue("12345");
       
        return results;        
	}

	
	public Bundle findOrganisationByName(String name)
	{
		FhirContext ctx = FhirContext.forDstu3();
        IGenericClient client = ctx.newRestfulGenericClient("https://directory.spineservices.nhs.uk/STU3/");
        Bundle results = client
                .search()
                .forResource(Organization.class)
                .where(Organization.NAME.matchesExactly().value(name))
                .returnBundle(Bundle.class)
                .execute();
        return results;        
	}
}
