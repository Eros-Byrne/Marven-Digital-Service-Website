package uk.ac.cf.spring.clientprojectteam3.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDetails {

    private Long TeamId;
    private String TeamName;
    private String TeamDescription;
    private Long join_code;

    private List<TeamMember> Managers;
    private List<TeamMember> Members;
}
