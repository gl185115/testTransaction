Sprint21- WebStoreServer
**********
date: March 24, 2013 from PS Sprint 21 Release
**********
Source (PS-WSS) : http://192.127.38.204/svn/repos/pa/res/WebStoreServer/branches/RESMobile1.3.0-WebStoreServer(Sprint21)
Source (RES3.3) : http://192.127.38.204/svn/repos/pa/res/RES3.3/RESTransactionService/

Following Changes:
-Configured Build Path from Tomcat6 to Tomcat7
-Imported (1)jar file (mail-1.4.1.jar) into WEB-INF/lib, to fix environment build error on NetworkReceipt.java
-Removed "import import org.apache.tomcat.util.buf.TimeStamp;" in SQLServerMasterMaintenceDAOSteps.java
-Modified pom.xml pointing to new svn http://subversion.sweng.ncr.com/svn/repos/pa/res/RES3.3/RESTransactionService/trunk/

Note to Developers:
-Copied (2) jar files, mail-1.4.1.jar & sqljdbc4.jar, into Tomcat 7.0/lib before running the source.
