package uk.ac.cf.spring.clientprojectteam3.summaries;

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
import uk.ac.cf.spring.clientprojectteam3.quiz.Question;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizService;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SummaryBarTest {

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
    public void barChartDisplaysOutcomesData() throws Exception {
        when(userService.getCurrentUserId()).thenReturn(1);

        List<Outcome> mockOutcomes = List.of(
                new Outcome(1L, "Building a team", null),
                new Outcome(2L, "Designing a user journey", null),
                new Outcome(3L, "Designing content", null)
        );
        when(capabilityService.getAllOutcomes()).thenReturn(mockOutcomes);

        when(quizRepository.findLatestCompletedAttempt(1L, 1L)).thenReturn(100L);
        when(quizRepository.findLatestCompletedAttempt(1L, 2L)).thenReturn(101L);
        when(quizRepository.findLatestCompletedAttempt(1L, 3L)).thenReturn(102L);

        Quiz quiz1 = new Quiz(1L, "Building a team", "Description", 15);
        quiz1.setQuestions(List.of(new Question(1L, 1L, "Question 1", 1L)));
        Quiz quiz2 = new Quiz(2L, "Designing a user journey", "Description", 20);
        quiz2.setQuestions(List.of(new Question(2L, 2L, "Question 2", 2L)));
        Quiz quiz3 = new Quiz(3L, "Designing content", "Description", 20);
        quiz3.setQuestions(List.of(new Question(3L, 3L, "Question 3", 3L)));

        when(quizService.getQuizForAttempt(1L, 0)).thenReturn(quiz1);
        when(quizService.getQuizForAttempt(2L, 0)).thenReturn(quiz2);
        when(quizService.getQuizForAttempt(3L, 0)).thenReturn(quiz3);

        Map<Long, Integer> answers1 = new HashMap<>();
        answers1.put(1L, 5);
        Map<Long, Integer> answers2 = new HashMap<>();
        answers2.put(2L, 3);
        Map<Long, Integer> answers3 = new HashMap<>();
        answers3.put(3L, 2);

        when(quizRepository.getAttemptAnswers(100L)).thenReturn(answers1);
        when(quizRepository.getAttemptAnswers(101L)).thenReturn(answers2);
        when(quizRepository.getAttemptAnswers(102L)).thenReturn(answers3);

        MvcResult result = mvc.perform(get("/summary/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("summary"))
                .andExpect(model().attributeExists("capabilityLabels"))
                .andExpect(model().attributeExists("capabilityScores"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("<canvas id=\"capabilitiesChart\"></canvas>"));
        assertTrue(content.contains("capabilityLabels"));
        assertTrue(content.contains("capabilityScores"));
        assertTrue(content.contains("Building a team"));
        assertTrue(content.contains("Designing a user journey"));
        assertTrue(content.contains("Designing content"));
        assertTrue(content.contains("Your confidence scores for each outcome"));
    }
}