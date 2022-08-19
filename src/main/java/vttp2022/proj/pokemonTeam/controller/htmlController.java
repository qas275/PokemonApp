package vttp2022.proj.pokemonTeam.controller;

import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    Trainer currentTrainer = new Trainer();
    public Pokemon reqPoke;
    List<String> tmp = new LinkedList<>();
    

    //initial page
    @GetMapping(path = "/")
    public String startPage(Model model){
        model.addAttribute("Trainer", new Trainer());
        return "startPage";
    }

    //User login
    @GetMapping(path = "/user")
    public String login(Model model, @ModelAttribute Trainer trainer) {
        currentTrainer = PokeSvc.getRedisTrainer(trainer.getTrainerName());
        logger.info(currentTrainer.getTrainerName());
        model.addAttribute("Trainer", currentTrainer);
        if(null!=currentTrainer.getPokeArrString()){
            for(String poke:currentTrainer.getPokeArrString()){
                tmp.add(poke);
            }
        }
        model.addAttribute("PokemonList",tmp);
        return "userTeam";
    }
    
    //Search for Pokemon //TODO change to restcon or mouseout function
    @PostMapping(path = "/search")
    public String searchPage(Model model, @ModelAttribute Trainer modelTrainer){
        reqPoke = PokeSvc.getApiPokemon(modelTrainer.getSearchPokeString()).get();
        logger.info(reqPoke.getName());
        model.addAttribute("reqPoke", reqPoke);
        return "showInfo";
    }

    //backup for get with pathvariable
    // @PostMapping(path = "/search/{pokeSearch}")
    // public String searchPage(Model model, @PathVariable String pokeName){
    //     reqPoke = PokeSvc.getApiPokemon(pokeName);
    //     model.addAttribute("reqPoke", reqPoke);
    //     return "userTeam";
    // }
    
    //add pokemon to team
    @GetMapping(path = "/add")
    public String addPoke(Model model){
        currentTrainer = PokeSvc.addPoketoTeam(reqPoke, currentTrainer);
        logger.info("CONTROLLER POKEMON ADDED TO TEAM");
        model.addAttribute("Trainer", currentTrainer);
        model.addAttribute("PokemonList",tmp);
        return "userTeam";
    }

    @GetMapping(path = "/back")
    public String noAddPoke(Model model){
        model.addAttribute("Trainer", currentTrainer);
        model.addAttribute("PokemonList",tmp);
        return "userTeam";
    }



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
