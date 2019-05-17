package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Province;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.ProvincesResponse;
import com.sharefood.ShareFood.service.base.ProvinceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class ProviceController extends AbstractController {

    private ProvinceService provinceService;

    @RequestMapping(method = RequestMethod.GET, value = "/provinces")
    public ResponseEntity<Response> getAll(){
        List<Province> provinces = provinceService.findAll();
        return responseData(new ProvincesResponse(provinces));
    }
}
