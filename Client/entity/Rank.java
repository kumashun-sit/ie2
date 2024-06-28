package entity;

import java.awt.*;

public class Rank {
  String rank;
  String userID;
  int rate;
  Color rankColor;
  int rankStyle;
  Color userColor;
  int userStyle;

  public Rank(String rank, String userID, int rate, Color rankColor, int rankStyle, Color userColor, int userStyle) {
    setRank(rank);
    setUserID(userID);
    setRate(rate);
    setRankColor(rankColor);
    setRankStyle(rankStyle);
    setUserColor(userColor);
    setUserStyle(userStyle);
  }

  public void setRank (String rank) { this.rank = rank; }
  public void setUserID (String userID) { this.userID = userID; }
  public void setRate (int rate) { this.rate = rate; }
  public void setRankColor (Color color) { this.rankColor = color; }
  public void setRankStyle (int style) { this.rankStyle = style; }
  public void setUserColor (Color color) { this.userColor = color; }
  public void setUserStyle (int style) { this.userStyle = style; }

  public String getRank() { return rank; }
  public String getUserID() { return userID; }
  public int getRate() { return rate; }
  public Color getRankColor() { return rankColor; }
  public int getRankStyle() { return rankStyle; }
  public Color getUserColor() { return userColor; }
  public int getUserStyle() { return userStyle; }

}
 
