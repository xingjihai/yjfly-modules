package cn.ejfy.web.tools.properties;

import java.util.Enumeration;
import java.util.Properties;


public class PropertyUtils{
	
    private Properties properties;
    
	public String getPropertiesString(String prefix) {
		String property = "";
		if (properties == null || prefix == null) {
			return property;
		}
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.equals(prefix)) {
				return properties.getProperty(key);
			}
		}
		return property;
	}

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
	
	
	
}
