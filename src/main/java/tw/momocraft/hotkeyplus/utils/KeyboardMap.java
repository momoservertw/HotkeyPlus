package tw.momocraft.hotkeyplus.utils;

import java.util.List;

public class KeyboardMap {

    private String group;
    private String key;
    private int page;
    private int Sequence;
    private String display;
    private List<String> commands;

    private boolean custom;

    public String getGroup() {
        return group;
    }

    public String getKey() {
        return key;
    }

    public int getPage() {
        return page;
    }

    public String getDisplay() {
        return display;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSequence(int sequence) {
        Sequence = sequence;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
