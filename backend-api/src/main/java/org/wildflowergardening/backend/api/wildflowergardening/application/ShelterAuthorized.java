package org.wildflowergardening.backend.api.wildflowergardening.application;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShelterAuthorized {}
