package ua.com.sofon.workoutlogger.parts;

import java.io.Serializable;

/**
 * Created on 21.02.2015.
 * @author Dimowner
 */
public class Exercise implements Serializable {

	public Exercise() {
		this.id = NO_ID;
		this.name = "";
		this.description = "";
	}

	public Exercise(long id, String name, String description) {
		if (id >= 0) {
			this.id = id;
		} else {
			this.id = NO_ID;
		}
		if (name != null) {
			this.name = name;
		} else {
			this.name = "";
		}
		if (description != null) {
			this.description = description;
		} else {
			this.description = "";
		}
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
		if (id >= 0) {
			this.id = id;
		} else {
			this.id = NO_ID;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name;
		} else {
			this.name = "";
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		} else {
			this.description = "";
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type >= EXERCISE_TYPE_OTHER && type <= EXERCISE_TYPE_SHIN) {
			this.type = type;
		} else {
			this.type = EXERCISE_TYPE_OTHER;
		}
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
