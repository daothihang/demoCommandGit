package com.sharefood.ShareFood.response.extend;

import com.sharefood.ShareFood.model.User;
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
public class UsersResponse extends AbstractResponse {
    private List<User> users;
}
