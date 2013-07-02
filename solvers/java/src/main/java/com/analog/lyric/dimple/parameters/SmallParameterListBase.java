package com.analog.lyric.dimple.parameters;

import net.jcip.annotations.ThreadSafe;

/**
 * Abstract base class for {@link IParameterList} implementations with eight or less parameters.
 * <p>
 * Provides implementation of {@link #isFixed(int)} and {@link #setFixed(int, boolean)} methods
 * based on a small bit mask.
 */
@ThreadSafe
public abstract class SmallParameterListBase<Key extends IParameterKey> extends AbstractParameterList<Key>
{
	/*-------
	 * State
	 */
	
	private static final long serialVersionUID = 1L;
	protected volatile byte _fixedMask;

	/*--------------
	 * Construction
	 */
	
	protected SmallParameterListBase(boolean fixed)
	{
		_fixedMask = fixed ? (byte)-1 : (byte)0 ;
	}

	protected SmallParameterListBase(SmallParameterListBase<Key> that)
	{
		_fixedMask = that._fixedMask;
	}
	
	/*------------------------
	 * IParameterList methods
	 */
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation supports individual parameter sharing and will return true.
	 */
	@Override
	public boolean canShare()
	{
		return true;
	}
	
	@Override
	public final double get(int index)
	{
		return getParameterValue(index).get();
	}
	
	@Override
	public final SharedParameterValue getSharedValue(int index)
	{
		return getParameterValue(index).asSharedValue();
	}
	
	@Override
	public final boolean isFixed(int index)
	{
		return isSet(index, _fixedMask);
	}
	
	@Override
	public final boolean isShared(int index)
	{
		return getParameterValue(index).isShared();
	}

	@Override
	public void set(int index, double value)
	{
		assertNotFixed(index);
		getParameterValue(index).set(value);
		valueChanged(index);
	}

	@Override
	public final void setAllFixed(boolean fixed)
	{
		_fixedMask = (byte)(fixed ? -1 : 0);
	}
	
	@Override
	public void setFixed(int index, boolean fixed)
	{
		_fixedMask = setBit(index, _fixedMask, fixed);
	}

	@Override
	public void setShared(int index, boolean shared)
	{
		setSharedValue(index, shared ? getParameterValue(index).toShared() : null);
	}
	
	@Override
	public final void setSharedValue(int index, SharedParameterValue value)
	{
		assertNotFixed(index);
		setParameterValue(index, value == null ? getParameterValue(index).toUnshared() : value);
		valueChanged(index);
	}
	
	/*-------------------
	 * Protected methods
	 */
	
	protected abstract ParameterValue getParameterValue(int index);
	protected abstract void setParameterValue(int index, ParameterValue value);
	
	/*-----------------
	 * Private methods
	 */

	private boolean isSet(int index, int mask)
	{
		assertIndexInRange(index);
		int bit = 1 << index;
		return (mask & bit) != 0;
	}
	
	private byte setBit(int index, byte mask, boolean value)
	{
		assertIndexInRange(index);
		int bit = 1 << index;
		if (value)
		{
			mask |= bit;
		}
		else
		{
			mask &= ~bit;
		}
		return mask;
	}
}