package ua.edu.internship.interview.service.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends UserBaseDto {
    private Long id;
}
