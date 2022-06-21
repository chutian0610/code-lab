module info.victorchu.demo.javamodule.appmodule {
    requires info.victorchu.demo.javamodule.entitymodule;
    requires info.victorchu.demo.javamodule.daomodule;
    requires info.victorchu.demo.javamodule.userdaomodule;
    uses info.victorchu.demo.javamodule.daomodule.Dao;
}