package uk.ac.cf.spring.clientprojectteam3.admin.outcome;

import java.util.List;

public interface AdminOutcomeService {

    List<AdminOutcome> getAllOutcomesWithCapabilityCount();
    void createOutcome(String outcomeTitle);
    void deleteOutcome(Long outcomeId);
    AdminOutcome getOutcomeById(Long id);
    void updateOutcome(Long id, String title);
}
