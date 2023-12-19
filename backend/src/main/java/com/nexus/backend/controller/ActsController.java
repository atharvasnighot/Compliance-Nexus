package com.nexus.backend.controller;

import com.nexus.backend.dto.UserTender;
import com.nexus.backend.entity.Act;
import com.nexus.backend.entity.User;
import com.nexus.backend.repository.ActRepository;
import com.nexus.backend.service.ActsService;
import com.nexus.backend.service.AiService;
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

    @Autowired
    private AiService aiService;

    @PostMapping("/create")
    public ResponseEntity<Act> createAct(@RequestBody Act newAct, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfile(jwt);
        if (user.getIsAdmin() == false){
            return new ResponseEntity<>(newAct, HttpStatus.BAD_REQUEST);
        }
        Act createdAct = actsService.createAct(newAct, user.getId());
        return new ResponseEntity<>(createdAct, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{actId}")
    public ResponseEntity<String> deleteAct(@PathVariable Integer actId) {
        actsService.deleteAct(actId);
        return new ResponseEntity<>("Act deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Act>> searchActs(@PathVariable String searchString) {
        List<Act> result = actsService.searchActs(searchString);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{actId}")
    public ResponseEntity<Act> getActById(@PathVariable Integer actId) {
        Act result = actRepository.findById(actId).get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Act> getAllActs() {
        List<Act> allActs = actRepository.findAllByOrderByDateDesc();
        return allActs;
    }

    @GetMapping("/by")
    public ResponseEntity<List<Act>> getAllActsByUserId(@RequestHeader("Authorization") String jwt) throws Exception {

        Integer userId = userService.findUserProfile(jwt).getId();
        List<Act> allActs = actRepository.findAllByUploaderId(userId);

        if(allActs == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allActs, HttpStatus.OK);
    }

    @GetMapping("/check/{actId}")
    public ResponseEntity<String> checkIfTenderIsCompliant(@PathVariable Integer actId, @RequestBody UserTender userTender){

        Act act = actRepository.findById(actId).get();
        if (act == null)
            return new ResponseEntity<>("Act not found", HttpStatus.NOT_FOUND);

        String response = aiService.checkIfCompliant(act, userTender);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

