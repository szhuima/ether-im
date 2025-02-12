package cn.ether.im.message.single.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringBootConfiguration;

@SecurityScheme(name = "token", type = SecuritySchemeType.HTTP, scheme = "bearer", in = SecuritySchemeIn.HEADER)
@SpringBootConfiguration
@OpenAPIDefinition(
        // ## API的基本信息，包括标题、版本号、描述、联系人等
        info = @Info(
                title = "以太IM消息推送系统接口文档",       // Api接口文档标题（必填）
                description = "用来对接以太IM消息推送系统",      // Api接口文档描述
                version = "1.2.1",                                   // Api接口版本
                termsOfService = "https://example.com/",             // Api接口的服务条款地址
                contact = @Contact(
                        name = "Martin",                            // 作者名称
                        email = "szhuima@gmail.com",                  // 作者邮箱
                        url = "www.google.com"  // 介绍作者的URL地址
                ),
                license = @License(                                                // 设置联系人信息
                        name = "Apache 2.0",                                       // 授权名称
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"   // 授权信息
                )
        ),
        // ## 表示服务器地址或者URL模板列表，多个服务地址随时切换（只不过是有多台IP有当前的服务API）
        servers = {
                @Server(url = "http://localhost:8888/im-message/", description = "本地服务器")
        },
        externalDocs = @ExternalDocumentation(description = "更多内容请查看该链接", url = "www.google.com"),
        security = {@SecurityRequirement(name = "token")})
public class SwaggerOpenApiConfig {

    private static final String headerName = "Authorization";//请求头名称

}
