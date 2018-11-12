package uk.nhs.careconnect.example;


import java.util.UUID;

import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;


public class ODSPatient {
	
	public void createPatient()
	{
		//FhirBundleUtil fhirBundle = new FhirBundleUtil(Bundle.BundleType.COLLECTION);

		// Create a patient object
		Patient patient = new Patient();
		patient.setId(UUID.randomUUID().toString());
		patient.addIdentifier()
			.setSystem("http://acme.org/mrns")
			.setValue("123459");
		patient.addName()
			.setFamily("Karimisetti")
			.addGiven("Anand");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
    	 
		 
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);
		
		bundle.addEntry()
			.setFullUrl("urn:uuid:"+patient.getId())
			.setResource(patient);
		
		// Creating ODS Organisation
				Organisation odsorganisation = new Organisation();
				Bundle results = odsorganisation.findOrganisationById("RR8");
				
				// For Single resource Practitioner practitioner = (Practitioner) results.getEntry().get(0).getResource();  
				 
				//For Multiple Resource
				Organization organisation = null;
				for (Bundle.BundleEntryComponent entry : results.getEntry()) 
				{
				    organisation = (Organization) entry.getResource();
				    organisation.setId(UUID.randomUUID().toString());
		            bundle.addEntry()
		            .setFullUrl("urn:uuid:"+organisation.getId())
					.setResource(organisation);
		            patient.setManagingOrganization(new Reference("urn:uuid:"+organisation.getId()));
				}
				
		
		// Creating ODS Practitioner
		ODSPractitioner odspractitioner = new ODSPractitioner();
		System.out.println("finding paractitioner");
		 results = odspractitioner.findPractitionerbyid("G8133438");
		 System.out.println("found paractitioner");
		
		// For Single resource Practitioner practitioner = (Practitioner) results.getEntry().get(0).getResource();  
		 
		//For Multiple Resource
		Practitioner practitioner = null;
		for (Bundle.BundleEntryComponent entry : results.getEntry()) 
		{
		    System.out.println("entry is " + entry.getFullUrl());
		    practitioner = (Practitioner) entry.getResource();
		    practitioner.setId(UUID.randomUUID().toString());
            bundle.addEntry()
            .setFullUrl("urn:uuid:"+practitioner.getId())
			.setResource(practitioner);
            patient.addGeneralPractitioner(new Reference("urn:uuid:"+practitioner.getId()));
		}
		
		
		
	//	fhirBundle.processBundleResources(bundle);

	    FhirContext ctxFHIR = FhirContext.forDstu3();
		IGenericClient client = null;
		client = ctxFHIR.newRestfulGenericClient("https://data.developer-test.nhs.uk/ccri-fhir/STU3/");
		System.out.println("creating patient resource");
        System.out.println(ctxFHIR.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));
        System.out.println("patient resource created");
        MethodOutcome outcome = client.create().resource(bundle).prettyPrint().encodedJson().execute();
        System.out.println(outcome.getCreated());
        
        
        
     // validate   
        Validate v = new Validate();
        try
        {
        v.validator(bundle);
        }
        catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
}


