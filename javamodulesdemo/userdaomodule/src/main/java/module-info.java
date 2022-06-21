module info.victorchu.demo.javamodule.userdaomodule {

    requires info.victorchu.demo.javamodule.daomodule;
    requires info.victorchu.demo.javamodule.entitymodule;

    provides info.victorchu.demo.javamodule.daomodule.Dao with info.victorchu.demo.javamodule.userdaomodule.UserDao;

    exports info.victorchu.demo.javamodule.userdaomodule;
}