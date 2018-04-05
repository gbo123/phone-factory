package bgu.spl.a2.sim.conf;

import java.util.List;

public class Plan {

private List<String> parts;
private List<String> tools;
private String product;




public Plan(List<String> parts, List<String> tools, String product) {
	this.parts = parts;
	this.tools = tools;
	this.product = product;
}

public String getProduct() {
return product;
}

public void setProduct(String product) {
this.product = product;
}

public List<String> getTools() {
return tools;
}

public void setTools(List<String> tools) {
this.tools = tools;
}

public List<String> getParts() {
return parts;
}

public void setParts(List<String> parts) {
this.parts = parts;
}

}