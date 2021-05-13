package fr.arthurbambou.fdlink.compat_1_16;

import fr.arthurbambou.fdlink.api.minecraft.Message;

public class CommandMessage1_16 implements Message {

    private final String commandName;
    private final String source;
    private final String message;

    public CommandMessage1_16(String source, String message, String commandName) {
        this.message = message;
        this.source = source;
        this.commandName = commandName;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public MessageObjectType getType() {
        return MessageObjectType.TEXT;
    }

    @Override
    public TextType getTextType() {
        return TextType.COMMAND;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getCommandName() {
        return this.commandName;
    }
}
