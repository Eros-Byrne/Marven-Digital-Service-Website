package uk.ac.cf.spring.clientprojectteam3.admin.capability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(AdminCapabilityRepositoryImpl.class)
@TestPropertySource(properties = {
        "spring.sql.init.mode=never"
})
@Sql(statements = {
        // --- schema (idempotent) ---
        "CREATE TABLE IF NOT EXISTS outcomes (" +
                " outcome_id BIGINT PRIMARY KEY," +
                " title VARCHAR(255)" +
                ");",

        "CREATE TABLE IF NOT EXISTS capabilities (" +
                " capability_id BIGINT PRIMARY KEY," +
                " title VARCHAR(255)," +
                " description TEXT," +
                " outcome_id BIGINT" +
                ");",

        // --- test data (safe overwrite) ---
        "DELETE FROM capabilities;",
        "DELETE FROM outcomes;",

        "INSERT INTO outcomes (outcome_id, title) VALUES (1, 'Test Outcome');",
        "INSERT INTO capabilities (capability_id, title, description, outcome_id) " +
                "VALUES (1, 'Test Capability', 'Test Description', 1);"
})
class AdminCapabilityRepositoryTest {

    @Autowired
    private AdminCapabilityRepository repository;

    @Test
    void findById_returnsCapability() {
        Optional<AdminCapability> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getTitle()).isEqualTo("Test Capability");
    }

    @Test
    void findById_returnsEmptyWhenNotFound() {
        Optional<AdminCapability> result = repository.findById(9999L);

        assertThat(result).isEmpty();
    }
}
