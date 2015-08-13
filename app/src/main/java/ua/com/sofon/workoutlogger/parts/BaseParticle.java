package ua.com.sofon.workoutlogger.parts;

/**
 * Base class for particles.
 * @author Dimowner
 */
public abstract class BaseParticle {

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

	/**
	 * Check that item has ID.
	 * @return Return true if ID not equals NO_ID;
	 */
	public boolean hasID() {
		return  (id >= 0);
	}

	public static final int NO_ID = -1;

	protected long id = NO_ID;
}
