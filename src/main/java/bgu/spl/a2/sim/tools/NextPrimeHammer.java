package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

public class NextPrimeHammer implements Tool {

	public NextPrimeHammer() {

	}

	@Override
	public String getType() {
		return "np-hammer";
	}

	@Override
	public long useOn(Product p) {
		long finalId = 0;
		for (int i = 0; i < p.getParts().size(); i++) {
			long id = p.getParts().get(i).getFinalId() + 1;
			while (!isPrime(id)) {
				id++;
			}
			finalId = finalId + id;
		}
		return finalId;
	}

	private boolean isPrime(long value) {
		if (value < 2)
			return false;
		if (value == 2)
			return true;
		long sq = (long) Math.sqrt(value);
		for (long i = 2; i <= sq; i++) {
			if (value % i == 0) {
				return false;
			}
		}

		return true;
	}

}