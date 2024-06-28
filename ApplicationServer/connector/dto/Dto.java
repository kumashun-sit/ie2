package connector.dto;

import connector.enumerate.ProcessNumber;

/**
 * Data Transfer Object
 * 全てのDTOはこのクラスを継承する
 * @author 鈴木直人
 */
public class Dto {
    private ProcessNumber processNumber;
    private String message;
    private boolean success;
    public Dto(ProcessNumber processNumber){
        this.setProcessNumber(processNumber);
    }
    public ProcessNumber getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(ProcessNumber processNumber) {
        this.processNumber = processNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Dto{" +
                "processNumber=" + processNumber +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
