package com.recipe.cookmaster.controller;

import com.recipe.cookmaster.entity.User;
import com.recipe.cookmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cookmaster")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    ResponseEntity<String> addUser(@RequestBody User user) {
        String response = userService.addUser(user);
        if(response == null)
            return new ResponseEntity<String>("Server error!", HttpStatus.INTERNAL_SERVER_ERROR);
        else if(response.startsWith("Error"))
            return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<String> loginUser(@RequestParam String name,
                                     @RequestParam String password) {
        String response = userService.verifyUser(name, password);
        if(response == null)
            return new ResponseEntity<String>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    ResponseEntity<User> getUserById(@RequestParam long userId) {
        User response = userService.getUser(userId);
        if(response == null)
            return new ResponseEntity<User>(new User(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<User>(response, HttpStatus.OK);
    }

    @GetMapping("/user/getId")
    ResponseEntity<Long> getUserIdByName(@RequestParam String name) {
        long response = userService.getUserId(name);
        if(response == -1)
            return new ResponseEntity<Long>(response, HttpStatus.NOT_FOUND);
        return new ResponseEntity<Long>(response, HttpStatus.OK);
    }

    @GetMapping("/users")
    ResponseEntity<List<User>> getUsers() {
        List<User> response = userService.getUsers();
        if(response == null)
            return new ResponseEntity<List<User>>(new ArrayList<User>(), HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<User>>(response, HttpStatus.OK);
    }

    @PutMapping("/editUser")
    ResponseEntity<String> editUser(@RequestParam long userId,
                                    @RequestParam(required = false) String pfpUrl,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String password) {
        User user = userService.getUser(userId);
        if(pfpUrl != null)
            user.setPfpUrl(pfpUrl);
        if(name != null)
            user.setName(name);
        if(password != null)
            user.setPassword(password);

        String response = userService.editUser(user);
        if(response == null)
            return new ResponseEntity<String>("Server error!", HttpStatus.INTERNAL_SERVER_ERROR);
        else if(response.startsWith("Error"))
            return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> editUser(@RequestParam long userId) {
        String response = userService.deleteUser(userId);
        if(response == null)
            return new ResponseEntity<String>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        else if(response.startsWith("Error"))
            return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
