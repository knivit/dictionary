Build & GWT development
-----------------------
1. Install jars to a local maven repository
   mvn install:install-file -DgroupId=com.google.appengine.orm -DartifactId=datanucleus-appengine -Dversion=1.0.7.final -Dpackaging=jar -DgeneratePom=true -Dfile=
   mvn install:install-file -DgroupId=com.google.appengine -DartifactId=appengine-api-labs -Dversion=1.3.6 -Dpackaging=jar -DgeneratePom=true -Dfile=
   mvn install:install-file -DgroupId=com.google.appengine -DartifactId=appengine-api-1.0-sdk -Dversion=1.3.6 -Dpackaging=jar -DgeneratePom=true -Dfile=
   mvn install:install-file -DgroupId=com.google.appengine -DartifactId=appengine-api-stubs -Dversion=1.3.6 -Dpackaging=jar -DgeneratePom=true -Dfile=
   mvn install:install-file -DgroupId=com.google.appengine -DartifactId=appengine-api -Dversion=1.3.6 -Dpackaging=jar -DgeneratePom=true -Dfile=
   mvn install:install-file -DgroupId=com.google.appengine -DartifactId=appengine-testing -Dversion=1.3.6 -DgeneratePom=true -Dpackaging=jar -Dfile=
   mvn install:install-file -DgroupId=com.google.code.gwt-log -DartifactId=gwt-log -Dversion=3.0.2 -Dpackaging=jar -DgeneratePom=true -Dfile=

2. Set logging properties for maven to prevent big output from DataNucleus
   mvn -Djava.util.logging.config.file=pom-logging.properties clean package

3. Use mvn gwt:run command to run the app in GWT development mode
   To debug the project (both client & server paths) use
   cd client
   mvn -DrunTarget=dictionary.html -Dgwt.debug="-DGWTDebugMode=true -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9009" gwt:run
   In this case ServerServlet class do an initialization of LocalServiceTestHelper (in LocalAppengineEnvironment class)

4. GWT Designer, CodePro AnalytiX, WindowBuilder Pro (GUI-designer for Swing, SWT, GWT и XWT) and WindowTester Pro (GUI testing in Swing and SWT)
   are free now. http://googlewebtoolkit.blogspot.com/2010/09/google-relaunches-instantiations.html

Google AppEngine Develop & Release info
---------------------------------------
1. To release, use
   appcfg.cmd update c:\jprj\tsoft\dictionary\target\dictionary-1.0.0

2. To develop, use
   dev_appserver.cmd c:\jprj\tsoft\dictionary\target\dictionary-1.0.0

   To switch on the debugging, edit dev_appserver.cmd:
   @java -cp "%~dp0\..\lib\appengine-tools-api.jar" ^
   com.google.appengine.tools.KickStart --jvm_flag=-Xdebug --jvm_flag=-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9009 ^
   com.google.appengine.tools.development.DevAppServerMain c:\jprj\tsoft\dictionary\target\dictionary-1.0.0 %*

3. Datastore Viewer is located on
   http://localhost:8080/_ah/admin
   It is uses client/target/dictionary-war-1.0.0/WEB-INF/appengine-generated/local_db.bin file

4. See parent/pom.xml for app and hosting links

Get a domain name for a project
-------------------------------
1. Register in Google Services
   http://www.google.com/a/cpanel/domain/new?hl=ru

2. Get a domain name (10$)
   http://www.google.com/a/cpanel/domain/new?hl=ru

   webtsoft.net

Google App Store
----------------
0. http://www.google.com/enterprise/marketplace/home
1. http://code.google.com/intl/ru-RU/googleapps/marketplace/sso.html
2. http://code.google.com/intl/ru-RU/googleapps/marketplace/data_access.html
3. http://code.google.com/intl/ru-RU/googleapps/marketplace/manifest.html
4. http://code.google.com/p/step2/
