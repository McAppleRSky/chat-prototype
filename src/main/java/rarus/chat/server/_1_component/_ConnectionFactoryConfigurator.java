package rarus.chat.server._1_component;

import rarus.chat.server._2_createConnection.ConnectionFactory;
import rarus.chat.server._2_createConnection.ConnectionHandler;
import rarus.chat.server._2_createConnection.creator.ConnectionCreator;
import rarus.chat.server._2_createConnection.creator.FactoryConfigurator;
import rarus.chat.server.main.Main;
import rarus.chat.server.main.config.ConfigComponent;

public class _ConnectionFactoryConfigurator implements FactoryConfigurator {

    private final ConfigComponent configComponent = (ConfigComponent)Main.context.get(ConfigComponent.class);
    private final String room = configComponent.getProperty("room");
    private ConnectionCreator creatorConnection = new ConnectionFactory();

    public _ConnectionFactoryConfigurator(ConnectionCreator creatorConnection) {
        this.creatorConnection = creatorConnection;
    }

    @Override
    public void configure(ConnectionCreator creator) {
//        this.creatorConnection.setCreator(() -> new ConnectionHandler());
    }

    void method(ConnectionCreator serverClientHandlerCreator){
        configure(serverClientHandlerCreator);
    }
}
