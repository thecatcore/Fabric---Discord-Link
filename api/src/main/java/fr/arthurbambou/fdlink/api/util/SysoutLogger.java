package fr.arthurbambou.fdlink.api.util;

public class SysoutLogger implements Logger {

    private final String name;

    protected SysoutLogger(String name) {
        this.name = "[" + name + "] ";
    }

    @Override
    public void info(String string) {
        System.out.println("[INFO]" + this.name + string);
    }

    @Override
    public void warn(String string) {
        System.out.println("[WARN]" + this.name + string);
    }

    @Override
    public void error(String string) {
        System.out.println("[ERROR]" + this.name + string);
    }

    @Override
    public void error(String format, Object e) {
        System.out.println("[ERROR]" + this.name + String.format(format.replace("{}", "%s"), e.toString()));
    }

    @Override
    public void error(String format, Object e, Object e2) {
        System.out.println("[ERROR]" + this.name + String.format(format.replace("{}", "%s"), e.toString(), e2.toString()));
    }
}
