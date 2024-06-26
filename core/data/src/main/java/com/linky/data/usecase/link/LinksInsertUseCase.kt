package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import com.linky.model.Link
import javax.inject.Inject

class LinksInsertUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {
    suspend operator fun invoke(links: List<Link>) = linkRepository.insertLinks(links)
}