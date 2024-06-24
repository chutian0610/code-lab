package info.victorchu.snippets.typeinfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeInfo<T>
{
    protected final Type type;

    public Type getType()
    {
        return type;
    }

    protected TypeInfo()
    {
        Type superClass = getClass().getGenericSuperclass();

        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        this.type = type;
    }
}
