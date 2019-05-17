package com.sharefood.ShareFood.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_restaurants")
public class Restaurant extends AbstractModel {

    @Id
    @Column(name = "id_restaurant")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "time_open")
    private String time_open;

    @Column(name = "time_close")
    private String time_close;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @Column(name = "logo")
    private String logo;

    @Column(name = "locked")
    private int locked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_province")
    @NotNull
    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
    private Province province;
}
