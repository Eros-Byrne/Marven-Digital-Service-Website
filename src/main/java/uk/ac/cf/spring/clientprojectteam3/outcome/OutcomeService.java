package uk.ac.cf.spring.clientprojectteam3.outcome;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutcomeService {

    private final OutcomeRepository outcomeRepository;

    public OutcomeService(OutcomeRepository outcomeRepository) {
        this.outcomeRepository = outcomeRepository;
    }

    public List<Outcome> getAllOutcomes() {
        return outcomeRepository.findAll();
    }

    public Outcome getOutcomeById(Long id) {
        return outcomeRepository.findById(id);
    }
}