package ua.edu.internship.interview.service.mapper;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class BaseMapper {
    public String map(ObjectId id) {
        return id.toString();
    }
}
