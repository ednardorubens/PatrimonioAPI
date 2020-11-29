package br.com.ermig.patrimonio.config;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
	
	@Autowired
	private BuildProperties buildProperties;
	
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	private static final String OAUTH2 = "oauth2";
	
	@Bean
	public SecurityConfiguration security() {
	    return SecurityConfigurationBuilder.builder()
	        .clientId(clientId)
	        .clientSecret(clientSecret)
	        .scopeSeparator(" ")
	        .useBasicAuthenticationWithAccessCodeGrant(true)
	        .build();
	}
	
    private List<SecurityContext> buildSecurityContext() {
		final List<SecurityReference> securityReferences = singletonList(
			SecurityReference.builder()
			.scopes(new AuthorizationScope[] {})
			.reference(OAUTH2)
			.build()
		);

        final SecurityContext context = SecurityContext.builder()
            .securityReferences(securityReferences)
            .build();

        return singletonList(context);
    }

    private List<SecurityScheme> buildSecurityScheme() {
        final List<GrantType> grantTypes = singletonList(
    		new ResourceOwnerPasswordCredentialsGrant("/oauth/token")
        );

        return singletonList(new OAuth(OAUTH2, emptyList(), grantTypes));
    }
	
    @Bean
    public Docket api() {
	    final List<ResponseMessage> responseMessages = Arrays.asList(
	    	response(102, "Processando"),
			response(200, "Requisição OK"),
			response(201, "Recurso criado"),
			response(202, "Requisição aceita"),
			response(204, "Sem conteúdo"),
			response(400, "Falha de requisição"),
			response(401, "Não autorizado"),
			response(403, "Acesso negado"),
			response(404, "Não encontrado"),
			response(405, "Método não permitido"),
			response(415, "Tipo não suportado"),
			response(422, "Entidade não processada"),
			response(500, "Erro interno no servidor"),
			response(501, "Não implementado"),
			response(502, "Falha de comunicação"),
			response(503, "Serviço não disponível")
		);
		
        return new Docket(DocumentationType.SWAGGER_2)
			.securityContexts(buildSecurityContext())
			.securitySchemes(buildSecurityScheme())
			.globalResponseMessage(RequestMethod.GET, responseMessages)
			.globalResponseMessage(RequestMethod.POST, responseMessages)
			.globalResponseMessage(RequestMethod.PUT, responseMessages)
			.select()
			.apis(RequestHandlerSelectors.basePackage("br.com.ermig.patrimonio.controller"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(metaData())
			.useDefaultResponseMessages(false);
}

	private ResponseMessage response(final int code, final String message) {
		return new ResponseMessageBuilder()
	       .code(code)
	       .message(message)
	       .build();
	}
    
    private ApiInfo metaData() {
    	return new ApiInfoBuilder()
            .title(buildProperties.get("title"))
            .description(buildProperties.get("description"))
            .version(buildProperties.getVersion())
            .license(buildProperties.get("license.name"))
            .licenseUrl(buildProperties.get("license.url"))
            .build();
    }
}