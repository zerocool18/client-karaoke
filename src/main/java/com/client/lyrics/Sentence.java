package com.client.lyrics;

import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Random;

@Data
public class Sentence implements Serializable {

    private static final long serialVersionUID=-7463622619946509670L;
    private long fromTime=-1;// milliseconds, sentence start include
    private long toTime=-1;// milliseconds,sentence end time include
    private String content="";// content of the sentence
    private int index=-1;//position of the sentence in the lyric
    private String missing;
    private String censoredString;

    // private final static long DISAPPEAR_TIME = 1000L;//

    public Sentence(long fromTime){
        this("",fromTime,-1);
    }

    public Sentence(String content){
        this(content,-1,-1);
    }

    public Sentence(String content,long fromTime){
        this(content,fromTime,-1);
    }

    public Sentence(String content,long fromTime,long toTime){
        this.content=content;
        this.fromTime=fromTime;
        this.toTime=toTime;
        this.missing = "";
        this.censoredString = "";
    }

    public void setMissingWord() {
        String[] words = getWordArray();
        if(words.length < 3) return;
        for (String w: words){
            if(w.length() > 3) {
                this.missing = words[new Random().nextInt(words.length)];
                this.censoredString = content.replaceAll(String.format("\\b%s\\b", this.missing), "______");
            }
        }
    }

    public String[] getWordArray(){
        return this.content.split("\\s+");
    }

    public String getContent(){
        return content;
    }

    public String showString(){
        String s =  !this.censoredString.equals("") ? this.censoredString : content ;

        return  String.format("%s -> %s", s, this.missing);
    }

    public boolean testAnswer(String test){
        String[] lyrics = getWordArray();
        for(String s: lyrics){
            if(s.equals(test)) return true;
        }

        return false;
    }

    /**
     * the duration of the sentence
     *
     * @return toTime-fromTime+1; in millisecondes
     */
    public long getDuring(){
        return toTime-fromTime+1;
    }

    public long getFromTime(){
        return fromTime;
    }

    public long getToTime(){
        return toTime;
    }

    /**
     * check if time is between fromTime and toTime if time is negative return true
     **/
    public boolean isInTime(long time){
        if (time<0)
            return true;
        boolean fromTimeCheck=(fromTime==-1||time>=fromTime);
        boolean toTimeCheck=(toTime==-1||time<=toTime);
        return fromTimeCheck&&toTimeCheck;
    }

    public void setContent(String content){
        this.content=content;
    }

    public void setFromTime(long fromTime){
        this.fromTime=fromTime;
    }

    public void setToTime(long toTime){
        this.toTime=toTime;
    }

    public String toString(){
        return "{index:"+index+"|"+fromTime+"("+content+")"+toTime+"}";
    }

    public static class SentenceComparator implements Comparator<Sentence> {

        @Override
        public int compare(Sentence sent1,Sentence sent2){
            return (int)(sent1.getFromTime()-sent2.getFromTime());
        }

    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index=index;
    }


}