package cn.ether.im.message;

import cn.ether.im.common.util.ThreadPoolUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * * @Author: Martin
 * * @Date    2024/9/15 12:28
 * * @Description
 **/
@SpringBootApplication(scanBasePackages = "cn.ether.im")
public class ImSingleMessageApplication implements DisposableBean {

    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logUseSlf4j", "false");
        SpringApplication.run(ImSingleMessageApplication.class);
    }

    @Override
    public void destroy() throws Exception {
        ThreadPoolUtils.shutdown();
    }
}
