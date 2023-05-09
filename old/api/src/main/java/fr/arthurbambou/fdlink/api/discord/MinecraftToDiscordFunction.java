package fr.arthurbambou.fdlink.api.discord;

import fr.arthurbambou.fdlink.api.config.Config;
import fr.arthurbambou.fdlink.api.minecraft.CompatText;
import fr.arthurbambou.fdlink.api.minecraft.Message;

public interface MinecraftToDiscordFunction {

    MinecraftMessage handleText(Message text, Config config);
}
