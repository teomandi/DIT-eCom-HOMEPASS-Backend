package com.exercise.mybnb.controller;

import com.exercise.mybnb.model.Rule;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.RuleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class RuleController {
    @Autowired
    RuleRepo ruleRepo;
    @Autowired
    PlaceRepo placeRepo;

    @GetMapping("/rule")
    public List<Rule> getAllRules(){
        return ruleRepo.findAll();
    }

    @GetMapping("/places/{pid}/rules")
    public Optional<Set<Rule>> getRulesByPlace(@PathVariable("pid") int pid){
        return ruleRepo.findByPlaceId(pid);
    }
    @PostMapping("/places/{pid}/rules")
    public Rule postRuleForPlace(@PathVariable("pid") int pid,
                                           Rule rule){
        System.out.println("Got rule : " + rule.toString());
        return placeRepo.findById(pid).map(place -> {
            System.out.println("found place: " + place.getId() + place.getAddress());
            rule.setPlace(place);
            return ruleRepo.save(rule);
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    @PostMapping("/places/{pid}/multi-rules")//not works.
    public ResponseEntity<?> postMultipleRulesForPlace(@PathVariable("pid") int pid,
                                                       List<Rule> rules){
        System.out.println("Got rules : " + rules.size());
        return placeRepo.findById(pid).map(place -> {
            System.out.println("found place: " + place.getId() + place.getAddress());
            for (Rule rule: rules ) {
                rule.setPlace(place);
                ruleRepo.save(rule);
            }
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PlaceID " + pid + " not found"));
    }

    @DeleteMapping("/places/{pid}/rules")
    public ResponseEntity<?> deleteRulesFromPlace(@PathVariable("pid") int pid){
        System.out.println("Deleting all rules from place " + pid);
        return placeRepo.findById(pid).map(place -> {
            System.out.println("Place found : " + place.getId() + " " + place.getAddress());
            Optional<Set<Rule>> placeRules = ruleRepo.findByPlaceId(pid);
            if(placeRules.isPresent())
                ruleRepo.deleteAll(placeRules.get());
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Places " + pid + " not found"));
    }

    @PutMapping("/rules/{rid}")
    public Rule editRule(@PathVariable("rid") int rid,
                                 Rule r){
        return ruleRepo.findById(rid).map(rule -> {
            rule.setContent(r.getContent());
            return ruleRepo.save(rule);
        }).orElseThrow(() -> new ResourceNotFoundException("RuleId " + rid + " not found"));
    }

    @DeleteMapping("/rules/{rid}")
    public ResponseEntity<?> deleteRule(@PathVariable("rid") int rid){
        return ruleRepo.findById(rid).map(rule -> {
            ruleRepo.delete(rule);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("RuleId " + rid + " not found"));
    }
}
