package nl.han.fswd.travelexpensepro.user;


import nl.han.fswd.travelexpensepro.exceptions.InvalidInputException;
import nl.han.fswd.travelexpensepro.security.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;


    public User selectUser(User user) {
        try {
            return userDAO.get(user);
        } catch (EmptyResultDataAccessException e) {
            throw new UnauthorizedException("Incorrect password or username");
        }
    }

    // Verify a password
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public void loginValidation(User user) {

        if (hasSpecialChar(user.getUsername())) {
            throw new InvalidInputException("special characters are not allowed in the username");
        }
        if (user.getUsername().length() < 3) {
            throw new InvalidInputException("Username must be at least 3 characters long");
        } else if (user.getUsername().length() > 16) {
            throw new InvalidInputException("Username can be at most 16 characters long");
        } else if (user.getUsername() == null) {
            throw new InvalidInputException("Username must be filled");
        }

        if (!hasNumbers(user.getPassword()) || !hasSpecialChar(user.getPassword())) {
            throw new InvalidInputException("Password must contain at least one number and one Special character");
        }
        if (user.getPassword() == null) {
            throw new InvalidInputException("Password must be filled");
        }else if (user.getPassword().length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters long");
        } else if (user.getPassword().length() > 20) {
            throw new InvalidInputException("Password can be at most 20 characters long");
        }
    }

    public boolean hasSpecialChar(String input) {
        Pattern specialChars = Pattern.compile("[^A-Za-z0-9]");
        Matcher match = specialChars.matcher(input);
        return match.find();
    }

    public boolean hasNumbers(String input) {
        Pattern number = Pattern.compile("[0-9]");
        Matcher match = number.matcher(input);
        return match.find();
    }
}
