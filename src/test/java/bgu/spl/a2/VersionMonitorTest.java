package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionMonitorTest {
	
	VersionMonitor vm;
	@Before
	public void setUp() throws Exception {
	vm=new VersionMonitor();
	}
	
	@After
	public void tearDown() throws Exception {
	vm=null;
	}

	@Test
	public void testGetVersion() {
		assertEquals(0, vm.getVersion());
	}

	@Test
	public void testInc() {	
		vm.inc();
		assertEquals(1,vm.getVersion());			
	}

	@Test
	public void testAwait() {
		boolean check=true;
		int currentVersion=vm.getVersion();
		 Thread t = new Thread(new Runnable() { public void run() { try 
			{
				vm.await(currentVersion);
			}
			catch (InterruptedException e){
				Thread.currentThread().interrupt();
			}; 
		 } 
		 });
		
		t.start();
		vm.inc();
		try 
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e) {	
		}
		if (!(t.isInterrupted())){
			check=false;
		}
		assertEquals(false,check);
	}
}
