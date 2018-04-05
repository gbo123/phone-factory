package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

public class ReleaseToolTask extends Task<Integer> {
	
	private final Tool tool;
	private final Warehouse warehouse;
	
	
	
	
	public ReleaseToolTask(Warehouse warehouse, Tool tool) {
		this.warehouse=warehouse;
		this.tool=tool;
		
		

	}

	

	@Override
	protected void start() {
		warehouse.releaseTool(tool);
		complete(0);
		
	}

}
