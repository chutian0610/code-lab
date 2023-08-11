package info.victorchu.demos.javamodules.userdao;

import info.victorchu.demos.javamodules.dao.Dao;
import info.victorchu.demos.javamodules.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDao implements Dao<User> {
    public UserDao() {
    }

    public UserDao(Map<Long, User> users) {
        this.users = users;
    }

    private Map<Long, User> users;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

}
