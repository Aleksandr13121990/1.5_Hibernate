package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection;

    private static final String createTableQuery = "CREATE TABLE IF NOT EXISTS users( id int NOT NULL AUTO_INCREMENT, " +
            "name varchar(15), lastname varchar(25), age tinyint, PRIMARY KEY (id))";

    private static final String dropTableQuery = "DROP TABLE IF EXISTS users";

    private static final String saveUserQuery = "INSERT INTO users(name, lastname, age) VALUES (?, ?, ?)";

    private static final String removeUserByIdQuery = "DELETE FROM users WHERE id = ?";

    private static final String getAllUsersQuery = "SELECT id, name, lastname, age FROM users";

    private static final String cleanUsersTableQuery = "DELETE FROM users";

    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table", e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to drop table", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveUserQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeUserByIdQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove user", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        User user;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllUsersQuery)) {
            while (resultSet.next()) {
                user = new User(resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all users", e);
        }
        users.stream().forEach(System.out::println);
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(cleanUsersTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean users table", e);
        }
    }
}
