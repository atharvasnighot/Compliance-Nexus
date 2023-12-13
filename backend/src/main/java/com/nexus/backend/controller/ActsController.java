package com.nexus.backend.controller;

import com.nexus.backend.entity.Act;
import com.nexus.backend.entity.User;
import com.nexus.backend.repository.ActRepository;
import com.nexus.backend.service.ActsService;
import com.nexus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/act")
public class ActsController {

    @Autowired
    private ActsService actsService;

    @Autowired
    private ActRepository actRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Act> createAct(@RequestBody Act newAct, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfile(jwt);
        Act createdAct = actsService.createAct(newAct, user.getId());
        return new ResponseEntity<>(createdAct, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{actId}")
    public ResponseEntity<String> deleteAct(@PathVariable Integer actId) {
        actsService.deleteAct(actId);
        return new ResponseEntity<>("Act deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Act> getAllActs() {
        List<Act> allActs = actRepository.findAllByOrderByDateDesc();
        return allActs;
    }
}

