# Tips [![](http://cf.way2muchnoise.eu/306549.svg)](https://minecraft.curseforge.com/projects/306549) [![](http://cf.way2muchnoise.eu/versions/306549.svg)](https://minecraft.curseforge.com/projects/306549)
Tips is a Minecraft mod that adds tips to some of the loading screens. 

[![Nodecraft](https://nodecraft.com/assets/images/logo-dark.png)](https://nodecraft.com/r/darkhax)
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

## Adding Tip

If you are making a modpack, you can can add new tips by editing the `config/tips.cfg` file. Each line in the customTips section will be read as a new tip that can be displayed. 

```
    # A list of custom tips added by the user or modpack. [default: ]
    S:customTips <
        This line of text will show up as a new tipe.
        This line of text is another seperate tip.
		This tip will have a custom title in game.#SPLIT#Custom TIP
     >
```

If you are a mod author and would like to add tips to your mod, you can simply add `mods.tips.modid.1=Your tip here.` to your lang file. This mod can automatically detect when mods have defined custom tooltips and will load them without you having to add any code! Your mod can add as many tooltips as you want, just make a new entry to the lang file with an increased number at the end. Please note that the detection code will stop detecting new tips if the next number in the sequence can not be found. For example if you have tips for the numbers 1, 2, 4, and 5, only tips 1 and 2 will be loaded because tip 3 can not be found. Modpack authors can disable all tips detected this way by setting `allowDefaultTips` to false in the config file.

## Mod API

If you want to do something a bit more complex, you can interact with the system directly using the mod API. This mod provides the `net.darkhax.tips.TipsAPI` class, which allows you to do things like add/remove tips, or even render tips on other GUIs. 

The list of tips available is built using a series of string list consumers. First all of the adders are invoked to generate the list of tips, and then all the removers are ran which allows for the list to be cleaned up. This system uses functional interfaces, lambdas, and method references. The system was designed this way to allow the tips list to be rebuilt/reloaded. If you don't know how to use those, this mod is actually very simple and can help you learn. Here are a few examples of how you can do things. 

Basic Lambda

```java
// Single line entry.
TipsAPI.addTips(tips -> tips.add("Hello World"));

// Multiple line entry.
TipsAPI.addTips(tips -> {           
    for (int i = 0; i < 10; i++) {               
        tips.add("Random Tip #" + i);
    }
});
```

Method Reference


```java
TipsAPI.addTips(YourClass::addTips);

public static void addTips(List<String> tips) {
    tips.add("Hello World");
    tips.add("Another Tip");
}
```

New class

```java
TipsAPI.addTips(new TipsPlugin());

public class TipsPlugin implements Consumer<List<String>> {

    @Override
    public void accept (List<String> tips) {
        tips.add("Hello World");
    }
}
```

# Maven Info
Coming soon!