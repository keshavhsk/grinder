// The Grinder
// Copyright (C) 2000, 2001  Paco Gomez
// Copyright (C) 2000, 2001  Philip Aston

// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

package net.grinder.util;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
//import junit.textui.TestRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;


/**
 * Unit test case for {@link Serialiser}.
 *
 * @author Philip Aston
 * @version $Revision$
 */
public class TestSerialiser extends TestCase
{
    public static void main(String[] args)
    {
	TestRunner.run(TestSerialiser.class);
    }

    public TestSerialiser(String name)
    {
	super(name);
    }

    private final Random m_random = new Random();

    public void testUnsignedLongs() throws Exception
    {
	final ByteArrayOutputStream byteArrayOutputStream =
	    new ByteArrayOutputStream();

	final ObjectOutputStream objectOutputStream =
	    new ObjectOutputStream(byteArrayOutputStream);

	final long[] longs = new long[10000];

	final Serialiser serialiser = new Serialiser();

	for (int i=0; i<longs.length; i++) {
	    if (i < 1000) {
		longs[i] = i;
	    }
	    else {
		longs[i] = Math.abs(m_random.nextLong() & 0xFFF);
	    }

	    serialiser.writeUnsignedLong(objectOutputStream, longs[i]);
	}

	try {
	    serialiser.writeUnsignedLong(objectOutputStream, -1);
	    fail("Should not reach");
	}
	catch (IOException e) {
	}

	objectOutputStream.close();

	final byte[] bytes = byteArrayOutputStream.toByteArray();

	assert("We should compress", bytes.length < 8 * longs.length);

	final ObjectInputStream objectInputStream =
	    new ObjectInputStream(new ByteArrayInputStream(bytes));

	for (int i=0; i<longs.length; i++) {
	    assertEquals(longs[i],
			 serialiser.readUnsignedLong(objectInputStream));
	}
    }

    public void testLongs() throws Exception
    {
	final ByteArrayOutputStream byteArrayOutputStream =
	    new ByteArrayOutputStream();

	final ObjectOutputStream objectOutputStream =
	    new ObjectOutputStream(byteArrayOutputStream);

	final long[] longs = new long[3002];

	final Serialiser serialiser = new Serialiser();

	for (int i=0; i<longs.length; i++) {
	    if (i < 1000) {
		longs[i] = i;
	    }
	    else if (i <2000) {
		longs[i] = i - 2000;
	    }
	    else {
		longs[i] = m_random.nextLong();
	    }

	    longs[3000] = Long.MIN_VALUE;
	    longs[3001] = Long.MAX_VALUE;

	    serialiser.writeLong(objectOutputStream, longs[i]);
	}

	objectOutputStream.close();

	final byte[] bytes = byteArrayOutputStream.toByteArray();

	assert("We should compress", bytes.length < 8 * longs.length);

	final ObjectInputStream objectInputStream =
	    new ObjectInputStream(new ByteArrayInputStream(bytes));

	for (int i=0; i<longs.length; i++) {
	    assertEquals(longs[i], serialiser.readLong(objectInputStream));
	}
    }

    public void testDoubles() throws Exception
    {
	final ByteArrayOutputStream byteArrayOutputStream =
	    new ByteArrayOutputStream();

	final ObjectOutputStream objectOutputStream =
	    new ObjectOutputStream(byteArrayOutputStream);

	final double[] doubles = new double[10000];

	final Serialiser serialiser = new Serialiser();

	for (int i=0; i<doubles.length; i++) {
	    if (i < 1000) {
		doubles[i] = i;
	    }
	    else {
		doubles[i] = m_random.nextDouble();
	    }

	    serialiser.writeDouble(objectOutputStream, doubles[i]);
	}

	objectOutputStream.close();

	final byte[] bytes = byteArrayOutputStream.toByteArray();

	// To do, make this work.
	//assert("We should compress", bytes.length < 8 * doubles.length);

	final ObjectInputStream objectInputStream =
	    new ObjectInputStream(new ByteArrayInputStream(bytes));

	for (int i=0; i<doubles.length; i++) {
	    assertEquals(doubles[i], serialiser.readDouble(objectInputStream),
			 0.00001);
	}
    }
}
