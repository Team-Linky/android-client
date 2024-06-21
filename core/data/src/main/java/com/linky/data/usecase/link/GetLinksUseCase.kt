package com.linky.data.usecase.link

import com.linky.data.link.LinkRepository
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {

    operator fun invoke() = linkRepository.selectPage()

}