package vttp2022.proj.pokemonTeam.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vttp2022.proj.pokemonTeam.model.Pokemon;
import vttp2022.proj.pokemonTeam.model.Trainer;
import vttp2022.proj.pokemonTeam.service.PokeService;

@Controller
@RequestMapping(path = "/")
public class htmlController {
    
    private static final Logger logger = LoggerFactory.getLogger(htmlController.class);
    
    @Autowired
    PokeService PokeSvc;

    // Trainer currentTrainer = new Trainer();
    // public Pokemon reqPoke;
    // public Pokemon[] reqPokeArr;
    //initial page
    @GetMapping(path = {"/home","/"})
    public String startPage(Model model){
        model.addAttribute("Trainer", new Trainer());
        return "startPage";
    }

    //User login
    @GetMapping(path = "/user")
    public String login(Model model, @ModelAttribute Trainer trainer) {
        Trainer currentTrainer = PokeSvc.getRedisTrainer(trainer.getTrainerName());
        logger.info("Retrieval of "+currentTrainer.getTrainerName()+"'s team completed.");
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }
    
    //Search for Pokemon //TODO future change to restcon or mouseout function
    @PostMapping(path = "/{username}/search")
    public String searchPage(Model model, @ModelAttribute Trainer modelTrainer, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        //special case to jump out of sequence in the event user tried to open info on "Your team is empty" object in the user team page
        if(modelTrainer.getSearchPokeString().toLowerCase().equals("your team is empty")){
            return "userTeam";
        }
        logger.info("SEARCH TYPE >>>"+modelTrainer.getSearchPokeCatString());
        switch (modelTrainer.getSearchPokeCatString()) {
            case "Name/ID":
                Optional<Pokemon> request = PokeService.getApiPokemon(modelTrainer.getSearchPokeString());
                if(request.isEmpty()){
                    return "error";
                }
                currentTrainer.reqPoke = request.get();
                logger.info(currentTrainer.reqPoke.getName()+currentTrainer.reqPoke.getID());
                model.addAttribute("reqTrainer", currentTrainer);
                return "showInfo";        
            case "Type":
                Optional<Pokemon[]> requestArr = PokeSvc.getRecommendedPokemons(modelTrainer.getSearchPokeString());
                if(requestArr.isEmpty()){
                    return "error";
                }
                currentTrainer.pokeSearchArrPoke = requestArr.get();
                currentTrainer.pokeSearchArrPoke[0].getName();
                model.addAttribute("reqTrainer", currentTrainer);
                return "pokeSearchList";
        }
        Optional<Pokemon> request = PokeService.getApiPokemon(modelTrainer.getSearchPokeString());
        if(request.isEmpty()){
            return "error";
        }
        Pokemon reqPoke = request.get();
        logger.info(reqPoke.getName()+reqPoke.getID());
        model.addAttribute("reqPoke", reqPoke);
        return "showInfo";
    }
    
    @GetMapping(path = "/{username}/search/{pokeName}")
    public String searchPage(Model model, @PathVariable String pokeName, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        Optional<Pokemon> request = PokeService.getApiPokemon(pokeName);
        if(request.isEmpty()){
            return "error";
        }
        currentTrainer.reqPoke = request.get();
        logger.info(currentTrainer.reqPoke.getName());
        model.addAttribute("reqTrainer", currentTrainer);
        model.addAttribute("reqPoke", currentTrainer.reqPoke);
        return "showInfo";
    }

    //add pokemon to team
    @GetMapping(path = "/{username}/add/{reqPoke}")
    public String addPoke(Model model, @PathVariable String username, @PathVariable Pokemon reqPoke){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        currentTrainer = PokeSvc.addPoketoTeam(reqPoke, currentTrainer);
        logger.info("CONTROLLER POKEMON ADDED TO TEAM");
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    //do not add pokemon to team
    @GetMapping(path = "/{username}/back")
    public String noAddPoke(Model model, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @GetMapping(path="/{username}/moveUp/{index}")
    public String movePokeUp(Model model, @PathVariable String index, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        logger.info("Index to change >>> "+index);
        currentTrainer = PokeSvc.moveUpPokemon(currentTrainer, index);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @GetMapping(path="/{username}/moveDown/{index}")
    public String movePokeDown(Model model, @PathVariable String index, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        currentTrainer = PokeSvc.moveDownPokemon(currentTrainer, index);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @GetMapping(path="/{username}/delete/{index}")
    public String deletePoke(Model model, @PathVariable String index, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        currentTrainer = PokeSvc.deletePokemon(currentTrainer, index);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @PostMapping(path="/{username}/addMultiple")
    public String addMultiplePoke(Model model, @ModelAttribute Trainer reqTrainer, @PathVariable String username){
        Trainer currentTrainer = PokeSvc.getRedisTrainer(username);
        logger.info(reqTrainer.getTrainerName());
        logger.info(reqTrainer.pokeSearchArrPoke[0].getName());
        logger.info(String.valueOf(reqTrainer.pokeSearchArrPoke[0].isChecked()));
        logger.info(reqTrainer.pokeSearchArrPoke[1].getName());
        logger.info(String.valueOf(reqTrainer.pokeSearchArrPoke[1].isChecked()));
        for(Pokemon poke :reqTrainer.pokeSearchArrPoke){
            if(poke.checked){
                poke.setChecked(false);
                currentTrainer = PokeSvc.addPoketoTeam(poke, currentTrainer);
            }
        }
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    // @ExceptionHandler
    // public String errorPage(){
    //     return "redirect";
    // }
}
