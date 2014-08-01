/**
 * Copyright (c) 2014 Jason Dunkelberger (dirkraft)
 *
 * See the file license.txt for copying permission.
 */
package com.github.dirkraft.geturi;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Feed me
 */
public class GetURITest {

    // These are REAL LIFE EXAMPLES. That means this is a REAL LIFE TEST.
    List<String> urls = Arrays.asList(
            "http://localhost/thing.php?stuff=3&other=12561&derp=cr982595&bad=3363984526|3266644|{Keyword}&sobad={MatchType}&anon=Mr. Broken Query String&url=http://localhost/thing.php?stuff=3&other=12561&derp=cr982595&bad=3363984526|3266644|{Keyword}&sobad={MatchType}&anon=Mr. Broken Query String",
            // http://stackoverflow.com/questions/749709/how-to-deal-with-the-urisyntaxexception
            "http://finance.yahoo.com/q/h?s=^IXIC",
            // http://stackoverflow.com/questions/18574154/urisyntaxexception-illegal-character-in-query
            "http://www.example.com/engine/myProcessor.jsp?Type=A Type&Name=1100110&Char=!",
            // http://stackoverflow.com/questions/18651722/java-net-urisyntaxexception-illegal-character-in-query-at-index-72
            "http://abc.com/api/api_name?XYZModelJsonString={[{},{},{},{},{},{},{},{},{},{},{},{}]{}}",
            // http://www.coderanch.com/t/609406/java/java/java-net-URISyntaxException-Illegal-character
            "http://200.16.86.50/uhtbin/cgisirsi.exe/7TMAN1mSLU/BC/110630017/123?query_type=search&searchdata1=0521446910&srchfield1=GENERAL^SUBJECT^GENERAL^^Todos+los+campos&library=ALL&sort_by=-PBYR",
            // http://ocpsoft.org/support/topic/urisyntaxexception-illegal-character-in-path-on-square-brackets/
            "http://localhost/1.+[=+100.]+Verba+mea+auribus",
            // I actually made this one up. Sorry. +1 Lie
            "http://localhost/fi-shocktrade%3B-fence-tool-combo?idklol=some%2fthing%another#crunch"
    );

    @Test
    public void testOrDieTryin() {
        for (String badUrl : urls) {
            URI uri = GetURI.orDieTryin(badUrl);
            Assert.assertNotNull(uri.toString(), uri);
            System.out.println(String.format("deadly : %s\nsterile: %s", badUrl, uri));
        }
    }
}
