package utility;

import java.util.HashMap;

public class PropertiesUtil {
	
	public static String getValue(String key) {
		HashMap resources = (HashMap) ApplicationResources.getInstance().getProperties("application.property");
		return (String) resources.get(key);
	}
}
