package connector.dto;

import connector.enumerate.ProcessNumber;

import java.util.Arrays;

public class DtoStartGame extends Dto{

    private String[] users = new String[4];

    public DtoStartGame(ProcessNumber processNumber, String[] users) throws IllegalArgumentException{
        super(processNumber);
        this.setUsers(users);
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) throws IllegalArgumentException{
        if(users == null) throw new IllegalArgumentException("users must not be null");
        this.users = users;
    }

    @Override
    public String toString() {
        return "DtoStartGame{" +
                "users=" + Arrays.toString(users) +
                "} " + super.toString();
    }
}
