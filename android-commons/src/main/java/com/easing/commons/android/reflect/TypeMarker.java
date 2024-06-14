package com.easing.commons.android.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//定义为抽象类，从而强制开发者重写继承此类
public abstract class TypeMarker<T> {

    //获取泛型参数类型
    protected Type genericParamType() {
        //获取父类类型
        //由于是抽象类，其实现类必然是继承当前类，所以父类类型即是TypeMarker<XXX>
        Type superType = getClass().getGenericSuperclass();
        //如果没有指定泛型参数，则返回的Type实际类型为Class
        //未指定泛型参数时，默认将泛型视为Object类
        if (superType instanceof Class)
            return Object.class;
        //如果有泛型参数，则返回的Type实际类型为ParameterizedType
        //强转并获取泛型参数，即XXX的实际类型
        ParameterizedType parameterizedType = (ParameterizedType) superType;
        Type argumentType = parameterizedType.getActualTypeArguments()[0];
        return argumentType;
    }
}
