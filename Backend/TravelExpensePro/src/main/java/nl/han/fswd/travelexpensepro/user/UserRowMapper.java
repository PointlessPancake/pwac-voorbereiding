package nl.han.fswd.travelexpensepro.user;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new User(
                rs.getString("username"),
                rs.getString("passwordhash"),
                rs.getString("salt"));
    }
}
