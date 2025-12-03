
package uk.ac.cf.spring.clientprojectteam3.quiz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.Capability;
import uk.ac.cf.spring.clientprojectteam3.Capabilities.CapabilityService;

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

    public SummaryController(QuizService quizService, QuizRepository quizRepository, CapabilityService capabilityService) {
        this.quizService = quizService;
        this.quizRepository = quizRepository;
        this.capabilityService = capabilityService;
    }

    // Landing page - redirects to latest attempt or shows empty state
    @GetMapping
    public String summaryLanding(Model model) {
        // For now, show empty summary with sample quiz/attempt
        // TODO: In future, fetch user's latest attempt from database
        // For demo purposes, use quiz 1, attempt 1
        return "redirect:/summary/quiz/1/attempt/1";
    }

    @GetMapping("/quiz/{quizId}/attempt/{attemptId}")
    public String showSummary(
            @PathVariable long quizId,
            @PathVariable long attemptId,
            Model model
    ) {
        // Initialize default values
        model.addAttribute("hasError", false);
        model.addAttribute("hasData", false);

        try {
            // Get quiz info using existing method
            Quiz quiz = quizService.getQuizForAttempt(quizId, 0);
            List<Question> questions = quiz.getQuestions();

            // Get attempt answers from database
            Map<Long, Integer> attemptAnswers = quizRepository.getAttemptAnswers(attemptId);
            int attemptNumber = quizRepository.getAttemptNumber(attemptId);

            // Initialize empty data structures
            Map<Long, List<Integer>> capabilityScores = new HashMap<>();
            Map<Long, String> capabilityTitles = new HashMap<>();

            // If there are answers, process them
            if (!attemptAnswers.isEmpty()) {
                for (Question question : questions) {
                    Long questionId = question.getQuestionId();
                    Long capabilityId = question.getCapabilityId();

                    if (attemptAnswers.containsKey(questionId)) {
                        Integer score = attemptAnswers.get(questionId);

                        capabilityScores.computeIfAbsent(capabilityId, k -> new ArrayList<>()).add(score);

                        if (!capabilityTitles.containsKey(capabilityId)) {
                            try {
                                Capability capability = capabilityService.getCapability(capabilityId);
                                capabilityTitles.put(capabilityId, capability.getTitle());
                            } catch (Exception e) {
                                capabilityTitles.put(capabilityId, "Capability " + capabilityId);
                            }
                        }
                    }
                }
            }

            // Calculate results
            List<CapabilityResult> capabilityResults = new ArrayList<>();
            List<String> capabilityLabels = new ArrayList<>();
            List<Integer> capabilityScoresList = new ArrayList<>();

            for (Map.Entry<Long, List<Integer>> entry : capabilityScores.entrySet()) {
                Long capabilityId = entry.getKey();
                List<Integer> scores = entry.getValue();

                int averageScore = (int) Math.round(scores.stream()
                        .mapToInt(Integer::intValue)
                        .average()
                        .orElse(0.0) * 20);

                String title = capabilityTitles.get(capabilityId);

                capabilityResults.add(new CapabilityResult(title, averageScore));
                capabilityLabels.add(title);
                capabilityScoresList.add(averageScore);
            }

            // Sort for strengths/weaknesses
            List<CapabilityResult> sortedResults = new ArrayList<>(capabilityResults);
            sortedResults.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

            List<String> strengths = sortedResults.stream()
                    .filter(r -> r.getScore() >= 60)
                    .limit(3)
                    .map(CapabilityResult::getCapabilityTitle)
                    .collect(Collectors.toList());

            List<String> weaknesses = sortedResults.stream()
                    .filter(r -> r.getScore() < 60)
                    .limit(3)
                    .map(CapabilityResult::getCapabilityTitle)
                    .collect(Collectors.toList());

            int overallScore = capabilityScoresList.isEmpty() ? 0 :
                    (int) Math.round(capabilityScoresList.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0));

            // Add to model - always add these, even if empty
            model.addAttribute("quizName", quiz.getName());
            model.addAttribute("attemptNumber", attemptNumber);
            model.addAttribute("completionDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            model.addAttribute("overallScore", overallScore);
            model.addAttribute("strengths", strengths);
            model.addAttribute("weaknesses", weaknesses);
            model.addAttribute("capabilityResults", capabilityResults);
            model.addAttribute("capabilityLabels", capabilityLabels);
            model.addAttribute("capabilityScores", capabilityScoresList);
            model.addAttribute("hasData", !attemptAnswers.isEmpty());

        } catch (Exception e) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", "Error loading summary: " + e.getMessage());
            e.printStackTrace();
        }

        return "summary";
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