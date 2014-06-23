/**
 * Copyright (c) 2014 Jason Dunkelberger (dirkraft)
 *
 * See the file license.txt for copying permission.
 */
package com.github.dirkraft.geturi;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

/**
 * Feed me
 */
public class GetURITest {

    @Test
    public void testOrDieTryin() {
        String badUrl = "http://localhost/thing.php?stuff=3&other=12561&derp=cr982595&bad=3363984526|3266644|{Keyword}&sobad={MatchType}&anon=Mr. Broken Query String&url=http://localhost/thing.php?stuff=3&other=12561&derp=cr982595&bad=3363984526|3266644|{Keyword}&sobad={MatchType}&anon=Mr. Broken Query String";
        URI uri = GetURI.orDieTryin(badUrl);
        Assert.assertNotNull(uri);
        System.out.println(String.format("deadly : %s\nsterile: %s", badUrl, uri));
    }
}
