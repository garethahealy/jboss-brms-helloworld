/*
 * #%L
 * HelloWorld
 * %%
 * Copyright (C) 2013 - 2015 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.brms;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.StatelessKieSession;

public class HelloWorldTest {

    @Test
    public void checkForAdult() {
        KieSessionFactory factory = new KieSessionFactory();
        factory.start();

        StatelessKieSession session = factory.getStatelessSession();

        Person person = new Person();
        person.setName("Adult Gareth");
        person.setIsOver18(null);
        person.setDob(LocalDate.now().minusYears(21));

        session.execute(person);

        Assert.assertNotNull(person.getIsOver18());
        Assert.assertTrue(person.getIsOver18());
        Assert.assertEquals("Adult Gareth", person.getName());
    }

    @Test
    public void checkForChild() {
        KieSessionFactory factory = new KieSessionFactory();
        factory.start();

        StatelessKieSession session = factory.getStatelessSession();

        Person person = new Person();
        person.setName("Babt Gareth");
        person.setIsOver18(null);
        person.setDob(LocalDate.now());

        session.execute(person);

        Assert.assertNotNull(person.getIsOver18());
        Assert.assertFalse(person.getIsOver18());
        Assert.assertEquals("Babt Gareth", person.getName());
    }
}
