package connector.dto;

import connector.enumerate.ProcessNumber;

import java.util.Arrays;

public class DtoInputNumber extends Dto{

    private int roomID;
    private int[] answer = new int[4];

    public DtoInputNumber(ProcessNumber processNumber, int roomID, int[] answer) throws IllegalArgumentException{
        super(ProcessNumber.INPUT_NUMBER);
        this.setRoomID(roomID);
        this.setAnswer(answer);
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) throws IllegalArgumentException{
        if(answer == null) throw new IllegalArgumentException("answer must not be null");
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "DtoInputNumber{" +
                "roomID=" + roomID +
                ", answer=" + Arrays.toString(answer) +
                "} " + super.toString();
    }
}
