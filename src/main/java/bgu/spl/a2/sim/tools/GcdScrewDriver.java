package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;

public class GcdScrewDriver implements Tool {

	public GcdScrewDriver(){
	}
	
	@Override
	public String getType() {
		return "gs-driver";
	}

	@Override
	public long useOn(Product p) {
		long finalId=0;
		for (int i=0; i<p.getParts().size(); i++){
			long id=p.getParts().get(i).getFinalId();
			
			
			BigInteger b1 = BigInteger.valueOf(id);
	        BigInteger b2 = BigInteger.valueOf(reverseNumber(id));
	        long value= (b1.gcd(b2)).longValue();
	        finalId=finalId+value;
		}
		return (finalId);
		
	}
	
	public long reverseNumber(long num){
		long reversedNumber = 0;
		long temp = 0;
		while(num > 0){
            temp = num%10;
            reversedNumber = reversedNumber * 10 + temp;
            num = num/10;
             
		}
		return reversedNumber;
	}	
	
	

}
