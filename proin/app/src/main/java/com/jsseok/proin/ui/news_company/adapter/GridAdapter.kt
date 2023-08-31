package com.jsseok.proin.ui.news_company.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jsseok.proin.R
import com.jsseok.proin.data.model.Company

// GridView Adapter
class GridAdapter(val context: Context, private val companys: ArrayList<Company>) : BaseAdapter() {

    var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //  Displays the data at the specified position in the data set
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.gridview_part, parent, false)
        }

        // 신문사 이름
        val nameTxt = view?.findViewById<TextView>(R.id.the_name)

        if (nameTxt != null) {
            nameTxt.text = companys[position].getName()
        }

        // 신문사 로고
        view?.findViewById<ImageView>(R.id.img1)?.setImageResource(companys[position].getImgno())

        return view!!
    }

    override fun getItem(position: Int): Any = companys[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = companys.size
}