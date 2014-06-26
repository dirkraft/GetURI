This probably doesn't deserve to be a repo, but as quarantine from other codebases (except as an insidious dependency), it's here.

GetURI
======
or die tryin.

There is one class with one static method. It takes any arbitrary URL and turns it into java.net.URI performing whatever is necessary to make the Java gods happy. Rant included in Java-docs.

    URI javaIsOkayWithThis = GetURI.orDieTryin(demonicUrl);

The approach includes "deep learning" features modeled after real human behavior.

  - Read the exception
  - Fix the thing complained about in the exception
  - Try it again
