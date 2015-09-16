package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.os.Parcelable;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;

/**
 * Created on 21.02.2015.
 * @author Dimowner
 */
public class Exercise extends BaseParticle implements Parcelable {

	public Exercise() {
		this.name = "";
		this.description = "";
	}

	public Exercise(int id, String name, String description) {
		if (id >= 0) {
			this.id = id;
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

	public Exercise(int id, String name, String description, int type) {
		if (id >= 0) {
			this.id = id;
		}
		if (type >= EXERCISE_TYPE_OTHER && type <= EXERCISE_TYPE_SHIN) {
			this.type = type;
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

	//----- START Parcelable implementation ----------
	public Exercise(Parcel in) {
		int[] intData = new int[2];
		in.readIntArray(intData);
		id = intData[0];
		type = intData[1];
		String[] data = new String[2];
		in.readStringArray(data);
		name = data[0];
		description = data[1];
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeIntArray(new int[] {id, type});
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
	//----- END Parcelable implementation ----------

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
		return "Exercise:["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_EXE_NAME + " = '" + name + "', "
				+ SQLiteHelper.COLUMN_EXE_DESCRIPTION + " = '" + description + "', "
				+ SQLiteHelper.COLUMN_EXE_TYPE + " = '" + type
				+ "']";
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

	protected int type = EXERCISE_TYPE_OTHER;
	protected String name;
	protected String description;
}
