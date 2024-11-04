package nl.han.fswd.travelexpensepro.expenseclaim;

import nl.han.fswd.travelexpensepro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenseclaims")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ExpenseClaimController {

    @Autowired
    private ExpenseClaimService expenseClaimService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> addExpenseClaim(@RequestBody ExpenseClaim expenseClaim, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        String username = jwtUtil.getUsername(token);

        expenseClaimService.insertExpenseClaim(username, expenseClaim);
        return ResponseEntity.ok().build();
    }
}