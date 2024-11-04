package nl.han.fswd.travelexpensepro.user;

import nl.han.fswd.travelexpensepro.exceptions.InvalidInputException;

public class User {
    private String username;
    private String password;
    private String salt;

    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
