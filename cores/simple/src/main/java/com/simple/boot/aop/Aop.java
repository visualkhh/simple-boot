package com.simple.boot.aop;

import com.simple.boot.util.ReflectionUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;


@Getter
@Setter
@NoArgsConstructor
public class Aop {

    Method method;
    Object object;
    Object[] args;
    Throwable throwable;

    @Builder
    public Aop(Method method, Object object, Object[] args, Throwable throwable) {
        this.method = method;
        this.object = object;
        this.args = args;
        this.throwable = throwable;
    }

    public Object invokeNoThrows() {
        try {
            return ReflectionUtils.invoke(method, object, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
