package main;

public class TileRange {
	int x1;
	int y1;
	int x2;
	int y2;
	int id;
	public TileRange (int x1, int y1, int x2, int y2, int id) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.id = id;
	}
	public int getArea () {
		return (x2 - x1 + 1) * (y2 - y1 + 1);
	}
	public void print () {
		System.out.print ("p1: (");
		System.out.print (this.x1);
		System.out.print (", ");
		System.out.print (this.y1);
		System.out.println (")");
		System.out.print ("p2: (");
		System.out.print (this.x2);
		System.out.print (", ");
		System.out.print (this.y2);
		System.out.println (")");
		System.out.print ("id: ");
		System.out.println (this.id);
	}
}
