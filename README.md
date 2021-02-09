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
    * Discord channel description
* Configure Emojis
* Configure how messages from Discord appear in the ingame chat (default is senders name in brackets to distinguish from ingame player's messages)

## Installation

### Dependencies

#### For Minecraft 1.14.4, 1.15.2 and any version higher than 1.16.1:
Fabric-Discord-Link requires Fabric API to work. The [Fabric-API](https://github.com/FabricMC/fabric/releases) jar needs to be in your mod folder.
#### For other Minecraft version (including snapshots):
No dependencies required.

### Obtaining the binaries
The release binaries are available on [curseforge](https://www.curseforge.com/minecraft/mc-mods/fabric-discord-link) and [modrinth](https://modrinth.com/mod/fabric-discord-link). 

After downloading the latest version, just drop the jar into the mods folder.

### Building from source
1. Download the repository
2. Follow [this guide](https://fabricmc.net/wiki/tutorial:setup) to setup gradle and build the project
3. Drop the resulting jar into the fabric mod folder

### Acquiring and configuring a Discord Bot

1. Go to the [discord developer portal](https://discord.com/developers/applications) and register a new Application
2. Register a new Bot for the application in the `<Bot>` tab to the left
3. Toggle `<Server Members Intent>` to on under `<Priviledged Gateway Intents>` after creating a bot
4. In the `<OAuth2>` Tab to the left, select the `<Bot>` option under `<Scopes>` and select the options `<View Channels>` and `<Send Messages>`
    - Also select the `<Manage Channels>` permissions if you intend to enable custom channel descriptions.
5. Copy the URL generated under `<Scopes>` and open it in a new tab to add the bot to your discord server

### Configuring Fabric-Discord-Link
1. Start your minecraft server once to let Fabric-Discord-Link generate it's default config `<fdlink.json>`
2. Turn your server back off
2. Copy your Bots Token from the Discord Developer Portal and paste it into the empty double quotes behind `<"token">` in the config
3. Obtain the channel IDs of the channels that you want your bot to function in (these can be from separate discord servers). To do this, you need to enable Discords Developer mode in your Discord Settings under `<Appearance>`. Afterwards you can right click channels and select `<Copy ID>` at the bottom
3. Paste the Discord Chat and/or Log Channel's IDs into the appropriate locations in the config. The IDs need to be in double quotes inside the brackets. If you have more than one Chat or Log Channel you need to separate the channel IDs inside the brackets with commas (no comma behind the last ID)
4. Configure the other settings in the config to your liking
   - When you set a certain message type to `false`, those messages will still go to any `logChannel` you configured, but they won't be sent to `chatChannels`
   - When `minecraftToDiscordDiscriminator` is set to `true`, messages sent from Discord into Minecraft will have the exact Discord name, e.g. `Bob#1234` instead of just `Bob`
5. Restart your server
