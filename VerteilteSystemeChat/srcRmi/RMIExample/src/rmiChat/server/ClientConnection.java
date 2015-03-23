package rmiChat.server;

import rmiChat.common.ClientHandler;

/**
 * Created by florian on 16.03.15.
 */
public class ClientConnection {
    private ClientHandler handler;
    private String nickname;

    public ClientConnection(ClientHandler handler, String nickname) {
        this.handler = handler;
        this.nickname = nickname;
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public void setHandler(ClientHandler handler) {
        this.handler = handler;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
