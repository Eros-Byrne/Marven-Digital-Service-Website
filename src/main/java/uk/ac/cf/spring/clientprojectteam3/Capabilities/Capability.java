package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capability {

    private Long id;
    private String title;
    private String description;

}
