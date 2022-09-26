package vttp2022.proj.pokemonTeam.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    Trainer currentTrainer = new Trainer();
    public Pokemon reqPoke;

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
        logger.info("Retrieval of "+currentTrainer.getTrainerName()+"'s team completed.");
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }
    
    //Search for Pokemon //TODO future change to restcon or mouseout function
    @PostMapping(path = "/search")
    public String searchPage(Model model, @ModelAttribute Trainer modelTrainer){
        if(modelTrainer.getSearchPokeString().equals("")){ //TODO check if enter blank will return error page
            return "error";
        }
        if(modelTrainer.getSearchPokeString().toLowerCase().equals("your team is empty")){ //TODO check if click on empty will return correct original team, else change to conditioning in html
            return "userTeam";
        }
        reqPoke = PokeSvc.getApiPokemon(modelTrainer.getSearchPokeString()).get();
        logger.info(reqPoke.getName()+reqPoke.getID());
        model.addAttribute("reqPoke", reqPoke);
        return "showInfo";
    }
    
    @GetMapping(path = "/search/{pokeName}")
    public String searchPage(Model model, @PathVariable String pokeName){
        reqPoke = PokeSvc.getApiPokemon(pokeName).get();
        logger.info(reqPoke.getName());
        model.addAttribute("reqPoke", reqPoke);
        return "showInfo";
    }

    //add pokemon to team
    @GetMapping(path = "/add")
    public String addPoke(Model model){
        currentTrainer = PokeSvc.addPoketoTeam(reqPoke, currentTrainer);
        logger.info("CONTROLLER POKEMON ADDED TO TEAM");
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    //do not add pokemon to team
    @GetMapping(path = "/back")
    public String noAddPoke(Model model){
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @GetMapping(path="/moveUp/{index}")
    public String movePokeUp(Model model, @PathVariable String index){
        logger.info("Index to change >>> "+index);
        currentTrainer = PokeSvc.moveUpPokemon(currentTrainer, index);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @GetMapping(path="/moveDown/{index}")
    public String movePokeDown(Model model, @PathVariable String index){
        currentTrainer = PokeSvc.moveDownPokemon(currentTrainer, index);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @GetMapping(path="/delete/{index}")
    public String deletePoke(Model model, @PathVariable String index){
        currentTrainer = PokeSvc.deletePokemon(currentTrainer, index);
        model.addAttribute("Trainer", currentTrainer);
        return "userTeam";
    }

    @ExceptionHandler
    public String errorPage(){
        return "error";
    }
}
