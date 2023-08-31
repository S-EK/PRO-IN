package com.jsseok.proin.ui.news_company

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jsseok.proin.R
import com.jsseok.proin.data.model.Company
import com.jsseok.proin.databinding.KoreaNewsBinding
import com.jsseok.proin.ui.alarm.AlarmActivity
import com.jsseok.proin.ui.eng_company.EngNewsCompanyActivity
import com.jsseok.proin.ui.main.MainActivity
import com.jsseok.proin.ui.news_company.adapter.GridAdapter

// 한국 신문사
class NewsCompanyActivity : AppCompatActivity() {
    // NewsCompanyActivity에 해당하는 KoreaNews 바인딩 : 레이아웃 관리
    private lateinit var binding: KoreaNewsBinding
    // 한국 뉴스 신문사 Data 저장
    private val data: ArrayList<Company>
        get() {
            val companys = ArrayList<Company>()

            var company = Company("동아신문", "https://www.donga.com/news/It/List", R.drawable.da)
            companys.add(company)
            company = Company("한국일보", "https://www.hankookilbo.com/News/Economy/HB05", R.drawable.hk)
            companys.add(company)
            company = Company("중앙일보", "https://www.joongang.co.kr/money/science", R.drawable.ja)
            companys.add(company)
            company = Company("전자신문", "https://www.etnews.com/", R.drawable.jj)
            companys.add(company)
            company = Company("지디넷코리아","https://zdnet.co.kr/news/?lstcode=0020&page=1", R.drawable.jk)
            companys.add(company)
            company = Company("조선일보", "https://www.chosun.com/economy/tech_it/", R.drawable.js)
            companys.add(company)
            company = Company("경향신문", "https://www.khan.co.kr/economy/it-electronic/articles", R.drawable.kh)
            companys.add(company)
            company = Company("국민일보", "http://news.kmib.co.kr/article/list.asp?sid1=inf", R.drawable.km)
            companys.add(company)
            company = Company("세계일보", "https://www.segye.com/newsList/0101030900000", R.drawable.sk)
            companys.add(company)
            company = Company("서울신문", "https://www.seoul.co.kr/news/newsList.php?section=it", R.drawable.su)
            companys.add(company)

            return companys
        }

    // onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KoreaNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)    // 화면 표시

        val gridadapter = GridAdapter(this, data)
        binding.grid.adapter = gridadapter  // 어댑터 연결

        // 특정 신문사를 클릭했을 때
        binding.grid.setOnItemClickListener { adapterView, view, position, id ->
            val com = gridadapter.getItem(position) as Company
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(com.getURL()))
            startActivity(intent)   // 그 신문사의 IT 코너로 이동
        }

        // '뉴스' 버튼을 클릭했을 때
        binding.mainAll.setOnClickListener {
            val mIntent = Intent(this, MainActivity::class.java)
            startActivity(mIntent)  // 해당 액티비티로 이동
            overridePendingTransition(0, 0) // 애니메이션 제거
        }

        // '언어' 버튼을 클릭했을 때
        binding.language.setOnClickListener {
            val mIntent = Intent(this, EngNewsCompanyActivity::class.java)
            startActivity(mIntent)  // 영어 신문사 액티비티로 이동
            overridePendingTransition(0, 0) // 애니메이션 제거
        }
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