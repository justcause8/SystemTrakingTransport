package com.example.systemtrakingtransport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.systemtrackingtransport.DatabaseHelper
import com.example.systemtrackingtransport.db.DaoSession
import com.example.systemtrackingtransport.db.Vehicle
import com.example.systemtrackingtransport.db.Sedan
import com.example.systemtrackingtransport.db.Wagon
import com.example.systemtrackingtransport.db.Suv

class MainActivity : AppCompatActivity() {
    private lateinit var etBrand: EditText
    private lateinit var etModel: EditText
    private lateinit var etYear: EditText
    private lateinit var rgVehicleType: RadioGroup
    private lateinit var btnSubmit: Button
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var daoSession: DaoSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация элементов интерфейса
        etBrand = findViewById(R.id.etBrand)
        etModel = findViewById(R.id.etModel)
        etYear = findViewById(R.id.etYear)
        rgVehicleType = findViewById(R.id.rgVehicleType)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Подключение к базе данных
        try {
            val databaseHelper = DatabaseHelper(this)
            daoSession = databaseHelper.getDaoSessionInstance()
            Log.d("Database", "Database connected successfully")

        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка подключения к базе данных: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("Database", "Error: ${e.message}", e)
        }

        // Получение данных для редактирования
        val vehicleId = intent.getLongExtra("vehicleId", -1)
        val isEditMode = vehicleId != -1L
        val vehicleToEdit = if (isEditMode) {
            daoSession.vehicleDao.load(vehicleId)
        } else null

        // Заполнение полей данными при редактировании
        vehicleToEdit?.let {
            etBrand.setText(it.brand)
            etModel.setText(it.model)
            etYear.setText(it.year)
            when (it.type) {
                "Седан" -> rgVehicleType.check(R.id.rbSedan)
                "Универсал" -> rgVehicleType.check(R.id.rbWagon)
                "Внедорожник" -> rgVehicleType.check(R.id.rbSuv)
            }
        }

        // Обработчик нажатия на кнопку "Submit"
        btnSubmit.setOnClickListener {
            submitForm(isEditMode, vehicleToEdit)
        }

        // Инициализация детектора жестов для свайпов
        gestureDetector = GestureDetectorCompat(this, SwipeGestureListener(isEditMode, vehicleToEdit))
        btnSubmit.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
    }

    fun saveVehicle(vehicle: Vehicle) {
        val vehicleId = daoSession.vehicleDao.insert(vehicle)
        Log.d("Database", "New Vehicle ID: $vehicleId")  // Логируем id добавленного автомобиля

        when (vehicle.type) {
            "Седан" -> {
                val sedan = Sedan()
                sedan.vehicleId = vehicleId
                daoSession.sedanDao.insert(sedan)
                Log.d("Database", "Sedan Vehicle ID: $vehicleId")
            }
            "Универсал" -> {
                val wagon = Wagon()
                wagon.vehicleId = vehicleId
                daoSession.wagonDao.insert(wagon)
                Log.d("Database", "Wagon Vehicle ID: $vehicleId")
            }
            "Внедорожник" -> {
                val suv = Suv()
                suv.vehicleId = vehicleId
                daoSession.suvDao.insert(suv)
                Log.d("Database", "SUV Vehicle ID: $vehicleId")
            }
        }
    }

    // Функция для отправки формы (добавление или обновление)
    private fun submitForm(isEditMode: Boolean, vehicleToEdit: Vehicle?) {
        val brand = etBrand.text.toString().trim()
        val model = etModel.text.toString().trim()
        val year = etYear.text.toString().trim()

        // Проверка на пустые поля
        if (brand.isEmpty() || model.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        // Определение типа автомобиля
        val vehicleType = when (rgVehicleType.checkedRadioButtonId) {
            R.id.rbSedan -> "Седан"
            R.id.rbWagon -> "Универсал"
            R.id.rbSuv -> "Внедорожник"
            else -> "Неизвестно"
        }

        // Создаем или обновляем объект в базе данных
        if (isEditMode && vehicleToEdit != null) {
            vehicleToEdit.brand = brand
            vehicleToEdit.model = model
            vehicleToEdit.year = year
            vehicleToEdit.type = vehicleType
            try {
                daoSession.vehicleDao.update(vehicleToEdit)
                Toast.makeText(this, "Данные обновлены", Toast.LENGTH_SHORT).show()
                Log.d("Database", "Vehicle updated successfully")
            } catch (e: Exception) {
                Log.e("Database", "Error updating vehicle: ${e.message}", e)
            }
        }
        else {
            val newVehicle = Vehicle(null, brand, model, year, vehicleType)
            try {
                saveVehicle(newVehicle)
                daoSession.vehicleDao.insertOrReplace(newVehicle)
                Toast.makeText(this, "Данные добавлены", Toast.LENGTH_SHORT).show()
                Log.d("Database", "New vehicle inserted successfully")
                // Очистка полей после добавления
                etBrand.text.clear()
                etModel.text.clear()
                etYear.text.clear()
                rgVehicleType.clearCheck()
            } catch (e: Exception) {
                Log.e("Database", "Error inserting vehicle: ${e.message}", e)
            }

            // Получаем все записи из таблиц и выводим их в лог
            val vehicles = daoSession.vehicleDao.loadAll()
            val sedans = daoSession.sedanDao.loadAll()
            val wagons = daoSession.wagonDao.loadAll()
            val suvs = daoSession.suvDao.loadAll()

            // Выводим содержимое в лог
            Log.d("Database", "Vehicles: $vehicles")
            Log.d("Database", "Sedans: $sedans")
            Log.d("Database", "Wagons: $wagons")
            Log.d("Database", "SUVs: $suvs")
        }
    }

    // Обработчик жестов (свайп)
    private inner class SwipeGestureListener(val isEditMode: Boolean, val vehicleToEdit: Vehicle?) :
        GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - (e1?.x ?: 0.0f)
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {  // Правый свайп
                    submitForm(isEditMode, vehicleToEdit)
                }
                return true
            }
            return false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pageMain -> return true
            R.id.pageInfo -> {
                startActivity(Intent(this, InfoActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}