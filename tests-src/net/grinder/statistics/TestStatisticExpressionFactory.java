// The Grinder
// Copyright (C) 2001  Paco Gomez
// Copyright (C) 2001  Philip Aston

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

package net.grinder.statistics;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
//import junit.textui.TestRunner;

import net.grinder.common.GrinderException;


/**
 * Unit test case for <code>StatisticExpressionFactory</code>.
 *
 * @author Philip Aston
 * @version $Revision$
 * @see RawStatistics
 */
public class TestStatisticExpressionFactory extends TestCase
{
    public static void main(String[] args)
    {
	TestRunner.run(TestStatisticExpressionFactory.class);
    }

    public TestStatisticExpressionFactory(String name)
    {
	super(name);
    }

    private final StatisticExpressionFactory m_factory =
	StatisticExpressionFactory.getInstance();

    private final StatisticsIndexMap m_indexMap =
	StatisticsIndexMap.getInstance();

    private final RawStatistics m_rawStatistics =
	new RawStatisticsImplementation();

    private final double m_accuracy = 0.00001d;

    protected void setUp() throws Exception
    {
	m_rawStatistics.addValue(m_indexMap.getIndexForLong("userLong0"), 1);
	m_rawStatistics.addValue(m_indexMap.getIndexForLong("userLong1"), 2);
    }

    public void testConstant() throws Exception
    {
	final StatisticExpression longExpression =
	    m_factory.createConstant(-22);

	myAssertEquals(-22, longExpression);
	assert(!longExpression.isDouble());

	final StatisticExpression doubleExpression =
	    m_factory.createConstant(2.3);

	myAssertEquals(2.3d, doubleExpression);
	assert(doubleExpression.isDouble());

	myAssertEquals(0, m_factory.createExpression("0"));
	myAssertEquals(99d, m_factory.createExpression("99f"));

	try {
	    m_factory.createExpression("1 2");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}
    }

    public void testPrimitive() throws Exception
    {
	final StatisticExpression expression =
	    m_factory.createPrimitive(m_indexMap.getIndexForLong("userLong0"));

	myAssertEquals(1, expression);
	assert(!expression.isDouble());

	final StatisticsIndexMap.DoubleIndex anotherIndex =
	    m_indexMap.getIndexForDouble("userDouble4");

	final StatisticExpression doubleExpresson =
	    m_factory.createExpression("  userDouble4");

	myAssertEquals(0d, doubleExpresson);
	assert(doubleExpresson.isDouble());

	myAssertEquals(2, m_factory.createExpression("userLong1"));

	try {
	    m_factory.createExpression("");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("userLong0 userLong1");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("Madeup");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}
    }

