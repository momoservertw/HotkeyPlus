package tw.momocraft.hotkeyplus.utils;

import java.util.List;

public class KeyboardMap {

    private String groupName;
    private boolean cancel;
    private List<String> commands;


    public String getGroupName() {
        return groupName;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
