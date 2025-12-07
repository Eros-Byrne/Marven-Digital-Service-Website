package uk.ac.cf.spring.clientprojectteam3.admin.outcome;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOutcome {

    private Long id;
    private String title;
    private Integer capabilityCount;
}
