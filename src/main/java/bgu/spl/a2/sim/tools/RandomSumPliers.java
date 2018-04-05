package bgu.spl.a2.sim.tools;

import java.util.Arrays;
import java.util.Random;

import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool {

	public RandomSumPliers(){
	}
	
	@Override
	public String getType() {
		return "rs-pliers";
	}

	@Override
	public long useOn(Product p) {
		long finalId = 0;
		for (int i = 0; i < p.getParts().size(); i++) {
			Random r = new Random(p.getParts().get(i).getFinalId());
			long sum = 0;
			for (long j = 0; j < p.getParts().get(i).getFinalId() % 10000; j++) {
				sum += r.nextInt();
			}
			sum = Math.abs(sum);
			finalId = finalId + sum;
		}

		return finalId;
	}

}
