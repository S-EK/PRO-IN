package com.jsseok.proin.ui.eng_company

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
import com.jsseok.proin.databinding.EngNewsBinding
import com.jsseok.proin.ui.alarm.AlarmActivity
import com.jsseok.proin.ui.eng_company.adapter.GridAdapter
import com.jsseok.proin.ui.eng_news.EngNewsActivity
import com.jsseok.proin.ui.news_company.NewsCompanyActivity

// 영어 신문사
class EngNewsCompanyActivity : AppCompatActivity() {
    // EngNewsCompanyActivity 해당하는 EngNews 바인딩 : 레이아웃 관리
    private lateinit var binding: EngNewsBinding
    // 영어 뉴스 신문사 Data 저장
    private val data: ArrayList<Company>
        get() {
            val companys = ArrayList<Company>()

            var company = Company("TNW", "https://thenextweb.com/", R.drawable.tnw)
            companys.add(company)
            company = Company("RECODE", "https://www.vox.com/recode", R.drawable.recode)
            companys.add(company)
            company = Company("ENGADGET", "https://www.engadget.com/", R.drawable.engadget)
            companys.add(company)
            company = Company("THE VERGE", "https://www.theverge.com/", R.drawable.theverge)
            companys.add(company)
            company = Company("SLASHGEAR","https://www.slashgear.com/", R.drawable.slashgea)
            companys.add(company)
            company = Company("WIRED", "https://www.wired.com/", R.drawable.wired)
            companys.add(company)
            company = Company("MASHABLE", "https://mashable.com/", R.drawable.mashable)
            companys.add(company)
            company = Company("cnet", "https://www.cnet.com/", R.drawable.cnet)
            companys.add(company)
            company = Company("DIGIDAY", "https://digiday.com/", R.drawable.digiday)
            companys.add(company)
            company = Company("TechRepublic", "https://www.techrepublic.com/", R.drawable.tech)
            companys.add(company)

            return companys
        }

    // onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EngNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)    // 화면 표시

        val gridadapter = GridAdapter(this, data)
        binding.grid.adapter = gridadapter

        // 특정 신문사를 클릭했을 때
        binding.grid.setOnItemClickListener { adapterView, view, position, id ->
            val com = gridadapter.getItem(position) as Company
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(com.getURL()))
            startActivity(intent)   // 그 신문사의 IT 코너로 이동
        }

        // 'NEWS' 버튼을 클릭했을 때
        binding.mainAll.setOnClickListener {
            val mIntent = Intent(this, EngNewsActivity::class.java)
            startActivity(mIntent)  // 해당 액티비티로 이동
            overridePendingTransition(0, 0) // 애니메이션 제거
        }

        // '언어' 버튼을 클릭했을 때
        binding.language.setOnClickListener {
            val mIntent = Intent(this, NewsCompanyActivity::class.java)
            startActivity(mIntent)  // 한국 신문사 액티비티로 이동
            overridePendingTransition(0, 0) // 애니메이션 제거
        }
    }

    // 옵션 메뉴 - 영어 ver
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_eng, menu)
        return true
    }

    // 메뉴의 아이템을 선택했을 때의 동작 구현
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 'Developer information'을 누른다면
            R.id.info -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/sek010324"))
                startActivity(intent)   // 개발자의 블로그로 이동
                true
            }
            // 'Suggestion'을 누른다면
            R.id.reque -> {
                // 건의를 전송할 이메일을 클립보드에 복사
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("label", "sek010324@naver.com")
                clipboard.setPrimaryClip(clip)
                // 복사 완료 메세지를 영어로 출력
                Toast.makeText(this, "The email has been copied to the clipboard.", Toast.LENGTH_SHORT).show()
                true
            }
            // 'Notification setting'을 누른다면
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
            setTitle("Are you sure you want to shut down the app?")
            setCancelable(false) // 다이얼로그 화면 밖 터치하는 것 방지
            setPositiveButton(
                "YES"   // 애플리케이션 종료
            ) { _, _ -> moveTaskToBack(true)
                finish()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            setNegativeButton(
                "NO"    // 다시 화면으로
            ) { _, _ -> }
            show() // 다이얼로그 보이기
        }
    }
}
