package nl.han.fswd.travelexpensepro.expenseclaim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExpenseClaimDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(String username, ExpenseClaim expenseClaim) {
        jdbcTemplate.update(
                "INSERT INTO ExpenseClaim (username, title, amount, description) VALUES (?, ?, ?, ?)",
                username, expenseClaim.getTitle(), expenseClaim.getAmount(), expenseClaim.getDescription());
    }
}
