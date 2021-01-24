package commands;

public class Item {

	private long id;
	private String answers;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
	    this.id = id;
	}
    public String getName() {
        return answers;
   }
   public void setName(String name) {
	    this.answers = name;
   }
	 
   @Override
   public String toString() {
	   return "User [id=" + id + ", name=" + answers + "]";
	    }
}
