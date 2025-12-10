package uk.ac.cf.spring.clientprojectteam3.summaries;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.cf.spring.clientprojectteam3.capabilities.CapabilityService;
import uk.ac.cf.spring.clientprojectteam3.capabilities.Outcome;
import uk.ac.cf.spring.clientprojectteam3.quiz.Question;
import uk.ac.cf.spring.clientprojectteam3.quiz.Quiz;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizRepository;
import uk.ac.cf.spring.clientprojectteam3.quiz.QuizService;
import uk.ac.cf.spring.clientprojectteam3.security.CustomUserDetails;
import uk.ac.cf.spring.clientprojectteam3.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/summary")
public class SummaryController {

    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final CapabilityService capabilityService;
    private final UserService userService;

    public SummaryController(QuizService quizService, QuizRepository quizRepository,
                             CapabilityService capabilityService, UserService userService) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.capabilityService = capabilityService;
        this.userService = userService;
    }

    @GetMapping
    public String summaryLanding(Model model) {
        try {
            Integer userId = userService.getCurrentUserId();
            return showUserSummary(userId.longValue(), model);
        } catch (Exception e) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", "Unable to load summary");
            return "summary";
        }
    }

    @GetMapping("/user/{userId}")
    public String showUserSummary(@PathVariable long userId, Model model) {
        model.addAttribute("hasError", false);
        model.addAttribute("hasData", false);

        try {
            String userName = "User";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                userName = userDetails.getName();
            }

            // Add quiz buttons info
            model.addAttribute("quizButtons", quizRepository.getQuizButtonsInfo(userId));

            List<Outcome> allOutcomes = capabilityService.getAllOutcomes();

            List<String> capabilityLabels = new ArrayList<>();
            List<Integer> capabilityScoresList = new ArrayList<>();
            List<CapabilityResult> capabilityResults = new ArrayList<>();

            int completedQuizCount = 0;

            for (Outcome outcome : allOutcomes) {
                Long outcomeId = outcome.getId();
                Long quizId = outcomeId;

                try {
                    Long latestAttemptId = quizRepository.findLatestCompletedAttempt(userId, quizId);

                    if (latestAttemptId != null) {
                        completedQuizCount++;

                        Quiz quiz = quizService.getQuizForAttempt(quizId, 0);
                        List<Question> questions = quiz.getQuestions();

                        Map<Integer, Integer> attemptAnswers = quizRepository.getAttemptAnswers(latestAttemptId);

                        if (!attemptAnswers.isEmpty()) {
                            List<Integer> scores = new ArrayList<>();

                            for (Question question : questions) {
                                if (attemptAnswers.containsKey((int) question.getQuestionId())) {
                                    scores.add(attemptAnswers.get((int) question.getQuestionId()));
                                }
                            }

                            if (!scores.isEmpty()) {
                                int avgScore = (int) Math.round(scores.stream()
                                        .mapToInt(Integer::intValue)
                                        .average()
                                        .orElse(0.0) * 20);

                                capabilityLabels.add(outcome.getTitle());
                                capabilityScoresList.add(avgScore);
                                capabilityResults.add(new CapabilityResult(outcome.getTitle(), avgScore));
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing outcome " + outcomeId + ": " + e.getMessage());
                }
            }

            List<CapabilityResult> sortedResults = new ArrayList<>(capabilityResults);
            sortedResults.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

            List<String> strengths = sortedResults.stream()
                    .limit(3)
                    .map(CapabilityResult::getCapabilityTitle)
                    .collect(Collectors.toList());

            List<String> weaknesses = sortedResults.stream()
                    .filter(r -> r.getScore() < 60)
                    .map(CapabilityResult::getCapabilityTitle)
                    .collect(Collectors.toList());

            int overallScore = capabilityScoresList.isEmpty() ? 0 :
                    (int) Math.round(capabilityScoresList.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0));

            model.addAttribute("userName", userName);
            model.addAttribute("quizName", "All Outcomes Summary");
            model.addAttribute("attemptNumber", completedQuizCount + " of 6 completed");
            model.addAttribute("completionDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            model.addAttribute("overallScore", overallScore);
            model.addAttribute("strengths", strengths);
            model.addAttribute("weaknesses", weaknesses);
            model.addAttribute("capabilityResults", capabilityResults);
            model.addAttribute("capabilityLabels", capabilityLabels);
            model.addAttribute("capabilityScores", capabilityScoresList);
            model.addAttribute("hasData", !capabilityScoresList.isEmpty());

        } catch (Exception e) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", "Error loading summary: " + e.getMessage());
            e.printStackTrace();
        }

        return "summary";
    }

    @GetMapping("/quiz/{quizId}/attempt/{attemptId}")
    public String showSummary(@PathVariable long quizId, @PathVariable long attemptId, Model model) {
        try {
            Integer userId = userService.getCurrentUserId();
            return "redirect:/summary/user/" + userId;
        } catch (Exception e) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", "Unable to load summary");
            return "summary";
        }
    }

    @GetMapping("/quiz/{quizId}")
    public String showQuizDetailSummary(@PathVariable long quizId, Model model) {
        model.addAttribute("hasError", false);
        model.addAttribute("hasData", false);

        try {
            Integer userId = userService.getCurrentUserId();

            String userName = "User";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
                userName = userDetails.getName();
            }

            // Get quiz info
            uk.ac.cf.spring.clientprojectteam3.quiz.Quiz quiz = quizService.getQuizForAttempt(quizId, 0);

            // Get all completed attempts for this quiz
            List<uk.ac.cf.spring.clientprojectteam3.quiz.QuizAttemptScore> attempts =
                quizRepository.getAllCompletedAttempts(userId, quizId);

            if (attempts.isEmpty()) {
                model.addAttribute("hasError", true);
                model.addAttribute("errorMessage", "No completed attempts found for this quiz");
                model.addAttribute("userName", userName);
                model.addAttribute("quizName", quiz.getName());
                return "quiz-detail-summary";
            }

            // Prepare data for line chart
            List<String> attemptLabels = new ArrayList<>();
            List<Integer> attemptScores = new ArrayList<>();

            for (uk.ac.cf.spring.clientprojectteam3.quiz.QuizAttemptScore attempt : attempts) {
                attemptLabels.add("Attempt " + attempt.getAttemptNumber());
                attemptScores.add((int) Math.round(attempt.getAverageScore()));
            }

            // Get the latest attempt for detailed breakdown
            uk.ac.cf.spring.clientprojectteam3.quiz.QuizAttemptScore latestAttempt = attempts.get(attempts.size() - 1);

            // Get detailed capability breakdown for the latest attempt
            List<Question> questions = quiz.getQuestions();
            Map<Integer, Integer> latestAnswers = quizRepository.getAttemptAnswers(latestAttempt.getAttemptId());

            List<CapabilityResult> capabilityResults = new ArrayList<>();
            Map<Long, List<Integer>> capabilityScoresMap = new HashMap<>();

            for (Question question : questions) {
                if (latestAnswers.containsKey((int) question.getQuestionId())) {
                    long capabilityId = question.getCapabilityId();
                    int score = latestAnswers.get((int) question.getQuestionId());

                    capabilityScoresMap.computeIfAbsent(capabilityId, k -> new ArrayList<>()).add(score);
                }
            }

            // Calculate average score per capability
            for (Map.Entry<Long, List<Integer>> entry : capabilityScoresMap.entrySet()) {
                try {
                    uk.ac.cf.spring.clientprojectteam3.capabilities.Capability capability =
                        capabilityService.getCapability(entry.getKey());

                    if (capability != null) {
                        int avgScore = (int) Math.round(entry.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0) * 20);

                        capabilityResults.add(new CapabilityResult(capability.getTitle(), avgScore));
                    }
                } catch (IllegalArgumentException e) {
                    // Skip capabilities that don't exist
                    System.err.println("Capability not found: " + entry.getKey());
                }
            }

            // Sort and get strengths/weaknesses
            List<CapabilityResult> sortedResults = new ArrayList<>(capabilityResults);
            sortedResults.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

            List<String> strengths = sortedResults.stream()
                    .limit(3)
                    .map(CapabilityResult::getCapabilityTitle)
                    .collect(Collectors.toList());

            List<String> weaknesses = sortedResults.stream()
                    .filter(r -> r.getScore() < 60)
                    .map(CapabilityResult::getCapabilityTitle)
                    .collect(Collectors.toList());

            int overallScore = attemptScores.isEmpty() ? 0 :
                    (int) Math.round(attemptScores.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0));

            model.addAttribute("userName", userName);
            model.addAttribute("quizName", quiz.getName());
            model.addAttribute("attemptNumber", attempts.size() + " attempt(s) completed");
            model.addAttribute("completionDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            model.addAttribute("overallScore", attemptScores.get(attemptScores.size() - 1)); // Latest score
            model.addAttribute("strengths", strengths);
            model.addAttribute("weaknesses", weaknesses);
            model.addAttribute("capabilityResults", capabilityResults);
            model.addAttribute("capabilityLabels", attemptLabels);
            model.addAttribute("capabilityScores", attemptScores);
            model.addAttribute("hasData", !attemptScores.isEmpty());

        } catch (Exception e) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", "Error loading quiz summary: " + e.getMessage());
            e.printStackTrace();
        }

        return "quiz-detail-summary";
    }

    public static class CapabilityResult {
        private String capabilityTitle;
        private int score;

        public CapabilityResult(String capabilityTitle, int score) {
            this.capabilityTitle = capabilityTitle;
            this.score = score;
        }

        public String getCapabilityTitle() {
            return capabilityTitle;
        }

        public int getScore() {
            return score;
        }
    }
}