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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import net.grinder.common.GrinderException;


/**
 * Pairing of relative filename and file contents.
 *
 * @author Philip Aston
 * @version $Revision$
 */
public final class FileContents implements Serializable {

  private static final long serialVersionUID = -3140708892260600117L;

  private final File m_filename;
  private final byte[] m_contents;

  /**
   * Constructor. Builds a FileContents from local filesystem.
   *
   * @param baseDirectory Base directory used to resolve relative filenames.
   * @param file Relative filename.
   * @exception FileContentsException If an error occurs.
   */
  public FileContents(File baseDirectory, File file)
    throws FileContentsException {

    if (file.isAbsolute()) {
      throw new FileContentsException(
        "Original file name '" + file + "' is not relative");
    }

    m_filename = file;

    final File localFile = new File(baseDirectory, file.getPath());

    InputStream inputStream = null;

    final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

    try {
      inputStream = new FileInputStream(localFile);

      final byte[] buffer = new byte[4096];
      int n;

      while ((n = inputStream.read(buffer)) != -1) {
        byteOutputStream.write(buffer, 0, n);
      }
    }
    catch (IOException e) {
      throw new FileContentsException(
        "Failed to read file: " + e.getMessage(), e);
    }
    finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        }
        catch (IOException e) {
          // Ignore.
        }
      }

      try {
        byteOutputStream.close();
      }
      catch (IOException e) {
        // Ignore.
      }
    }

    m_contents = byteOutputStream.toByteArray();
  }


  /**
   * Allow unit tests access to the relative file name.
   *
   * @return The file name.
   */
  File getFilename() {
    return m_filename;
  }

  /**
   * Allow unit tests access to the file contents.
   *
   * @return a <code>byte[]</code> value
   */
  byte[] getContents() {
    return m_contents;
  }

  /**
   * Write the <code>FileContents</code> to the given directory,
   * overwriting any existing content.
   *
   * @param baseDirectory The base directory.
   * @exception FileContentsException If an error occurs.
   */
  public void create(File baseDirectory) throws FileContentsException {

    final File localFile = new File(baseDirectory, getFilename().getPath());

    localFile.getParentFile().mkdirs();

    try {
      final OutputStream outputStream = new FileOutputStream(localFile);
      outputStream.write(getContents());
      outputStream.close();
    }
    catch (IOException e) {
      throw new FileContentsException(
        "Failed to create file: " + e.getMessage(), e);
    }
  }

  /**
   * Return a description of the <code>FileContents</code>.
   *
   * @return The description.
   */
  public String toString() {
    return "\"" + getFilename() + "\" (" + getContents().length + " bytes)";
  }

  /**
   * Exception that indicates a <code>FileContents</code> related
   * problem.
   */
  public static final class FileContentsException extends GrinderException {
    FileContentsException(String message) {
      super(message);
    }

    FileContentsException(String message, Throwable nested) {
      super(message, nested);
    }
  }
}