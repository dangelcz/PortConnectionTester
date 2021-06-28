package libs.tuples;

public class Quartet<T1, T2, T3, T4> extends Triplet<T1, T2, T3>
{
	private T4 fourth;

	public Quartet(T1 first, T2 second, T3 third, T4 fourth)
	{
		super(first, second, third);
		this.fourth = fourth;
	}

	public T4 getFourth()
	{
		return fourth;
	}

	public void setFourth(T4 fourth)
	{
		this.fourth = fourth;
	}
}
