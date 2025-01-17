/*
 * Copyright 2020-2022 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package cn.rwhps.lwjgl.headless.redirections;

import cn.rwhps.lwjgl.headless.api.Redirection;
import cn.rwhps.lwjgl.headless.api.RedirectionManager;
import cn.rwhps.lwjgl.headless.util.DescriptionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class ObjectRedirection implements Redirection {
    private final RedirectionManager manager;

    public ObjectRedirection(RedirectionManager manager) {
        this.manager = manager;
    }

    @Override
    public Object invoke(Object obj, String d, Class<?> type, Object... args) {
        if (type.isInterface()) {
            return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, new ProxyRedirection(manager, DescriptionUtil.getDesc(type)));
        } else if (type.isArray()) {
            int dimension = 0;
            while (type.isArray()) {
                dimension++;
                type = type.getComponentType();
            }

            final int[] dimensions = new int[dimension];
            Arrays.fill(dimensions, 0);
            return Array.newInstance(type, dimensions);
        } else if (Modifier.isAbstract(type.getModifiers())) {
            // TODO: logger for headlessmc-lwjgl?
            System.err.println("Can't return abstract class: " + d);
            return null;
        }

        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (SecurityException | ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

}
