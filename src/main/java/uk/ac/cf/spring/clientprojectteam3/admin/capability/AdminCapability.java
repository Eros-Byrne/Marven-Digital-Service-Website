package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCapability {

    private Long id;
    private String title;
    private String description;
    private Long outcomeId;
    private String outcomeTitle;
}
