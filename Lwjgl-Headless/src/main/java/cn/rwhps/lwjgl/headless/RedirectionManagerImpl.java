/*
 * Copyright 2020-2022 RW-HPS Team and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/RW-HPS/RW-HPS/blob/master/LICENSE
 */

package cn.rwhps.lwjgl.headless;

import cn.rwhps.lwjgl.headless.api.Redirection;
import cn.rwhps.lwjgl.headless.api.RedirectionManager;
import cn.rwhps.lwjgl.headless.redirections.CastRedirection;
import cn.rwhps.lwjgl.headless.redirections.DefaultRedirections;
import cn.rwhps.lwjgl.headless.redirections.LwjglRedirections;
import cn.rwhps.lwjgl.headless.redirections.ObjectRedirection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RedirectionManagerImpl implements RedirectionManager {
    private final Map<String, Redirection> redirects = new HashMap<>();
    private final Redirection object;
    private final Redirection cast;

    public RedirectionManagerImpl() {
        object = new ObjectRedirection(this);
        cast = new CastRedirection(this);
        LwjglRedirections.register(this);
    }

    @Override
    public void redirect(String desc, Redirection redirection) {
        redirects.put(desc, redirection);
    }

    @Override
    public Object invoke(Object obj, String desc, Class<?> type, Object... args) throws Throwable {
        return invoke(desc, type, obj, () -> getFallback(desc, type), args);
    }

    @Override
    public Object invoke(String desc, Class<?> type, Object obj, Supplier<Redirection> fb, Object... args)
        throws Throwable {
        Redirection redirection = redirects.get(desc);
        if (redirection == null) {
            redirection = fb.get();
        }

        return redirection.invoke(obj, desc, type, args);
    }

    private Redirection getFallback(String desc, Class<?> type) {
        if (desc.startsWith(Redirection.CAST_PREFIX)) {
            // TODO: currently cast redirection looks like this:
            //  <cast> java/lang/String
            //  <init> <cast> java/lang/String
            //  It contains no information about the calling class!
            return cast;
        }

        return DefaultRedirections.fallback(type, object);
    }

}
