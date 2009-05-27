/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/

package com.syncleus.dann;

/**
 * This means a synapse is not a connected synpase.<BR>
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Jeffrey Phillips Freeman
 * @since 0.1
 */
public class SynapseNotConnectedException extends DannException
{
	public SynapseNotConnectedException()
	{
	}
	
	public SynapseNotConnectedException(String msg)
	{
		super(msg);
	}

	public SynapseNotConnectedException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public SynapseNotConnectedException(Throwable cause)
	{
		super(cause);
	}
}
