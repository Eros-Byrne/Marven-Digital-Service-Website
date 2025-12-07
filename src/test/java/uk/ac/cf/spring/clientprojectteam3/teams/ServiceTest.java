package uk.ac.cf.spring.clientprojectteam3.teams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private TeamRepository teamRepo;

    @Mock
    UserService userService;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    public void createNewTeamShouldCallCorrectMethods() {

        when(teamRepo.createTeam(any(NewTeam.class))).thenReturn(123L);
        when(userService.getCurrentUserId()).thenReturn(27);

        NewTeam testTeam = new NewTeam("Test title", "Test description");

        teamService.createNewTeam(testTeam);

        verify(teamRepo).createTeam(testTeam);
        verify(userService).getCurrentUserId();
        verify(teamRepo).setUserAsManager(27L, 123L);
    }

    @Test
    public void shouldReturnListOfTeamsForUser() {
        List<UserTeam> teams = List.of(
                new UserTeam(1L, "test1", true, 35L),
                new UserTeam(2L, "test2", false, 123L));

        when(userService.getCurrentUserId()).thenReturn(48);
        when(teamRepo.getAllTeamsForAUser(48L)).thenReturn(teams);

        List<UserTeam> result = teamService.listOfTeamsForCurrentUser();

        verify(userService).getCurrentUserId();
        verify(teamRepo).getAllTeamsForAUser(48L);
        assertEquals(2, result.size());

    }

}
