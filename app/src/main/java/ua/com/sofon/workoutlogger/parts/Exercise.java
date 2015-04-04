package ua.com.sofon.workoutlogger.parts;

import java.io.Serializable;

/**
 * Created on 21.02.2015.
 * @author Dimowner
 */
public class Exercise implements Serializable {

	public Exercise() {
		this.id = NO_ID;
		this.name = null;
		this.description = null;
	}

	public Exercise(long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	/**
	 * Check that Exercise has ID.
	 * @return Return true if ID not equals NO_ID;
	 */
	public boolean hasID() {
		return  (id != NO_ID);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Exercise:[ id = '" + id + "', "
				+ "name = '" + name + "', "
				+ "description = '" + description + "']";
	}

	private static final long serialVersionUID = 4018750221300108624L;

	public static final long NO_ID = -1;
	private long id;
	private String name;
	private String description;
}
