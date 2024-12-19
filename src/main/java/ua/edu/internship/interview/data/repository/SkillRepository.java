package ua.edu.internship.interview.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.service.dto.skill.SkillTreeDto;
import java.util.List;
import java.util.Optional;

public interface SkillRepository extends MongoRepository<SkillDocument, ObjectId> {

    @Aggregation(pipeline = {
            """
            {
              $match: {
                  parentId: null
              }
            }
            """,
            "?#{ T(ua.edu.internship.interview.data.repository.SkillRepository).getChildLookUp()}"})
    List<SkillTreeDto> findSkillTrees();

    @Aggregation(pipeline = {
            """
            {
              $match: {
                  _id: ?0
              }
            }
            """,
            "?#{ T(ua.edu.internship.interview.data.repository.SkillRepository).getChildLookUp()}"})
    Optional<SkillTreeDto> findSkillTreeById(String id);

    static String getChildLookUp() {
        return """
            {
              $lookup: {
                from: "skills",
                  let: {
                    parentId: "$_id"
                  },
                  pipeline: [
                    {
                      $match: {
                        $expr: {
                          $eq: ["$$parentId", "$parentId"]
                        }
                      }
                    },
                    {
                      $lookup: {
                        from: "skills",
                        localField: "_id",
                        foreignField: "parentId",
                        as: "children"
                      }
                    }
                  ],
                as: "children"
              }
            }
            """;
    }
}
