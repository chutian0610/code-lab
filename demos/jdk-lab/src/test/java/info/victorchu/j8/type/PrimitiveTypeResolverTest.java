package info.victorchu.j8.type;

import info.victorchu.jdk.lab.usage.type.util.PrimitiveTypeResolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypeResolverTest {

    @Test
    public void testBoolean(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Boolean
                .equals(PrimitiveTypeResolver.resolve(boolean.class)));
    }

    @Test
    public void testCharacter(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Character
                .equals(PrimitiveTypeResolver.resolve(char.class)));
    }

    @Test
    public void testByte(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Byte
                .equals(PrimitiveTypeResolver.resolve(byte.class)));
    }

    @Test
    public void testShort(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Short
                .equals(PrimitiveTypeResolver.resolve(short.class)));
    }

    @Test
    public void testInteger(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Integer
                .equals(PrimitiveTypeResolver.resolve(int.class)));
    }

    @Test
    public void testLong(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Long
                .equals(PrimitiveTypeResolver.resolve(long.class)));
    }

    @Test
    public void testFloat(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Float
                .equals(PrimitiveTypeResolver.resolve(float.class)));
    }

    @Test
    public void testDouble(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Double
                .equals(PrimitiveTypeResolver.resolve(double.class)));
    }

    @Test
    public void testVoid(){
        assertTrue(PrimitiveTypeResolver.PrimitiveType.Void
                .equals(PrimitiveTypeResolver.resolve(void.class)));
    }


}