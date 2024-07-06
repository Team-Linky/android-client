package com.linky.core.data_base

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.linky.core.data_base.LinkyDataBase
import com.linky.core.data_base.backup.LinkTagCrossRefBackupDao
import com.linky.core.data_base.link.dao.LinkDao
import com.linky.core.data_base.link.dao.LinkTagCrossRefDao
import com.linky.core.data_base.link.data_source.LinkDataSource
import com.linky.core.data_base.link.data_source.LinkDataSourceImpl
import com.linky.core.data_base.tag.dao.TagDao
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var database: LinkyDataBase

    private lateinit var linkDataSource: LinkDataSource

    private lateinit var linkDao: LinkDao
    private lateinit var tagDao: TagDao
    private lateinit var linkTagCrossRefDao: LinkTagCrossRefDao
    private lateinit var linkTagCrossRefBackupDao: LinkTagCrossRefBackupDao

    @BeforeEach
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            LinkyDataBase::class.java
        ).build()

        tagDao = database.getTagDao()
        linkDao = database.getLinkDao()
        linkTagCrossRefDao = database.getLinkTagCrossRefDao()
        linkTagCrossRefBackupDao = database.getLinkTagCrossRefBackupDao()

        linkDataSource = LinkDataSourceImpl(
            linkDao = linkDao,
            linkTagCrossRefDao = linkTagCrossRefDao,
            linkTagCrossRefBackupDao = linkTagCrossRefBackupDao
        )
    }

    @Test
    fun test_getLinksUseCase() = runTest {

    }
}