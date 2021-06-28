package libs.tuples;

public class ATuple<T1>
{
	private T1 first;

	public ATuple(T1 first)
	{
		this.first = first;
	}

	public T1 getFirst()
	{
		return first;
	}

	public void setFirst(T1 first)
	{
		this.first = first;
	}
}
