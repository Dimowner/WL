package ua.com.sofon.workoutlogger.parts;

import java.util.List;
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
						List<TrainedExercise> exerciseList) {
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
		in.readList(exerciseList, Exercise.class.getClassLoader());
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

	public List<TrainedExercise> getExerciseList() {
		return exerciseList;
	}

	public int getExercisesCount() {
		if (exerciseList != null) {
			return exerciseList.size();
		}
		return 0;
	}

	public void setExerciseList(List<TrainedExercise> exerciseList) {
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
	public TrainedExercise getExercise(int position) {
		return exerciseList.get(position);
	}

	/**
	 * Add one exercise to workout.
	 * @param e Exercise that want to add.
	 */
	public void addExercise(TrainedExercise e) {
		if (e != null) {
			e.setWorkoutID(id);
			exerciseList.add(e);
		}
	}

	@Override
	public String toString() {
		StringBuilder exes = new StringBuilder();
		for (int i = 0; i < exerciseList.size(); i++) {
			exes.append(exerciseList.get(i).toString()).append(", ");
		}
		return "TrainedWorkout["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_W_NAME + " = '" + name + "', "
				+ SQLiteHelper.COLUMN_W_DESCRIPTION + " = '" + description + "', ["
				+ exes.toString()
				+ "]]";
	}

	protected String name;
	protected String description;
	protected List<TrainedExercise> exerciseList;
}
