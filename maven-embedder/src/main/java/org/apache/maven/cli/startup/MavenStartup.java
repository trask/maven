package org.apache.maven.cli.startup;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.cli.MavenCli;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.realm.NoSuchRealmException;

import java.net.URL;

/**
 * @author Olivier Lamy
 */
public class MavenStartup
{
    private static final String DEFAULT_LOGGING_REALM = "log4j2";

    /**
     * just here to prevent "warning" from classworld which try to get this method
     * @param args
     * @return
     */
    public static int main( String[] args )
    {
        System.out.println("this main method cannot be used");
        return 1;
    }

    public static int main( String[] args, ClassWorld classWorld )
    {
        String loggerImplRealmId = System.getProperty( "maven.logger.impl", DEFAULT_LOGGING_REALM );

        // take care of empty sys props !
        if (loggerImplRealmId == null || loggerImplRealmId.length()<1)
        {
            loggerImplRealmId = DEFAULT_LOGGING_REALM;
        }

        try
        {
            ClassRealm classRealm = getLoggingClassRealm( loggerImplRealmId, classWorld );
            if (classRealm == null)
            {
                classRealm = classWorld.getClassRealm( DEFAULT_LOGGING_REALM );
            }
            for (URL url : classRealm.getURLs())
            {
                classWorld.getRealm( "plexus.core" ).addURL( url );
            }
            return MavenCli.doMain( args, classWorld );
        }
        catch ( NoSuchRealmException e )
        {
            System.out.println("Issue configuring Apache Maven: " + e.getMessage());
            e.printStackTrace();
        }

        return 1;
    }

    private static ClassRealm getLoggingClassRealm(String loggerImplRealmId, ClassWorld classWorld)
    {
        ClassRealm classRealm = null;
        try
        {
            classRealm = classWorld.getRealm( loggerImplRealmId );
        }
        catch ( NoSuchRealmException e )
        {
            System.out.println("Cannot find realm logger with id: '" + loggerImplRealmId + "', revert to default");
            return null;
        }

        return classRealm;
    }
}
