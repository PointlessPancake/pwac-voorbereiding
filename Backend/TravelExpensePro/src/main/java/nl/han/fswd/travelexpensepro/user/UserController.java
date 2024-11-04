package nl.han.fswd.travelexpensepro.user;


import nl.han.fswd.travelexpensepro.security.JwtResponse;
import nl.han.fswd.travelexpensepro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nl.han.fswd.travelexpensepro.user.UserService.checkPassword;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        userService.loginValidation(user);

        User userFRomDatabase = userService.selectUser(user);
        boolean isPasswordMatch = checkPassword(user.getPassword(), userFRomDatabase.getPassword());
        if (isPasswordMatch) {
            String token = jwtUtil.createJwt(user.getUsername());
            return ResponseEntity.ok(new JwtResponse(token, "Bearer"));
        } else {
            return new ResponseEntity("Incorrect password or username", HttpStatus.UNAUTHORIZED);
        }

    }
}