    public void testSum() throws Exception
    {
	final StatisticExpression[] expressions = {
	    m_factory.createExpression("userLong0"),
	    m_factory.createExpression("userLong1"),
	    m_factory.createExpression("userLong1"),
	};

	final StatisticExpression expression =
	    m_factory.createSum(expressions);

	myAssertEquals(5, expression);
	assert(!expression.isDouble());

	myAssertEquals(2,
		       m_factory.createExpression("(+ userLong0 userLong0)"));

	myAssertEquals(4,
		       m_factory.createExpression(
			   "(+ userLong0 userLong1 userLong0)"));

	myAssertEquals(5,
		       m_factory.createExpression(
			   "(+ userLong0 (+ userLong0 userLong1) userLong0)"));

	try {
	    m_factory.createExpression("(+)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("(+ userLong0)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}
    }

    public void testProduct() throws Exception
    {
	final StatisticExpression[] expressions = {
	    m_factory.createExpression("userLong0"),
	    m_factory.createExpression("userLong1"),
	    m_factory.createExpression("userLong1"),
	};

	final StatisticExpression expression =
	    m_factory.createProduct(expressions);

	myAssertEquals(4, expression);
	assert(!expression.isDouble());

	myAssertEquals(1,
		       m_factory.createExpression("(* userLong0 userLong0)"));

	myAssertEquals(4,
		       m_factory.createExpression(
			   "(* userLong0 userLong1 userLong1)"));

	myAssertEquals(8,
		       m_factory.createExpression(
			   "(* userLong1 (* userLong1 userLong1) userLong0)"));

	try {
	    m_factory.createExpression("(*)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("(* userLong0)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}
    }

    public void testDivision() throws Exception
    {
	final StatisticExpression expression =
	    m_factory.createDivision(m_factory.createExpression("userLong1"),
				     m_factory.createExpression("userLong1"));

	myAssertEquals(1, expression);
	assert(expression.isDouble());

	myAssertEquals(1d,
		       m_factory.createExpression("(/ userLong0 userLong0)"));

	myAssertEquals(0.5d,
		       m_factory.createExpression("(/ userLong0 userLong1)"));

	myAssertEquals(2d,
		       m_factory.createExpression("(/ userLong1 userLong0)"));

	try {
	    m_factory.createExpression("(/)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("(/ userLong0)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("(/ userLong0 userLong0 userLong0)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}
    }

    public void testLongPeak() throws Exception
    {
	final StatisticsIndexMap.LongIndex peakIndex1 =
	    m_indexMap.getIndexForLong("userLong2");

	final StatisticsIndexMap.LongIndex peakIndex2 =
	    m_indexMap.getIndexForLong("userLong3");

	final StatisticExpression expression =
	    m_factory.createPeak(peakIndex1,
				 m_factory.createExpression("userLong1"));

	myAssertEquals(0, expression);
	assert(!expression.isDouble());

	final StatisticsIndexMap.LongIndex statIndex =
	    m_indexMap.getIndexForLong("userLong4");

	final PeakStatisticExpression peak = 
	    m_factory.createPeak(peakIndex2,
				 m_factory.createExpression("userLong4"));

	final RawStatistics rawStatistics = new RawStatisticsImplementation();

	rawStatistics.setValue(statIndex, 2);
	myAssertEquals(0, peak, rawStatistics);
	peak.update(rawStatistics, rawStatistics);
	myAssertEquals(2, peak, rawStatistics);

	rawStatistics.setValue(statIndex, 33);
	peak.update(rawStatistics, rawStatistics);
	myAssertEquals(33, peak, rawStatistics);

	rawStatistics.setValue(statIndex, 2);
	peak.update(rawStatistics, rawStatistics);
	myAssertEquals(33, peak, rawStatistics);
    }

    public void testDoublePeak() throws Exception
    {
	final StatisticsIndexMap.DoubleIndex peakIndex1 =
	    m_indexMap.getIndexForDouble("userDouble2");

	final StatisticsIndexMap.DoubleIndex peakIndex2 =
	    m_indexMap.getIndexForDouble("userDouble3");

	final StatisticExpression expression =
	    m_factory.createPeak(peakIndex1,
				 m_factory.createExpression(
				     "(/ userLong1 userLong0)"));

	myAssertEquals(0, expression);
	assert(expression.isDouble());

	final StatisticsIndexMap.DoubleIndex statIndex =
	    m_indexMap.getIndexForDouble("userDouble4");

	final PeakStatisticExpression peak = 
	    m_factory.createPeak(peakIndex2,
				 m_factory.createExpression("userDouble4"));

	final RawStatistics rawStatistics = new RawStatisticsImplementation();

	rawStatistics.setValue(statIndex, 0.5);
	myAssertEquals(0d, peak, rawStatistics);
	peak.update(rawStatistics, rawStatistics);
	myAssertEquals(0.5d, peak, rawStatistics);

	rawStatistics.setValue(statIndex, 33d);
	peak.update(rawStatistics, rawStatistics);
	myAssertEquals(33d, peak, rawStatistics);

	rawStatistics.setValue(statIndex, -2d);
	peak.update(rawStatistics, rawStatistics);
	myAssertEquals(33d, peak, rawStatistics);
    }

    public void testParseCompoundExpessions() throws Exception
    {
	myAssertEquals(0.5,
		       m_factory.createExpression(
			   "(/ userLong0 (+ userLong0 userLong0))"));

	myAssertEquals(2.25d,
		       m_factory.createExpression(
			   "(+ userLong0 (/ userLong0 (+ userLong1 userLong1)) userLong0)"));

	myAssertEquals(2.25d,
		       m_factory.createExpression(
			   "(+ userLong0 (/ userLong0 (* userLong1 userLong1)) userLong0)"));

	myAssertEquals(9d,
		       m_factory.createExpression(
			   "(* 4 (+ userLong0 (/ userLong0 (* userLong1 userLong1)) userLong0))"));

	try {
	    m_factory.createExpression("(+");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}

	try {
	    m_factory.createExpression("+)");
	    fail("Expected a GrinderException");
	}
	catch (GrinderException e) {
	}
    }

    public void testNormaliseExpressionString() throws Exception
    {
	assertEquals("userLong0",
		     m_factory.normaliseExpressionString(" userLong0 "));

	assertEquals("(+ userLong0 userLong1 (* userLong0 userLong1))",
		     m_factory.normaliseExpressionString(
			 "\t(+ userLong0 userLong1( \n  * userLong0 userLong1) )"));
    }

    private void myAssertEquals(long expected, StatisticExpression expression)
    {
	myAssertEquals(expected, expression, m_rawStatistics);
    }

    private void myAssertEquals(long expected, StatisticExpression expression,
				RawStatistics rawStatistics)
    {
	assertEquals(expected, expression.getLongValue(rawStatistics));
	myAssertEquals((double)expected, expression, rawStatistics);
    }

    private void myAssertEquals(double expected,
				StatisticExpression expression)
    {
	myAssertEquals(expected, expression, m_rawStatistics);
    }

    private void myAssertEquals(double expected,
				StatisticExpression expression,
				RawStatistics rawStatistics)
    {
	assertEquals(expected, expression.getDoubleValue(rawStatistics),
		     0.00001d);
    }
}
