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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Exercise:[ id = '" + id + "', "
				+ "name = '" + name + "', "
				+ "description = '" + description + "']";
	}

	private static final long serialVersionUID = 4018750221300108624L;

	public static final int EXERCISE_TYPE_OTHER		= 0;
	public static final int EXERCISE_TYPE_BACK		= 1;
	public static final int EXERCISE_TYPE_BREAST		= 2;
	public static final int EXERCISE_TYPE_SHOULDERS	= 3;
	public static final int EXERCISE_TYPE_BELLY		= 4;
	public static final int EXERCISE_TYPE_BICEPS		= 5;
	public static final int EXERCISE_TYPE_TRICEPS 	= 6;
	public static final int EXERCISE_TYPE_FOREARMS	= 7;
	public static final int EXERCISE_TYPE_FEET		= 8;
	public static final int EXERCISE_TYPE_SHIN		= 9;

	public static final long NO_ID = -1;

	private long id;
	private int type;
	private String name;
	private String description;
}
