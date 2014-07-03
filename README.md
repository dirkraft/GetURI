*Fixes* URISyntaxException when trying to use (or through a library use) java.net.URI. Now you can use Java again!


GetURI
======
or die tryin.

There is one class with one static method. It takes any arbitrary URL and turns it into java.net.URI performing whatever is necessary to make the Java gods happy. Rant included in Java-docs.

    URI javaIsOkayWithThis = GetURI.orDieTryin(demonicUrl);

The approach includes "deep learning" features modeled after real human behavior.

  - Read the exception
  - Fix the thing complained about in the exception
  - Try it again

Realize that by "fixing" the URL so that Java will accept it against rfc3986 (java.net.URI), you are changing the bytes. It is not identical to the original. Most of the time this will not result in any measurable difference, but there's your warning.


## Maven ##
We has a maven!

    <dependency>
    	<groupId>com.github.dirkraft</groupId>
    	<artifactId>GetURI</artifactId>
    	<version>0.0.2</version>
    </dependency>

Other copyable dependency inclusions: http://mvnrepository.com/artifact/com.github.dirkraft/GetURI/0.0.2


## Contributions ##
Have you spotted the rare and wistful URISyntaxException through GetURI? Breaking examples (and patches to deal with them) are welcome.

