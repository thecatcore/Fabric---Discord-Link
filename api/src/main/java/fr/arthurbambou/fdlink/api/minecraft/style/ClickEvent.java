package fr.arthurbambou.fdlink.api.minecraft.style;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ClickEvent {
    private final Action action;
    private final String value;

    public ClickEvent(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            ClickEvent lv = (ClickEvent)obj;
            if (this.action != lv.action) {
                return false;
            } else {
                if (this.value != null) {
                    if (!this.value.equals(lv.value)) {
                        return false;
                    }
                } else if (lv.value != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public String toString() {
        return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
    }

    public int hashCode() {
        int i = this.action.hashCode();
        i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
        return i;
    }

    public static enum Action {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true),
        COPY_TO_CLIPBOARD("copy_to_clipboard", true);

        private static final Map<String, Action> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Action::getName, (a) -> a));
        private final boolean userDefinable;
        private final String name;

        private Action(String name, boolean userDefinable) {
            this.name = name;
            this.userDefinable = userDefinable;
        }

        public boolean isUserDefinable() {
            return this.userDefinable;
        }

        public String getName() {
            return this.name;
        }

        public static Action byName(String name) {
            return BY_NAME.get(name);
        }
    }
}
