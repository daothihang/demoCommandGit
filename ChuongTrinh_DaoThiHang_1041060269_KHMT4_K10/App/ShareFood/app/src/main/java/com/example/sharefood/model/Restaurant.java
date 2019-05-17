package com.example.sharefood.model;

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

    public Restaurant(String name, String created_at, String time_open, String time_close, String address, String phone, String website, String logo, int locked, Province province) {
        this.name = name;
        this.created_at = created_at;
        this.time_open = time_open;
        this.time_close = time_close;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.logo = logo;
        this.locked = locked;
        this.province = province;
    }
}
