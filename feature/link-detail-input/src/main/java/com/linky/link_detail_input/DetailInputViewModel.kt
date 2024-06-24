package com.linky.link_detail_input

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kedia.ogparser.OpenGraphCacheProvider
import com.kedia.ogparser.OpenGraphCallback
import com.kedia.ogparser.OpenGraphParser
import com.kedia.ogparser.OpenGraphResult
import com.linky.data.usecase.link.FindLinkByIdUseCase
import com.linky.data.usecase.link.LinkInsertUseCase
import com.linky.data.usecase.tag.SelectAllWithUsageUseCase
import com.linky.data.usecase.tag.TagDeleteUseCase
import com.linky.data.usecase.tag.TagInsertUseCase
import com.linky.data.usecase.tag.UpdateLinkIdsUseCase
import com.linky.link_detail_input.model.toOpenGraphData
import com.linky.link_detail_input.state.DetailInputSideEffect
import com.linky.link_detail_input.state.DetailInputState
import com.linky.link_detail_input.state.DetailInputState.Companion.Init
import com.linky.link_detail_input.state.LinkSaveStatus
import com.linky.link_detail_input.state.Mode
import com.linky.link_detail_input.state.OpenGraphStatus
import com.linky.model.Link
import com.linky.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class DetailInputViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val selectAllWithUsageUseCase: SelectAllWithUsageUseCase,
    private val findLinkByIdUseCase: FindLinkByIdUseCase,
    private val tagInsertUseCase: TagInsertUseCase,
    private val tagDeleteUseCase: TagDeleteUseCase,
    private val linkInsertUseCase: LinkInsertUseCase,
    private val updateLinkIdsUseCase: UpdateLinkIdsUseCase,
) : ContainerHost<DetailInputState, DetailInputSideEffect>, AndroidViewModel(application) {

    override val container = container<DetailInputState, DetailInputSideEffect>(Init)

    fun doAction(action: DetailInputAction) {
        when (action) {
            is DetailInputAction.AddTag -> addTag(action.name)
            is DetailInputAction.DeleteTag -> deleteTag(action.id)
            is DetailInputAction.SaveLink -> saveLink(action.link)
        }
    }

    private fun setMode() {
        intent {
            savedStateHandle.get<Int>("mode")?.let { modeKey ->
                val mode = when (modeKey) {
                    2 -> Mode.Editor
                    else -> Mode.Creator
                }

                reduce { state.copy(mode = mode) }
            }
        }
    }

    private fun getTags() {
        intent {
            savedStateHandle.get<Long>("linkId")?.let { linkId ->
                reduce {
                    state.copy(
                        tags = selectAllWithUsageUseCase.invoke(linkId).cachedIn(viewModelScope)
                    )
                }
            }
        }
    }

    private fun getLink() {
        intent {
            savedStateHandle.get<Long>("linkId")?.let { linkId ->
                val link = findLinkByIdUseCase.invoke(linkId)

                reduce { state.copy(link = link) }
            }
        }
    }

    private fun parse() {
        savedStateHandle
            .get<String>("url")
            ?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            ?.also { url ->
                OpenGraphParser(
                    listener = object : OpenGraphCallback {
                        override fun onError(error: String) {
                            intent {
                                reduce { state.copy(openGraphStatus = OpenGraphStatus.Error(error)) }
                            }
                        }

                        override fun onPostResponse(openGraphResult: OpenGraphResult) {
                            intent {
                                reduce {
                                    state.copy(
                                        openGraphStatus = OpenGraphStatus.Success(
                                            openGraphResult.toOpenGraphData()
                                        )
                                    )
                                }
                            }
                        }
                    },
                    showNullOnEmpty = true,
                    cacheProvider = OpenGraphCacheProvider(application.applicationContext),
                ).apply { parse(url) }
            }
    }

    private fun addTag(name: String) {
        intent {
            tagInsertUseCase.invoke(Tag(name = name))

            postSideEffect(DetailInputSideEffect.TagTextClear)
        }
    }

    private fun deleteTag(id: Long) {
        intent {
            tagDeleteUseCase.invoke(id)
        }
    }

    private fun saveLink(link: Link) {
        intent {
            try {
                reduce { state.copy(linkSaveStatus = LinkSaveStatus.Loading) }

                val id = linkInsertUseCase.invoke(link)
                updateLinkIdsUseCase.invoke(link.tags.map { it.id ?: 0L }, id)

                reduce { state.copy(linkSaveStatus = LinkSaveStatus.Success) }
            } catch (e: Exception) {
                reduce { state.copy(linkSaveStatus = LinkSaveStatus.Error) }
            }
        }
    }

    init {
        setMode()
        getTags()
        getLink()
        parse()
    }

}

sealed interface DetailInputAction {
    data class AddTag(val name: String) : DetailInputAction
    data class DeleteTag(val id: Long) : DetailInputAction
    data class SaveLink(val link: Link) : DetailInputAction
}