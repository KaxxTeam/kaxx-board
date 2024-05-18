# KaxxBoard
Advanced Scoreboard Management API for your Minecraft Bukkit plugins.

*Tested Minecraft versions: 1.8.8*  
**You can use this as a library** (see [the docs](https://kaxx.gitbook.io/kaxxboard/))

## Features
* Easy to use
* Customizable
* Packet based
* Asynchronous

## Usage

### Initialize the handler
```java
final KaxxScoreboardHandler handler = KaxxScoreboardHandler.create(plugin);
```

### Create an adapter

```java
public final class ExampleAdapter implements KaxxScoreboardAdapter {

    @Override
    public String getTitle(final Player player) {
        return "Your Title!";
    }

    @Override
    public Collection<String> getLines(final Player player) {
        return List.of(
                "MyFirstLine",
                "MySecondLine",
                "MyThirdLine",
                "MyFourthLine"
        );
    }

}

```

## Docs
[Click here to read the docs on Gitbook](https://kaxx.gitbook.io/kaxxboard/)

## Download
You can download the latest version on the [releases page](https://github.com/KaxxTeam/kaxx-board/releases) on Github.

## Issues
If you have a problem with the API, or you want to request a feature, make an issue [here](https://github.com/KaxxTeam/kaxx-board/issues).

## Credits

Thanks to these people :

- [mstjr](https://github.com/mstjr) - *Adapting the original code into this library*
- [Vekooo](https://github.com/Vekooo) - *Providing the original code, and the idea*