package bgu.spl.a2.sim.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

public class ManufactureTask extends Task<Product> {
	private final Warehouse warehouse;
	private final ManufactoringPlan plan;
	private Product product;

	
	public ManufactureTask(Warehouse warehouse, String productName, long startId) {
		this.warehouse=warehouse;
		plan=warehouse.getPlan(productName);
		this.product=new Product(startId, productName);

	}
	@Override
	protected void start() {
		if(plan.getParts().length==0){ //if no parts are required, the task completes immediately
			assemblyProcess();
			
		}
		
		else{
			List<Task<Product>> tasks = new ArrayList<>();
			for (int i = 0; i < plan.getParts().length; i++) {
				ManufactureTask newTask = new ManufactureTask(warehouse, plan.getParts()[i], product.getStartId()+1);
				spawn(newTask);
				tasks.add(newTask);
			}
			whenResolved(tasks, () -> {
				for (int j = 0; j < plan.getParts().length; j++) {
					product.addPart(tasks.get(j).getResult().get());
				}

				assemblyProcess();
			});
		}	
	}
	
	private void assemblyProcess (){
		List<Task<Long>> tasks = new ArrayList<>();
		if (plan.getTools().length==0)
			complete (product);
		
		for (int i=0; i<plan.getTools().length; i++){ //create getToolTask for each tool in the plan's list
			GetToolTask newTask= new GetToolTask(warehouse, plan.getTools()[i], product);
			spawn(newTask);
			tasks.add(newTask);
		}
		whenResolved(tasks, () -> {
			long[] res=new long[plan.getTools().length];
			for (int j = 0; j < plan.getTools().length; j++) { //save the useOn result of each tool
				res[j]=tasks.get(j).getResult().get().longValue();
			}
			long answer=0;
			for (int j = 0; j < res.length; j++) { //sum the results of all the tools
				answer=answer+res[j];
			}
			long finalId=product.getStartId()+answer; //add startId
			product.setFinalId(finalId);
			complete (product);
			
		});
		} 

}
