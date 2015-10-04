package ua.com.sofon.workoutlogger.parts;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class Workout extends BaseParticle implements Parcelable {

	public Workout() {
		this.name = "";
		this.description = "";
		this.exerciseList = new ArrayList<>();
	}

	public Workout(int id, String name, String description,
						ArrayList<TrainedExercise> exerciseList) {
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
		if (exerciseList != null) {
			this.exerciseList = exerciseList;
		} else {
			this.exerciseList = new ArrayList<>();
		}
	}

	//----- START Parcelable implementation ----------
	public Workout(Parcel in) {
		id = in.readInt();
		String[] strData = new String[2];
		in.readStringArray(strData);
		name = strData[0];
		description = strData[1];
		this.exerciseList = new ArrayList<>();
		in.readList(exerciseList, TrainedExercise.class.getClassLoader());
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeStringArray(new String[]{name, description});
		out.writeList(exerciseList);
	}

	public static final Parcelable.Creator<Workout> CREATOR
			= new Parcelable.Creator<Workout>() {
		public Workout createFromParcel(Parcel in) {
			return new Workout(in);
		}

		public Workout[] newArray(int size) {
			return new Workout[size];
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

	public ArrayList<TrainedExercise> getExerciseList() {
		return exerciseList;
	}

	public int getExercisesCount() {
		if (exerciseList != null) {
			return exerciseList.size();
		}
		return 0;
	}

	public void setExerciseList(ArrayList<TrainedExercise> exerciseList) {
		if (exerciseList != null) {
			this.exerciseList = exerciseList;
			for (int i = 0; i < exerciseList.size(); i++) {
				exerciseList.get(i).setWorkoutID(id);
			}
		}
	}

	/**
	 * Get one exercise from workout.
	 * @param position Position in exercise list.
	 * @return Selected exercise.
	 */
	public TrainedExercise getTrainedExe(int position) {
		return exerciseList.get(position);
	}

	/**
	 * Add one exercise to workout.
	 * @param e Exercise that want to add.
	 */
	public void addTrainedExe(TrainedExercise e) {
		if (e != null) {
			e.setWorkoutID(id);
			exerciseList.add(e);
		}
	}

	/**
	 * Remove {@link TrainedExercise} from exe list.
	 * @param id ID of {@link TrainedExercise} to delete.
	 */
	public void removeTrainedExe(int id) {
		for (int i = 0; i < exerciseList.size(); i++) {
			if (id == exerciseList.get(i).getId()) {
				exerciseList.remove(i);
				return;
			}
		}
	}

	/**
	 * Check {@link Workout} contains {@link TrainedExercise} with
	 * specified id.
	 * @param id ID of {@link TrainedExercise} to check.
	 * @return True if {@link Workout} contains {@link TrainedExercise}, else return false.
	 */
	public boolean containsTrainedExe(int id) {
		for (int i = 0; i < exerciseList.size(); i++) {
			if (id == exerciseList.get(i).getId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check that workout contains {@link Exercise} in {@link TrainedExercise} list.
	 * @param id {@link Exercise} id
	 * @return true if contains, else returns false.
	 */
	public boolean containsExercise(int id) {
		for (int i = 0; i < exerciseList.size(); i++) {
			if (exerciseList.get(i).getParentExeID() == id) {
				return true;
			}
		}
		return false;
	}

	public void removeItem(int position) {
		if (position >= 0 && position < exerciseList.size()) {
			exerciseList.remove(position);
		}
	}

	@Override
	public String toString() {
		StringBuilder exes = new StringBuilder();
		for (int i = 0; i < exerciseList.size(); i++) {
			exes.append(exerciseList.get(i).toString()).append(", ");
		}
		return "Workout["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_W_NAME + " = '" + name + "', "
				+ SQLiteHelper.COLUMN_W_DESCRIPTION + " = '" + description + "', ["
				+ exes.toString()
				+ "]]";
	}

	protected String name;
	protected String description;
	protected ArrayList<TrainedExercise> exerciseList;
}
