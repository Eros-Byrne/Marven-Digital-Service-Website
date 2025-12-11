package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminCapabilityServiceTest {

    private final AdminCapabilityRepository repository =
            Mockito.mock(AdminCapabilityRepository.class);

    private final AdminCapabilityService service =
            new AdminCapabilityServiceImpl(repository);

    @Test
    void getCapabilitiesForOutcome_returnsList() {
        Mockito.when(repository.findByOutcomeId(1L))
                .thenReturn(List.of(new AdminCapability()));

        List<AdminCapability> result =
                service.getCapabilitiesForOutcome(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void createCapability_callsRepository() {
        service.createCapability(1L, "Title", "Desc");

        Mockito.verify(repository)
                .create(1L, "Title", "Desc");
    }

    @Test
    void updateCapability_callsRepository() {
        service.updateCapability(1L, "Updated", "Updated");

        Mockito.verify(repository)
                .update(1L, "Updated", "Updated");
    }

    @Test
    void deleteCapability_callsRepository() {
        service.deleteCapability(1L);

        Mockito.verify(repository)
                .delete(1L);
    }
}
