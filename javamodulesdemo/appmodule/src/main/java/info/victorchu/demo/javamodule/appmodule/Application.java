package info.victorchu.demo.javamodule.appmodule;

import info.victorchu.demo.javamodule.daomodule.Dao;
import info.victorchu.demo.javamodule.entitymodule.User;
import info.victorchu.demo.javamodule.userdaomodule.UserDao;

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
