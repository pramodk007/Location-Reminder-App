package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    Add testing implementation to the RemindersDao.kt

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun testInsertRetrieveData() = runBlockingTest {

        val data = ReminderDTO(
            "Test Title",
            "Test Description",
            "Test Location",
            -19.917299,
            -43.934559
        )

        database.reminderDao().saveReminder(data)

        val loadedDataList = database.reminderDao().getReminders()

        MatcherAssert.assertThat(loadedDataList.size, CoreMatchers.`is`(1))

        val loadedData = loadedDataList[0]
        MatcherAssert.assertThat(loadedData.id, CoreMatchers.`is`(data.id))
        MatcherAssert.assertThat(loadedData.title, CoreMatchers.`is`(data.title))
        MatcherAssert.assertThat(loadedData.description, CoreMatchers.`is`(data.description))
        MatcherAssert.assertThat(loadedData.location, CoreMatchers.`is`(data.location))
        MatcherAssert.assertThat(loadedData.latitude, CoreMatchers.`is`(data.latitude))
        MatcherAssert.assertThat(loadedData.longitude, CoreMatchers.`is`(data.longitude))

    }
}