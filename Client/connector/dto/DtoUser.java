package connector.dto;

import connector.enumerate.ProcessNumber;

public class DtoUser extends Dto{
    private String userID;
    private String password;
    private int rate;

    public DtoUser(ProcessNumber processNumber) {
        super(processNumber);
    }

    public DtoUser(ProcessNumber processNumber, String userID, String password, int rate) throws IllegalArgumentException{
        super(processNumber);
        this.userID = userID;
        this.password = password;
        this.rate = rate;
    }

    public DtoUser(ProcessNumber processNumber, String userID, String password) throws IllegalArgumentException{
        super(processNumber);
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) throws IllegalArgumentException{
        if(userID == null) throw new IllegalArgumentException("userID must not be null");
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws IllegalArgumentException{
        if(password == null) throw new IllegalArgumentException("password must not be null");
        this.password = password;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "DtoLogin{" +
                "userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                ", rate=" + rate +
                "} " + super.toString();
    }
}
