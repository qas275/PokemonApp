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
    Pokemon reqPoke;
    Pokemon[] tmp;
    

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
    
    //Search for Pokemon //TODO change to restcon or mouseout function
    @PostMapping(path = "/search")
    public String searchPage(Model model, @ModelAttribute Trainer modelTrainer){
        reqPoke = PokeSvc.getApiPokemon(modelTrainer.getSearchPoke()).get();
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
        if(null!=currentTrainer.getPokeTeam()){
            if(currentTrainer.getPokeTeam().length<6){
                Trainer updatedTrainer = PokeSvc.addPoketoTeam(reqPoke, currentTrainer);
                List<String> pokeList = updatedTrainer.getPokeList(); //TODO check if can just use an array instead of a linkedlist
                model.addAttribute("PokemonList",updatedTrainer.getPokeList());
                return "userTeam";
            }else{
                model.addAttribute("PokemonList",currentTrainer.getPokeList());
                return "userTeam";//TODO send user message team is full
            }

        }else{
            //TODO code for new pokemon added to team
        }
    }

    @GetMapping(path = "/back")
    public String noAddPoke(Model model){
        model.addAttribute("PokemonList",currentTrainer.getPokeList());
        return "userTeam";
    }

    //other code to use
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
