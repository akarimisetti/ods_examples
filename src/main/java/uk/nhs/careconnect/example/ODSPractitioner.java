package uk.nhs.careconnect.example;

import ca.uhn.fhir.context.FhirContext;

//import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Bundle;

public class ODSPractitioner {
	public Bundle findPractitionerbyid(String identifier)
	{
		FhirContext ctx = FhirContext.forDstu3();
        IGenericClient client = ctx.newRestfulGenericClient("https://data.developer.nhs.uk/ccri-fhir/STU3/");

        Bundle results = client
                .search()
                .forResource(Practitioner.class)
                .where(Practitioner.IDENTIFIER.exactly().identifier(identifier))
                //.where(Practitioner.IDENTIFIER.exactly().identifier("G8133438"))
                //.where(Practitioner.NAME.matches().value("Bhatia"))
                //.where(Practitioner.IDENTIFIER.exactly().systemAndCode("https://fhir.nhs.uk/Id/sds-user-id", "G8133438"))
                .returnBundle(Bundle.class)
                .execute();
        
        
        return  results;        
	}
}
