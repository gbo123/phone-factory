package bgu.spl.a2.sim.conf;

/**
 * Created by גיל on 24/12/2016.
 */
public class Order
{

private String product;
private int qty;
private long startId;


public Order(String product, int qty, int startId) {
    this.product = product;
    this.qty = qty;
    this.startId = startId;
}


public String getProduct() {
	return product;
}


public int getQty() {
	return qty;
}


public long getStartId() {
	return startId;
}


public void setProduct(String product) {
	this.product = product;
}


public void setQty(int qty) {
	this.qty = qty;
}


public void setStartId(long startId) {
	this.startId = startId;
}














}

