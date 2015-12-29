This project runs the tck tests tailor made for the weblogic-oracle compatible JUDDI version.
It relies on the following environment variables which must be passed as JVM argument:
 - ${service.registry.http.url}
 - ${juddi.gui.http.url}
 - ${local.hostname} -> hostname of the machine on which JUDDI running