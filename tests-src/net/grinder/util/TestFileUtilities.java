// Copyright (C) 2004 Philip Aston
// All rights reserved.
//
// This file is part of The Grinder software distribution. Refer to
// the file LICENSE which is part of The Grinder distribution for
// licensing details. The Grinder distribution is available on the
// Internet at http://grinder.sourceforge.net/
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package net.grinder.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import net.grinder.testutility.AbstractFileTestCase;
import net.grinder.testutility.AssertUtilities;
import net.grinder.testutility.Serializer;


/**
 * Unit test case for {@link FileUtilities}.
 *
 * @author Philip Aston
 * @version $Revision$
 */
public class TestFileUtilities extends AbstractFileTestCase {

  public void testDeleteContents() throws Exception {

    final String[] files = {
      "directory/foo/bah/blah",
      "directory/blah",
      "a/b/c/d/e",
      "a/b/f/g/h",
      "a/b/f/g/i",
      "x",
      "y/z",
      "another",
    };

    for (int i=0; i<files.length; ++i) {
      final File file = new File(getDirectory(), files[i]);
      file.getParentFile().mkdirs();
      file.createNewFile();
    }

    try {
      FileUtilities.deleteContents(new File(getDirectory(), "x"));
      fail("Expected FileUtilitiesException");
    }
    catch (FileUtilities.FileUtilitiesException e) {
    }
    
    FileUtilities.deleteContents(getDirectory());

    assertEquals(0, getDirectory().list().length);
  }
}
