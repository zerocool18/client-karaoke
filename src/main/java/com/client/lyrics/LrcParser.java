package com.client.lyrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LrcParser{
    private Lyric lyric;

    public LrcParser(BufferedReader reader) throws IOException{
        this(LyricParser.create(reader));
    }


    private LrcParser(LyricParser parser){
        lyric=new Lyric(parser.getTags(),parser.getSentences());
    }

    public Lyric getLyric(){
        return lyric;
    }

    public static Lyric create(BufferedReader reader) throws IOException{
        return (new LrcParser(reader)).getLyric();
    }

    public static Lyric create(InputStream in) throws IOException {
        return create(new BufferedReader(new InputStreamReader(in)));
    }

}