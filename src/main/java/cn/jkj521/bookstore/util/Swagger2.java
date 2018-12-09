package cn.jkj521.bookstore.util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class Swagger2 {
	/*@Bean
	public Docket webApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		        .groupName("支付项目后台API接口文档")
		        .apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("cn.jkj521.modules."))
				.paths(PathSelectors.any()).build();
	}*/
	@Bean
	public Docket alimodulesApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		        .groupName("支付API接口文档")  
		        .apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("cn.jkj521.serving.controller.pay"))
				.paths(PathSelectors.any()).build();
	}
	/*@Bean
	public Docket weixinmodulesApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		        .groupName("微信API接口文档")  
		        .apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("cn.jkj521.modules.wx"))
				.paths(PathSelectors.any()).build();
	}*/
	/*@Bean
	public Docket unionmodulesApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		        .groupName("银联API接口文档")  
		        .apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("cn.jkj521.pay.unionmodules"))
				.paths(PathSelectors.any()).build();
	}*/
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("优账云支付服务")
				.description("微信、支付宝支付服务")
				.termsOfServiceUrl("http://www.jkj521.cn")
				.contact(new Contact("优账云支付服务", "http://www.jkj521.cn", "ghostxbh@hotmail.com"))
				.version("1.0").build();
	}

}