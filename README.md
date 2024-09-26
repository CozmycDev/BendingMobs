[![GitHub Pre-Release](https://img.shields.io/github/release-pre/CozmycDev/BendingMobs.svg)](https://github.com/CozmycDev/BendingMobs/releases)
[![Github All Releases](https://img.shields.io/github/downloads/CozmycDev/BendingMobs/total.svg)](https://github.com/CozmycDev/BendingMobs/releases)
![Size](https://img.shields.io/github/repo-size/CozmycDev/BendingMobs.svg)

# BendingMobs Plugin

BendingMobs is a Spigot plugin that brings the elements from Avatar: The Last Airbender and Korra to Minecraft mobs. 

Originally created by [jedk1](https://github.com/philip-iv/ProjectKorraMobs), it is now maintained and updated by Cozmyc. This plugin allows mobs to bend elements and can even disguise them as players using LibsDisguises. 

Future updates will support custom mobs and models through MythicMobs and ModelEngine.

## Features

- **Elemental Abilities for Mobs**: Mobs can use elements like Air, Earth, Fire, and Water.
- **LibsDisguises Integration**: Mobs can be disguised as players with custom skins.
- **Planned Support for MythicMobs and ModelEngine**: Create and use custom mobs and models.
- **ProjectKorra Integration**: If ProjectKorra is present, BendingMobs will use sound and particle settings from ProjectKorra and allow players to block some mob abilities with their own.
- **Configurable Settings**: Adjust damage, attack frequency, swimming behavior, skin names, and more.
- **Ability to Fight Back**: Certain entities like villagers can be configured to fight back.
- **No Commands**: All settings are managed through the configuration file. Commands will be implemented soon.

## Installation

1. Download the latest BendingMobs jar from [releases](https://github.com/CozmycDev/BendingMobs/releases).
2. Place the jar in the `plugins` directory of your Minecraft server.
3. Restart the server to generate the default configuration file.
4. Modify the configuration file as needed and restart the server to apply changes.

## Configuration

Here is the default `config.yml` for BendingMobs:

```yaml
Properties:
  EntityTypes:
  - ZOMBIE
  BendFrequency: 75
  MovementSpeed: 0.3
  DoMobsSwim: true
  DenyOtherMobSpawns: false
  Air:
    NoFallDamage: true
  Avatar:
    Enabled: false
    Frequency: 150
  Entity:
    Villager:
      FightBack: true
  DisplayName:
    Avatar: Avatar
    Air: AirBender
    Earth: EarthBender
    Fire: FireBender
    Water: WaterBender
LibsDisguises:
  Enabled: false
  SkinName:
    Avatar: Avatar
    Air: AirBender
    Earth: EarthBender
    Fire: FireBender
    Water: WaterBender
Abilities:
  Air:
    Enabled: true
    AirBlast:
      Knockback: 2.0
    AirScooter:
      Duration: 1000
      Speed: 0.5
  Earth:
    Enabled: true
    EarthBlast:
      Damage: 6.0
    LavaBlast:
      Damage: 6.0
  Fire:
    Enabled: true
    FireBlast:
      Damage: 5.0
      FireTick: 2000
    FireJet:
      Duration: 1000
      Speed: 0.5
    Combustion:
      Damage: 6.0
      FireTick: 2000
    Lightning:
      Damage: 7.0
  Water:
    Enabled: true
    WaterBlast:
      Damage: 4.0
    IceBlast:
      Damage: 6.0
```

### Configuration Descriptions

- **EntityTypes**: List of entity types that can bend elements. 
- **BendFrequency**: Frequency of bending attacks. 75 would be a 1/75 chance of using an ability each tick.
- **MovementSpeed**: How fast the mobs move.
- **DoMobsSwim**: Forces mobs to swim, preventing mobs like zombies from sinking in water. 
- **DenyOtherMobSpawns**: Prevents spawning of other mobs, will only allow spawning the mobs configured to have an element.
- **Ability Settings**: Customize damage, duration, speed, and other attributes for each ability.
- **LibsDisguises**: Enable or disable disguises and set skin names for different elements.

## Future Plans

- **Support for MythicMobs and ModelEngine**: Create and use custom mobs and models.
- **Additional Abilities**: More abilities for mobs.
- **Enhanced Logic**: Improved AI and behavior for mobs.

## Dependencies

- **ProjectKorra**: Optional, for interaction with player abilities, sounds, and particles.
- **LibsDisguises**: Optional, for disguising mobs as players.

## Contribution

Feel free to contribute to the project by submitting issues, feature requests, or pull requests on the GitHub repository.
