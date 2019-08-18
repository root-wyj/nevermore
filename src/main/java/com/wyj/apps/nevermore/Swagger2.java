package com.wyj.apps.nevermore;

/**
 * @Description:
 * @Auther: zhz
 * @Date: 18/8/21 15:15
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 功能:使用Swagger2构建强大的RESTful API文档
 *   Swagger2配置类
 *
 * @author zhz
 * @date 18/7/3 上午10:13
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Value("${spring.profiles.active}")
    private String profiles;
    @Bean
    public Docket createRestApi() {
        if(!profiles.equals("prod") ) {

            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.wyj.apps.nevermore.controller"))
                    .paths(PathSelectors.any())
                    .build();
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wyj.apps.nevermore.controller"))
                .paths(PathSelectors.none())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("NEVERMORE APIs")
//                .description("")
//                .termsOfServiceUrl("")
//                .contact("")
                .version("1.0")
                .build();
    }

}
