package app6;

import java.util.ArrayList;


public class RecDescentResp {
  public ElemAST elem;
  public ArrayList<Terminal> remainder;
  // E() doesn't have a remainder as it's the root node
  RecDescentResp(ArrayList<Terminal> EData) {
    this.elem = null;
    this.remainder = EData;
  }
  RecDescentResp(ArrayList<Terminal> remainder, ElemAST elem) {
    this.elem = elem;
    this.remainder = remainder;
  }
}
