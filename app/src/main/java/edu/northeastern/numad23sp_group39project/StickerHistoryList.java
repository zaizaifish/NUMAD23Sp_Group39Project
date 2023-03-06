package edu.northeastern.numad23sp_group39project;

import android.widget.TextView;

public class StickerHistoryList {
    private String userSticker;
    private String fromUser;
    private String timeReceived;

    public StickerHistoryList(String userSticker, String fromUser, String timeReceived) {
        this.userSticker = userSticker;
        this.fromUser = fromUser;
        this.timeReceived = timeReceived;
    }

    public  StickerHistoryList() {

    }

    public String getUserSticker() {
        return userSticker;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getTimeReceived() {
        return timeReceived;
    }
}
