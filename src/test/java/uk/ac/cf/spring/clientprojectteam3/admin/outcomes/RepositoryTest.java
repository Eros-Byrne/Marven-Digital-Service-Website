package uk.ac.cf.spring.clientprojectteam3.admin.outcomes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(CapabilityRepositoryImpl.class)
public class RepositoryTest {

    @Autowired
    private CapabilityRepository capRepo;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    public void createOutcomeAndList() {

        capRepo.CreateOutcome("Outcome 1");
        capRepo.CreateOutcome("Outcome 2");

        List<AdminOutcome> outcomes = capRepo.findAllOutcomesWithNumberOfCapabilities();
        assertEquals("Outcome 1", outcomes.getFirst().getTitle());
        assertEquals(2, outcomes.size());
    }

//    Ai assisted with this test
    @Test
    public void deleteOutcomeShouldExcludeFromList() {
//        Given
        String title = "Testing Delete Outcome";
        capRepo.CreateOutcome(title);
        String sql = "select outcome_id from outcomes where title = ?";
        Long outcomeId = jdbc.queryForObject(sql, Long.class, title);

        List<AdminOutcome> outcomesBefore = capRepo.findAllOutcomesWithNumberOfCapabilities();
        assertTrue(outcomesBefore
                .stream()
                .anyMatch(outcome -> title.equals(outcome.getTitle())));

//        When
        capRepo.deleteOutcome(outcomeId);

//        Then
        List<AdminOutcome> outcomesAfter = capRepo.findAllOutcomesWithNumberOfCapabilities();
        assertFalse(outcomesAfter
                .stream()
                .anyMatch(outcome -> title.equals(outcome.getTitle())));

        Boolean disabled = jdbc.queryForObject("select disabled from outcomes where title = ?", Boolean.class, title);
        assertTrue(disabled);
    }
}
