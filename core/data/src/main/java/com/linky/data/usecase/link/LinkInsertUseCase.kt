package com.linky.data.usecase.link

import com.linky.data.link.LinkRepository
import com.linky.model.Link
import javax.inject.Inject

class LinkInsertUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {

    suspend operator fun invoke(link: Link) = linkRepository.insert(link)

}