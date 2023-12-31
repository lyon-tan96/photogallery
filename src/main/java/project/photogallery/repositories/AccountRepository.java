package project.photogallery.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import project.photogallery.models.Login;
import project.photogallery.models.Registration;
import project.photogallery.models.User;

@Repository
public class AccountRepository {

    @Autowired
    private JdbcTemplate template;

    public static final String SQL_NEW_REGISTRATION = "insert into account(username, email, password, pic) values(?,?,sha1(?),?)";
    public final static String SQL_SELECT_USERNAME_EMAIL = "select username, email from account where email = ? and password = sha1(?)";
    public static final String SQL_SELECT_ACC_BY_EMAIL = "select * from account where email = ?";
    public static final String SQL_SELECT_ACC_BY_USERNAME = "select * from account where username = ?";

    public Optional<Registration> findUserByEmail(String email, String username) {
        final SqlRowSet rsEmail = template.queryForRowSet(SQL_SELECT_ACC_BY_EMAIL, email);
        final SqlRowSet rsUsername = template.queryForRowSet(SQL_SELECT_ACC_BY_USERNAME, username);
        if (!rsEmail.next()) {
            return Optional.empty();
        } else if (!rsUsername.next()) {
            return Optional.empty();
        }
        Registration r = new Registration();
        r.setUsername(rsUsername.getString("username"));
        r.setEmail(rsUsername.getString("email"));
        return Optional.of(r);
    }

    public boolean newRegistration(Registration r) {

        int updated = template.update(SQL_NEW_REGISTRATION, r.getUsername(), r.getEmail(), r.getPassword(), r.getPic());
        return updated == 1;
    }
    
    public Optional<User> loginAndSelectUsername (Login login) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USERNAME_EMAIL, login.getEmail(), login.getPassword());
        if (!rs.next())
            return Optional.empty();
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));

        return Optional.of(user);
    }
}
