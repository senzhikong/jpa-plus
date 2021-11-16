package com.senzhikong.db.sql.wrapper;

import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedAction;

public class SetAccessibleAction<T extends AccessibleObject> implements PrivilegedAction<T> {
    private final T obj;

    public SetAccessibleAction(T obj) {
        this.obj = obj;
    }

    public T run() {
        this.obj.setAccessible(true);
        return this.obj;
    }
}

