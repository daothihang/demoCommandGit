package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Category;
import com.sharefood.ShareFood.model.Manager;
import com.sharefood.ShareFood.model.User;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.*;
import com.sharefood.ShareFood.service.base.ManagerService;
import com.sharefood.ShareFood.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class ManagerController extends AbstractController {

    private ManagerService managerService;
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/managers")
    public ResponseEntity<Response> getAll(){
        List<Manager> managers = (List<Manager>) managerService.gettAll();
        return responseData(new ManagersResponse(managers));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/managers")
    public ResponseEntity<Response> insert(@RequestBody Manager manager){
        return responseData(new ManagerResponse(managerService.insert(manager)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/managers")
    public ResponseEntity<Response> update(@RequestBody Manager manager){
        return responseData(new ManagerResponse(managerService.insert(manager)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/managers")
    public ResponseEntity<Response> delete(@RequestParam("id_manager") int id_manager){
        managerService.delete(id_manager);
        return responseData(new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/managers/get")
    public ResponseEntity<Response> insert(@RequestParam("id_user") int id_user){
        User user = userService.findUserById(id_user);
        return responseData(new ManagerResponse(managerService.findManagerByUser(user)));
    }
}
