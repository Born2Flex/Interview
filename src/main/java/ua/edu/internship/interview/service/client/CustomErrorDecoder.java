package ua.edu.internship.interview.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import ua.edu.internship.interview.web.handler.ErrorResponse;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Primary
@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private final ObjectMapper mapper;
    private final ErrorDecoder errorDecoder;

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorResponse message;
        try (InputStream inputStream = response.body().asInputStream()) {
            message = mapper.readValue(inputStream, ErrorResponse.class);
        } catch (IOException e) {
            return new RuntimeException(e.getMessage());
        }
        if (message.getStatus() == HttpStatus.NOT_FOUND) {
            return new NoSuchEntityException(message.getMessage());
        } else {
            return errorDecoder.decode(methodKey, response);
        }
    }
}
