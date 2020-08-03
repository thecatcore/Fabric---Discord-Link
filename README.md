# Fabric<->Discord Link

Fabric-Discord-Link is a lightweight server mod for the fabric mod loader which links your minecraft server and it's chat to a discord server via your very own discord bot.

## Features
* Configure a Chat channel to chat between your minecraft world and a discord channel
* Configure a Log channel to show server messages and admin actions
* Use commands to get live information on your Minecraft Servers status:
    * !status - Shows number of players that are online right now and the server's uptime
    * !uptime - Shows server's uptime
    * !playercount - Shows number of online players / server player maximum
    * !playerlist - Shows list of names of online players
* Configure custom messages for
    * Server starting
    * Server started
    * Server stopping
    * Server stopped
    * Player joining server
    * Player joining server with a different name to their known name
    * Player leaving server
    * Player making advancement
    * Player completing challenge
    * Player reaching goal
    * Prefix for death messages (i.e. emojis like :skull_crossbones:)
    * Postfix for death messages
* Configure Emojis
* Configure how messages from Discord appear in the ingame chat (default is senders name in brackets to distinguish from ingame player's messages)

## Installation

### Dependencies
Fabric-Discord-Link requires Fabric API to work. The [Fabric-API](https://github.com/FabricMC/fabric/releases) jar needs to be in your mod folder.

### Obtaining the binaries
The release binaries are available in the [releases](https://github.com/arthurbambou/Fabric---Discord-Link/releases) section or on [curseforge](https://www.curseforge.com/minecraft/mc-mods/fabric-discord-link). 

After downloading the latest version, just drop the jar into the mods folder.

### Building from source
1. Download the repository
2. Follow [this guide](https://fabricmc.net/wiki/tutorial:setup) to setup gradle and build the project
3. Drop the resulting jar into the fabric mod folder

### Acquiring and configuring a Discord Bot

1. Go to the [discord developer portal](https://discord.com/developers/applications) and register a new Application
2. Register a new Bot for the application in the `<Bot>` tab to the left
3. Toogle `<Server Members Intent>` to on under `<Priviledged Gateway Intents>` after creating a bot
4. In the `<OAuth2>` Tab to the left, select the `<Bot>` option under `<Scopes>` and select the options `<View Channels>` and `<Send Messages>` 
5. Copy the URL generated under `<Scopes>` and open it in a new tab to add the bot to your discord server

### Configuring Fabric-Discord-Link
1. Start your minecraft server once to let Fabric-Discord-Link generate it's default config `<fdlink.json>`
2. Turn your server back off
2. Copy your Bots Token from the Discord Developer Portal and paste it into the empty double quotes behind `<"token">` in the config
3. Obtain the channel IDs of the channels that you want your bot to function in (these can be from separate discord servers). To do this, you need to enable Discords Developer mode in your Discord Settings under `<Appearance>`. Afterwards you can right click channels and select `<Copy ID>` at the bottom
3. Paste the Discord Chat and/or Log Channel's IDs into the appropriate locations in the config. The IDs need to be in double quotes inside the brackets. If you have more than one Chat or Log Channel you need to separate the channel IDs inside the brackets with commas (no comma behind the last ID)
4. Configure the other settings in the config to your liking
5. Restart your server