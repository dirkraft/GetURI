/**
 * Copyright (c) 2014 Jason Dunkelberger (dirkraft)
 *
 * See the file license.txt for copying permission.
 */
package com.github.dirkraft.geturi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetURI {

    private static final Pattern PAT_BAD_QUERY_CHAR = Pattern.compile("^Illegal character in (query|path|fragment) at index (\\d+):.*");
    private static final Pattern PAT_MALFORMED_ESCAPE_PAIR = Pattern.compile("^Malformed escape pair at index (\\d+):.*");

    /**
     * Maximum number of exceptions to tolerate and attempt to fix in appeasing URI.
     */
    public static final int MAX_GRIT = 100;

    /**
     * A collection of UGHWTF to attempt to validify (to make valid) urls that would otherwise trigger
     * {@link URISyntaxException}s with {@link URI}. The problem is Java's rfc3986-strict URI parser. Servers accept
     * whatever bytes you send to them. By "fixing" a URI, <strong>you are changing the link</strong>. This tries to
     * do the minimum possible to a URI through the <code>java.net.URI</code> exception wall. Yay, you can still use
     * Apache HttpClient, or whatever else is hard set on java.net.URI, unless of course these changes cause the server
     * at the end to return something else, in which case, (╯°□°）╯︵ ┻━┻
     * <p>
     * This method is probably not performant, but it guarantees the absolute minimum changes required to pass
     * validation because it bases "fixes" directly on the URISyntaxExceptions themselves.
     * <hr/>
     * <em>Rant:</em>
     * YEAH, you could "fix" the URL to coincide with some well-defined standard with reserved characters
     * (I'm looking at you, rfc3986). Then you wouldn't be doing something so terrible as parsing exception
     * message strings (as this method does), and in the least performant way possible.
     * <p>
     * But the assumption there is that you would be parsing the (invalid) form of the URI identical to the way
     * java.net.URI does in its private code, because if you tried to eagerly fix all the broken stuff, you may have
     * changed the original URL TOO MUCH to even be the same request anymore. The thing is, servers accept
     * rfc3986-invalid requests. Java just won't make them. So the standard is not at the other end. You can try to
     * eagerly fix it in one pass if you wish, but you would have to clone and modify java.net.URI's logic to make
     * Java happy and <strong>minimize changes to the original request</strong>.
     * <p>
     * Also realize that the standard defines the <em>correct</em> form, not how to fallback and resolve errors in
     * non-compliant forms. The correctest fix would be to fork Apache HttpClient, replace all usages of java.net.URI
     * with a custom URI class with configurable, arbitrary amounts of encoding and validation, so that any request, no
     * matter how subjectively horrible, could put its bytes on the wire to give the horrible remote server gods at the
     * other ends of the ether whatever byte sacrifice they require to release their particular document-form blessings.
     * <p>
     * So what I'm saying is: <strong>don't try to "fix" this anymore than is necessary</strong>. Also <strong>don't try
     * to be efficient in "fixing" URLs</strong>. You don't even know what "fixed" is. You can't because it's really up
     * to each server. Do not stare directly at java.net.URI source.
     * <p>
     * <h1><a href="http://gunshowcomic.com/648">Everything is fine.</a></h1>
     *
     * @throws RuntimeException "orDieTryin", if unable to appease the URI gods of Java.
     */
    public static URI orDieTryin(String url) throws RuntimeException {
        URI uri = null;

        for (int i = 0; uri == null && i < MAX_GRIT; i++) { // maximum cleaning iterations for safety
            try {
                uri = new URI(url);

            } catch (URISyntaxException e) {
                if (e.getMessage() == null) {
                    throw new RuntimeException(e);
                }

                boolean handled = false;
                HandlerResult result;

                // Exception processors

                if (!handled) {
                    result = handleIllegalCharacter(url, e.getMessage());
                    url = result.url;
                    handled |= result.handled;
                }

                if (!handled) {
                    result = handleMalformedEscapePair(url, e.getMessage());
                    url = result.url;
                    handled |= result.handled;
                }

                // We're out of tricks. "Die tryin'"

                if (!handled) {
                    throw new RuntimeException(e);
                }
            }
        }

        return uri;
    }

    static HandlerResult handleIllegalCharacter(String url, String exceptionMsg) {
        Matcher m = PAT_BAD_QUERY_CHAR.matcher(exceptionMsg);
        boolean handled = false;
        if (m.find()) {
            int badCharIdx = Integer.parseInt(m.group(2));
            char badChar = url.charAt(badCharIdx);
            String hex = Integer.toHexString(badChar);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            if (hex.length() != 2) {
                throw new RuntimeException("Character " + badChar + " hexed too large " + hex +
                        ". I only know how to make 2-digit %-hex codes.");
            }
            String pfx = url.substring(0, badCharIdx);
            String sfx = badCharIdx + 1 == url.length() ? "" : url.substring(badCharIdx + 1);
            url = pfx + '%' + hex + sfx;
            handled = true;
        }
        return new HandlerResult(url, handled);
    }

    static HandlerResult handleMalformedEscapePair(String url, String exceptionMsg) {
        Matcher m = PAT_MALFORMED_ESCAPE_PAIR.matcher(exceptionMsg);
        boolean handled = false;
        if (m.find()) {
            // Let's just go with: They want an actual % which is encoded as %25. Too much guessing if its a bad escape,
            // and how long the escape should be since UTF-8 characters have varying byte sizes.
            int badCharIdx = Integer.parseInt(m.group(1));
            String pfx = url.substring(0, badCharIdx);
            String sfx = badCharIdx + 1 == url.length() ? "" : url.substring(badCharIdx + 1);
            url = pfx + "%25" + sfx;
            handled = true;
        }
        return new HandlerResult(url, handled);
    }

    static class HandlerResult {
        String url;
        boolean handled;

        HandlerResult(String url, boolean handled) {
            this.url = url;
            this.handled = handled;
        }
    }
}
