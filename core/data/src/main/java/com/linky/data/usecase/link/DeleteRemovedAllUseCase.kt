package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import javax.inject.Inject

class DeleteRemovedAllUseCase  @Inject constructor(
    private val linkRepository: LinkRepository
) {
    suspend operator fun invoke() = linkRepository.deleteRemovedAll()
}