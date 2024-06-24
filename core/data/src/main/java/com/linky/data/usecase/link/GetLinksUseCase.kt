package com.linky.data.usecase.link

import com.linky.data.repository.link.LinkRepository
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {

    operator fun invoke() = linkRepository.selectPage()

}