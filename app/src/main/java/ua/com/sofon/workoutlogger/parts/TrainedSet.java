package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.os.Parcelable;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class TrainedSet extends BaseParticle implements Parcelable {

	public TrainedSet() {
		this.number = 0;
		this.reps = 0;
		this.weight = 0f;
		this.trainedExeID = NO_ID;
	}

	public TrainedSet(int id, int number, int reps, float weight,
							int state, int trainedExeID) {
		if (id >= 0) {
			this.id = id;
		}
		if (number > 0) {
			this.number = number;
		} else {
			this.number = 0;
		}
		if (reps > 0) {
			this.reps = reps;
		} else {
			this.reps = 0;
		}
		if (weight > 0) {
			this.weight = weight;
		} else {
			this.weight = 0f;
		}
		if (state == STATE_EXECUTED) {
			this.state = STATE_EXECUTED;
		}
		if (trainedExeID > 0) {
			this.trainedExeID = trainedExeID;
		} else {
			this.trainedExeID = NO_ID;
		}
	}

	//----- START Parcelable implementation ----------
	public TrainedSet(Parcel in) {
		weight = in.readFloat();
		int[] intData = new int[4];
		in.readIntArray(intData);
		id = intData[0];
		number = intData[1];
		reps = intData[2];
		state = intData[3];
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeFloat(weight);
		out.writeIntArray(new int[] {id, number, reps, state});
	}

	public static final Parcelable.Creator<TrainedSet> CREATOR
			= new Parcelable.Creator<TrainedSet>() {
		public TrainedSet createFromParcel(Parcel in) {
			return new TrainedSet(in);
		}

		public TrainedSet[] newArray(int size) {
			return new TrainedSet[size];
		}
	};
	//----- END Parcelable implementation ----------

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		if (number > 0) {
			this.number = number;
		} else {
			this.number = 0;
		}
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		if (reps > 0) {
			this.reps = reps;
		} else {
			this.reps = 0;
		}
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		if (weight > 0) {
			this.weight = weight;
		} else {
			this.weight = 0f;
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		if (state == STATE_EXECUTED) {
			this.state = STATE_EXECUTED;
		} else {
			this.state = STATE_NOT_EXECUTED;
		}
	}

	public int getTrainedExeID() {
		return trainedExeID;
	}

	public void setTrainedExeID(int trainedExeID) {
		if (trainedExeID > 0) {
			this.trainedExeID = trainedExeID;
		} else {
			this.trainedExeID = NO_ID;
		}
	}

	@Override
	public String toString() {
		return "TrainedSet:["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_TE_ID + " = '" + trainedExeID + "', "
				+ SQLiteHelper.COLUMN_SET_NUMBER + " = '" + number + "', "
				+ SQLiteHelper.COLUMN_SET_REPS + " = '" + reps + "', "
				+ SQLiteHelper.COLUMN_SET_WEIGHT + " = '" + weight + "', "
				+ SQLiteHelper.COLUMN_SET_STATE + " = '" + state
				+ "']";
	}


	public static int STATE_NOT_EXECUTED = 0;
	public static int STATE_EXECUTED = 1;
	private int number;
	private int reps;
	private float weight;
	private int state = STATE_NOT_EXECUTED;
	private int trainedExeID = NO_ID;

}
