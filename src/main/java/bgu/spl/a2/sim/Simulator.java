/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.Deferred;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.JsonData;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.conf.Order;
import bgu.spl.a2.sim.conf.Plan;
import bgu.spl.a2.sim.conf.ToolReader;
import bgu.spl.a2.sim.tasks.ManufactureTask;
import bgu.spl.a2.sim.tasks.WaveTask;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
	private static WorkStealingThreadPool pool;
	private static String json;

	/**
	 * {@link WorkStealingThreadPool myWorkStealingThreadPool};
	 * Begin the simulation
	 * Should not be called before attachWorkStealingThreadPool()
	 */
	public static ConcurrentLinkedQueue<Product> start() {
		ConcurrentLinkedQueue<Product> products = new ConcurrentLinkedQueue<Product>();
		Gson gson = new Gson();

		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader(json));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		pool.start();
		JsonData jsonD = gson.fromJson(reader, JsonData.class);
		
		Warehouse warehouse = new Warehouse();

		
		List<ToolReader> tools = jsonD.getTools();
		for (int i = 0; i < tools.size(); i++) { //add the tools to the warehouse
			Tool newTool;
			if (tools.get(i).getTool().equals("gs-driver"))
				newTool = new GcdScrewDriver();
			else if (tools.get(i).getTool().equals("np-hammer"))
				newTool = new NextPrimeHammer();
			else
				newTool = new RandomSumPliers();
			warehouse.addTool(newTool, tools.get(i).getQty());
		}
		List<Plan> plans = jsonD.getPlans();
		for (int i = 0; i < plans.size(); i++) { //add the plans to the warehouse
			ManufactoringPlan newPlan;
			String[] planTools = new String[plans.get(i).getTools().size()];
			for (int j = 0; j < plans.get(i).getTools().size(); j++)
				planTools[j] = plans.get(i).getTools().get(j);
			String[] planParts = new String[plans.get(i).getParts().size()];
			for (int j = 0; j < plans.get(i).getParts().size(); j++)
				planParts[j] = plans.get(i).getParts().get(j);
			newPlan = new ManufactoringPlan(plans.get(i).getProduct(), planParts, planTools);
			warehouse.addPlan(newPlan);
		}

		List<List<Order>> waves = jsonD.getWaves();


			for (int i = 0; i < waves.size(); i++) {
			CountDownLatch l = new CountDownLatch(1);
			List<Order> orders = waves.get(i); //create waveTask for each wave
			WaveTask newTask = new WaveTask(orders,warehouse,products);
			pool.submit(newTask);
			newTask.getResult().whenResolved(() -> {

				l.countDown(); //waits until all the tasks of the current wave are done
			});

			try {
				l.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

			try {
				pool.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		return products;
}




	/**
	 * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	 * @param - the WorkStealingThreadPool which will be used by the simulator
	 */
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){

		pool=myWorkStealingThreadPool;
	}

	public static int getWaveQty (List<Order> wave){
		int qty=0;
		for (int i=0; i<wave.size(); i++){
			qty=qty+wave.get(i).getQty();
		}
		return qty;
	}



	public static void main(String [] args)  {

		json=args [0];
		
		Gson gson = new Gson();

		JsonReader reader = null;
		try {
			reader = new JsonReader(new FileReader(json));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JsonData json = gson.fromJson(reader, JsonData.class);
		WorkStealingThreadPool pool=new WorkStealingThreadPool(json.getThreads());
		Simulator s=new Simulator();
		s.attachWorkStealingThreadPool(pool);
		ConcurrentLinkedQueue<Product> SimulationResult;
		SimulationResult = s.start();
		FileOutputStream fout;
		try {
			fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(SimulationResult);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}
