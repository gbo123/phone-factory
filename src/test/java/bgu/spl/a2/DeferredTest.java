package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeferredTest {
	Deferred<Integer> def;
	int variable;
	

	@Before
	public void setUp() throws Exception {
		def=new Deferred<Integer>();
		variable=0;
	}

	@After
	public void tearDown() throws Exception {
		def=null;
		variable=0;
	}

	@Test
	public void testGet() {
		boolean rightException=true;
		try{
			def.get();
		}
		catch(IllegalStateException e){
			rightException=true;
		}
		catch (Exception e){
			rightException=false;
		}
		assertEquals(true, rightException);
		
		def.resolve((Integer)5);
		assertEquals((Integer)5, def.get());
			
	}

	@Test
	public void testIsResolved() {
		assertEquals(false, def.isResolved());
		def.resolve((Integer)5);
		assertEquals(true, def.isResolved());
	}

	@Test
	public void testResolve() {
		boolean rightException=true;
		Runnable run=()-> variable++;
		def.whenResolved(run);
		try{
			def.resolve((Integer)5);
		}
		catch(IllegalStateException e){
			rightException=true;
		}
		catch (Exception e){
			rightException=false;
		}
		assertEquals(true, rightException);
		
		assertEquals((Integer)5, def.get());
		
		assertEquals(1,variable);

	}

	@Test
	public void testWhenResolved() {
		Runnable run=()-> variable++;
		def.whenResolved(run);
		def.resolve((Integer)5);
		assertEquals(1,variable);
		
	}

}
