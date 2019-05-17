package com.sharefood.ShareFood.controller;

import com.sharefood.ShareFood.model.Category;
import com.sharefood.ShareFood.response.base.Response;
import com.sharefood.ShareFood.response.extend.BooleanResponse;
import com.sharefood.ShareFood.response.extend.CategoriesResponse;
import com.sharefood.ShareFood.response.extend.CategoryResponse;
import com.sharefood.ShareFood.service.base.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class CategoryController extends AbstractController {

    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, value = "/categories")
    public ResponseEntity<Response> getAll(){
        List<Category> categories = categoryService.findAll();
        return responseData(new CategoriesResponse(categories));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/categories")
    public ResponseEntity<Response> insert(@RequestBody Category category){
        return responseData(new CategoryResponse(categoryService.insert(category)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/categories")
    public ResponseEntity<Response> update(@RequestBody Category category){
        return responseData(new CategoryResponse(categoryService.insert(category)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/categories")
    public ResponseEntity<Response> delete(@RequestParam("id_category") int id_category){
        categoryService.delete(id_category);
        return responseData(new BooleanResponse(true));
    }
}
