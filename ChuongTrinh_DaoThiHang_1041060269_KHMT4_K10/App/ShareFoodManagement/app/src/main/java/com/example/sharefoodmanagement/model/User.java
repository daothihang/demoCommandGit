package com.example.sharefoodmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User  extends AbstractModel{
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
}
