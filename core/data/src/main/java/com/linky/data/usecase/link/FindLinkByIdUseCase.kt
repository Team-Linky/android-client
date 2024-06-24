package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import javax.inject.Inject

class FindLinkByIdUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {
    suspend operator fun invoke(id: Long) = linkRepository.findLinkById(id)
}