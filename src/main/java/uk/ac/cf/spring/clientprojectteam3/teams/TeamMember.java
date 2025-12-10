package uk.ac.cf.spring.clientprojectteam3.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMember {

    private Long id;
    private String name;
    private String email;
    private boolean isManager;
}
