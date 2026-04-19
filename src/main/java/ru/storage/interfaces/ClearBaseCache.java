package ru.storage.interfaces;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited // Добавьте это
public @interface ClearBaseCache {
}