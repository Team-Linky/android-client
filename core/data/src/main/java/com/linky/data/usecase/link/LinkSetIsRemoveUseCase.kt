package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import javax.inject.Inject

class LinkSetIsRemoveUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {
    suspend operator fun invoke(id: Long, isRemove: Boolean) =
        linkRepository.setIsRemoveLink(id, isRemove)
}