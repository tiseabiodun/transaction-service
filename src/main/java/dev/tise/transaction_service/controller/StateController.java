package dev.tise.transaction_service.controller;

import dev.tise.transaction_service.service.StateService;
import dev.tise.transaction_service.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/state")
public class StateController {

    @Autowired
    private StateService stateService;

    @GetMapping("/get-lga")
    public ResponseEntity<List<String>> localGovernments(@RequestParam String state){

        return ResponseUtils.mapResponse(stateService.getLGAs(state));

    }
}
