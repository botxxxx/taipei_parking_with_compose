package com.example.parking.main
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.parking.R
//import com.example.parking.api.data.TimeZone
//
//class ChooseTimeZoneAdapter(private var dataList: List<TimeZone>, private val onItemClick: (TimeZone) -> Unit) :
//    RecyclerView.Adapter<ChooseTimeZoneAdapter.ViewHolder>() {
//
//    /**
//     * Provide a reference to the type of views that you are using
//     * (custom ViewHolder).
//     */
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val textView: TextView = view.findViewById(R.id.tv_name)
//    }
//
//    fun setData(bankInfoList: List<TimeZone>) {
//        this.dataList = bankInfoList
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog, viewGroup, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val timeZone = dataList[position]
//        viewHolder.textView.text = timeZone.name
//        viewHolder.textView.setOnClickListener {
//            onItemClick.invoke(timeZone)
//        }
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataList.size
//
//}