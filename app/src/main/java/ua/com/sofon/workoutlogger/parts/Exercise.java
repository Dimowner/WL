package ua.com.sofon.workoutlogger.parts;

import java.util.Arrays;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Created on 21.02.2015.
 * @author Dimowner
 */
public class Exercise extends BaseParticle implements Parcelable {

	public Exercise() {
		this.name = "";
		this.description = "";
		this.groups = new int[0];
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
		this.groups = new int[0];
	}

	public Exercise(int id, String name, String description, int type, int[] groups) {
		if (id >= 0) {
			this.id = id;
		}
		if (type >= MUSCLE_GROUP_OTHER && type <= MUSCLE_GROUP_SHIN) {
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
		setGroups(groups);
	}

	//----- START Parcelable implementation ----------
	public Exercise(Parcel in) {
		int[] intData = new int[2];
		in.readIntArray(intData);
		id = intData[0];
		type = intData[1];
		String[] data = new String[3];
		in.readStringArray(data);
		name = data[0];
		description = data[1];
		groups = groupsStrToArray(data[2]);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeIntArray(new int[] {id, type});
		out.writeStringArray(new String[]{name, description, groupsArrayToStr(groups)});
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
		if (type >= MUSCLE_GROUP_OTHER && type <= MUSCLE_GROUP_SHIN) {
			this.type = type;
		} else {
			this.type = MUSCLE_GROUP_OTHER;
		}
	}

	/**
	 * Convert string that contains muscle groups ids into int array.
	 * @param groups group ids example: "0;1;2;3;4;9"
	 * @return Converted groups into int array.
	 */
	public static int[] groupsStrToArray(String groups) {
		if (groups != null && !groups.isEmpty()) {
			String[] grStr = groups.split(";");
			SparseIntArray ids = new SparseIntArray(grStr.length);
			for (int i = 0; i < grStr.length; i++) {
				try {
					ids.put(i, Integer.parseInt(grStr[i]));
				} catch (NumberFormatException e) {
					LOGE(LOG_TAG, "", e);
				}
			}
			int[] grInt = new int[grStr.length];
			for (int i = 0; i < ids.size(); i++) {
				grInt[i] = ids.get(i);
			}
			return grInt;
		}
		return new int[0];
	}

	/**
	 * Convert int array that contains muscle groups of exercise into String separated by ";"
	 * @return String that contains muscle groups example "0;1;2;3;4;5;9"
	 */
	public static String groupsArrayToStr(int[] groups) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < groups.length; i++) {
			sb.append(groups[i]).append(";");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}

	public String getGroupsIdsStr() {
		return groupsArrayToStr(groups);
	}

	public String getMuscleGroupsNames(Context context) {
		StringBuilder sb = new StringBuilder();
		String[] names = context.getResources()
				.getStringArray(R.array.exercises_types_array);
		for (int i = 0; i < groups.length; i++) {
			sb.append(names[groups[i]]).append(", ");
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.toString();
	}

	public int[] getMuscleGroupsIds() {
		return groups;
	}

	public void setGroups(int[] ids) {
		boolean ok = true;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] < MUSCLE_GROUP_OTHER || ids[i] > MUSCLE_GROUP_SHIN) {
				ok = false;
			}
		}
		if (ok) {
			this.groups = ids;
		} else {
			LOGE(LOG_TAG, "setGroups error param \"ids\" has wrong data.");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Exercise)) {
			return false;
		}
		Exercise e = (Exercise) o;
		return (this.id == e.getId()
						&& this.name.equals(e.getName())
						&& this.description.equals(e.getDescription())
						&& Arrays.equals(this.groups, e.getMuscleGroupsIds()));
	}

	@Override
	public String toString() {
		return "Exercise:["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_EXE_NAME + " = '" + name + "', "
				+ SQLiteHelper.COLUMN_EXE_DESCRIPTION + " = '" + description + "', "
				+ SQLiteHelper.COLUMN_EXE_TYPE + " = '" + type
				+ SQLiteHelper.COLUMN_MUSCLE_GROUPS + " = " + groupsArrayToStr(groups)
				+ "']";
	}


	public static final int MUSCLE_GROUP_OTHER		= 0;
	public static final int MUSCLE_GROUP_BACK			= 1;
	public static final int MUSCLE_GROUP_BREAST		= 2;
	public static final int MUSCLE_GROUP_SHOULDERS	= 3;
	public static final int MUSCLE_GROUP_BELLY 		= 4;
	public static final int MUSCLE_GROUP_BICEPS		= 5;
	public static final int MUSCLE_GROUP_TRICEPS		= 6;
	public static final int MUSCLE_GROUP_FOREARMS	= 7;
	public static final int MUSCLE_GROUP_FEET			= 8;
	public static final int MUSCLE_GROUP_SHIN			= 9;

	protected int type = 0;
	protected int[] groups;
	protected String name;
	protected String description;

	/** Tag for logging messages. */
	private static final String LOG_TAG = LogUtils.makeLogTag("Exercise");
}
