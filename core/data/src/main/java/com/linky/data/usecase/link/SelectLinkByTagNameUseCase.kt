package com.linky.data.usecase.link

import com.linky.data.link.LinkRepository
import javax.inject.Inject

class SelectLinkByTagNameUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {
    operator fun invoke(tagName: String) =
        linkRepository.selectLinksByTagName(tagName)
}