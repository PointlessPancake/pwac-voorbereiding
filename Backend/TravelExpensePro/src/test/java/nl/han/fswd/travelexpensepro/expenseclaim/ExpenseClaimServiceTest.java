package nl.han.fswd.travelexpensepro.expenseclaim;

import nl.han.fswd.travelexpensepro.exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExpenseClaimServiceTest {

    @Mock
    private ExpenseClaimDAO expenseClaimDAO;

    @InjectMocks
    private ExpenseClaimService sut;

    private final String VALID_USERNAME = "testUsername";
    private ExpenseClaim expenseClaim;

    @BeforeEach
    void setUp() {
        // Arrange
        expenseClaim = new ExpenseClaim("Lunch", "Business lunch with client", 50.00);
    }

    @Test
    void insertExpenseClaim_WithNullTitle_ShouldThrowException() {
        // Arrange
        expenseClaim.setTitle(null);

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Title must contain 3 through 10 characters", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithTwoCharacterTitle_ShouldThrowException() {
        // Arrange
        expenseClaim.setTitle("ab");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Title must contain 3 through 10 characters", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithThreeCharacterTitle_ShouldInsertSuccessfully() {
        // Arrange
        expenseClaim.setTitle("abc");

        // Act
        sut.insertExpenseClaim(VALID_USERNAME, expenseClaim);

        // Assert
        verify(expenseClaimDAO).save(VALID_USERNAME, expenseClaim);
    }

    @Test
    void insertExpenseClaim_WithTenCharacterTitle_ShouldInsertSuccessfully() {
        // Arrange
        expenseClaim.setTitle("abcdefghij");

        // Act
        sut.insertExpenseClaim(VALID_USERNAME, expenseClaim);

        // Assert
        verify(expenseClaimDAO).save(VALID_USERNAME, expenseClaim);
    }

    @Test
    void insertExpenseClaim_WithElevenCharacterTitle_ShouldThrowException() {
        // Arrange
        expenseClaim.setTitle("abcdefghijk");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Title must contain 3 through 10 characters", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithSpecialCharacterInTitle_ShouldThrowException() {
        // Arrange
        expenseClaim.setTitle("abc@def");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Title must contain only alphanumeric characters and spaces", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithNullDescription_ShouldThrowException() {
        // Arrange
        expenseClaim.setDescription(null);

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Description must contain 3 through 50 characters", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithTwoCharacterDescription_ShouldThrowException() {
        // Arrange
        expenseClaim.setDescription("ab");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Description must contain 3 through 50 characters", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithThreeCharacterDescription_ShouldInsertSuccessfully() {
        // Arrange
        expenseClaim.setDescription("abc");

        // Act
        sut.insertExpenseClaim(VALID_USERNAME, expenseClaim);

        // Assert
        verify(expenseClaimDAO).save(VALID_USERNAME, expenseClaim);
    }

    @Test
    void insertExpenseClaim_WithFiftyCharacterDescription_ShouldInsertSuccessfully() {
        // Arrange
        expenseClaim.setDescription("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWX");

        // Act
        sut.insertExpenseClaim(VALID_USERNAME, expenseClaim);

        // Assert
        verify(expenseClaimDAO).save(
                eq(VALID_USERNAME),
                eq(expenseClaim)
        );
    }

    @Test
    void insertExpenseClaim_WithFiftyOneCharacterDescription_ShouldThrowException() {
        // Arrange
        expenseClaim.setDescription("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXY");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Description must contain 3 through 50 characters", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithSpecialCharacterInDescription_ShouldThrowException() {
        // Arrange
        expenseClaim.setDescription("abcdef@ghijkl");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Description must contain only alphanumeric characters and spaces", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithThreeDecimalsAmount_ShouldThrowException() {
        // Arrange
        expenseClaim.setAmount(1.001);

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Amount must have at most 2 decimal places", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithZeroAmount_ShouldThrowException() {
        // Arrange
        expenseClaim.setAmount(0.00);

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Amount must be greater than 0.00 and less than 10000.00", exception.getMessage());
    }

    @Test
    void insertExpenseClaim_WithZeroPointZeroOneAmount_ShouldInsertSuccessfully() {
        // Arrange
        expenseClaim.setAmount(0.01);

        // Act
        sut.insertExpenseClaim(VALID_USERNAME, expenseClaim);

        // Assert
        verify(expenseClaimDAO).save(VALID_USERNAME, expenseClaim);
    }

    @Test
    void insertExpenseClaim_WithNineThousandNineHundredNinetyNinePointNineNineAmount_ShouldInsertSuccessfully() {
        // Arrange
        expenseClaim.setAmount(9999.99);

        // Act
        sut.insertExpenseClaim(VALID_USERNAME, expenseClaim);

        // Assert
        verify(expenseClaimDAO).save(VALID_USERNAME, expenseClaim);
    }

    @Test
    void insertExpenseClaim_WithTenThousandAmount_ShouldThrowException() {
        // Arrange
        expenseClaim.setAmount(10000.00);

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.insertExpenseClaim(VALID_USERNAME, expenseClaim));
        assertEquals("Amount must be greater than 0.00 and less than 10000.00", exception.getMessage());
    }
}