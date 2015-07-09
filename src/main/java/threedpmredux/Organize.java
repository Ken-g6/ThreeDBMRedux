package threedpmredux;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Organize {

	public void handlethreads(Collection<Particles> particlelist, int steps, boolean threaded) {
		int threads = 1;

		if (threaded) {
			threads = Runtime.getRuntime().availableProcessors();
		}

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads, (r) -> {
			Thread thread = new Thread(r);
			thread.setPriority(Thread.MIN_PRIORITY);
			return thread;
		});
		executor.prestartAllCoreThreads();
		particlelist.stream()
			.map((p) -> new ParticleMovement(p, steps))
			.forEach(executor::execute);

		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		}
		catch (InterruptedException e) {
			//Do nothing
		}
	}
}
