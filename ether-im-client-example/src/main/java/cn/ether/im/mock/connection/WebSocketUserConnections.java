package cn.ether.im.mock.connection;

import cn.ether.im.common.model.protoc.ImProtoType;
import cn.ether.im.mock.client.WebSocketMockClient;
import cn.ether.im.mock.user.MockUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * * @Author: Martin(微信：martin-jobs)
 * * @Date    2024/9/27 14:07
 * * @Description
 * * @Github <a href="https://github.com/mardingJobs">Github链接</a>
 **/
@Slf4j
@Data
public class WebSocketUserConnections {

    private static final int DEFAULT_USER_COUNT = 10;

    private static final String DEFAULT_URL = "ws://localhost:8888/im";

    private List<WebSocketMockClient> clients = new LinkedList<>();

    private List<MockUser> users = new LinkedList<>();

    private Integer userCount = DEFAULT_USER_COUNT;

    private String url = DEFAULT_URL;

    public WebSocketUserConnections() {
        this.userCount = DEFAULT_USER_COUNT;
    }

    public WebSocketUserConnections(Integer userCount) {
        this.userCount = userCount;
    }

    public void connect() throws URISyntaxException, InterruptedException {
        connect(url);
    }

    public void connect(String url) throws URISyntaxException, InterruptedException {
        users.addAll(MockUser.createUsers(userCount));
        for (int i = 0; i < userCount; i++) {
            MockUser mockUser = users.get(i);
            WebSocketMockClient webSocketMockClient = new WebSocketMockClient(mockUser, new URI(url), ImProtoType.JSON);
            boolean connected = webSocketMockClient.connectBlocking();
            if (connected) {
                clients.add(webSocketMockClient);
            }
        }
    }

}
