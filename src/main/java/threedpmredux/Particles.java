package threedpmredux;

public class Particles {
	private final float[] xpos = new float[8];
	private final float[] ypos = new float[8];
	private final float[] zpos = new float[8];

	public float getxpos(byte entry) {
		return entry > 7 ? 0 : xpos[entry];
	}

	public float getypos(byte entry) {
		return entry > 7 ? 0 : ypos[entry];
	}

	public float getzpos(byte entry) {
		return entry > 7 ? 0 : zpos[entry];
	}

	public void addvalues(float[] xvalues, float[] yvalues, float[] zvalues) {
		int i = 0;

		do {
			xpos[i] += xvalues[i];
			ypos[i] += yvalues[i];
			zpos[i] = Math.abs(zpos[i] + zvalues[i]);

			++i;
		} while (i < 8);
	}
}
