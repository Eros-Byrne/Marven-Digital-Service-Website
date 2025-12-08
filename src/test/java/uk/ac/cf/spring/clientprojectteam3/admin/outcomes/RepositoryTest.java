package uk.ac.cf.spring.clientprojectteam3.admin.outcomes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ActiveProfiles("test")
@Import(CapabilityRepositoryImpl.class)
public class RepositoryTest {

    @Autowired
    private CapabilityRepository capRepo;

    @Test
    public void createOutcomeAndList() {

        capRepo.CreateOutcome("Outcome 1");
        capRepo.CreateOutcome("Outcome 2");

        List<AdminOutcome> outcomes = capRepo.findAllOutcomesWithNumberOfCapabilities();
        assertEquals("Outcome 1", outcomes.getFirst().getTitle());
        assertEquals(2, outcomes.size());
    }
}
