package vttp2022.proj.pokemonTeam.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.proj.pokemonTeam.model.Trainer;
import vttp2022.proj.pokemonTeam.service.PokeService;

@RestController
public class restApiController {
    
    @Autowired
    PokeService PokeSvc;

    private static final Logger logger = LoggerFactory.getLogger(restApiController.class);
    
    @GetMapping(path = "/username/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restJsonResponse (@PathVariable String username){
        logger.info("RESTCONT START");
        Optional<Trainer> response = PokeSvc.restConResponse(username);
        return ResponseEntity.ok(response.get());
    }
}
