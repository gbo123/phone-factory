package bgu.spl.a2.sim.conf;

import java.util.List;

/**
 * Created by גיל on 24/12/2016.
 */
public class Wave {
private List<Order> Orders;


    public Wave(List<Order> orders) {
        Orders = orders;
    }


    public List<Order> getOrders() {
        return Orders;
    }


	public void setOrders(List<Order> orders) {
		Orders = orders;
	}


}
