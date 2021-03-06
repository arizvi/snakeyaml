/**
 * Copyright (c) 2008-2010 Andrey Somov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @see http://yaml.org/type/null.html
 */
public class NullTagTest extends AbstractTest {

    public void testNull() throws IOException {
        assertNull("Got: '" + load("---\n") + "'", load("---\n"));
        assertNull(load("---\n..."));
        assertNull(load("---\n...\n"));
        assertNull(load("\n"));
        assertNull(load(""));
        assertNull(load(" "));
        assertNull(load("~"));
        assertNull(load("---\n~"));
        assertNull(load("null"));
        assertNull(load("Null"));
        assertNull(load("NULL"));
        assertNull(getMapValue("empty:\n", "empty"));
        assertNull(getMapValue("canonical: ~", "canonical"));
        assertNull(getMapValue("english: null", "english"));
        assertNull(getMapValue("english: Null", "english"));
        assertNull(getMapValue("english: NULL", "english"));
        assertEquals("null key", getMapValue("~: null key\n", null));
    }

    @SuppressWarnings("unchecked")
    public void testSequenceNull() throws IOException {
        String input = "---\n# This sequence has five\n# entries, two have values.\nsparse:\n  - ~\n  - 2nd entry\n  -\n  - 4th entry\n  - Null\n";
        List<String> parsed = (List<String>) getMapValue(input, "sparse");
        assertEquals(5, parsed.size());
        assertNull(parsed.get(0));
        assertEquals("2nd entry", parsed.get(1));
        assertNull("Got: '" + parsed.get(2) + "'", parsed.get(2));
        assertEquals("4th entry", parsed.get(3));
        assertNull(parsed.get(4));
    }

    public void testNullInMap() throws IOException {
        String input = "key1: null\n~: value1";
        Map<String, Object> parsed = getMap(input);
        assertEquals(2, parsed.size());
        assertTrue(parsed.containsKey(null));
        Object value1 = parsed.get(null);
        assertEquals("value1", value1);
        //
        assertNull(parsed.get("key1"));
        //
        assertFalse(getMap("key2: value2").containsKey(null));
    }

    public void testBoolShorthand() throws IOException {
        assertNull(getMapValue("nothing: !!null null", "nothing"));
    }

    public void testBoolTag() throws IOException {
        assertNull(getMapValue("nothing: !<tag:yaml.org,2002:null> null", "nothing"));
    }

    public void testBoolOut() throws IOException {
        String output = dump(null);
        assertEquals("null\n", output);
    }

}
