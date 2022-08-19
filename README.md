# Current Status
**Login Page:** Able to display. Not Styled
**User's Team Page:** Able to retrieve username from redis and login, display, username, search box for pokemon usable, pokemon team can be displayed. Not styled.
**Pokemon Search Result Page:** Able to display, show searched pokemon and all stats, able to add to team. Back function not tested, missing recommendation function, not styled. To test if team has 6 pokemon, can add function work. 

# URGENT TODOS
**RESOLVED_Unable to retrieve values from keys from redis, consider changing serializer to JDK object instead of json_**

# NOTES
**_Remove redis password and change code from @Value to Sys env in config and application properties when pushing._**


# Sample Api Result (desired only)
{
    "id": 132,
    "name": "ditto",
    "stats": [
        {
            "base_stat": 48,
            "effort": 1,
            "stat": {
                "name": "hp",
                "url": "https://pokeapi.co/api/v2/stat/1/"
            }
        },
        {
            "base_stat": 48,
            "effort": 0,
            "stat": {
                "name": "attack",
                "url": "https://pokeapi.co/api/v2/stat/2/"
            }
        },
        {
            "base_stat": 48,
            "effort": 0,
            "stat": {
                "name": "defense",
                "url": "https://pokeapi.co/api/v2/stat/3/"
            }
        },
        {
            "base_stat": 48,
            "effort": 0,
            "stat": {
                "name": "special-attack",
                "url": "https://pokeapi.co/api/v2/stat/4/"
            }
        },
        {
            "base_stat": 48,
            "effort": 0,
            "stat": {
                "name": "special-defense",
                "url": "https://pokeapi.co/api/v2/stat/5/"
            }
        },
        {
            "base_stat": 48,
            "effort": 0,
            "stat": {
                "name": "speed",
                "url": "https://pokeapi.co/api/v2/stat/6/"
            }
        }
    ],
    "types": [
        {
            "slot": 1,
            "type": {
                "name": "normal",
                "url": "https://pokeapi.co/api/v2/type/1/"
            }
        }
    ]
}