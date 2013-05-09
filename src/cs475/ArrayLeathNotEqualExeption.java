package cs475;

public class ArrayLeathNotEqualExeption extends Exception {
	int length1;
	int length2;
	public ArrayLeathNotEqualExeption(int length1,int length2){
		this.length1=length1;
		this.length2=length2;
	}
	
	public String toString(){
	return ("String length not equal!"+this.length1+", "+this.length2+".");	
	}
}
