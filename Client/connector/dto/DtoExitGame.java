package connector.dto;

import connector.enumerate.ProcessNumber;

public class DtoExitGame extends Dto{
    private String userID;
    private int roomID;

    public DtoExitGame(ProcessNumber processNumber, String userID, int roomID) throws IllegalArgumentException{
        super(processNumber);
        this.setRoomID(roomID);
        this.setUserID(userID);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) throws IllegalArgumentException{
        if(userID == null) throw new IllegalArgumentException("userID must not be null");
        this.userID = userID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
