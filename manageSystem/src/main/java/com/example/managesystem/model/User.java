package com.example.managesystem.model;

/**
 * @projectName: manageSystem
 * @package: com.example.managesystem.model
 * @className: User
 * @author: zhangcx
 * @description: TODO
 * @date: 2024/5/9 19:47
 * @version: 1.0
 */
public class User {
    private String userId;
    private String accountName;
    private String role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
