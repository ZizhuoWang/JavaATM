package myATM;

/**
 * @author wang
 * 一切存款账户的接口
 * 继承了Runnable类
 * 有run方法和increase加息方法
 */
public interface Balance extends Runnable {
	
	@Override
	public void run();
	
	public void increase();
}
