package io.github.brunoconde07;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "API Quarkus Social",
                version = "1.0",
                contact = @Contact(
                        name = "Bruno Conde",
                        url = "https://www.google.com.br",
                        email = "bruno@email"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/license/LICENSE-2.0.html"
                )
        )
)
public class QuarkusSocialApplication extends Application {
}
