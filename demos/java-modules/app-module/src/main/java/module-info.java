module info.victorchu.demos.javamodules.app {
    requires info.victorchu.demos.javamodules.entity;
    requires info.victorchu.demos.javamodules.dao;
    requires info.victorchu.demos.javamodules.userdao;
    uses info.victorchu.demos.javamodules.dao.Dao;
}