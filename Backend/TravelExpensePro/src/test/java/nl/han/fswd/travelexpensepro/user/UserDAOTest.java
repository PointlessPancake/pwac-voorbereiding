package nl.han.fswd.travelexpensepro.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDAOTest {
    @Mock
    private JdbcTemplate mockJdbcTemplate;
    @InjectMocks
    private UserDAO sut;

    @Test
    void get_withGoodUser_ShouldGetTheSameUser() {
        //Arrange
        User u = new User("tester", "abcdefg555", "null");
        String EXPECTED_SQL = "SELECT * FROM AppUser WHERE username = ?";
        when(mockJdbcTemplate.queryForObject(eq(EXPECTED_SQL), any(UserRowMapper.class), eq(u.getUsername())))
                .thenReturn(u);
        // Act
        var testValue = sut.get(u);
        //Assert
        assertEquals("tester", testValue.getUsername());
    }
}