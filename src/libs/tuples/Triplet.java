package libs.tuples;

public class Triplet<T1, T2, T3> extends Pair<T1, T2>
{
	private T3 third;

	public Triplet(T1 first, T2 second, T3 third)
	{
		super(first, second);
		this.third = third;
	}

	public T3 getThird()
	{
		return third;
	}

	public void setThird(T3 third)
	{
		this.third = third;
	}
}
