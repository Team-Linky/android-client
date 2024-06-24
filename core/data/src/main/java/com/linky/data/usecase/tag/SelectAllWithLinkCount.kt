package com.linky.data.usecase.tag

import com.linky.data.repository.tag.TagRepository
import javax.inject.Inject

class SelectAllWithLinkCount @Inject constructor(
    private val tagRepository: TagRepository
) {
    operator fun invoke(count: Int) = tagRepository.selectAllWithLinkCount(count)
}