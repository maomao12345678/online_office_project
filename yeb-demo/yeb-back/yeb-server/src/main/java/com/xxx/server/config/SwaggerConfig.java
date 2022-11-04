package com.xxx.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2 配置类
 *
 * @author bing  @create 2021/1/13-下午5:10
 */
@Configuration
//如果要配置Swagger就要写下面这个@EnableSwagger2
@EnableSwagger2
public class SwaggerConfig {
    //Swagger用于去写并生成接口文档
    @Bean
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//apiInfo在下面有定义(设置文档基本信息)
                .select()
                //指定哪个包下面生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.xxx.server.controller"))
                .paths(PathSelectors.any())
                .build()
                //授权
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes()); // 配置请求头信息
    }

    /**
     * 文档基本信息
     * 用于去设置接口文档的文档入口url等
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version("1.0")
                .title("云E办接口文档")
                .description("云E办接口文档")
                //设置登录的url
                .contact(new Contact("Bing", "localhost:8081/doc.html", "xxx@xxx.com"))
                .build();

    }

    //解决使用SpringSecurity页面还是登陆不了问题
    // 1. 解决访问接口登录问题
    private List<ApiKey> securitySchemes() {
        // 设置请求头信息
        List<ApiKey> result = new ArrayList<>();
        // 参数：api key 名字 { 准备的 key 名字，value 请求头 }
        result.add(new ApiKey("Authorization", "Authorization", "header"));
        return result;
    }

    // 2. 解决访问接口登录问题
    private List<SecurityContext> securityContexts() {
        // 设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath());//下面有这个定义的方法
        return result;
    }

    // 3. 解决访问接口登录问题
    private SecurityContext getContextByPath() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())//下面有定义这个方法
                .forPaths(PathSelectors.regex("/hello/.*"))
                .forPaths(PathSelectors.regex("/register/.*"))
                .build();
    }

    // 4. 设置默认授权 - 解决访问接口登录问题
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", scopes));
        return result;
    }

}
