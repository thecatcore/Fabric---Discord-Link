package fr.catcore.fdlink;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.fabricmc.api.ModInitializer;

public class FDLink implements ModInitializer {
    @Override
    public void onInitialize() {
        String token = "";
        JDA bot = JDABuilder.createDefault(token).setActivity(Activity.playing("Minecraft")).build();
    }
}
