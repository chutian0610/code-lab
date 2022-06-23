package info.victorchu.demo.javamodule.daomodule;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> findById(Long id);

    List<T> findAll();

}