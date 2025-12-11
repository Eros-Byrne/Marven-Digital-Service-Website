package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCapabilityControllerTest {

    @Mock
    private AdminCapabilityService service;

    @InjectMocks
    private AdminCapabilityController controller;

    @Test
    void listCapabilities_returnsCapabilitiesListView() {
        Long outcomeId = 1L;

        when(service.getCapabilitiesForOutcome(outcomeId))
                .thenReturn(List.of(new AdminCapability()));

        ModelAndView mv = controller.listCapabilities(outcomeId);

        assertThat(mv.getViewName()).isEqualTo("admin/capabilities-list");
        assertThat(mv.getModel().get("capabilities")).isNotNull();
        assertThat(mv.getModel().get("outcomeId")).isEqualTo(outcomeId);

        verify(service).getCapabilitiesForOutcome(outcomeId);
    }

    @Test
    void editCapabilityForm_loadsCapability() {
        AdminCapability capability = new AdminCapability();
        capability.setId(5L);

        when(service.getCapability(5L)).thenReturn(capability);

        ModelAndView mv = controller.editCapabilityForm(5L);

        assertThat(mv.getViewName()).isEqualTo("admin/edit-capability");
        assertThat(mv.getModel().get("capability")).isEqualTo(capability);
    }
}
