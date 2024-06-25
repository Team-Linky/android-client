package com.linky.feature.link_modifier

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
import com.linky.feature.link_modifier.model.toOpenGraphData
import com.linky.feature.link_modifier.state.LinkModifierSideEffect
import com.linky.feature.link_modifier.state.LinkModifierState
import com.linky.feature.link_modifier.state.LinkModifierState.Companion.Init
import com.linky.feature.link_modifier.state.LinkSaveStatus
import com.linky.feature.link_modifier.state.Mode
import com.linky.feature.link_modifier.state.OpenGraphStatus
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
class LinkModifierViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val selectAllWithUsageUseCase: SelectAllWithUsageUseCase,
    private val findLinkByIdUseCase: FindLinkByIdUseCase,
    private val tagInsertUseCase: TagInsertUseCase,
    private val tagDeleteUseCase: TagDeleteUseCase,
    private val linkInsertUseCase: LinkInsertUseCase,
    private val updateLinkIdsUseCase: UpdateLinkIdsUseCase,
) : ContainerHost<LinkModifierState, LinkModifierSideEffect>, AndroidViewModel(application) {

    override val container = container<LinkModifierState, LinkModifierSideEffect>(Init)

    fun doAction(action: LinkModifierAction) {
        when (action) {
            is LinkModifierAction.AddTag -> addTag(action.name)
            is LinkModifierAction.DeleteTag -> deleteTag(action.id)
            is LinkModifierAction.SaveLink -> saveLink(action.link)
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

            postSideEffect(LinkModifierSideEffect.TagTextClear)
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

sealed interface LinkModifierAction {
    data class AddTag(val name: String) : LinkModifierAction
    data class DeleteTag(val id: Long) : LinkModifierAction
    data class SaveLink(val link: Link) : LinkModifierAction
}