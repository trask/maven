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

import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.realm.NoSuchRealmException;

import java.net.URL;

/**
 * @author Olivier Lamy
 */
public class MavenStartup
{
    public static int main( String[] args )
    {
        //int result = doMain( args, null );

        return 1;
    }

    public static int main( String[] args, ClassWorld classWorld )
    {
        String loggerImplRealmId = System.getProperty( "maven.logger.impl", "log4j2" );

        try
        {
            ClassRealm classRealm = classWorld.getRealm( loggerImplRealmId );
            for (URL url : classRealm.getURLs())
            {
                classWorld.getRealm( "plexus.core" ).addURL( url );
            }
            return org.apache.maven.cli.MavenCli.doMain( args, classWorld );
        }
        catch ( NoSuchRealmException e )
        {
            e.printStackTrace();
        }
        return 1;
    }
}
