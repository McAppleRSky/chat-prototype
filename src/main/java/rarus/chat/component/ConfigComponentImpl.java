package rarus.chat.component;

import java.util.HashMap;
import java.util.Map;

public class ConfigComponentImpl implements ConfigComponent {

    private final Map<String, String> properties = new HashMap<>();

    public ConfigComponentImpl(String property, String value) {
        setProperty(property, value);
    }

    @Override
    public String getProperty(String property) {
        return properties.get(property);
    }

    @Override
    public void setProperty(String property, String value) {
        properties.put(property, value);
    }

}
