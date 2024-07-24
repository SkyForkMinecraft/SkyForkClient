package com.skyfork.client.annotations.event;

import com.skyfork.client.Access;
import com.skyfork.client.events.Priority;

import java.lang.annotation.*;
import java.util.function.BooleanSupplier;

/**
 * Marks a method so that the EventManager knows that it should be registered.
 * The priority of the method is also set with this.
 *
 * @author DarkMagician6
 * @see Priority
 * @since July 30, 2013
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
	byte value() default Priority.MEDIUM;
	Class<?> depend() default Access.class;
}
