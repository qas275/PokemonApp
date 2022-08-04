package vttp2022.proj.pokemonTeam.controller;

import java.util.LinkedList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.proj.pokemonTeam.model.Pokemon;
import vttp2022.proj.pokemonTeam.model.Trainer;
import vttp2022.proj.pokemonTeam.service.PokeService;

@Controller
@RequestMapping(path = "/")
public class htmlController {
    Pokemon[] tmp;
    private static final Logger logger = LoggerFactory.getLogger(htmlController.class);
    @Autowired
    PokeService PokeSvc;
    Trainer currentTrainer;
    Pokemon reqPoke;

    @GetMapping(path = "/")
    public String startPage(Model model){
        model.addAttribute("Trainer", new Trainer());
        return "startPage";
    }
    
    @GetMapping(path = "/user")
    public String login(Model model, @ModelAttribute Trainer trainer) {
        Trainer currentTrainer = PokeSvc.getRedisTrainer(trainer.getTrainerName());
        logger.info(currentTrainer.getTrainerName());
        model.addAttribute("Trainer", currentTrainer);
        List<String> pokeList = new LinkedList<>();
        if(null!=currentTrainer.getPokeTeam()){
            for(Pokemon poke:currentTrainer.getPokeTeam()){
                pokeList.add(poke.getName());
            }
            currentTrainer.setPokeList(pokeList);
        }
        model.addAttribute("PokemonList",currentTrainer.getPokeList());
        return "userTeam";
    }
    
    @PostMapping(path = "/search")
    public String searchPage(Model model, @ModelAttribute Trainer modelTrainer){
        reqPoke = PokeSvc.getApiPokemon(modelTrainer.getSearchPoke());
        logger.info(reqPoke.getName());
        model.addAttribute("reqPoke", reqPoke);
        return "showInfo";
    }
    // @PostMapping(path = "/search/{pokeSearch}")
    // public String searchPage(Model model, @PathVariable String pokeName){
    //     reqPoke = PokeSvc.getApiPokemon(pokeName);
    //     model.addAttribute("reqPoke", reqPoke);
    //     return "userTeam";
    // }

    @GetMapping(path = "/add")
    public String addPoke(Model model){
        List<String> currentPokeTeam = currentTrainer.getPokeList();
        currentPokeTeam.add(reqPoke.getName());
        model.addAttribute("PokemonList",currentTrainer.getPokeList());
        return "userTeam";
    }


    // @Autowired
    // private berryService berrySvc;

    // @GetMapping(path = "/")
    // public String showPage(@RequestParam(required = true) String berryInput, Model model){

    //     //Optional<Berry> berry = berrySvc.getBerry(berryInput); 
    //     //if(berry.isempty())
    //     //    return "berryInfo";
    //     //model.addAttribute("berry", berry.get());
    //     return "berryInfo";
    // }

    // @GetMapping(path="/{berryInput}")
    // public String showPage(@PathVariable String berryInput){
    //     //Optional<Berry> berry = berrySvc.getBerry(berryInput); 
    //     //if(berry.isempty())
    //     //    return "berryInfo";
    //     //model.addAttribute("berry", berry.get());
    //     return "berryInfo";
    // }
}
