08.11.2010: Since a few days we have problems with the maven2 build.
It depends on a loaded JasperReports file at each build. This file is corupt.
At time JasperReports have not fixed this problem. So here is only a workaraound.
 
The attached file 'settings.xml' is copying in your local Maven2 
repository folder, or the JasperReports stuff in this file is 
copying in your settings.xml file.

