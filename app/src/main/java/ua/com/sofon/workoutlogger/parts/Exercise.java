package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 21.02.2015.
 * @author Dimowner
 */
public class Exercise implements Parcelable {

	public Exercise() {
		this.id = NO_ID;
		this.name = "";
		this.description = "";
		this.type = EXERCISE_TYPE_OTHER;
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

	public Exercise(long id, int type, String name, String description) {
		if (id >= 0) {
			this.id = id;
		} else {
			this.id = NO_ID;
		}
		if (type >= EXERCISE_TYPE_OTHER && type <= EXERCISE_TYPE_SHIN) {
			this.type = type;
		} else {
			this.type = EXERCISE_TYPE_OTHER;
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

	public Exercise(Parcel in) {
		id = in.readLong();
		type = in.readInt();
		String[] data = new String[2];
		in.readStringArray(data);
		name = data[0];
		description = data[1];
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeInt(type);
		out.writeStringArray(new String[]{name, description});
	}

	public static final Parcelable.Creator<Exercise> CREATOR
			= new Parcelable.Creator<Exercise>() {
		public Exercise createFromParcel(Parcel in) {
			return new Exercise(in);
		}

		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};

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
