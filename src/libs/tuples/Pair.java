package libs.tuples;

public class Pair<T1, T2> extends ATuple<T1>
{
	public Pair(T1 first, T2 second)
	{
		super(first);
		this.second = second;
	}

	private T2 second;

	public T2 getSecond()
	{
		return second;
	}

	public void setSecond(T2 second)
	{
		this.second = second;
	}
}
