package com.linky.core.data_base

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.linky.core.data_base.link.dao.LinkDao
import com.linky.core.data_base.link.entity.LinkEntity
import com.linky.model.open_graph.OpenGraphData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LinkDaoTest {

    private lateinit var database: LinkyDataBase
    private lateinit var linkDao: LinkDao
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @BeforeEach
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = LinkyDataBase.build(context, moshi)
        linkDao = database.getLinkDao()
    }

    @AfterEach
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertLink() = runBlocking {
        val link = LinkEntity(
            id = null,
            memo = "test_memo",
            openGraphData = OpenGraphData(
                title = "Site_Title",
                description = "Site_Desc",
                url = "https://example.com",
                image = "https://example.com/asdasdasd.jpg",
                siteName = "Site_Name",
                type = "website",
            ),
            readCount = 0,
            createAt = System.currentTimeMillis(),
            isRemove = false
        )
        val id = withContext(Dispatchers.IO) {
            linkDao.insert(link)
        }

        Assertions.assertTrue(id > 0L)
    }
}