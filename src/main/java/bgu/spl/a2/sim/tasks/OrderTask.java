package bgu.spl.a2.sim.tasks;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.Order;

public class OrderTask extends Task<Product[]> {
	private final Order order;
	private final Warehouse warehouse;

	
	public OrderTask(Order order, Warehouse warehouse) {
		this.order=order;
		this.warehouse=warehouse;

	}
	@Override
	protected void start() {
		List<Task<Product>> tasks=new ArrayList<>();
		for (int k=0; k<order.getQty(); k++){ //create Manufacture Task for each product
           	ManufactureTask newTask=new ManufactureTask(warehouse,order.getProduct(), order.getStartId()+k);
            spawn(newTask);
            tasks.add(newTask);
		
		}
        whenResolved(tasks,()->{
        	
        	Product[] orderProducts=new Product [order.getQty()];
			for (int j = 0; j < order.getQty(); j++) {
				orderProducts[j] = tasks.get(j).getResult().get();
			}
            complete(orderProducts);
        });
	}
}

