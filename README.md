# HitCommand
Hit Command is a Minecraft plugin for Bukkit.
Request: (https://bukkit.org/forums/plugin-requests.96/)
***

### Config Format:
```
'1':
    type:
    displayname:
    lore:
        -
        -
    command:
    chance:
    deathChance:

```

### Commands
`/hitcommand <Command> <Chance> <DeathChance>`

The variables `%player%` and `%victim%` can be used in place of the player's names. Prefix the command with `p:` if you wish for the player to execute this command rather than the console (the player will need the correct permissions)

This command adds the current weapon in the player's main hand to the config with the specified arguments
##### Requires: `hitcommand.addweapon`
