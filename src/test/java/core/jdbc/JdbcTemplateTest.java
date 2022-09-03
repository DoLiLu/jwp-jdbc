package core.jdbc;

import next.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;
import java.util.stream.Collectors;

class JdbcTemplateTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("insert & update & querySingle 검증")
    @Test
    void executeAndQuerySingleTest() {
        User expected = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().execute("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail());

        User actual = JdbcTemplate.getInstance()
                .querySingle("SELECT userId, password, name, email FROM USERS WHERE userId = ?", User.class, expected.getUserId());
        Assertions.assertThat(actual).isEqualTo(expected);

        JdbcTemplate.getInstance().execute("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", "password2", "name2", "kimkim@gmail.com", expected.getUserId());
        actual = JdbcTemplate.getInstance()
                .querySingle("SELECT userId, password, name, email FROM USERS WHERE userId = ?", User.class, expected.getUserId());
        Assertions.assertThat(actual.getPassword()).isEqualTo("password2");
        Assertions.assertThat(actual.getName()).isEqualTo("name2");
        Assertions.assertThat(actual.getEmail()).isEqualTo("kimkim@gmail.com");
    }

    @DisplayName("리스트 조회 검증")
    @Test
    void query() {
        User expected1 = new User("userId1", "password", "name1", "fistkim101@email.com");
        User expected2 = new User("userId2", "password", "name2", "fistkim101@email.com");
        User expected3 = new User("userId3", "password", "name3", "fistkim101@email.com");

        JdbcTemplate.getInstance().execute("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected1.getUserId(), expected1.getPassword(), expected1.getName(), expected1.getEmail());
        JdbcTemplate.getInstance().execute("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected2.getUserId(), expected2.getPassword(), expected2.getName(), expected2.getEmail());
        JdbcTemplate.getInstance().execute("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected3.getUserId(), expected3.getPassword(), expected3.getName(), expected3.getEmail());

        List<User> expected = JdbcTemplate.getInstance()
                .query("SELECT * FROM USERS", User.class)
                .stream()
                .map(resultObject -> (User) resultObject)
                .collect(Collectors.toList());

        Assertions.assertThat(expected.size()).isEqualTo(3);
    }
}