This is so dumb it doesn't deserve to even be a repo. But in an effort to not infect other codebases with this terrible (except as an insidious dependency), it has been placed here.

GetURI
======
or die tryin.

There is one class with one static method. It takes any arbitrary URL and turns it into java.net.URI performing whatever is necessary to make the Java gods happy. Rant included in Java-docs.

    URI javaIsOkayWithThis = GetURI.orDieTryin(demonicUrl);

