package nl.han.fswd.travelexpensepro.user;

import nl.han.fswd.travelexpensepro.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDAO mockUserDAO;
    @InjectMocks
    private UserService sut;


    @Test
    void should_get_user_From_Database() {
        //Arrange
        User u = new User("tester", "abcdefg555", "null");
        //Act
        sut.selectUser(u);
        // Assert
        verify(mockUserDAO).get(u);
    }

    @Test
    void checkPassword() {

        //Act
        var testValue = UserService.checkPassword("abcdefg555", "$2a$12$HteUlBrCiPGRsGP8LKwJmewKCAqeogvNDqJHHQGxYYrW38K3lUFry");
        //Assert
        assertTrue(testValue);
    }

    @Test
    void giveUsername_WithSpecialCharacters_ShouldThrowException() {
        // Arrange
        User user = new User("tester!", "abcdefg555", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("special characters are not allowed in the username", exception.getMessage());
    }

    @Test
    void giveUsername_WithTwoCharacters_ShouldThrowException() {
        // Arrange
        User user = new User("te", "abcdefg555!", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("Username must be at least 3 characters long", exception.getMessage());
    }
    @Test
    void giveUsername_WithSeventeenCharacters_ShouldThrowException() {
        // Arrange
        User user = new User("testerdenewghjets", "abcdefg555!", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("Username can be at most 16 characters long", exception.getMessage());
    }
    @Test
    void givePassword_WithoutNumbers_ShouldThrowException() {
        // Arrange
        User user = new User("tester", "abcdefg555", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("Password must contain at least one number and one Special character", exception.getMessage());
    }

    @Test
    void givePassword_WithoutSpecialCharacters_ShouldThrowException() {
        // Arrange
        User user = new User("tester", "abcdefg5", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("Password must contain at least one number and one Special character", exception.getMessage());
    }

    @Test
    void givePassword_WithFiveCharacters_ShouldThrowException() {
        // Arrange
        User user = new User("tester", "tes5!", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }
    @Test
    void givePassword_WithTwentyoneCharacters_ShouldThrowException() {
        // Arrange
        User user = new User("tester", "abcdefg555!!!!!!!!!!!", "null");

        // Act and Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> sut.loginValidation(user));
        assertEquals("Password can be at most 20 characters long", exception.getMessage());
    }

    @Test
    void giveString_WithSpecialCharacters_ShouldReturnTrue(){
        //Arrange
        String input = "has$pecialChar";
        //Act
        var testvalue = sut.hasSpecialChar(input);
        //Assert
        assertTrue(testvalue);
    }
    @Test
    void giveString_WithANumber_ShouldReturnTrue(){
        //Arrange
        String input = "hasNumb3r";
        //Act
        var testvalue = sut.hasNumbers(input);
        //Assert
        assertTrue(testvalue);
    }
}