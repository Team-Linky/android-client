package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import javax.inject.Inject

class IncrementLinkReadCountUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {
    suspend operator fun invoke(id: Long) = linkRepository.incrementReadCount(id)
}