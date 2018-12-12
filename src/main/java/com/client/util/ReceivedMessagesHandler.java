package com.client.util;

import com.client.chatwindow.GameController;
import com.client.lyrics.Lyric;
import com.client.lyrics.Sentence;
import com.client.lyrics.Song;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ReceivedMessagesHandler implements Runnable {

    private InputStream server;
    private GameController controller;


    public ReceivedMessagesHandler(InputStream server, GameController controller) {
        this.server = server;
        this.controller = controller;
    }

    @Override
    public void run() {
        // receive server messages and print out to screen
        Scanner s = new Scanner(server);
        while (s.hasNextLine()) {
            String msg = s.nextLine();
            System.out.println(msg);
            processMessage(Json.fromJson(msg,Messenger.class));
            controller.addToChat(msg);
        }
        s.close();
    }


    public void processMessage(Messenger msg){
        switch (msg.getStatus()){
            case "WAITING_FOR_PLAYERS":
                controller.setGameStatus("WAITING_FOR_PLAYERS");
                controller.disableMessageBox();
                break;
            case "PLAYLIST":
                ArrayList map = Json.fromJson(msg.getPayload(), ArrayList.class);

                System.out.println(map.toString());
                controller.setSong(map);
                controller.playSong();
                break;
        }
    }

}