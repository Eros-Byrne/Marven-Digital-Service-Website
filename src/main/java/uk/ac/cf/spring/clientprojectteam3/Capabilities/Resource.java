package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {

    private Long id;
    private String content;
    private Difficulty difficulty;
}
