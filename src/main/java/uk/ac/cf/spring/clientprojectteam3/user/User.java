package uk.ac.cf.spring.clientprojectteam3.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer userid;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(07\\d{9}|\\+44\\d{10})$"
    )
    private String phone;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String jobRole;
}
