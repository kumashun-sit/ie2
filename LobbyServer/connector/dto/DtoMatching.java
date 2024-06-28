package connector.dto;

import connector.enumerate.ProcessNumber;

public class DtoMatching extends Dto {
    private String userID;

    public DtoMatching(ProcessNumber processNumber, String userID) throws IllegalArgumentException{
        super(processNumber);
        this.setUserID(userID);
    }

    public void setUserID(String userID) throws IllegalArgumentException{
        if(userID == null) throw new IllegalArgumentException("userID must not be null");
        this.userID = userID;
    }

    public String getUserID() { return userID; }

}
