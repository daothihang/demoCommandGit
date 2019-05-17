package com.sharefood.ShareFood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_provinces")
public class Province extends AbstractModel{
    @Id
    @Column(name = "id_province")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_province;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
