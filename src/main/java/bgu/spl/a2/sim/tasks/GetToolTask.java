package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

public class GetToolTask extends Task<Long> {
	private final Warehouse warehouse;
	private final String toolType;
	private final Product product;


	public GetToolTask(Warehouse warehouse, String toolType, Product product) {
		this.warehouse=warehouse;
		this.toolType=toolType;
		this.product=product;
		

	}
	@Override
	protected void start() {
		Deferred<Tool> myTool=warehouse.acquireTool(toolType);
		myTool.whenResolved(()->{
			Tool tool=myTool.get();
			long value=tool.useOn(product); //compute the useOn value of the product
			complete(new Long(value));
			ReleaseToolTask task = new ReleaseToolTask(warehouse, tool);
			spawn (task);
			
		//	warehouse.releaseTool(tool); //return the tool to the warehouse
			
			
			});

	}

}
