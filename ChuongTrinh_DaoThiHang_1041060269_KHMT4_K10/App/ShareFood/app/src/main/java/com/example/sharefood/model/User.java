package com.example.sharefood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractModel{
    private int id;
    private String user_name;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String avatar;
    private String gender;
    private String created_at;
    private int permission;
    private int locked;

    public User(String user_name, String password, String name, String phone, String address, String avatar, String gender, String created_at, int permission, int locked) {
        this.user_name = user_name;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.gender = gender;
        this.created_at = created_at;
        this.permission = permission;
        this.locked = locked;
    }
}
