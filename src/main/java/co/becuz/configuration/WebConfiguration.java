package co.becuz.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//@EnableWebMvc
public class WebConfiguration  {
//extends WebMvcConfigurerAdapter {
/*
	@Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	            .allowedOrigins("*")
	            .allowedMethods("HEAD", "GET", "OPTIONS", "POST", "PUT","DELETE")
	            .allowedHeaders("Content-Type", "Content-Range", "Content-Disposition", "Content-Disposition", "Content-Description")
	            .exposedHeaders("access-control-allow-headers", "access-control-allow-methods", "access-control-allow-origin", "access-control-max-age")
	            .allowCredentials(false).maxAge(3600);
	    }
*/	    
}
