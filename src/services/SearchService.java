package services;

import java.util.List;

import models.PN_Entry;

public interface SearchService {
	public List<PN_Entry>searchWithParams(String params);
}
