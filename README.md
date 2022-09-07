# Current Status
**Login Page:** Able to display. Styled
**User's Team Page:** Able to retrieve username from redis and login, display, username, search box for pokemon usable, pokemon team can be displayed, each pokemon in team can be clicked to display info, able to sort up and down, delete. Partially styled, considering adding images in, tables need to have better sizing.
**Pokemon Search Result Page:** Able to display, show searched pokemon and all stats, able to add to team, add does nothing if team has 6 pokemon already, back button working. Missing recommendation function, partially styled, table need better sizing.

# URGENT TODOS
**RESOLVED _Unable to retrieve values from keys from redis, consider changing serializer to JDK object instead of json_**
**RESOLVED _Inital creation of team has an empty object holding the first index of the array._**
**RESOLVED _When user's team has 6 pokemon, adding another would cause team to be reset._**
**RESOLVED _!!!When team has duplicate pokemon, shifting and deleting will always only affect first of the same name pokemon - add hidden index input, change mapping to post for controller, change movement and deletion from finding name to finding index_**
**Unable to find solution, background image put in userTeam and showInfo unable to stay because somehow sent to request handler as input**

# NOTES
**_Remove redis password and change code from @Value to Sys env in config and application properties when pushing._**
**POKEMON API ONLY HAS INFO UP UNTIL POKEMON ID 898**
**_Unknown bug, unable to find why sometimes id is not sent to view properly_**


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