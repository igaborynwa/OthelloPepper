package hu.netlife.othellopepper.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoApplicationScope


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultApplicationScope


@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MainApplicationScope
