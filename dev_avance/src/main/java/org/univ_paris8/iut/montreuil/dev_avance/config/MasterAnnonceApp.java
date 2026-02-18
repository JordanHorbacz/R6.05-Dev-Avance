package org.univ_paris8.iut.montreuil.dev_avance.config;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class MasterAnnonceApp extends ResourceConfig {
    public MasterAnnonceApp() {
        packages("org.univ_paris8.iut.montreuil.dev_avance.api",
                "org.univ_paris8.iut.montreuil.dev_avance.security",
                "org.univ_paris8.iut.montreuil.dev_avance.api.exception");
    }
}
