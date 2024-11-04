package nl.han.fswd.travelexpensepro.expenseclaim;

import nl.han.fswd.travelexpensepro.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseClaimControllerTest {

    @Mock
    private ExpenseClaimService expenseClaimService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ExpenseClaimController sut;

    private ExpenseClaim validExpenseClaim;
    private static final String VALID_TOKEN = "validToken";
    private static final String VALID_AUTH_HEADER = "Bearer " + VALID_TOKEN;
    private static final String VALID_USERNAME = "testUsername";

    @BeforeEach
    void setUp() {
        // Arrange
        validExpenseClaim = new ExpenseClaim("Lunch", "Business lunch with client", 50.00);
        when(jwtUtil.getUsername(VALID_TOKEN)).thenReturn(VALID_USERNAME);
    }

    @Test
    void addExpenseClaim_WithValidTokenAndExpenseClaim_ShouldReturnOk() {
        // Act
        ResponseEntity<String> response = sut.addExpenseClaim(validExpenseClaim, VALID_AUTH_HEADER);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addExpenseClaim_WithValidTokenAndExpenseClaim_ShouldCallInsert() {
        // Act
        sut.addExpenseClaim(validExpenseClaim, VALID_AUTH_HEADER);

        // Assert
        verify(expenseClaimService).insertExpenseClaim(VALID_USERNAME, validExpenseClaim);
    }
}