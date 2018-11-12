package uk.nhs.careconnect.example;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FhirBundleUtil {

	 private Bundle fhirDocument = null;
	 Map<String,String> referenceMap = new HashMap();
	 private Patient patient = null;
	 final String uuidtag = "urn:uuid:";
	 private static final Logger log = LoggerFactory.getLogger(FhirBundleUtil.class);
	 
	 FhirBundleUtil(Bundle.BundleType value) {
	        fhirDocument = new Bundle()
	                .setType(value);
	        fhirDocument.getIdentifier().setValue(UUID.randomUUID().toString()).setSystem("https://tools.ietf.org/html/rfc4122");
	    }
	 
	 public void processBundleResources(Bundle bundle) {
	        
		 for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
	            Resource resource = entry.getResource();
	            resource.setId(getNewId(resource));
	          //  fhirDocument.addEntry().setResource(entry.getResource()).setFullUrl(uuidtag + resource.getId());
	     //       if (entry.getResource() instanceof Patient) {
	     //           patient = (Patient) entry.getResource();
	     //       }
	        }
	    }
	 
	 public String getNewId(Resource resource) {
	        String reference = resource.getId();
	        String newReference = null;
	        if (reference!=null) {
	            newReference = referenceMap.get(reference);
	            if (newReference != null) return newReference;
	        }
	        newReference = UUID.randomUUID().toString();
	        if (reference == null) {
	            reference = newReference;
	            referenceMap.put(resource.getClass().getSimpleName()+"/"+reference,newReference);
	        } else {
	            log.info(resource.getClass().getSimpleName()+"/"+resource.getIdElement().getIdPart()+" [-] "+newReference);
	            referenceMap.put(resource.getClass().getSimpleName()+"/"+resource.getIdElement().getIdPart(),newReference);
	        }
	        log.info(reference +" [-] "+newReference);
	        referenceMap.put(reference,newReference);
	        referenceMap.put(newReference,newReference); // Add in self
	        referenceMap.put(uuidtag + newReference,newReference); // Add in self
	        System.out.println("new reference = " + newReference);
	        return newReference;
	    }
	 
	 public Bundle getFhirDocument() {
	        return fhirDocument;
	    }

}


