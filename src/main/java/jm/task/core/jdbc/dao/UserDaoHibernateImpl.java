package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String createTableQuery = "CREATE TABLE IF NOT EXISTS users( id int NOT NULL AUTO_INCREMENT, " +
            "name varchar(15), lastname varchar(25), age tinyint, PRIMARY KEY (id))";
    private static final String dropTableQuery = "DROP TABLE IF EXISTS users";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery(createTableQuery, User.class).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery(dropTableQuery, User.class).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
//    @Transactional
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
//            session.beginTransaction();
            session.save(new User(name, lastName, age));
//            session.getTransaction().commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
//            session.beginTransaction();
            session.delete(session.get(User.class, id));
//            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try (Session session = Util.getSessionFactory().openSession()) {
//            session.beginTransaction();
            users = session.createQuery("from User ").list();
//            session.getTransaction().commit();
        }
        users.stream().forEach(System.out::println);
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
