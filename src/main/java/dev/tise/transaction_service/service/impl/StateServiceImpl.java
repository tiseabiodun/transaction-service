package dev.tise.transaction_service.service.impl;

import dev.tise.transaction_service.service.StateService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private static final String STATE_URL =  "https://nga-states-lga.onrender.com/";

    @Override
    public List<String> getLGAs(String state) {

        if (state == null || state.trim().isEmpty()){
            return new ArrayList<>();
        }

        RestTemplate restTemplate = new RestTemplate();

        String url = STATE_URL.concat("fetch");;
        ResponseEntity<List>response = restTemplate.getForEntity(url,List.class);
        if (response.hasBody()){
            List<String> listOfStates = response.getBody();
            if (listOfStates.contains(state)){
                System.err.println(listOfStates);

                String lgaUrl = STATE_URL.concat("?state=").concat(state);

               var localGovernments =  restTemplate.getForEntity(lgaUrl, List.class);

               if (localGovernments.hasBody()){
                   return localGovernments.getBody();
               }


            }else {
                System.err.println("Invalid state ::: "+state);
            }
        }

        return List.of();
    }
}
