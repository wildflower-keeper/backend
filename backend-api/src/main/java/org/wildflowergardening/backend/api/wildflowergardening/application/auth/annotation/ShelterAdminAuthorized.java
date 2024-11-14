package org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Documented
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShelterAdminAuthorized {
}
