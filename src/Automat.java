 import java.util.*;

public class Automat {
	private int[][] s;  //t
	private int[][] sn; //t+1
	private int[][] s2;
	private int size = 0;  
	private int model = 0;
	public int steps = 0;
	private Random r = new Random();
	
	public Automat(int size) {
		s = new int[size][size];
		sn = new int[size][size];
		s2 = new int[size][size];
		this.size = size;
		init();
	}
	
	public int get(int x, int y) {
		return s[y][x];
	}
	
	public int get2(int x, int y) {
		return s2[y][x];
	}
	
	public int getSize() {
		return size;
	}
	
	public void changeSize(int newsize) {
		copy();
		size = newsize;
	}
	
	public void step() {
		nextState();
		
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				s[y][x] = sn[y][x];
			}
		}
		
		++steps;
	}
	
	public void nextState() {
		clearSn();
		generateTop();
		
		int neighbors = -1, neighborsLeft = -1, neighborsRight = -1;
				
		for(int y = 0; y < size-1; ++y) {
			for(int x = 0; x < size; ++x) {
//		for(int y = 1; y < size-1; ++y) {
//			for(int x = 1; x < size-1; ++x) {
				if(s[y][x] == 1) {
					switch(model) {
					case 0:
						//normalnie
						if(y+1 == size-1 || s2[y+1][x] == 1) {
							s2[y][x] = 1;
						} else {
							sn[y+1][x] = 1;
						}
						break;
					case 1:
						//tam gdzie ni¿ej
						if(y+1 == size-1 || s2[y+1][x] == 1 || s[y+1][x] == 1) {
							if(highestRight(x, y) > y && highestRight(x, y) > highestLeft(x, y)) {
								if(x+1 == size) s2[y][x] = 1;
								else sn[y][x+1] = 1;
							} else if(highestLeft(x, y) > y && highestLeft(x, y) > highestRight(x, y)) {
								if(x-1 == -1) s2[y][x] = 1;
								else sn[y][x-1] = 1;
							} else if(highestLeft(x, y) > y && highestRight(x, y) > y && highestLeft(x, y) == highestRight(x, y)) {
								if(r.nextInt(100) < 50) {
									if(x-1 == -1) s2[y][x] = 1;
									else sn[y][x-1] = 1;
								} else {
									if(x+1 == size) s2[y][x] = 1;
									else sn[y][x+1] = 1;
								}
							} 
							else s2[y][x] = 1;
						} else {
							sn[y+1][x] = 1;
						}
						break;
					case 2:
						if(y+1 == size-1 || s2[y+1][x] == 1) {
							//zlicz s¹siadów w aktualnej pozycji
							neighbors = countNeighbors(x, y);
							//jeœli po lewej jest pusto
							if(x-1 > -1) {
								if(s2[y][x-1] == 0) {
									//jad¹c w dó³ szukaj pozycji z najwiêksz¹ liczb¹ s¹siadów
									int tmpy = y;
									int tmp = 0;
									neighborsLeft = countNeighbors(x-1, y);
									while(tmpy < size-1 && s2[tmpy][x-1] == 0) {
										tmp = countNeighbors(x-1, tmpy);
										if(tmp > neighborsLeft) neighborsLeft = tmp;
										++tmpy;
									}
								}
							} else neighborsLeft = 0;
							//jeœli po prawej jest pusto, robimy to samo
							if(x+1 < size) {
								if(s2[y][x+1] == 0) {
									int tmpy = y;
									int tmp = 0;
									neighborsRight = countNeighbors(x+1, y);
									while(tmpy < size-1 && s2[tmpy][x+1] == 0) {
										tmp = countNeighbors(x+1, tmpy);
										if(tmp > neighborsRight) neighborsRight = tmp;
										++tmpy;
									}
								}
							} else neighborsRight = 0;
							//przesuwamy cz¹stkê w lewo, prawo lub zostawiamy w zale¿noœci od tego, gdzie uda³o siê znaleŸæ wiêcej s¹siadów
							int m = max(neighbors, neighborsLeft, neighborsRight);
							if(m == neighbors) {
								s2[y][x] = 1;
							} else if(neighborsLeft > -1 && m == neighborsLeft && x-1 > -1) {
								sn[y][x-1] = 1;									
							} else if(neighborsRight > -1 && m == neighborsRight && x+1 < size) {
								sn[y][x+1] = 1;
							}
							neighborsLeft = -1;
							neighborsRight = -1;
							neighbors = -1;
						} else {
							sn[y+1][x] = 1;
						}
						break;
					case 3:
						if(y+1 == size-1 || s2[y+1][x] == 1) {
							boolean resultLeft = false;
							boolean resultRight = false;
							int tmpy = -1;
							if(isCorner(x, y)) s2[y][x] = 1;
							else {
								//sprawdzenie lewej strony
								tmpy = y;
								while(tmpy < size-1 && (x-1 == -1 || s2[tmpy][x-1] == 0)) {
									if(isCorner(x-1, tmpy)) {
										resultLeft = true;
										break;
									}
									++tmpy;
								}
								//sprawdzenie prawej strony
								tmpy = y;
								while(tmpy < size-1 && (x+1 == size-1 || s2[tmpy][x+1] == 0)) {
									if(isCorner(x+1, tmpy)) {
										resultRight = true;
										break;
									}
									++tmpy;
								}
								
								if(resultLeft && resultRight) {
									if(r.nextInt(100) < 50) sn[y][x-1 < 0 ? 0 : x-1] = 1;
									else					sn[y][x+1 > size-1 ? size-1 : x+1] = 1;
								}
								else if(resultLeft)			sn[y][x-1 < 0 ? 0 : x-1] = 1;
								else if(resultRight)    	sn[y][x+1 > size-1 ? size-1 : x+1] = 1;
								else						s2[y][x] = 1;
								
								resultLeft = false;
								resultRight = false;
							}
						} else {
							sn[y+1][x] = 1;
						}
						break;
					}
				}
			}
		}
	}
	
	public int particles() {
		int result = 0;
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				if(s2[y][x] == 1) ++result;
			}
		}
		return result;
	}
	
	public void changeModel(int i) {
		model = i;
	}
	
	private int max(int ... a) {
		int max = a[0];
		for(int i : a)
			if(max < i) max = i;
		return max;
	}
	
	private void init() {
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				s[y][x] = sn[y][x] = s2[y][x] = 0;
			}
		}
	}
	
	private void clearSn() {
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				sn[y][x] = 0;
			}
		}
	}
	
	private void generateTop() {
		//if(r.nextInt(100) < 10) s[0][size-1] = 1;
		//if(r.nextInt(100) < 10) s[0][size-3] = 1;
		for(int x = 0; x < size; ++x) {
			if(r.nextInt(100) < 2) s[0][x] = 1;
			//s[0][1] = 1;
		}
	}
	
	private int highestLeft(int x, int y) {
		for(int h = size-2; h >= 0; --h) {
			if(s2[h][x == 0 ? size-1 : x-1] == 0) return h;
		}
		return 0;
	}
	
	private int highestRight(int x, int y) {
		for(int h = size-2; h >= 0; --h) {
			if(s2[h][x == size ? 0 : x+1] == 0) return h;
		}
		return 0;
	}
	
	private void copy() {
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				if(s2[y][x] == 1) s[y][x] = 1;
			}
		}
		for(int y = 0; y < size; ++y) {
			for(int x = 0; x < size; ++x) {
				s2[y][x] = 0;
			}
		}
	}
	
	//oblicza iloœæ s¹siadów w tablicy s2 danej komórki
	private int countNeighbors(int x, int y) {
		int sum = 0;
		int start_y = y-1, start_x = x-1, stop_y = y+1, stop_x = x+1;
		
		if(start_x < 0) start_x = 0;
		if(stop_x > size-1) stop_x = size-1;
		if(start_y < 0) start_y = 0;
		if(stop_y > size-1) stop_y = size-1;

		for(int h = start_y; h <= stop_y; ++h) {
			for(int w = start_x; w <= stop_x; ++w) {
				if(h == y && w == x) continue;
				if(s2[h][w] == 1) ++sum;
			}
		}
		
		return sum;
	}
	
	//sprawdza czy aktualna pozycja znajduje siê w naro¿niku
	private boolean isCorner(int x, int y) {
		boolean result = false;
		
		if(x < 0)      return false;
		else if(x > size-1) return false;
		else if(x == 0) {
			if(y < size-2) {
				if(s2[y][x+1] == 1 && s2[y+1][x+1] == 1 && s2[y+1][x] == 1) result = true;
			} else {
				if(s2[y][x+1] == 1) result = true;
			}
		}
		else if(x == size-1) {
			if(y < size-2) {
				if(s2[y][x-1] == 1 && s2[y+1][x-1] == 1 && s2[y+1][x] == 1) result = true;
			} else {
				if(s2[y][x-1] == 1) result = true;
			}
		}
		else {
			if(y < size-2) {
				if(s2[y][x+1] == 1 && s2[y+1][x+1] == 1 && s2[y+1][x] == 1) result = true;
				if(s2[y][x-1] == 1 && s2[y+1][x-1] == 1 && s2[y+1][x] == 1) result = true;
			} else {
				if(s2[y][x+1] == 1) result = true;
				if(s2[y][x-1] == 1) result = true;
			}
		}
		
		return result;
	}
}