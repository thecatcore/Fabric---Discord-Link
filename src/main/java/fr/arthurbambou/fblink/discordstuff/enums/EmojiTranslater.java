package fr.arthurbambou.fblink.discordstuff.enums;

public enum EmojiTranslater {
    SMILE("U+1F600", ":-)");

    public String discordID;
    public String minecraftID;

    EmojiTranslater(String discordid, String minecraft) {
        this.discordID = discordid;
        this.minecraftID = minecraft;
    }
}
