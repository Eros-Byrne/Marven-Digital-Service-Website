package uk.ac.cf.spring.clientprojectteam3.outcomes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class OutcomeRepositoryTest {

    @Autowired
    private CapabilityRepository outcomeRepository;

    @Test
    void testFindAllOutcomes() {
        List<Outcome> outcomes = outcomeRepository.findAllOutcomes();

        assertThat(outcomes).isNotNull();
        assertThat(outcomes.size()).isGreaterThan(0);
    }

    @Test
    void testFindAllOutcomesContainsExpectedData() {
        List<Outcome> outcomes = outcomeRepository.findAllOutcomes();

        assertThat(outcomes).isNotEmpty();

        // Test that at least one outcome has the expected title
        assertThat(outcomes)
                .extracting(Outcome::getTitle)
                .contains(
                        "Building a team",
                        "Designing a user journey",
                        "Designing content",
                        "Managing a service",
                        "Managing technology for a service"
                );
    }

    @Test
    void testFindByIdReturnsCorrectOutcome() {
        List<Outcome> outcomes = outcomeRepository.findAllOutcomes();
        assertThat(outcomes).isNotEmpty();

        Long firstOutcomeId = outcomes.get(0).getId();
        Outcome outcome = outcomeRepository.findOutcomeById(firstOutcomeId);

        assertThat(outcome).isNotNull();
        assertThat(outcome.getId()).isEqualTo(firstOutcomeId);
        assertThat(outcome.getTitle()).isNotBlank();
    }

}