package nl.han.fswd.travelexpensepro.expenseclaim;

import nl.han.fswd.travelexpensepro.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseClaimService {

    @Autowired
    private ExpenseClaimDAO expenseClaimDAO;

    public void insertExpenseClaim(String username, ExpenseClaim expenseClaim) {
        validateExpenseClaim(expenseClaim);
        expenseClaimDAO.save(username, expenseClaim);
    }

    private void validateExpenseClaim(ExpenseClaim expenseClaim) {
        validateTitle(expenseClaim.getTitle());
        validateDescription(expenseClaim.getDescription());
        validateAmount(expenseClaim.getAmount());
    }

    private void validateTitle(String title) {
        if (title == null || title.length() < 3 || title.length() > 10) {
            throw new InvalidInputException("Title must contain 3 through 10 characters");
        }
        if (title.matches(".*[^a-zA-Z0-9\\s].*")) {
            throw new InvalidInputException("Title must contain only alphanumeric characters and spaces");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.length() < 3 || description.length() > 50) {
            throw new InvalidInputException("Description must contain 3 through 50 characters");
        }
        if (description.matches(".*[^a-zA-Z0-9\\s].*")) {
            throw new InvalidInputException("Description must contain only alphanumeric characters and spaces");
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0 || amount >= 10000) {
            throw new InvalidInputException("Amount must be greater than 0.00 and less than 10000.00");
        }
        if (amount != Math.round(amount * 100) / 100.0) {
            throw new InvalidInputException("Amount must have at most 2 decimal places");
        }
    }
}