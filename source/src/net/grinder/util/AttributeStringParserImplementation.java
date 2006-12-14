// Copyright (C) 2006 Philip Aston
// All rights reserved.
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parse attribute strings from HTML tokens.
 *
 * @author Philip Aston
 * @version $Revision$
 */
public final class AttributeStringParserImplementation
  implements AttributeStringParser {

  private final Pattern m_nameValuePattern;

  /**
   * Constructor.
   */
  public AttributeStringParserImplementation() {
    m_nameValuePattern =
      Pattern.compile("(\\w+)\\s*=\\s*['\"](.*?)['\"]");
  }

  /**
   * Do the parse.
   *
   * @param string String to parse.
   * @return Parse result.
   */
  public AttributeMap parse(String string) {
    final Matcher matcher = m_nameValuePattern.matcher(string);

    final Map map = new HashMap();

    while (matcher.find()) {
      map.put(matcher.group(1).toLowerCase(), matcher.group(2));
    }

    return new AttributeMapImplementation(map);
  }

  private static final class AttributeMapImplementation
    implements AttributeMap {

    private final Map m_map;

    public AttributeMapImplementation(Map map) {
      m_map = map;
    }

    public String get(String key) {
      return (String)m_map.get(key.toLowerCase());
    }
  }
}