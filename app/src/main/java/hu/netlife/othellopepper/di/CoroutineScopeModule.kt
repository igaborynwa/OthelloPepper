package hu.netlife.othellopepper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object CoroutineScopeModule {
    @Singleton
    @IoApplicationScope
    @Provides
    fun provideIoCoroutineScope(@IoDispatcher ioDispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + ioDispatcher)
    }

    @Singleton
    @DefaultApplicationScope
    @Provides
    fun provideDefaultCoroutineScope(@DefaultDispatcher defaultDispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + defaultDispatcher)
    }

    @Singleton
    @MainApplicationScope
    @Provides
    fun provideMainCoroutineScope(@MainDispatcher mainDispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(SupervisorJob() + mainDispatcher)
    }
}