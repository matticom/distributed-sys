// Autor: 			Matthias Kugler
// Erstelldatum: 	17.10.2017
// 
// Funktion der Klasse:
// - Interface für Suchservices
// - wird implementiert von PhonebookSearchService

package services;

import java.util.List;
import java.util.Map;

import models.PN_Entry;

public interface SearchService {
	public List<PN_Entry> searchWithParams(String params);
	public Map<String, Boolean> getFeedBack();
}
