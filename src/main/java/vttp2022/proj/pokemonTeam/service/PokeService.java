package vttp2022.proj.pokemonTeam.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vttp2022.proj.pokemonTeam.model.Pokemon;
import vttp2022.proj.pokemonTeam.model.Trainer;

@Service
public class PokeService {
    private static final Logger logger = LoggerFactory.getLogger(PokeService.class);

    @Autowired
    @Qualifier("PokeRedis")
    RedisTemplate<String, Trainer> redisTemplate;
    
    public Pokemon reqPoke;

    String overallURL = "https://pokeapi.co/api/v2/pokemon";
    String userInputUsername;
    Trainer currentTrainer;

    //login to existing user or create new user in Redis
    public Trainer getRedisTrainer(String username){
        userInputUsername = username;
        boolean existingTrainer=redisTemplate.hasKey(username);
        logger.info(username + " is EXISTING TRAINER>>> "+ existingTrainer);
        if (!existingTrainer){
            currentTrainer = new Trainer(username);
            redisTemplate.opsForValue().setIfAbsent(username, currentTrainer);
        }else{
            currentTrainer = (Trainer) redisTemplate.opsForValue().get(username);//TODO username mapped correctly, check poketeam mapping when add done
            logger.info("CURRENT LOGGED IN TRAINER>>> "+currentTrainer.getTrainerName());
            if(null!=currentTrainer.getPokeArrString()){ //
                logger.info("SIZE OF TRAINER TEAM >>> "+ currentTrainer.getPokeArrString().length);
            }
        }
        return currentTrainer;
    }

    //connect to api and unmarshal pokemon info
    public Optional<Pokemon> getApiPokemon(String searchName){
        String reqPokeUrl = overallURL + "/" +searchName.toLowerCase();
        RestTemplate templatePoke = new RestTemplate();
        ResponseEntity<String> resp = null;
        try {
            resp = templatePoke.exchange(reqPokeUrl, HttpMethod.GET, null, String.class, 1);
            Pokemon reqPoke = Pokemon.createJson(resp.getBody());
            logger.info("WITHIN SERVICE>>> "+reqPoke.getStatsMap().get("hp"));
            return Optional.of(reqPoke);
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //add pokemon to team, update redis
    public Trainer addPoketoTeam(Pokemon pokeToAdd, Trainer currentTrainer){
        Trainer trainerToUpdate = currentTrainer;
        String[] newTeam = new String[1];
        if(null!=currentTrainer.getPokeArrString()){
            if(currentTrainer.getPokeArrString()[0].equals("Your Team is empty")){
                newTeam[0]=pokeToAdd.getName();
                trainerToUpdate.setPokeArrString(newTeam);
                redisTemplate.opsForValue().set(trainerToUpdate.getTrainerName(), trainerToUpdate);
                return trainerToUpdate;
            }
            if(currentTrainer.getPokeArrString().length<6){
                newTeam = new String[currentTrainer.getPokeArrString().length +1];
                for (int i=0; i< currentTrainer.getPokeArrString().length; i++){
                    newTeam[i]=currentTrainer.getPokeArrString()[i];
                }
                newTeam[newTeam.length-1]=pokeToAdd.getName();    
                trainerToUpdate.setPokeArrString(newTeam);
                redisTemplate.opsForValue().set(trainerToUpdate.getTrainerName(), trainerToUpdate);
                return trainerToUpdate;
            }
        }
        return trainerToUpdate;
    }
    
    //move pokemon up in team, update redis
    public Trainer moveUpPokemon(Trainer currentTrainer, String pokeName){
        Trainer trainerToUpdate = currentTrainer;
        String[] newTeam = trainerToUpdate.getPokeArrString();
        int lowerPokeIndex = Arrays.asList(newTeam).indexOf(pokeName);
        int upperPokeIndex = lowerPokeIndex-1;
        if(lowerPokeIndex>0){
            String oldLowerPoke = newTeam[lowerPokeIndex];
            String oldUpperPoke = newTeam[upperPokeIndex];
            newTeam[upperPokeIndex]=oldLowerPoke;
            newTeam[lowerPokeIndex]=oldUpperPoke;
            trainerToUpdate.setPokeArrString(newTeam);
        }
        redisTemplate.opsForValue().set(trainerToUpdate.getTrainerName(), trainerToUpdate);
        return trainerToUpdate;
    }

    //move pokemon down in team, update redis
    public Trainer moveDownPokemon(Trainer currentTrainer, String pokeName){
        Trainer trainerToUpdate = currentTrainer;
        String[] newTeam = trainerToUpdate.getPokeArrString();
        int upperPokeIndex = Arrays.asList(newTeam).indexOf(pokeName);
        int lowerPokeIndex = upperPokeIndex+1;
        if(upperPokeIndex<5){
            String oldUpperPoke = newTeam[upperPokeIndex];
            String oldLowerPoke = newTeam[lowerPokeIndex];
            newTeam[lowerPokeIndex]=oldUpperPoke;
            newTeam[upperPokeIndex]=oldLowerPoke;
            trainerToUpdate.setPokeArrString(newTeam);
        }
        redisTemplate.opsForValue().set(trainerToUpdate.getTrainerName(), trainerToUpdate);
        return trainerToUpdate;
    }

    //delete pokemon, update redis
    public Trainer deletePokemon(Trainer currentTrainer, String pokeName){
        Trainer trainerToUpdate = currentTrainer;
        List<String> oldTeamList = Arrays.asList(trainerToUpdate.getPokeArrString());
        int delIndex = oldTeamList.indexOf(pokeName);
        logger.info("Delete request >>> "+pokeName+"|||del IDX >> "+delIndex+"||| deleting >>> "+oldTeamList.get(delIndex));
        List<String> newTeam= new ArrayList<>();
        String[] newTeamArr = new String[1];
        if(oldTeamList.size()>1){
            for(int i=0;i<oldTeamList.size();i++){
                if(i!=delIndex){
                    newTeam.add(oldTeamList.get(i));
                }
            }
            newTeamArr = new String[newTeam.size()];
            for(int i=0;i<newTeam.size();i++){
                newTeamArr[i]=newTeam.get(i);
            }
        }else{
            newTeamArr[0] = "Your Team is empty";
        }
        trainerToUpdate.setPokeArrString(newTeamArr);
        redisTemplate.opsForValue().set(trainerToUpdate.getTrainerName(), trainerToUpdate);
        return trainerToUpdate;
    }
}
