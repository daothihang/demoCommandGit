package com.example.sharefoodmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends AbstractModel {
    private int id;
    private String name;
    private String created_at;
    private String time_open;
    private String time_close;
    private String address;
    private String phone;
    private String website;
    private String logo;
    private int locked;
    private Province province;
}
