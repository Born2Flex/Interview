package ua.edu.internship.interview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
public abstract class BaseIntegrationTest {

    private static final String FILE_PREFIX = "src/test/resources/data/";
    protected final MongoTemplate mongoTemplate;

    protected void executeQueryFromFile(String fileName) throws IOException {
        String query = new String(Files.readAllBytes(Paths.get(FILE_PREFIX + fileName)));
        mongoTemplate.executeCommand(query);
    }
}