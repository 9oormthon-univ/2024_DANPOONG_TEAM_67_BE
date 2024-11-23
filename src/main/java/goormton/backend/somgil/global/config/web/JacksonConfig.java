package goormton.backend.somgil.global.config.web;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modules(new JavaTimeModule()); // JavaTimeModule 등록
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO 8601 날짜 형식 사용
        return builder;
    }
}
