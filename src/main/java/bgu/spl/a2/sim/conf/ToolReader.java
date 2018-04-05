package bgu.spl.a2.sim.conf;

/**
 * Created by גיל on 24/12/2016.
 */
public class ToolReader {

    private String tool;
    private int qty;


    public ToolReader(String tool, int qty) {
        this.tool = tool;
        this.qty = qty;
    }

    public String getTool() {
        return tool;
    }

    public int getQty() {
        return qty;
   
    
    }

	public void setTool(String tool) {
		this.tool = tool;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}







}
