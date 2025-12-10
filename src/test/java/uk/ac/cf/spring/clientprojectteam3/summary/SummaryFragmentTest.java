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
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;
import uk.ac.cf.spring.clientprojectteam3.quiz.*;
import uk.ac.cf.spring.clientprojectteam3.summaries.SummaryController;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SummaryFragmentTest {

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
    public void testFragment_rendersWithBarChart() throws Exception {
        // Arrange - main summary page uses bar chart
        when(userService.getCurrentUserId()).thenReturn(1);

        List<Outcome> mockOutcomes = List.of(
                new Outcome(1L, "Building a team", null)
        );
        when(capabilityService.getAllOutcomes()).thenReturn(mockOutcomes);

        when(quizRepository.findLatestCompletedAttempt(1L, 1L)).thenReturn(100L);
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(new ArrayList<>());

        Quiz quiz1 = new Quiz(1L, "Building a team", "Description", 15);
        quiz1.setQuestions(List.of(new Question(1L, 1L, "Q1", 1L)));
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(quiz1);

        Map<Integer, Integer> answers = new HashMap<>();
        answers.put(1, 4);
        when(quizRepository.getAttemptAnswers(100L)).thenReturn(answers);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify bar chart is rendered (not line chart)
        assertTrue(content.contains("type: 'bar'"));
        assertTrue(content.contains("indexAxis: 'y'")); // Horizontal bar chart
        assertTrue(content.contains("Outcomes Breakdown"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testFragment_rendersWithLineChart() throws Exception {
        // Arrange - quiz detail page uses line chart
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Description", 15);
        mockQuiz.setQuestions(List.of(new Question(1L, 1L, "Q1", 1L)));
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(101L, 1, 60.0),
                new QuizAttemptScore(102L, 2, 80.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(attempts);

        Map<Integer, Integer> answers = new HashMap<>();
        answers.put(1, 4);
        when(quizRepository.getAttemptAnswers(102L)).thenReturn(answers);

        Capability mockCapability = new Capability();
        mockCapability.setId(1L);
        mockCapability.setTitle("Test Capability");
        when(capabilityService.getCapability(1L)).thenReturn(mockCapability);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify line chart is rendered (not bar chart)
        assertTrue(content.contains("type: 'line'"));
        assertTrue(content.contains("tension: 0.3"));
        assertTrue(content.contains("Progress Over Attempts"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testFragment_displaysCommonElements() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Description", 15);
        mockQuiz.setQuestions(List.of(new Question(1L, 1L, "Q1", 1L)));
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(101L, 1, 80.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(attempts);

        Map<Integer, Integer> answers = new HashMap<>();
        answers.put(1, 4);
        when(quizRepository.getAttemptAnswers(101L)).thenReturn(answers);

        Capability mockCapability = new Capability();
        mockCapability.setId(1L);
        mockCapability.setTitle("Test Capability");
        when(capabilityService.getCapability(1L)).thenReturn(mockCapability);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Common elements that should appear in both bar and line chart views
        assertTrue(content.contains("Your Quiz Results"));
        assertTrue(content.contains("Overall Score"));
        assertTrue(content.contains("Key Strengths"));
        assertTrue(content.contains("Needs Improvement"));
        assertTrue(content.contains("Detailed Results"));
        assertTrue(content.contains("summary-card"));
        assertTrue(content.contains("summary-card-header"));
        assertTrue(content.contains("summary-card-body"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testFragment_handlesNoData() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Description", 15);
        mockQuiz.setQuestions(new ArrayList<>());
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        // No completed attempts
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(new ArrayList<>());

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify "No data yet!" message appears
        assertTrue(content.contains("No data yet!") ||
                   content.contains("No completed attempts"));
        assertTrue(content.contains("Complete a quiz") ||
                   content.contains("No completed"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testFragment_displaysStrengthsAndWeaknesses() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        List<Outcome> mockOutcomes = List.of(
                new Outcome(1L, "Strong Skill", null),
                new Outcome(2L, "Weak Skill", null)
        );
        when(capabilityService.getAllOutcomes()).thenReturn(mockOutcomes);

        when(quizRepository.findLatestCompletedAttempt(1L, 1L)).thenReturn(100L);
        when(quizRepository.findLatestCompletedAttempt(1L, 2L)).thenReturn(101L);
        when(quizRepository.getQuizButtonsInfo(1L)).thenReturn(new ArrayList<>());

        Quiz quiz1 = new Quiz(1L, "Strong Skill", "Description", 15);
        quiz1.setQuestions(List.of(new Question(1L, 1L, "Q1", 1L)));
        Quiz quiz2 = new Quiz(2L, "Weak Skill", "Description", 15);
        quiz2.setQuestions(List.of(new Question(2L, 2L, "Q2", 2L)));

        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(quiz1);
        when(quizService.getQuizForAttempt(2L, 0)).thenReturn(quiz2);

        Map<Integer, Integer> strongAnswers = new HashMap<>();
        strongAnswers.put(1, 5); // High score
        Map<Integer, Integer> weakAnswers = new HashMap<>();
        weakAnswers.put(2, 2); // Low score

        when(quizRepository.getAttemptAnswers(100L)).thenReturn(strongAnswers);
        when(quizRepository.getAttemptAnswers(101L)).thenReturn(weakAnswers);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify strengths and weaknesses sections
        assertTrue(content.contains("Key Strengths"));
        assertTrue(content.contains("Needs Improvement"));
        assertTrue(content.contains("skill-pill"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testFragment_displaysCapabilityTable() throws Exception {
        // Arrange
        when(userService.getCurrentUserId()).thenReturn(1);

        Quiz mockQuiz = new Quiz(1L, "Building a team", "Description", 15);
        mockQuiz.setQuestions(List.of(new Question(1L, 1L, "Q1", 1L)));
        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(mockQuiz);

        List<QuizAttemptScore> attempts = List.of(
                new QuizAttemptScore(101L, 1, 75.0)
        );
        when(quizRepository.getAllCompletedAttempts(1L, 1L)).thenReturn(attempts);

        Map<Integer, Integer> answers = new HashMap<>();
        answers.put(1, 4);
        when(quizRepository.getAttemptAnswers(101L)).thenReturn(answers);

        Capability mockCapability = new Capability();
        mockCapability.setId(1L);
        mockCapability.setTitle("Leadership");
        when(capabilityService.getCapability(1L)).thenReturn(mockCapability);

        // Act & Assert
        MvcResult result = mvc.perform(get("/summary/quiz/1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify detailed results table structure
        assertTrue(content.contains("<table"));
        assertTrue(content.contains("<thead>"));
        assertTrue(content.contains("<th>Capability</th>"));
        assertTrue(content.contains("<th"));
        assertTrue(content.contains("Score"));
        assertTrue(content.contains("<th>Status</th>"));
        assertTrue(content.contains("results-table"));
    }
}
