package uk.ac.cf.spring.clientprojectteam3.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewTeam {

    private String teamName;
    private String teamDescription;
}
