package info.victorchu.demos.javamodules.app;

import info.victorchu.demos.javamodules.dao.Dao;
import info.victorchu.demos.javamodules.entity.User;
import info.victorchu.demos.javamodules.userdao.UserDao;

import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) {
        Map<Long, User> users = new HashMap<>();
        users.put(1L, new User("Julie",1L));
        users.put(2L, new User("David",2L));
        Dao userDao = new UserDao(users);
        userDao.findAll().forEach(System.out::println);
    }
}
