package tw.momocraft.hotkeyplus.utils;

import java.util.List;

public class HotkeyMap {

    String groupName;
    String key;
    List<String> commands;
    String featurePlaceHolder;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public String getFeaturePlaceHolder() {
        return featurePlaceHolder;
    }

    public void setFeaturePlaceHolder(String featurePlaceHolder) {
        this.featurePlaceHolder = featurePlaceHolder;
    }
}
