package uk.ac.cf.spring.clientprojectteam3.summary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Capability;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityService;
import uk.ac.cf.spring.clientprojectteam3.quiz.*;
import uk.ac.cf.spring.clientprojectteam3.summaries.SummaryController;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuizDetailSummaryControllerTest {

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
    public void testQuizDetailEndpoint_displaysLineChart() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Test Description", 15);
        List<Question> questions = List.of(new Question(1L, 1L, "Q1", 1L));
        mockQuiz.setQuestions(questions);
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(101L, 1, 60.0),
                new QuizAttemptScore(102L, 2, 80.0),
                new QuizAttemptScore(103L, 3, 90.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(attempts);

        Map<Integer, Integer> answers = new HashMap<>();
        answers.put(1, 4);
        when(quizRepository.getAttemptAnswers(103L)).thenReturn(answers);

        Capability mockCapability = new Capability();
        mockCapability.setCapabilityId(1L);
        mockCapability.setTitle("Test Capability");
        when(capabilityService.getCapability(1L)).thenReturn(mockCapability);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-detail-summary"))
                .andExpect(model().attributeExists("capabilityLabels"))
                .andExpect(model().attributeExists("capabilityScores"))
                .andExpect(model().attribute("hasData", true))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify line chart elements
        assertTrue(content.contains("<canvas id=\"capabilitiesChart\"></canvas>"));
        assertTrue(content.contains("type: 'line'"));
        assertTrue(content.contains("tension: 0.3"));
        assertTrue(content.contains("fill: true"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testQuizDetailEndpoint_showsProgressOverAttempts() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Test Description", 15);
        mockQuiz.setQuestions(List.of(new Question(1L, 1L, "Q1", 1L)));
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(101L, 1, 60.0),
                new QuizAttemptScore(102L, 2, 80.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(attempts);

        when(quizRepository.getAttemptAnswers(102L)).thenReturn(new HashMap<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("hasData", true))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify attempt labels are in the content
        assertTrue(content.contains("Attempt 1") || content.contains("attempt 1"));
        assertTrue(content.contains("Attempt 2") || content.contains("attempt 2"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testQuizDetailEndpoint_displaysQuizSpecificData() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(2L, "Designing content", "Content quiz description", 20);
        mockQuiz.setQuestions(List.of(new Question(1L, 2L, "Q1", 1L)));
        when(quizService.getQuizForAttempt(2L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(201L, 1, 70.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 2L)).thenReturn(attempts);

        when(quizRepository.getAttemptAnswers(201L)).thenReturn(new HashMap<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-detail-summary"))
                .andExpect(model().attribute("quizName", "Designing content"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("Designing content"));
        assertTrue(content.contains("Quiz Detail:"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testQuizDetailEndpoint_handlesNoCompletedAttempts() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(3L, "Managing a service", "Service quiz", 20);
        mockQuiz.setQuestions(new ArrayList<>());
        when(quizService.getQuizForAttempt(3L, 0)).thenReturn(mockQuiz);

        // No completed attempts
        when(quizRepository.getAllCompletedAttempts(1L, 3L)).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-detail-summary"))
                .andExpect(model().attribute("hasError", true))
                .andExpect(model().attribute("hasData", false))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("No completed attempts found") ||
                   content.contains("No data yet"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testQuizDetailEndpoint_handlesInvalidQuizId() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        // Mock service throwing exception for invalid quiz
        when(quizService.getQuizForAttempt(999L, 0))
                .thenThrow(new RuntimeException("Quiz not found"));

        // Act & Assert
        mvc.perform(get("/summary/quiz/999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("quiz-detail-summary"))
                .andExpect(model().attribute("hasError", true));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testQuizDetailEndpoint_calculatesLatestAttemptCapabilities() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Test", 15);
        List<Question> questions = List.of(
                new Question(1L, 1L, "Q1", 1L),
                new Question(2L, 1L, "Q2", 1L),
                new Question(3L, 1L, "Q3", 2L)
        );
        mockQuiz.setQuestions(questions);
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(101L, 1, 60.0),
                new QuizAttemptScore(102L, 2, 80.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(attempts);

        // Latest attempt (102) has these answers
        Map<Integer, Integer> latestAnswers = new HashMap<>();
        latestAnswers.put(1, 5); // Q1 -> capability 1, score 5
        latestAnswers.put(2, 4); // Q2 -> capability 1, score 4
        latestAnswers.put(3, 3); // Q3 -> capability 2, score 3
        when(quizRepository.getAttemptAnswers(102L)).thenReturn(latestAnswers);

        Capability cap1 = new Capability();
        cap1.setCapabilityId(1L);
        cap1.setTitle("Capability 1");

        Capability cap2 = new Capability();
        cap2.setCapabilityId(2L);
        cap2.setTitle("Capability 2");

        when(capabilityService.getCapability(1L)).thenReturn(cap1);
        when(capabilityService.getCapability(2L)).thenReturn(cap2);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("capabilityResults"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify capability data is present
        assertTrue(content.contains("Capability 1") || content.contains("capability"));
        assertTrue(content.contains("Detailed Results"));
    }
}
