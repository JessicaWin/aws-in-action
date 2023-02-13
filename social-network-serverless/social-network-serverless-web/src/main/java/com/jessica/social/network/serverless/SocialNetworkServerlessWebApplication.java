package com.jessica.social.network.serverless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAspectJAutoProxy
public class SocialNetworkServerlessWebApplication {

	public static void main(String[] args) {

		SpringApplication.run(SocialNetworkServerlessWebApplication.class, args);
	}

}
