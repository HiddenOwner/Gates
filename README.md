This plugin was originally created by Tyrotoxism and updated to current version by HiddenOwner Geekz Multimedia

commands:

    gates:
        description: Used to modify gate signs to your liking
        usage: |
            /gates owner <player>
            /gates type <type>
            /gates redstone <ON|OFF|TOGGLE>
permissions:

    gates.player:
        description: Default gate permissions
            children:
                gates.create: gives player permission to create gates
                gates.use.self: gives player permission to use their own gates
                gates.modify.self: gives player permission to modify their own gates
                gates.destroy.self: gives player permission to destroy their own gates         
    gates.all: 
        description: Gives all gate permissions to everyone. Anyone can make, edit and destroy ALL gates
    gates.create:
        description: Gives player the ability to create gates
    gates.use.all:
        description: Gives player all use permissions
            children:
                gates.use.player.all: gives player permission to use ANYONES gates
                gates.use.player.<player>: gives player permission to use defined players gates
                gates.use.type.all: gives player permission to use ANY type of gate
                gates.use.type.<type>: gives player permission to use defined types of gates (ex. redstone)
                gates.use.self: gives player permission to use their own gates
                gates.use.others: gives players permission to use other peoples gates 
    gates.modify.all:
        description: Gives player all modify permissions
            children:
                gates.modify.player.all: gives player permission to modify ANYONES gates
                gates.modify.player.<player>: gives player permission to modify defined players gates
                gates.modify.type.all: gives player permission to modify ANY type of gate
                gates.modify.type.<type>: gives player permission to modify defined types of gates (ex. redstone)
                gates.modify.self: gives player permission to modify their own gates
                gates.modify.others: gives players permission to modify other peoples gates
    gates.destroy.all:
        description: Gives player all destroy permissions
            children:
                gates.destroy.player.all: gives player permission to destroy ANYONES gates
                gates.destroy.player.<player>: gives player permission to destroy defined players gates
                gates.destroy.type.all: gives player permission to destroy ANY type of gate
                gates.destroy.type.<type>: gives player permission to destroy defined types of gates (ex. redstone)
                gates.destroy.self: gives player permission to destroy their own gates
                gates.destroy.others: gives players permission to destroy other peoples gates
/n