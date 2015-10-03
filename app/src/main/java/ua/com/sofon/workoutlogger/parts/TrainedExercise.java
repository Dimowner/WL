package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.os.Parcelable;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;

/**
 * Created on 06.09.2015.
 * @author Dimowner
 */
public class TrainedExercise extends Exercise {

	public TrainedExercise() {
		super();
	}

	public TrainedExercise(int id, String name, String description) {
		super(id, name, description);
	}

	public TrainedExercise(Exercise exe) {
		super(NO_ID, exe.getName(), exe.getDescription());
		parentExeID = exe.getId();
	}

	public TrainedExercise(int id, String name, String description, int type,
							int number, int workoutID, int trainedWorkoutID, int parentExeID) {
		super(id, name, description);

		if (type >= Exercise.EXERCISE_TYPE_OTHER && type <= Exercise.EXERCISE_TYPE_SHIN) {
			this.type = type;
		}
		if (number != NUMBER_DEFAULT) {
			this.number = number;
		}
		if (workoutID > 0) {
			this.workoutID = workoutID;
		}
		if (trainedWorkoutID > 0) {
			this.trainedWorkoutID = trainedWorkoutID;
		}
		if (parentExeID > 0) {
			this.parentExeID = parentExeID;
		}
	}
	//----- START Parcelable implementation ----------
	public TrainedExercise(Parcel in) {
		String[] strData = new String[2];
		in.readStringArray(strData);
		name = strData[0];
		description = strData[1];
		int[] intData = new int[6];
		in.readIntArray(intData);
		id = intData[0];
		type = intData[1];
		number = intData[2];
		workoutID = intData[3];
		trainedWorkoutID = intData[4];
		parentExeID = intData[5];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeStringArray(new String[] {name, description});
		out.writeIntArray(new int[]{id, type, number, workoutID, trainedWorkoutID, parentExeID});
	}

	public static final Parcelable.Creator<TrainedExercise> CREATOR
			= new Parcelable.Creator<TrainedExercise>() {
		public TrainedExercise createFromParcel(Parcel in) {
			return new TrainedExercise(in);
		}

		public TrainedExercise[] newArray(int size) {
			return new TrainedExercise[size];
		}
	};
	//----- END Parcelable implementation ----------

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type >= Exercise.EXERCISE_TYPE_OTHER
				&& type <= Exercise.EXERCISE_TYPE_SHIN) {
			this.type = type;
		} else {
			this.type = Exercise.EXERCISE_TYPE_OTHER;
		}
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		if (number > 0) {
			this.number = number;
		} else {
			this.number = NUMBER_DEFAULT;
		}
	}

	public int getWorkoutID() {
		return workoutID;
	}

	public void setWorkoutID(int workoutID) {
		if (workoutID > 0) {
			this.workoutID = workoutID;
		} else {
			this.workoutID = NO_ID;
		}
	}

	public int getTrainedWorkoutID() {
		return trainedWorkoutID;
	}

	public void setTrainedWorkoutID(int trainedWorkoutID) {
		if (trainedWorkoutID > 0) {
			this.trainedWorkoutID = trainedWorkoutID;
		} else {
			this.trainedWorkoutID = NO_ID;
		}
	}

	public int getParentExeID() {
		return parentExeID;
	}

	public void setParentExeID(int parentExeID) {
		if (parentExeID > 0) {
			this.parentExeID = parentExeID;
		} else {
			this.parentExeID = NO_ID;
		}
	}

	@Override
	public String toString() {
		return "TrainedExercise:["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_PARENT_EXE_ID + " = '" + parentExeID + "', "
				+ SQLiteHelper.COLUMN_TE_NAME + " = '" + name + "', "
				+ SQLiteHelper.COLUMN_TE_DESCRIPTION + " = '" + description + "', "
				+ SQLiteHelper.COLUMN_TE_TYPE + " = '" + type + "', "
				+ SQLiteHelper.COLUMN_TE_NUMBER + " = '" + number + "', "
				+ SQLiteHelper.COLUMN_TW_ID + " = '" + trainedWorkoutID + "', "
				+ SQLiteHelper.COLUMN_WORKOUT_ID + " = '" + workoutID
				+ "']";
	}


	public static final int NUMBER_DEFAULT = 0;
	private int parentExeID = NO_ID;
	private int number = NUMBER_DEFAULT;
	private int workoutID = NO_ID;
	private int trainedWorkoutID = NO_ID;

	public static class Builder {

		public TrainedExercise build() {
			return new TrainedExercise(
					id, name, description, type, number,
					workoutID, trainedWorkoutID, parentExeID
			);
		}

		public Builder setId(int id) {
			this.id = id;
			return this;
		}

		public Builder setType(int type) {
			this.type = type;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setParentExeID(int parentExeID) {
			this.parentExeID = parentExeID;
			return this;
		}

		public Builder setNumber(int number) {
			this.number = number;
			return this;
		}

		public Builder setWorkoutID(int workoutID) {
			this.workoutID = workoutID;
			return this;
		}

		public Builder setTrainedWorkoutID(int trainedWorkoutID) {
			this.trainedWorkoutID = trainedWorkoutID;
			return this;
		}

		private int id = NO_ID;
		private int type = EXERCISE_TYPE_OTHER;
		private String name;
		private String description;
		private int parentExeID = NO_ID;
		private int number = NUMBER_DEFAULT;
		private int workoutID = NO_ID;
		private int trainedWorkoutID = NO_ID;
	}
}
