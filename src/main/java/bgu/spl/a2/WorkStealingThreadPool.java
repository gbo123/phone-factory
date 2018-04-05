package bgu.spl.a2;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {
				


	private Processor[] processors;
	private Thread[] threads;
	private VersionMonitor vm;
	private LinkedList <ConcurrentLinkedDeque <Task<?>>> processorsQueues;
	private boolean shutdown;
	private int numberOfTasks;
	

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) { //constructor
    	processors=new Processor[nthreads];
    	threads=new Thread [nthreads];
    	vm=new VersionMonitor();
    	numberOfTasks=0;
    	processorsQueues=new LinkedList<ConcurrentLinkedDeque <Task<?>>>();
    	for (int i=0; i<nthreads; i++){
    		processorsQueues.add(new ConcurrentLinkedDeque<Task<?>>());
    	}
    	shutdown=false;
    	
    }

    public int getNumberOfTasks() {
		return numberOfTasks;
	}

	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	/**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
    	int random=(int) (Math.random()*processors.length);
    	(processorsQueues.get(random)).addFirst(task);
    	numberOfTasks++;
    	vm.inc();
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
    	shutdown=true;
    	for (int i=0; i<threads.length; i++){
    		vm.inc();
    		threads[i].interrupt();
    	}
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
    	for (int i=0; i<processors.length; i++){ //initialize processors array
    		processors[i]=new Processor(i, this);
    		Thread t= new Thread (processors[i]);
    		threads [i]=t;
    		t.start();
    	}
 	
    }
    
    public ConcurrentLinkedDeque <Task<?>> getMyQueue (int myId){
    	return processorsQueues.get(myId);
    }

    
    
	public VersionMonitor getVm() {
		return vm;
	}

	public int size() {
		
		return processorsQueues.size();
	}

	public boolean isShutdown() {
		return shutdown;
	}
  
}
