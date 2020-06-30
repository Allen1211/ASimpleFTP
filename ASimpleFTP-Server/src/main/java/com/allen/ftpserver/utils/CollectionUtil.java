package com.allen.ftpserver.utils;

import java.util.Collection;

/**
 * @ClassName CollectionUtil
 * @Description
 * @Author XianChuLun
 * @Date 2020/6/1
 * @Version 1.0
 */
public class CollectionUtil {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T[] arrays) {
        return arrays == null || arrays.length == 0;
    }


}
