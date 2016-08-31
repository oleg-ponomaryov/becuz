package co.becuz.advices;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
/*    
    @Bean
    public MappingJackson2HttpMessageConverter MappingJackson2HttpMessageConverter (ApplicationContext applicationContext) {
     ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(applicationContext).build();
     return new MappingJackson2HttpMessageConverter(objectMapper) {
         @Override
         protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
             String jsonpFunction =
     				(object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
             if (jsonpFunction != null) {
                 generator.writeRaw(jsonpFunction + "(");
             }
         }
     };
    }
*/        
}


