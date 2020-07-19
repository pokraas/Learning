package Timing;

/**
 * A class to conveniently time procedures and other things.
 * @author Alex Pokras
 */
public class Timer {
	private static final double NS_IN_S = 1000000000d;
	private long start = 0;
	private long totalTime = 0;
	private int totalCalls = 0;
	
	/** Reset the timer */
	public void reset() {
		start=0;
		totalTime=0;
		totalCalls=0;
	}
	
	/** Start to measure a new period of time, increase the number of calls
	 * (typically call at the beginning of a procedure) */
	public void start() {
		start = System.nanoTime();
		totalCalls++;
	}
	
	/** Stop measuring the period of time and add it to the total time 
	 * (typically call at the end of a procedure) */
	public long stop() {
		totalTime += (System.nanoTime() - start);
		return totalTime;
	}
	
	/** Total time: &Sigma;(time_of_stop - time_of_start) in nanoseconds*/
	public long time() {
		return totalTime;
	}
	
	/** Total time: &Sigma;(time_of_stop - time_of_start) in seconds*/
	public double timeS() {
		return (double)totalTime / NS_IN_S;
	}
	
	/** Total calls: number of times start() has been called */
	public int calls() {
		return totalCalls;
	}
	
	/** Total time in nanoseconds / total calls */
	public double timePerCall() {
		return (double)totalTime/totalCalls;
	}
	
	/** Total time in seconds / total calls */
	public double timeSPerCall() {
		return ((double)totalTime/NS_IN_S) / totalCalls;
	}
	
	/** Total time in nanoseconds / n. Helps estimate O() complexity */
	public double timeOverN(int n){
		return (double)totalTime/n;
	}
	
	/** Total calls / n. Helps estimate O() complexity */
	public double callsOverN(int n){
		return (double)totalCalls/n;
	}
	
	/** Total time, total calls and time/call ratio as String */
	@Override
	public String toString() {
		return time()+" "+calls()+" "+timePerCall();
	}
}
