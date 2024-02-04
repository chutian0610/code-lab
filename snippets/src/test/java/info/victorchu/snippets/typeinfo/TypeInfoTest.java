package info.victorchu.snippets.typeinfo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypeInfoTest
{
    @Test
    void test01()
    {
        TypeInfo typeInfo = new TypeInfo<String>() {};
        System.out.println(typeInfo.getType());
    }

    @Test
    void test02()
    {
        TypeInfo typeInfo = new TypeInfo<Map<String, String>>() {};
        System.out.println(typeInfo.getType());
    }
}