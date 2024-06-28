package connector.dto;

import connector.enumerate.ProcessNumber;

import java.util.Arrays;

public class DtoEndGame extends Dto{

    private String winnerID;
    private int[] answer;
    private int[] afterRates;
    private boolean is8TurnSkipped;

    public DtoEndGame(ProcessNumber processNumber, String winnerID, int[] answer, int[] afterRates, boolean is8TurnSkipped) throws IllegalArgumentException{
        super(processNumber);
        this.setWinnerID(winnerID);
        this.setAnswer(answer);
        this.setAfterRates(afterRates);
        this.setIs8TurnSkipped(is8TurnSkipped);
    }

    public String getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(String winnerID) {
        this.winnerID = winnerID;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) throws  IllegalArgumentException{
        if(answer == null) throw new IllegalArgumentException("answer must not be null");
        this.answer = answer;
    }

    public int[] getAfterRates() {
        return afterRates;
    }

    public void setAfterRates(int[] afterRates) throws  IllegalArgumentException{
        if(afterRates == null) throw new IllegalArgumentException("afterRates must not be null");
        this.afterRates = afterRates;
    }

    public boolean isIs8TurnSkipped() {
        return is8TurnSkipped;
    }

    public void setIs8TurnSkipped(boolean is8TurnSkipped) {
        this.is8TurnSkipped = is8TurnSkipped;
    }

    @Override
    public String toString() {
        return "DtoEndGame{" +
                "winnerID='" + winnerID + '\'' +
                ", answer=" + Arrays.toString(answer) +
                ", afterRates=" + Arrays.toString(afterRates) +
                ", is8TurnSkipped=" + is8TurnSkipped +
                "} " + super.toString();
    }
}
