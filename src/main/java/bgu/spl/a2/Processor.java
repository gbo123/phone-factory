package bgu.spl.a2;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;
   
    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
    }
    
    /*package*/ WorkStealingThreadPool getPool(){
    	return pool;
    }    
    /*package*/ int getId(){
    	return id;
    }    
    /*package*/ void addTask(Task<?> t){ //add task to the queue of the current processor
    	pool.getMyQueue(id).add(t);
    	pool.getVm().inc();
    }
    @Override
    public void run() {
    	while (pool.isShutdown()==false){
        int version= pool.getVm().getVersion();
    
    	ConcurrentLinkedDeque <Task<?>> myDeque= pool.getMyQueue(this.id);
    	if(!myDeque.isEmpty()&& pool.isShutdown()==false){ //check if there is a task in the queue and handle it
    		Task<?> temp=myDeque.poll();
    		if(temp!=null)
    			temp.handle(this);
    		
    	}
    	
    	else{//try stealing from other processors
    		
    		boolean ans=false;	
    		for (int i=((id+1)%pool.size()); i%pool.size()!=id && ans==false && pool.isShutdown()==false; i++){
    			if (pool.getMyQueue(i%pool.size()).size()>1 && pool.isShutdown()==false){
    				int toSteal= pool.getMyQueue(i%pool.size()).size()/2; //get the number of tasks we attempt to steal
    				for (int j=0; j<toSteal; j++){
    					if (pool.getMyQueue(i%pool.size()).isEmpty()==false){
    						Task <?> temp=pool.getMyQueue(i%pool.size()).pollLast();
    						if (temp!=null){
    							addTask(temp); //steal from another processor a single task
    							ans=true;
    						}
    				
    					}
    				}
	
    			}
    		}
    		
    		if(ans==false && (pool.getMyQueue(id).isEmpty())){ //if stealing didn't succeed, wait until a new task is submitted
    			try{
    				pool.getVm().await(version);
    				Thread.currentThread().sleep(1);
    			}
    			catch (InterruptedException e){
    			
    			}		
    		}
	
    	}

    	}
    }

}
