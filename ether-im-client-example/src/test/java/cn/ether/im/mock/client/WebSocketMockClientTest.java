package cn.ether.im.mock.client;

import cn.ether.im.mock.user.MockUser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class WebSocketMockClientTest {

    private static final int USER_COUNT = 1000 * 10;

    private List<WebSocketMockClient> clients = new LinkedList<>();

    private List<MockUser> users = new LinkedList<>();

    @Before
    public void init() throws URISyntaxException, InterruptedException {
        users.addAll(MockUser.createUsers(USER_COUNT));
        for (int i = 0; i < USER_COUNT; i++) {
            MockUser mockUser = users.get(i);
            WebSocketMockClient webSocketMockClient = new WebSocketMockClient(mockUser, new URI("ws://localhost:8888/im"));
            boolean connected = webSocketMockClient.connectBlocking();
            if (connected) {
                log.info("编号:{},用户{}已连接", i, JSON.toJSONString(mockUser));
            }
            clients.add(webSocketMockClient);
        }
    }

    @Test
    public void testWs() throws URISyntaxException, InterruptedException {
        log.info("websocket 客户端总数：{}", clients.size());
        new CountDownLatch(1).await();
    }

}