package blocks.demo.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DemoPerformance {

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		DemoPerformance demo = new DemoPerformance();
		// demo.forClassic();
		// demo.observableClassic();
		// demo.forExecutor();
		// demo.observableExecutor();

		// TODO Executor (plusieurs types)
		// TODO Observables (mêmes schedulers) --> plus simple
		// TODO Quasar avec Observables --> plus performant

	}

	// TODO Implémentation for classique (5000ms)
	public void forClassic() {

		List<Integer> initialData = getInitialData();

		List<Integer> result = new ArrayList<>();

		long startTime = System.currentTimeMillis();

		for (Integer data : initialData) {

			System.out.println(data);

			if (isPrime(data)) {
				result.add(data);
			}
		}

		long elapsedTime = System.currentTimeMillis() - startTime;

		System.out.println("Prime number count: " + result.size());
		System.out.println("... computed in " + elapsedTime + " ms");
	}

	// TODO Implémentation for utilisant un Executor (1051ms)
	public void forExecutor() throws InterruptedException, ExecutionException {

		// TODO

		List<Integer> initialData = getInitialData();

		final List<Integer> result = new ArrayList<>();
		List<Future<?>> futures = new ArrayList<>();

		ExecutorService exec = Executors.newCachedThreadPool();

		long startTime = System.currentTimeMillis();

		for (final Integer data : initialData) {

			System.out.println(data);

			futures.add(exec.submit(new Runnable() {
				@Override
				public void run() {
					if (isPrime(data)) {
						result.add(data);
					}
				}
			}));
		}

		for (Future<?> future : futures) {
			future.get();
		}

		long elapsedTime = System.currentTimeMillis() - startTime;

		System.out.println("Prime number count: " + result.size());
		System.out.println("... computed in " + elapsedTime + " ms");
	}

	// TODO Implémentation observable classique (5020ms)
	public void observableClassic() {

		List<Integer> initialData = getInitialData();

		Observable<List<Integer>> observable = Observable.from(initialData)
				.filter(new Func1<Integer, Boolean>() {
					@Override
					public Boolean call(Integer number) {
						return isPrime(number);
					}
				}).toList();

		final long startTime = System.currentTimeMillis();

		List<Integer> result = observable.toBlocking().first();

		long elapsedTime = System.currentTimeMillis() - startTime;

		System.out.println("Prime number count: " + result.size());
		System.out.println("... computed in " + elapsedTime + " ms");
	}

	// TODO Implémentation observable utilisant un Executor (3090ms)
	// TODO ACY Compile pas quand on fait un run...
	// public void observableExecutor() {
	//
	// List<Integer> initialData = getInitialData();
	//
	// final Func1<Integer, Boolean> filterFunc = new Func1<Integer, Boolean>()
	// {
	// @Override
	// public Boolean call(Integer number) {
	// return isPrime(number);
	// }
	// };
	//
	// Observable<List<Integer>> observable = Observable
	// .from(initialData)
	// .parallel(
	// new Func1<Observable<Integer>, Observable<Integer>>() {
	//
	// @Override
	// public Observable<Integer> call(
	// Observable<Integer> obs) {
	// return obs.filter(filterFunc);
	// }
	//
	// }, Schedulers.from(Executors.newCachedThreadPool()))
	// .toList();
	//
	// final long startTime = System.currentTimeMillis();
	//
	// List<Integer> result = observable.toBlocking().first();
	//
	// long elapsedTime = System.currentTimeMillis() - startTime;
	//
	// System.out.println("Prime number count: " + result.size());
	// System.out.println("... computed in " + elapsedTime + " ms");
	//
	// // TODO Memory leak
	// }

	private List<Integer> getInitialData() {
		List<Integer> result = new ArrayList<>();

		Random generator = new Random(12);
		for (int i = 0; i < 5; i++) {
			result.add(generator.nextInt());
		}

		return result;
	}

	private static final boolean isPrime(int num) {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (num % 2 == 0)
			return false;
		for (int i = 3; i * i <= num; i += 2)
			if (num % i == 0)
				return false;
		return true;
	}
}
