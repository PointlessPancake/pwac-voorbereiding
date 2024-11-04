package nl.han.fswd.travelexpensepro.expenseclaim;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExpenseClaimDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ExpenseClaimDAO sut;

    private final ExpenseClaim VALID_EXPENSECLAIM = new ExpenseClaim("Lunch", "Business lunch with client", 50.00);
    private final String VALID_USERNAME = "testUsername";
    private final String EXPECTED_SQL = "INSERT INTO ExpenseClaim (username, title, amount, description) VALUES (?, ?, ?, ?)";

    @Test
    void save_WithValidUsernameAndExpenseClaim_ShouldInsertSuccessfully() {

        // Act
        sut.save(VALID_USERNAME, VALID_EXPENSECLAIM);

        // Assert
        verify(jdbcTemplate).update(
                eq(EXPECTED_SQL),
                eq(VALID_USERNAME),
                eq(VALID_EXPENSECLAIM.getTitle()),
                eq(VALID_EXPENSECLAIM.getAmount()),
                eq(VALID_EXPENSECLAIM.getDescription())
        );
    }
}