package cn.ether.im.mock.connection;

import java.net.URISyntaxException;

public class WebSocketUserConnectionsTest {

    private WebSocketUserConnections connections = new WebSocketUserConnections(1000 * 10);

    private String url = "ws://localhost:8888/im";

    public void connect() throws URISyntaxException, InterruptedException {
        connections.connect(url);
        System.out.println(connections.getClients().size());

    }
}