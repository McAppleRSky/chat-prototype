package rarus.chat.server.main.config;

public interface ConfigComponent {

    String getProperty(String property);

    void setProperty(String property, String value);

}
