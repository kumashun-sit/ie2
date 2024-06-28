package connector.dto;

import connector.enumerate.ProcessNumber;

import java.util.Arrays;

public class DtoChangeTurn extends Dto{

    private int hit, blow;
    private int[] answer;
    private boolean isTimeUp;

    public DtoChangeTurn(ProcessNumber processNumber, int hit, int blow, int[] answer, boolean isTimeUp) throws IllegalArgumentException{
        super(processNumber);
        this.setHit(hit);
        this.setBlow(blow);
        this.setAnswer(answer);
        this.setTimeUp(isTimeUp);
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getBlow() {
        return blow;
    }

    public void setBlow(int blow) {
        this.blow = blow;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) throws IllegalArgumentException{
        if(answer == null) throw new IllegalArgumentException("answer must not be null");
        this.answer = answer;
    }

    public boolean isTimeUp() {
        return isTimeUp;
    }

    public void setTimeUp(boolean timeUp) {
        isTimeUp = timeUp;
    }

    @Override
    public String toString() {
        return "DtoChangeTurn{" +
                "hit=" + hit +
                ", blow=" + blow +
                ", answer=" + Arrays.toString(answer) +
                ", isTimeUp=" + isTimeUp +
                "} " + super.toString();
    }
}
