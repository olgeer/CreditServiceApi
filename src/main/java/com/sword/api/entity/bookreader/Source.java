package com.sword.api.entity.bookreader;

public class Source {
    String source_name;
    String source_script;
    boolean state;
    int level;
    String author;
    int author_id;
    boolean free;

    public Source(String source_name,String source_script,boolean state){
        this.source_name=source_name;
        this.source_script=source_script;
        this.state=state;
        this.level=3;
        this.free=true;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_script() {
        return source_script;
    }

    public void setSource_script(String source_script) {
        this.source_script = source_script;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
