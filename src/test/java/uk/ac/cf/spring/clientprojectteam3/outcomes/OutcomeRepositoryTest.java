package uk.ac.cf.spring.clientprojectteam3.outcome;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OutcomeRepositoryTest {

    @Autowired
    private OutcomeRepository outcomeRepository;

    @Test
    void testFindAllOutcomes() {
        List<Outcome> outcomes = outcomeRepository.findAll();

        assertThat(outcomes).isNotNull();
        assertThat(outcomes.size()).isGreaterThan(0);
    }

    @Test
    void testFindAllOutcomesContainsExpectedData() {
        List<Outcome> outcomes = outcomeRepository.findAll();

        assertThat(outcomes).isNotEmpty();

        // Test that at least one outcome has the expected title
        assertThat(outcomes)
                .extracting(Outcome::getTitle)
                .contains("Career Exploration");
    }

    @Test
    void testFindByIdReturnsCorrectOutcome() {
        List<Outcome> outcomes = outcomeRepository.findAll();
        assertThat(outcomes).isNotEmpty();

        Long firstOutcomeId = outcomes.get(0).getId();
        Outcome outcome = outcomeRepository.findById(firstOutcomeId);

        assertThat(outcome).isNotNull();
        assertThat(outcome.getId()).isEqualTo(firstOutcomeId);
        assertThat(outcome.getTitle()).isNotBlank();
    }

    //Needs to be implemented once we have linked to capabilities page - probably in next sprint
//    @Test
//    void testOutcomesHaveCapabilities() {
//        List<Outcome> outcomes = outcomeRepository.findAll();
//
//        assertThat(outcomes).isNotEmpty();
//
//        // Check if capabilities are loaded (at least one outcome should have capabilities)
//        boolean hasCapabilities = outcomes.stream()
//                .anyMatch(outcome -> outcome.getCapabilities() != null && !outcome.getCapabilities().isEmpty());
//
//        assertThat(hasCapabilities).isTrue();
//    }
//
//
//    @Test
//    void testCapabilitiesAreCorrectlyParsed() {
//        List<Outcome> outcomes = outcomeRepository.findAll();
//
//        // Find an outcome with capabilities
//        Outcome outcomeWithCapabilities = outcomes.stream()
//                .filter(outcome -> outcome.getCapabilities() != null && !outcome.getCapabilities().isEmpty())
//                .findFirst()
//                .orElse(null);
//
//        assertThat(outcomeWithCapabilities).isNotNull();
//        assertThat(outcomeWithCapabilities.getCapabilities()).isInstanceOf(List.class);
//        assertThat(outcomeWithCapabilities.getCapabilities().get(0)).isInstanceOf(String.class);
//    }
}