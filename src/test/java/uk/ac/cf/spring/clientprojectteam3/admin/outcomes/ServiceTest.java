package uk.ac.cf.spring.clientprojectteam3.admin.outcomes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcome;
import uk.ac.cf.spring.clientprojectteam3.admin.outcome.AdminOutcomeServiceImpl;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private CapabilityRepository capRepo;

    @InjectMocks
    private AdminOutcomeServiceImpl adminOutcomeService;

    @Test
    public void listAllOutcomesCallsRepoAndReturnsList() {
        when(capRepo.findAllOutcomesWithNumberOfCapabilities()).thenReturn(List.of(
                new AdminOutcome(1L, "Outcome 1", 1),
                new AdminOutcome(2L, "Outcome 2", 2)
        ));

        List<AdminOutcome> results = adminOutcomeService.getAllOutcomesWithCapabilityCount();

        verify(capRepo).findAllOutcomesWithNumberOfCapabilities();
        assertEquals(2, results.size());
        assertEquals("Outcome 1", results.getFirst().getTitle());
        assertEquals(2, results.get(1).getCapabilityCount());
    }

    @Test
    public void createOutcomeCallsRepoFunction() {

        adminOutcomeService.createOutcome("Test outcome");

        verify(capRepo).CreateOutcome("Test outcome");
    }

    @Test
    public void deleteOutcomeCallsRepoFunction() {

        adminOutcomeService.deleteOutcome(27L);

        verify( capRepo ).deleteOutcome(27L);
    }
}
