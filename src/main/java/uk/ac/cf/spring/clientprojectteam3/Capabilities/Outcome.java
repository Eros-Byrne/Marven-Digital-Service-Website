
package uk.ac.cf.spring.clientprojectteam3.Capabilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Outcome {
    private Long id;
    private String title;
    private List<Capability> capabilities; // Optional: for capability items
}