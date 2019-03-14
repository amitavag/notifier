package utility;

import java.util.HashMap;
import java.util.Map;


public class ApplicationResources {

	private static ApplicationResources applicationResources = null;

	private Map resources;
	
	private ApplicationResources() {
		resources = new HashMap();
	}
	
	public static ApplicationResources getInstance() {
		if(applicationResources == null) {
			applicationResources = new ApplicationResources();
		}
		return applicationResources;
	}

	public Map getProperties(String key) {
		return (Map)resources.get(key);		
	}

	public void setProperties(String key, Map props) {
		resources.put(key, props);		
	}
}