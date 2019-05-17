package com.sharefood.ShareFood.response.extend;

import com.sharefood.ShareFood.model.Manager;
import com.sharefood.ShareFood.response.base.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManagersResponse extends AbstractResponse {
    private List<Manager> managers;
}
