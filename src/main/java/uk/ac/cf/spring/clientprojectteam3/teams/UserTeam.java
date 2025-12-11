package uk.ac.cf.spring.clientprojectteam3.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeam {

    private Long teamId;
    private String teamName;
    private Boolean isManager;
    private Long joinCode;
    private Long memberCount;
}
