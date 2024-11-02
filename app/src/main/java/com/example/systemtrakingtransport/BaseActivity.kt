//package com.example.transporttrakingsystem
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import com.example.systemtrakingtransport.R
//
//open class BaseActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    // Метод для установки toolbar на каждой активности
//    fun setupToolbar(toolbar: Toolbar) {
//        setSupportActionBar(toolbar)
//    }
//
//    // Подключение меню навигации
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    // Обработка нажатий на пункты меню
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.pageMain -> {
//                startActivity(Intent(this, MainActivity::class.java))
//                true
//            }
//            R.id.pageInfo -> {
//                startActivity(Intent(this, InfoActivity::class.java))
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//}
