package com.clipsaver.quickreels.data.repositories

import com.clipsaver.quickreels.domain.repositories.LocalRepository
import com.clipsaver.quickreels.domain.repositories.SettingsRepository

class SettingsRepositoryImpl(
    private val localRepositoryImpl: LocalRepository,
) : SettingsRepository, LocalRepository by localRepositoryImpl