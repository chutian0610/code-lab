package info.victorchu.demo.javamodule.userdaomodule;

import info.victorchu.demo.javamodule.daomodule.Dao;
import info.victorchu.demo.javamodule.entitymodule.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDao implements Dao<User> {

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
