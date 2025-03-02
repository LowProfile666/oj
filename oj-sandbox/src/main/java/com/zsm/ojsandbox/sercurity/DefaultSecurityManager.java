package com.zsm.ojsandbox.sercurity;

import java.security.Permission;

/**
 * 默认安全管理器
 * author: ZSM
 * time: 2025-03-02 22:36
 */
public class DefaultSecurityManager extends SecurityManager{
    /**
     * 检查所有的权限
     * @param perm   the requested permission.
     */
//    @Override
//    public void checkPermission(Permission perm) {
//        System.out.println("Checking permission: " + perm);
//        super.checkPermission(perm);
//    }

    /**
     * 拒绝所有的权限
     * @param perm   the requested permission.
     */
//    @Override
//    public void checkPermission(Permission perm) {
//        System.out.println("Checking permission: " + perm);
//        throw new SecurityException("拒绝权限");
//    }


    /**
     * 限制读权限
     * @param file   the system-dependent file name.
     */
    @Override
    public void checkRead(String file) {
        super.checkRead(file);
    }

    /**
     * 限制写的权限
     * @param file   the system-dependent filename.
     */
    @Override
    public void checkWrite(String file) {
        System.out.println("checkWrite: " + file);
        throw new SecurityException("不准写：" + file );
    }

    @Override
    public void checkDelete(String file) {
        super.checkDelete(file);
    }

    @Override
    public void checkExec(String cmd) {
        super.checkExec(cmd);
    }
}
