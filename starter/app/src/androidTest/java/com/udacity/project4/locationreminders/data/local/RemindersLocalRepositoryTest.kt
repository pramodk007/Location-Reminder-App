package com.udacity.project4.locationreminders.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    Add testing implementation to the RemindersLocalRepository.kt

    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var remindersLocalRepository: RemindersLocalRepository

    @Before
    fun setup() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        remindersLocalRepository = RemindersLocalRepository(remindersDatabase.reminderDao())
    }

    @After
    fun closeDatabase() = remindersDatabase.close()

    @Test
    fun testInsertRetrieveData() = runBlocking {

        val data = ReminderDTO(
            "Test Title",
            "Test Description",
            "Test Location",
            -19.917299,
            -43.934559
        )

        remindersLocalRepository.saveReminder(data)

        val result = remindersLocalRepository.getReminder(data.id)

        result as Result.Success
        assertThat(true, CoreMatchers.`is`(true))

        val loadedData = result.data
        assertThat(loadedData.id, CoreMatchers.`is`(data.id))
        assertThat(loadedData.title, CoreMatchers.`is`(data.title))
        assertThat(loadedData.description, CoreMatchers.`is`(data.description))
        assertThat(loadedData.location, CoreMatchers.`is`(data.location))
        assertThat(loadedData.latitude, CoreMatchers.`is`(data.latitude))
        assertThat(loadedData.longitude, CoreMatchers.`is`(data.longitude))
    }
    @Test
    fun testDataNotFound_returnError() = runBlocking {
        val result = remindersLocalRepository.getReminder("1")
        val error =  (result is Result.Error)
        assertThat(error, CoreMatchers.`is`(true))
        result as Result.Error
        assertThat(result.message, CoreMatchers.`is`("Reminder not found!"))
    }
}