package ua.com.sofon.workoutlogger.parts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 22.02.2015.
 * @author Dimowner
 */
public class Set extends BaseParticle implements Parcelable {

	public Set(long id, int setNumber, int reps, int weight) {
		this.id = id;
		this.setNumber = setNumber;
		this.reps = reps;
		this.weight = weight;
	}

	//----- START Parcelable implementation ----------
	public Set(Parcel in) {
		id = in.readLong();
		int[] intData = new int[3];
		in.readIntArray(intData);
		setNumber = intData[0];
		reps = intData[1];
		weight = intData[2];
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeIntArray(new int[] {setNumber, reps, weight});
	}

	public static final Parcelable.Creator<Set> CREATOR
			= new Parcelable.Creator<Set>() {
		public Set createFromParcel(Parcel in) {
			return new Set(in);
		}

		public Set[] newArray(int size) {
			return new Set[size];
		}
	};
	//----- END Parcelable implementation ----------

	public int getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(int setNumber) {
		this.setNumber = setNumber;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	private int setNumber;
	private int reps;
	private int weight;

}
