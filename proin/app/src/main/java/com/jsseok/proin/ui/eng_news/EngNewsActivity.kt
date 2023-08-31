package com.jsseok.proin.ui.eng_news

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jsseok.proin.R
import com.jsseok.proin.data.model.News
import com.jsseok.proin.databinding.ActivityMainEngBinding
import com.jsseok.proin.ui.alarm.AlarmActivity
import com.jsseok.proin.ui.eng_company.EngNewsCompanyActivity
import com.jsseok.proin.ui.main.MainActivity
import com.jsseok.proin.ui.main.adapter.NewsListAdapter
import dagger.hilt.android.AndroidEntryPoint

// 영문 IT 뉴스
@AndroidEntryPoint
class EngNewsActivity : AppCompatActivity() {
    // EngNewsActivity 해당하는 ActivityMainEng 바인딩 : 레이아웃 관리
    private lateinit var binding: ActivityMainEngBinding
    private val mainViewModel: EngNewsViewModel by viewModels()

    // onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainEngBinding.inflate(layoutInflater)
        setContentView(binding.root)    // 화면 표시

        binding.mainNews.setOnClickListener {
            val mIntent = Intent(this, EngNewsCompanyActivity::class.java)
            startActivity(mIntent)
            overridePendingTransition(0, 0)
        }

        binding.language.setOnClickListener {
            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)
            overridePendingTransition(0, 0)
        }

        val newsListAdapter = NewsListAdapter() // 어댑터 클래스 선언
        val newsListLayoutManager = LinearLayoutManager(this)   // Layout Manager

        binding.myRecyclerView.apply {
            setHasFixedSize(true)   // 아이템의 크기가 변하지 않음을 명시
            adapter = newsListAdapter   // myReyclerView의 어댑터로 newListAdapter를 사용하겠다?
            layoutManager = newsListLayoutManager   // LM으로 newsListLayoutManger를 사용하겠다
        }

        mainViewModel.newsListLiveData.observe(this, { newsList ->
            newsList?.let {
                newsListAdapter.updateNews(newsList)
            }
        })

        mainViewModel.list()

        newsListAdapter.setItemClickListenr(object : NewsListAdapter.OnItemClickListener {
            override fun onClick(v: View, newNewsList: List<News>, position: Int) {
                val item = newNewsList[position].getURL()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item))
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_eng, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.info -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/sek010324"))
                startActivity(intent)
                true
            }
            R.id.reque -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("label", "sek010324@naver.com")
                clipboard.setPrimaryClip(clip)
                var t1 = Toast.makeText(this, "The email has been copied to the clipboard.", Toast.LENGTH_SHORT)
                t1.show()
                true
            }
            R.id.setAlram -> {
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {   // 뒤로가기 누르면 다이얼로그 생성
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Are you sure you want to shut down the app?")
            setCancelable(false) // 다이얼로그 화면 밖 터치 방지
            setPositiveButton(
                "YES"
            ) { _, _ -> moveTaskToBack(true)
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
//            ) { _, _ -> finish() }
            setNegativeButton(
                "NO"
            ) { _, _ -> }
            show() // 다이얼로그 보이기
        }
    }
}