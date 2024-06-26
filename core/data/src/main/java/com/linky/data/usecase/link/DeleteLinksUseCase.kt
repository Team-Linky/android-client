package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import javax.inject.Inject

class DeleteLinksUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {
    suspend operator fun invoke(linkIds: List<Long>) = linkRepository.deleteLinks(linkIds)
}