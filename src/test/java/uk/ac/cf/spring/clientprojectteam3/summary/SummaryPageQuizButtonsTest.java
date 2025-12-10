package uk.ac.cf.spring.clientprojectteam3.summary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityService;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizButtonInfo;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizService;
import uk.ac.cf.spring.clientprojectteam3.summaries.SummaryController;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SummaryPageQuizButtonsTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private QuizService quizService;

    @MockitoBean
    private QuizRepository quizRepository;

    @MockitoBean
    private CapabilityService capabilityService;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_displaysQuizButtons() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Building a team", true),
                new QuizButtonInfo(2L, "Designing a user journey", true),
                new QuizButtonInfo(3L, "Designing content", false),
                new QuizButtonInfo(4L, "Managing a service", false),
                new QuizButtonInfo(5L, "Managing technology", false),
                new QuizButtonInfo(6L, "Managing data", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);

        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attributeExists("quizButtons"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify quiz buttons section exists
        assertTrue(content.contains("View Individual Quiz Results") ||
                   content.contains("quiz-buttons-grid"));
        assertTrue(content.contains("Building a team"));
        assertTrue(content.contains("Designing a user journey"));
        assertTrue(content.contains("Designing content"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_activeButtonForCompletedQuiz() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Building a team", true), // Completed
                new QuizButtonInfo(2L, "Designing a user journey", false) // Not completed
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify active button class is applied for completed quiz
        assertTrue(content.contains("quiz-nav-btn-active"));
        assertTrue(content.contains("Completed") || content.contains("✓"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_disabledButtonForIncompleteQuiz() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Building a team", false), // Not completed
                new QuizButtonInfo(2L, "Designing a user journey", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify disabled button class is applied
        assertTrue(content.contains("quiz-nav-btn-disabled"));
        assertTrue(content.contains("Not completed") || content.contains("not completed"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_quizButtonsLinkCorrectly() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Building a team", true),
                new QuizButtonInfo(2L, "Designing a user journey", true),
                new QuizButtonInfo(3L, "Designing content", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify links to quiz detail pages
        assertTrue(content.contains("/summary/quiz/1"));
        assertTrue(content.contains("/summary/quiz/2"));
        assertTrue(content.contains("/summary/quiz/3"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_displaysCompletionStatus() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Building a team", true),
                new QuizButtonInfo(2L, "Designing a user journey", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify completion status text
        assertTrue(content.contains("Completed") || content.contains("✓"));
        assertTrue(content.contains("Not completed"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_quizButtonsGridLayout() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Quiz 1", true),
                new QuizButtonInfo(2L, "Quiz 2", true),
                new QuizButtonInfo(3L, "Quiz 3", false),
                new QuizButtonInfo(4L, "Quiz 4", false),
                new QuizButtonInfo(5L, "Quiz 5", false),
                new QuizButtonInfo(6L, "Quiz 6", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify grid layout CSS class
        assertTrue(content.contains("quiz-buttons-grid"));

        // Verify all 6 quizzes are displayed
        for (int i = 1; i <= 6; i++) {
            assertTrue(content.contains("Quiz " + i));
        }
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_mixedCompletionStates() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        // User has completed quizzes 1, 3, 5 but not 2, 4, 6
        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Quiz 1", true),
                new QuizButtonInfo(2L, "Quiz 2", false),
                new QuizButtonInfo(3L, "Quiz 3", true),
                new QuizButtonInfo(4L, "Quiz 4", false),
                new QuizButtonInfo(5L, "Quiz 5", true),
                new QuizButtonInfo(6L, "Quiz 6", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Should have both active and disabled buttons
        assertTrue(content.contains("quiz-nav-btn-active"));
        assertTrue(content.contains("quiz-nav-btn-disabled"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testSummaryPage_noQuizzesCompleted_allDisabled() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<QuizButtonInfo> quizButtons = List.of(
                new QuizButtonInfo(1L, "Quiz 1", false),
                new QuizButtonInfo(2L, "Quiz 2", false),
                new QuizButtonInfo(3L, "Quiz 3", false),
                new QuizButtonInfo(4L, "Quiz 4", false),
                new QuizButtonInfo(5L, "Quiz 5", false),
                new QuizButtonInfo(6L, "Quiz 6", false)
        );
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(quizButtons);
        when(capabilityService.getAllOutcomes()).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // All buttons should be disabled
        int disabledCount = countOccurrences(content, "quiz-nav-btn-disabled");
        assertTrue(disabledCount >= 6, "All 6 quiz buttons should be disabled");
    }

    private int countOccurrences(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }
}
