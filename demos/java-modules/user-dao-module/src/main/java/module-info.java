import info.victorchu.demos.javamodules.dao.Dao;
import info.victorchu.demos.javamodules.userdao.UserDao;

module info.victorchu.demos.javamodules.userdao {

    requires info.victorchu.demos.javamodules.dao;
    requires info.victorchu.demos.javamodules.entity;

    provides Dao with UserDao;

    exports info.victorchu.demos.javamodules.userdao;
}