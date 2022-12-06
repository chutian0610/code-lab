package info.victorchu.commontool.struct.tuple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author victorchu
 * @date 2022/8/10 23:29
 */
class TupleTest {
    public static TupleType tripletTupleType = TupleType.DefaultFactory.create(
            Number.class,
            String.class,
            Character.class);
    public static TupleType emptyTupleType = TupleType.DefaultFactory.create();

    @Test
    public void one() {
        Assertions.assertDoesNotThrow(()->{
        final Tuple t1 = tripletTupleType.createTuple(1, "one", 'a');
        System.out.println("t1 = " + t1);
        });
    }

    @Test
    public void two() {
        Assertions.assertDoesNotThrow(()->{
        final Tuple t2 = tripletTupleType.createTuple(2l, "two", 'b');
        System.out.println("t2 = " + t2);
        });
    }

    @Test
    public void three() {
        Assertions.assertDoesNotThrow(()->{
        final Tuple t3 = tripletTupleType.createTuple(3f, "three", 'c');
        System.out.println("t3 = " + t3);
        });
    }

    @Test
    public void nullTuple() {
        Assertions.assertDoesNotThrow(()->{
            final Tuple tnull = tripletTupleType.createTuple(null, "(null)", null);
            System.out.println("tnull = " + tnull);
        });
    }

    @Test
    public void empty() {
        Assertions.assertDoesNotThrow(()->{
            final Tuple tempty= emptyTupleType.createTuple();
            System.out.println("\ntempty = " + tempty);
        });
    }

    @Test
    public void wrongTypes() {
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            tripletTupleType.createTuple(1, 2, 3);
        });


    }

    @Test
    public void wrongNumberOfArguments() {
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            emptyTupleType.createTuple(1);
        });

    }

    @Test
    public void valueType() {
        final Tuple t9 = tripletTupleType.createTuple(9, "nine", 'i');
        assertEquals(Integer.class, t9.getNthValue(0).getClass());
    }
}