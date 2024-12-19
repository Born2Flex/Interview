package ua.edu.internship.interview.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.internship.interview.data.enumeration.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserBaseDto {
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
