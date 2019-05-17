package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.User;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.CategoriesResponse;
import com.sharefood.ShareFood.response.extend.UserResponse;
import com.sharefood.ShareFood.response.extend.UsersResponse;
import com.sharefood.ShareFood.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class UserController extends AbstractController {

    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public ResponseEntity<Response> login(@RequestParam("username") String username, @RequestParam("pass") String pass){
        User user = userService.findUserByUserNameAndPass(username, pass);
        return responseData(new UserResponse(user));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/all")
    public ResponseEntity<Response> login(@RequestParam("permission") int permission, @RequestParam("lock") int lock){
        List<User> users = userService.findAllByPermission(permission);
        return responseData(new UsersResponse(users));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/users")
    public ResponseEntity<Response> update(@RequestBody User user){
        return responseData(new UserResponse(userService.update(user)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public ResponseEntity<Response> insert(@RequestBody User user){
        return responseData(new UserResponse(userService.insert(user)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/get")
    public ResponseEntity<Response> update(@RequestParam("id_user") int id_user){
        return responseData(new UserResponse(userService.findUserById(id_user)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/forgot")
    public ResponseEntity<Response> forgot(@RequestParam("user_name") String user_name){
        return responseData(new UserResponse(userService.forgotPassword(user_name)));
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/users_all")
//    public ResponseEntity<Response> getAll(){
//        List<User> users = userService.findAll();
//        return responseData(new UsersResponse(users));
//    }

}
