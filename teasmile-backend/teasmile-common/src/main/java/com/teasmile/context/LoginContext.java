package com.teasmile.context;

/**
 * 登录用户上下文。
 * <p>
 * 基于 ThreadLocal 在一次请求的线程内共享当前登录用户 id：
 * 拦截器完成令牌解析后写入，业务层（如公共字段自动填充）读取，
 * 请求结束时必须调用 {@link #clear()} 释放，避免 Tomcat 线程复用造成数据串用。
 */
public final class LoginContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    private LoginContext() {
    }

    /**
     * 绑定当前登录用户 id
     */
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    /**
     * 获取当前登录用户 id（管理端为员工 id，用户端为会员 id）
     */
    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    /**
     * 移除当前线程绑定的用户 id
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
    }
}
