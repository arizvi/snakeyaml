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
package org.yaml.snakeyaml.representer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

public class RepresenterTest extends TestCase {

    public void testRepresenter() {
        MyBean bean = new MyBean();
        bean.setName("Gnome");
        bean.setValid(true);
        bean.setPrimitive(true);
        Yaml yaml = new Yaml();
        assertEquals(
                "!!org.yaml.snakeyaml.representer.RepresenterTest$MyBean {name: Gnome, primitive: true}\n",
                yaml.dump(bean));
    }

    public static class MyBean {
        private String name;
        private Boolean valid;
        private boolean primitive;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean isValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }

        public boolean isPrimitive() {
            return primitive;
        }

        public void setPrimitive(boolean primitive) {
            this.primitive = primitive;
        }
    }

    public void testRepresenterNoConstructorAvailable() {
        MyBean2 bean = new MyBean2("Gnome", true);
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
        assertEquals("!!org.yaml.snakeyaml.representer.RepresenterTest$MyBean2 {valid: true}\n",
                yaml.dump(bean));
    }

    public static class MyBean2 {
        private String name;
        private Boolean valid;

        public MyBean2(String name, Boolean valid) {
            this();
            this.name = name;
            this.valid = valid;
        }

        private MyBean2() {
            super();
        }

        private String getName() {
            return name;
        }

        public Boolean getValid() {
            return valid;
        }

        @Override
        public String toString() {
            return getName() + " " + getValid();
        }
    }

    public void testRepresenterGetterWithException() {
        MyBean3 bean = new MyBean3("Gnome", true);
        Yaml yaml = new Yaml();
        try {
            yaml.dump(bean);
            fail("Exception must be reported");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public static class MyBean3 {
        private String name;
        private Boolean valid;

        public MyBean3(String name, Boolean valid) {
            this.name = name;
            this.valid = valid;
        }

        public String getName() {
            throw new UnsupportedOperationException("Test.");
        }

        public Boolean isValid() {
            return valid;
        }

        @Override
        public String toString() {
            return name + " " + isValid();
        }
    }

    public void testRepresenterAddNull() {
        Representer representer = new Representer();
        try {
            representer.addClassTag(EmptyBean.class, (Tag) null);
            fail("Tag must be provided.");
        } catch (Exception e) {
            assertEquals("Tag must be provided.", e.getMessage());
        }
    }

    public void testRepresenterEmptyBean() {
        EmptyBean bean = new EmptyBean();
        Yaml yaml = new Yaml();
        try {
            yaml.dump(bean);
            fail("EmptyBean has empty representation.");
        } catch (Exception e) {
            assertEquals(
                    "No JavaBean properties found in org.yaml.snakeyaml.representer.RepresenterTest$EmptyBean",
                    e.getMessage());
        }
    }

    public static class EmptyBean {
        private int number;

        public void process() {
            number += 1;
        }

        public int obtain() {
            return number;
        }
    }
}
