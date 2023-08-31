package com.jsseok.proin.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.jsseok.proin.databinding.ActivityMainBinding
import com.jsseok.proin.ui.alarm.AlarmActivity
import com.jsseok.proin.ui.eng_news.EngNewsActivity
import com.jsseok.proin.ui.main.adapter.NewsListAdapter
import com.jsseok.proin.ui.news_company.NewsCompanyActivity
import dagger.hilt.android.AndroidEntryPoint

// 한국 IT 뉴스
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // MainActivity 해당하는 ActivityMain 바인딩 : 레이아웃 관리
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    // onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)    // 화면 표시

        // '신문사' 버튼을 클릭했을 때
        binding.mainNews.setOnClickListener {
            val mIntent = Intent(this, NewsCompanyActivity::class.java)
            startActivity(mIntent)  // 해당 액티비티로 이동
            overridePendingTransition(0, 0) // 애니메이션 제거
        }

        // '언어' 버튼을 클릭했을 때
        binding.language.setOnClickListener {
            val mIntent = Intent(this, EngNewsActivity::class.java)
            startActivity(mIntent)  // 영어 뉴스 액티비티로 이동
            overridePendingTransition(0, 0) // 애니메이션 제거
        }

        val newsListAdapter = NewsListAdapter() // 어댑터 클래스 선언
        val newsListLayoutManager = LinearLayoutManager(this)   // LayoutManager

        binding.myRecyclerView.apply {
            setHasFixedSize(true)   // 아이템의 크기가 변하지 않음을 명시
            adapter = newsListAdapter   // Adapter 설정
            layoutManager = newsListLayoutManager   // LayoutManager 설정
        }

        // View Model을 활용 -> News Update
        mainViewModel.newsListLiveData.observe(this, { newsList ->
            newsList?.let {
                newsListAdapter.updateNews(newsList)
            }
        })

        mainViewModel.list()

        // 특정 뉴스를 클릭했을 때
        newsListAdapter.setItemClickListenr(object : NewsListAdapter.OnItemClickListener {
            override fun onClick(v: View, newNewsList: List<News>, position: Int) {
                val item = newNewsList[position].getURL()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item))
                startActivity(intent)   // 그 뉴스의 웹으로 이동
            }
        })
    }

    // 옵션 메뉴 - 한국어 ver
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    // 메뉴의 아이템을 선택했을 때의 동작 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // '개발자 정보'를 누른다면
            R.id.info -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/sek010324"))
                startActivity(intent)   // 개발자의 블로그로 이동
                true
            }
            // '건의 사항'을 누른다면
            R.id.reque -> {
                // 건의를 전송할 이메일을 클립보드에 복사
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("label", "sek010324@naver.com")
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "클립보드에 이메일이 복사되었습니다.", Toast.LENGTH_SHORT).show()
                true
            }
            // '알림 설정'을 누른다면
            R.id.setAlram -> {
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)   // 알림을 설정할 수 있는 액티비티로 이동
                overridePendingTransition(0, 0) // 애니메이션 제거
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // '뒤로 가기'를 눌렀을 때 종료 여부를 묻는 다이얼로그
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("종료할까요?")
            setCancelable(false) // 다이얼로그 화면 밖 터치하는 것 방지
            setPositiveButton(
                "예" // 애플리케이션 종료
            ) { _, _ -> moveTaskToBack(true)
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            setNegativeButton(
                "아니요"   // 다시 화면으로
            ) { _, _ -> }
            show() // 다이얼로그 보이기
        }
    }
}