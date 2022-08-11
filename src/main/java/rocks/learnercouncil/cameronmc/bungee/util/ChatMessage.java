package rocks.learnercouncil.cameronmc.bungee.util;

import java.util.Objects;

public class ChatMessage {

    private final String message;
    private final String stripped;

    public ChatMessage(String message) {
        this.message = message;
        this.stripped = message;
    }

    public ChatMessage(String message, String stripped) {
        this.message = message;
        this.stripped = stripped;
    }

    public boolean contains(String s) {
        return s.equals(message) || s.equals(stripped);
    }

    public String getMessage() {
        return message;
    }

    public String getStripped() {
        return stripped;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof ChatMessage)) return false;
        ChatMessage c = (ChatMessage) o;
        return c.message.equals(this.message) && c.stripped.equals(this.stripped);
    }

    @Override
    public int hashCode() {
        return stripped.hashCode();
    }
}
