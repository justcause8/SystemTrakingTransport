package com.example.systemtrakingtransport

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class InfoActivity : AppCompatActivity() {
    private lateinit var rvVehicleList: RecyclerView
    private lateinit var vehicleAdapter: VehicleAdapter
    private val vehicleInfoList = mutableListOf<Vehicle>()
    private lateinit var storageManager: StorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        storageManager = StorageManager() // Инициализация StorageManager

        rvVehicleList = findViewById(R.id.rvVehicleList) //Создаем список в RecyclerView
        rvVehicleList.layoutManager = GridLayoutManager(this, 3)

        // Получаем данные из SharedPreferences и добавляем их в список
        val vehicleList = storageManager.getFromSharedPreferences(this)
        vehicleInfoList.addAll(vehicleList)

        // Связываем адаптер и список
        vehicleAdapter = VehicleAdapter(vehicleInfoList) { position ->
            vehicleAdapter.setSelectedItem(position)
        }
        rvVehicleList.adapter = vehicleAdapter

        // Обновляем адаптер, чтобы отображать новые данные
        vehicleAdapter.notifyDataSetChanged()

        // Извлекаем из Intent переданный список
        val receivedList = intent.getParcelableArrayListExtra<Vehicle>("vehicleList") ?: arrayListOf()
        vehicleInfoList.addAll(receivedList)
        vehicleAdapter.notifyDataSetChanged()

        // Настройка ItemTouchHelper для различных свайпов
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // перемещения элементов в списке не поддерживаются
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Определяем какой элемент выбран (его позицию)
                val position = viewHolder.adapterPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // Удаление элемента
                        vehicleInfoList.removeAt(position)
                        vehicleAdapter.notifyDataSetChanged()

                        // Обновляем SharedPreferences после удаления
                        storageManager.saveToSharedPreferences(viewHolder.itemView.context, vehicleInfoList)
                    }
                    ItemTouchHelper.RIGHT -> {
                        // Переход на редактирование
                        editItem(position)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback) //Создаем экземпляр для обработки жестов
        itemTouchHelper.attachToRecyclerView(rvVehicleList) //Привязываем жесты к RecyclerView
    }

    private fun editItem(position: Int) {
        val vehicle = vehicleInfoList[position]
        val intent = Intent(this, MainActivity::class.java)

        // Передаем объект для редактирования в MainActivity
        intent.putExtra("vehicleToEdit", vehicle)
        intent.putExtra("editPosition", position)
        startActivityForResult(intent, REQUEST_EDIT)
    }

    //Возвращаем результат активности
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) { //Если это редактирование и все нормально, то обрабатываем результаты
            val updatedVehicle = data?.getParcelableExtra<Vehicle>("updatedVehicle") // Извлекаем элемент
            val editPosition = data?.getIntExtra("editPosition", -1) ?: -1 // Извлекаем позицию

            if (updatedVehicle != null && editPosition != -1) {  // Если оба значения корректные
                // Обновляем элемент через адаптер
                vehicleAdapter.editItem(editPosition, updatedVehicle)

                // Обновляем SharedPreferences после редактирования
                storageManager.saveToSharedPreferences(this, vehicleInfoList)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pageMain -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.pageInfo -> {
                val intent = Intent(this, InfoActivity::class.java)
                intent.putParcelableArrayListExtra("vehicleList", ArrayList(vehicleInfoList)) // Передаем список
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_EDIT = 1
    }
}