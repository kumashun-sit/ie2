package entity;

public class Answer {
  int hit;
  int blow;
  int answer1;
  int answer2;
  int answer3;
  int answer4;
  int colorNum;

  public Answer(int hit, int blow, int answer1, int answer2, int answer3, int answer4, int colorNum) {
    setHit(hit);
    setBlow(blow);
    setAnswer1(answer1);
    setAnswer2(answer2);
    setAnswer3(answer3);
    setAnswer4(answer4);
    setColorNum(colorNum);
  }

  public void setHit(int hit) { this.hit = hit; }
  public void setBlow(int blow) { this.blow = blow; }
  public void setAnswer1(int answer) { this.answer1 = answer; }
  public void setAnswer2(int answer) { this.answer2 = answer; }
  public void setAnswer3(int answer) { this.answer3 = answer; }
  public void setAnswer4(int answer) { this.answer4 = answer; }
  public void setColorNum(int colorNum) { this.colorNum = colorNum; }

  public int getHit() { return hit; }
  public int getBlow() { return blow; }
  public int getAnswer1() { return answer1; }
  public int getAnswer2() { return answer2; }
  public int getAnswer3() { return answer3; }
  public int getAnswer4() { return answer4; }
  public int getColorNum() { return colorNum; }

}
 
