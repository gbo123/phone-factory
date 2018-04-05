package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;

import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.Deferred;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse {
	private LinkedList <ManufactoringPlan> plans;
	private AtomicInteger numOfgcdScrewDriver;
	private AtomicInteger numOfnextPrimeHammer;
	private AtomicInteger numOfrandomSumPliers;
	private ConcurrentLinkedQueue<Deferred<Tool>> gcdScrewDriverWaitingList;
	private ConcurrentLinkedQueue<Deferred<Tool>> nextPrimeHammerWaitingList;
	private ConcurrentLinkedQueue<Deferred<Tool>> randomSumPliersWaitingList;

	/**
	* Constructor
	*/
    public Warehouse ( ){
    	plans=new LinkedList<ManufactoringPlan>();
    	numOfgcdScrewDriver=new AtomicInteger(0);
    	numOfnextPrimeHammer=new AtomicInteger(0);
    	numOfrandomSumPliers=new AtomicInteger(0);
    	gcdScrewDriverWaitingList=new ConcurrentLinkedQueue<Deferred<Tool>>();
    	nextPrimeHammerWaitingList=new ConcurrentLinkedQueue<Deferred<Tool>>();
    	randomSumPliersWaitingList=new ConcurrentLinkedQueue<Deferred<Tool>>();
    	
    }

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
    public synchronized Deferred<Tool> acquireTool(String type){ //the method is synchronized so that the amount of tools in the inventory will be correct when every time a thread  try to acquire tool
    	Tool tool;
    	Deferred<Tool> temp = new Deferred<Tool> ();
    	
    	if (type.equals("gs-driver")){
    		
    		
    		tool=new GcdScrewDriver();
    		if (numOfgcdScrewDriver.get()<1){ //no tool is available
    			gcdScrewDriverWaitingList.add(temp); //add to the waiting list
    			numOfgcdScrewDriver.decrementAndGet();
    			return temp;
    		}
    		else{
    			numOfgcdScrewDriver.decrementAndGet(); 
    			temp.resolve(tool);
    			return temp;
    		}		
    	}
    	
    	else if (type.equals("np-hammer")){
    		tool=new NextPrimeHammer();
    		if (numOfnextPrimeHammer.get()<1){ //no tool is available
    			nextPrimeHammerWaitingList.add(temp); //add to the waiting list
    			numOfnextPrimeHammer.decrementAndGet(); 
    			return temp;
    		}
    		else{
    			numOfnextPrimeHammer.decrementAndGet(); 
    			temp.resolve(tool);
    			return temp;
    		}		
    	}
    	
    	else {
    		tool=new RandomSumPliers();
       		if (numOfrandomSumPliers.get()<1){ //no tool is available
       			randomSumPliersWaitingList.add(temp); //add to the waiting list
    			numOfrandomSumPliers.decrementAndGet(); 
    			return temp;
    		}
    		else{
    			numOfrandomSumPliers.decrementAndGet(); 
    			temp.resolve(tool);
    			return temp;
    		}	
    	}
    }

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/
    public synchronized void releaseTool(Tool tool){//the method is synchronized so that the amount of tools in the inventory will be correct when every time a thread try release tool 
    	String type=tool.getType();
    	Deferred<Tool> temp;
    	
    	if (type.equals("gs-driver")){
    		temp=gcdScrewDriverWaitingList.poll();
    		if (temp==null){ //check if the waiting list is empty
    			numOfgcdScrewDriver.incrementAndGet();
    		}
    		else{
    			numOfgcdScrewDriver.incrementAndGet();
    			temp.resolve(tool);
    		}
    	}
    	
    	else if (type.equals("np-hammer")){
    		temp=nextPrimeHammerWaitingList.poll();
    		if (temp==null){ //check if the waiting list is empty
    			numOfnextPrimeHammer.incrementAndGet();
    		}
    		else{
    			numOfnextPrimeHammer.incrementAndGet();
    			temp.resolve(tool);
    		}
    	}
    	
    	else {
    		temp=randomSumPliersWaitingList.poll();
    		if (temp==null) //check if the waiting list is empty
    			numOfrandomSumPliers.incrementAndGet();
    		else{	
    			numOfrandomSumPliers.incrementAndGet();
    			temp.resolve(tool);
    		}
    	}
    }

	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product){
    	for (int i=0; i<plans.size(); i++){
    		if (plans.get(i).getProductName().equals(product))
    			return plans.get(i);
    			
    	}
    	return null;
    }

	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
    public void addPlan(ManufactoringPlan plan){
    	plans.add(plan);
    }
    
	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
    public void addTool(Tool tool, int qty){
    	if (tool.getType().equals("gs-driver")){
    		numOfgcdScrewDriver.addAndGet(qty);
    	}
    	if (tool.getType().equals("np-hammer")){
    		numOfnextPrimeHammer.addAndGet(qty);
    	}
    	if (tool.getType().equals("rs-pliers")){
    		numOfrandomSumPliers.addAndGet(qty);
    	}
    }

}
