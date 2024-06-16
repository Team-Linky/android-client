package com.linky.data.usecase.link

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.linky.data.link.LinkRepository
import javax.inject.Inject

class GetLinksUseCase @Inject constructor(
    private val linkRepository: LinkRepository
) {

    operator fun invoke() = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { linkRepository.selectPage() }
    ).flow

}