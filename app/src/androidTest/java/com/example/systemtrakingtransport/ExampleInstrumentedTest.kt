package com.example.systemtrakingtransport

import android.content.Intent
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InfoActivityTest {
    @Test
    fun testVehicleAdditionAndEdit() {
        // Запускаем MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Заполняем поля формы добавления автомобиля
        onView(withId(R.id.etBrand)).perform(replaceText("Toyota"), closeSoftKeyboard())
        onView(withId(R.id.etModel)).perform(replaceText("Camry"), closeSoftKeyboard())
        onView(withId(R.id.etYear)).perform(replaceText("2020"), closeSoftKeyboard())
        onView(withId(R.id.rbSedan)).perform(click())
        onView(withId(R.id.btnSubmit)).perform(click())

        // Открываем InfoActivity через меню
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Информация")).perform(click())

        // Проверяем, что InfoActivity открыта и автомобиль добавлен
        onView(withId(R.id.rvVehicleList)).check(matches(isDisplayed()))

        // Проверяем, что данные автомобиля отображаются
        onView(withText("Марка: Toyota")).check(matches(isDisplayed()))
        onView(withText("Модель: Camry")).check(matches(isDisplayed()))
        onView(withText("Год: 2020")).check(matches(isDisplayed()))
        onView(withText("Тип: Седан")).check(matches(isDisplayed()))

        // Свайп вправо для редактирования автомобиля
        onView(withText("Марка: Toyota")).perform(swipeRight())

        // Проверяем, что поля для редактирования заполнены корректными данными
        onView(withId(R.id.etBrand)).check(matches(withText("Toyota")))
        onView(withId(R.id.etModel)).check(matches(withText("Camry")))
        onView(withId(R.id.etYear)).check(matches(withText("2020")))

        // Изменяем данные автомобиля
        onView(withId(R.id.etModel)).perform(replaceText("Corolla"), closeSoftKeyboard())
        onView(withId(R.id.btnSubmit)).perform(click())

        // Проверяем, что данные обновлены в InfoActivity
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Информация")).perform(click())

        // Проверяем, что данные обновлены
        onView(withText("Марка: Toyota")).check(matches(isDisplayed()))
        onView(withText("Модель: Corolla")).check(matches(isDisplayed()))
    }

    @Test
    fun testVehicleAdditionAndDeletion() {
        // Запускаем MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Заполняем поля формы добавления автомобиля
        onView(withId(R.id.etBrand)).perform(replaceText("Toyota"), closeSoftKeyboard())
        onView(withId(R.id.etModel)).perform(replaceText("Camry"), closeSoftKeyboard())
        onView(withId(R.id.etYear)).perform(replaceText("2020"), closeSoftKeyboard())
        onView(withId(R.id.rbSedan)).perform(click())
        onView(withId(R.id.btnSubmit)).perform(click())

        // Открываем InfoActivity через меню
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Информация")).perform(click())

        // Проверяем, что InfoActivity открыта и автомобиль добавлен
        onView(withId(R.id.rvVehicleList)).check(matches(isDisplayed()))

        // Проверяем, что данные автомобиля отображаются
        onView(withText("Марка: Toyota")).check(matches(isDisplayed()))
        onView(withText("Модель: Camry")).check(matches(isDisplayed()))
        onView(withText("Год: 2020")).check(matches(isDisplayed()))
        onView(withText("Тип: Седан")).check(matches(isDisplayed()))

        // Свайп влево для удаления автомобиля
        onView(withText("Марка: Toyota")).perform(swipeLeft())

        // Проверяем, что автомобиль был удален
        onView(withText("Марка: Toyota")).check(doesNotExist())
    }

    @Test
    fun testAddVehicleWithSwipe() {
        // Запускаем MainActivity
        ActivityScenario.launch(MainActivity::class.java)

        // Заполняем поля
        onView(withId(R.id.etBrand)).perform(typeText("Toyota"), closeSoftKeyboard())
        onView(withId(R.id.etModel)).perform(typeText("Corolla"), closeSoftKeyboard())
        onView(withId(R.id.etYear)).perform(typeText("2022"), closeSoftKeyboard())
        onView(withId(R.id.rbSedan)).perform(click())

        // Выполняем свайп вправо на кнопке отправки
        onView(withId(R.id.btnSubmit)).perform(swipeRight())

        // Открываем InfoActivity через меню
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText("Информация")).perform(click())

        // Проверяем, что InfoActivity открыта и автомобиль добавлен
        onView(withId(R.id.rvVehicleList)).check(matches(isDisplayed()))

        // Проверяем, что данные автомобиля отображаются
        onView(withText("Марка: Toyota")).check(matches(isDisplayed()))
        onView(withText("Модель: Corolla")).check(matches(isDisplayed()))
        onView(withText("Год: 2022")).check(matches(isDisplayed()))
        onView(withText("Тип: Седан")).check(matches(isDisplayed()))
    }
}
