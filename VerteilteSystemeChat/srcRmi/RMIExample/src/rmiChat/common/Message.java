package rmiChat.common;

import java.io.Serializable;

/**
 * Created by florian on 16.03.15.
 */
public class Message implements Serializable {
    private String message;
    private String nickname;

    public Message(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return this.getNickname() + ": " + this.getMessage();
    }
}
