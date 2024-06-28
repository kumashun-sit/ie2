package connector.dto;

import connector.enumerate.ProcessNumber;

import java.util.Arrays;

public class DtoGameInfo extends Dto{

    private int roomID, turn;
    private String[] users = new String[4];
    private int[] rates = new int[4];

    public DtoGameInfo(ProcessNumber processNumber, int roomID, int turn, String[] users, int[] rates) throws IllegalArgumentException{
        super(processNumber);
        this.setRoomID(roomID);
        this.setTurn(turn);
        this.setUsers(users);
        this.setRates(rates);
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) throws IllegalArgumentException{
        if(users == null) throw new IllegalArgumentException("users must not be null");
        this.users = users;
    }

    public int[] getRates() {
        return rates;
    }

    public void setRates(int[] rates) throws IllegalArgumentException{
        if(rates == null) throw new IllegalArgumentException("rates must not be null");
        this.rates = rates;
    }

    //ゲーム開始時　
    @Override
    public String toString() {
        return "DtoGameInfo{" +
                "roomID=" + roomID +
                ", turn=" + turn +
                ", users=" + Arrays.toString(users) +
                ", rates=" + Arrays.toString(rates) +
                "} " + super.toString();
    }
}
