package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


public class WaveTask extends Task<Product[][]> {

private List<Order>  orders;
private Warehouse warehouse;
private ConcurrentLinkedQueue<Product> products;




    public WaveTask(List<Order> orders, Warehouse warehouse, ConcurrentLinkedQueue<Product> products) {
      this.orders=orders;
      this.warehouse=warehouse;
      this.products=products;

    }


    @Override
    protected void start() {
    	List<Task<Product[]>> tasks=new ArrayList<>();
    	for (int i=0; i<orders.size(); i++){
    		OrderTask newTask=new OrderTask(orders.get(i), warehouse); //create order task for each order in the wave
    		spawn(newTask);
    		tasks.add(newTask);
    				
    	}
    	whenResolved(tasks, ()->{
    		Product[][]result=new Product[orders.size()][];
    		for (int j = 0; j < result.length; j++) {
				result[j] = tasks.get(j).getResult().get();
			}
    		for (int j=0; j<orders.size(); j++){
				for (int k=0; k<orders.get(j).getQty(); k++){
					products.add(result[j][k]);
				}
			}

            complete(result);
    	});

             }

}
