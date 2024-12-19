package ua.edu.internship.interview.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder defaultErrorDecoder() {
        return new ErrorDecoder.Default();
    }
}
