package connector.dto;

import connector.enumerate.ProcessNumber;

import java.util.List;

public class DtoRank extends Dto{
    private List<User> users;
    public DtoRank(ProcessNumber processNumber){
        super(processNumber);
    }
    public DtoRank(ProcessNumber processNumber, List<User> users) throws IllegalArgumentException{
        this(processNumber);
        this.setUsers(users);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) throws IllegalArgumentException{
        if(users == null) throw new IllegalArgumentException("users must not be null");
        this.users = users;
    }

    @Override
    public String toString() {
        return "DtoRank{" +
                "users=" + users +
                "} " + super.toString();
    }
}
