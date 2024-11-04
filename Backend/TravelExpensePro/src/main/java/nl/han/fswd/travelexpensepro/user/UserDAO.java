package nl.han.fswd.travelexpensepro.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User get(User user){
        return jdbcTemplate.queryForObject("SELECT * FROM AppUser WHERE username = ?", new UserRowMapper(), user.getUsername());
    }
}
