package com.nexus.backend.controller;


import com.nexus.backend.dto.UpdateUserRequest;
import com.nexus.backend.entity.User;
import com.nexus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws Exception {

        User user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<User> searchUserByEmail(@PathVariable String email){
        User  user = userService.searchUser(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUserHandler(@RequestBody UpdateUserRequest request, @RequestHeader("Authorization") String token) throws Exception {

        System.out.println("Update Request");
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(), request);
        System.out.println("Updating user " + user.getUsername());
        String response = "User updates successfully";

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
