package utility;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;


public class FileResourceLoader {

	public void loadResource(ServletConfig config) {
		loadProperties(config);		
	}
	

	private void loadProperties(ServletConfig config) {
		Map propertiesMap = null;
		try {
			propertiesMap = loadResourceProp(config.getServletContext().getResourceAsStream("/WEB-INF/application.property"));
			config.getServletContext().setAttribute("application.property", propertiesMap);
			ApplicationResources.getInstance().setProperties("application.property", propertiesMap);
			
		} catch (Exception e) {
			e.printStackTrace();		
		}
	}
	

	private Map loadResourceProp(InputStream is) {
		Properties properties = new Properties();
		Map propertiesMap = new HashMap();
		String copyBuffer = null;
		try {
			properties.load(is);
			Enumeration e = properties.propertyNames();
			String key = null;
			while (e.hasMoreElements()) {
				key = (String) e.nextElement();
				propertiesMap.put(key, properties.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			return propertiesMap;
		}
	}
}