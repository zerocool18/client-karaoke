package com.client.util;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class MessageSender{

    private PrintStream output;


    public MessageSender(PrintStream output) {
        this.output = output;
    }

    public void sendMessage(String msg){
        output.println(msg);
    }
}
