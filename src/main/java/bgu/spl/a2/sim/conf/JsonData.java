package bgu.spl.a2.sim.conf;

import java.util.List;

/**
 * Created by גיל on 24/12/2016.
 */
public class JsonData {

    private int threads;
    private List<ToolReader> tools;
    private List<Plan> plans;
    private List<List<Order>> waves;



    public JsonData(int threads, List<ToolReader> tools, List<Plan> plans, List<List<Order>> waves) {
        this.threads = threads;
        this.tools = tools;
        this.plans = plans;
        this.waves = waves;
    }



	public int getThreads() {
		return threads;
	}



	public List<ToolReader> getTools() {
		return tools;
	}



	public List<Plan> getPlans() {
		return plans;
	}



	public List<List<Order>> getWaves() {
		return waves;
	}



	public void setThreads(int threads) {
		this.threads = threads;
	}



	public void setTools(List<ToolReader> tools) {
		this.tools = tools;
	}



	public void setPlans(List<Plan> plans) {
		this.plans = plans;
	}



	public void setWaves(List<List<Order>> waves) {
		this.waves = waves;
	}




}