package datatransfer;

import java.io.Serializable;

public class EmailTO implements Serializable, Cloneable {
	private String id;
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
