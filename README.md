# Predicate Custom Models

A client side mod to enable swapping of built-in models and entity models based on conditions using a resource pack. This is not a replacement for changing item models in your inventory, that is possible using default resource packs. This is meant to build on top of the vanilla systems and add more customizations completely optional of the people on a server.

This is meant as an option for things such as changing out how your trident looks in hand or having a custom helmet model based on an NBT tag.

This is in early alpha stages with limited options, but the basic premise is here and will be expanded on.

The idea comes from Optifines custom entity models and took reference from Fabric [CEM](https://www.curseforge.com/minecraft/mc-mods/custom-entity-models-cem) but these are not compatible.
I decided to take my own model formats and write my own system to handle conditions.
## How to:
1. In your resource pack, make a folder for `assets/robotmonkey/cem` Inside this folder is where you will put your file that contain conditions.
2. Create a file inside the above folder called `entityname.rem` (eg. trident.rem) inside here you will have some conditions and the models that each condition points to. Check the example file on the [GitHub](https://github.com/robotmonkey1000/PredicateEntityModels/tree/main/Test_Resource_Pack)
3. Create a folder to hold your custom models e.g. `assets/minecraft/models/custom`. You can call this folder whatever you want, as long as your conditions reference above point there.
4. In here add your model files. Right now there is no easy way to create these, I have been hand making them my self from exports of Blockbench models. If someone would like to help create a Blockbench plugin please reach out.
5. Create a folder to hold your custom textures e.g. `assets/minecraft/textures/item`.
6. In the above folder add your textures that your models reference.
7. Reload your resource pack and as long as the above was done correctly you should now have your custom models.

## Currently Supported 
1. Trident Models in Hand
2. Trident Models in world (With limitations, check below)
3. Iron/Gold/Diamond/Netherite helmets.


## Limitations:
Currently, as far as I can tell, the NBT data of a trident is not synced to clients once it has been thrown. So, if you want to render a trident in the world differently than a normal trident there is limited conditions to change how it looks.

As an example, using the NBT tag for CustomModelData you can have multiple tridents with different models while you are holding them, but once you throw them, you have to assign a condition for the Owner being your username and have it render as a specific model if you threw it.

## Compatibility

I cannot guarantee compatibility with other mods as of right now. I am trying to write this in a way such that it should not cause any issues, but any mods that change how things render might be incompatible.

If you encounter any specific issues, please open an issue on the GitHub and I will look into it.

## TODO

1. Add more armor pieces
   1. This involves making sure that each piece has their proper named groups for making them move with the body.
2. Consider other entities.
3. Write a plugin for Blockbench to export the models to the custom format.
4. Optimize model creation
   1. Right now each condition creates a model. If multiple conditions reference the same model it will be built and stored multiple times.
   2. Having a registry of conditions based on the Identifier will allow for reference to already created models.
5. Create Wiki with possible conditions to be checked and examples.
