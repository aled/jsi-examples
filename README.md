jsi-examples
============

Simple examples of how to use the JSI library. The following commands will run the examples:

    git clone https://github.com/aled/jsi-examples.git
    cd jsi-examples
    mvn package
    cd target
    unzip jsi-examples-1.1.0-SNAPSHOT-jar-with-dependencies.jar
    java -cp .:./classes net.sf.jsi.examples.Contains
    java -cp .:./classes net.sf.jsi.examples.NearestN
