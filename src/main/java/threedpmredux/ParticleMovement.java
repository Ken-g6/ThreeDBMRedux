package threedpmredux;

public final class ParticleMovement implements Runnable {
	private final Particles particle;
	private final MersenneTwisterFast random = new MersenneTwisterFast();

	private final int steps;

	private static final float x3c = .16666f;
	private static final float x5c = .0083143f;
	private static final float x7c = .00018542f;
	private static final float twopi = (float) Math.PI * 2f;
	private static final float pi = (float) Math.PI;
	private static final float halfpi = (float) Math.PI * .5f;

	public ParticleMovement(Particles thisparticle, int steps) {
		this.particle = thisparticle;
		this.steps = steps / 128;
	}

	@Override
	public void run() {

		final float[][] randomone = new float[128][8];
		final float[][] randomtwo = new float[128][8];

		int i = 0;

		do {
			loadrandoms(randomone, randomtwo);
			moveparticles(randomone, randomtwo);

			++i;
		} while (i < steps);
	}

	private void loadrandoms(final float[][] randomone, final float[][] randomtwo) {
		int k = 0;
		int l = 0;

		do {
			l = 0;

			do {
				randomone[k][l] = random.nextFloat();
				randomtwo[k][l] = random.nextFloat();

				++l;
			} while (l < 8);

			++k;
		} while (k < 128);
	}

	private void moveparticles(final float[][] randomone, final float[][] randomtwo) {
		int j = 0;

		float[] newx = new float[8];
		float[] newy = new float[8];
		float[] newz = new float[8];
		float newZ;
		float alpha;
		float r;
		float tempcos;
		do {
			int m = 0;
			do {
				newZ = (randomone[j][m] * 2) - 1;

				alpha = (twopi * randomtwo[j][m]);
				r = (float) Math.sqrt(1 - (newZ * newZ));
				tempcos = minicos(alpha - halfpi);

				newx[m] = r * tempcos;
				newy[m] = r * (Math.signum(pi - alpha) * (float) Math.sqrt(1.0f - (tempcos * tempcos)));
				newz[m] = newZ;
				++m;
			} while (m < 8);

			particle.addvalues(newx, newy, newz);

			++j;
		} while (j < 128);
	}

	private static float minicos(float tempx) {
		float x = tempx;
		boolean swap = false;

		if (x > pi) {
			x -= twopi;
		}

		if (x < -halfpi) {
			x += pi;
			swap = true;
		}
		else if (x > halfpi) {
			x = pi - x;
		}

		float x2 = x * x;
		float x3 = x2 * x;
		float x4 = x2 * x2;

		float result = x - (x3c * x3) + (x5c * x * x4) - (x7c * x3 * x4);

		if (swap) {
			return result;
		}
		return -result;
	}
}
