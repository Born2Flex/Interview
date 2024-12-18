package ua.edu.internship.interview;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//TODO Add validation that user create question for his skill (he have that skill and create question for it)
//TODO Fix bug when updating user question

@SpringBootApplication
@EnableMongock
@EnableFeignClients
public class InterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }

}
