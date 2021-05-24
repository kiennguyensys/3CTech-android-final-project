package com.yoshitoke.weatheringwithyou.mvp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.vipulasri.timelineview.TimelineView
import com.yoshitoke.weatheringwithyou.R
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.Daily
import com.yoshitoke.weatheringwithyou.mvp.model.DataClass.Hourly
import com.yoshitoke.weatheringwithyou.utils.*
import com.yoshitoke.weatheringwithyou.utils.DateConstant.Companion.HOUR_FORMAT
import com.yoshitoke.weatheringwithyou.utils.network.ICON_URL_POSTFIX_2X
import com.yoshitoke.weatheringwithyou.utils.network.ICON_URL_POSTFIX_4X
import com.yoshitoke.weatheringwithyou.utils.network.ICON_URL_PREFIX
import kotlinx.android.synthetic.main.timeline_item.view.*

class HourlyTimeLineAdapter(private val mFeedList: List<Hourly>) : RecyclerView.Adapter<HourlyTimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if(!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return TimeLineViewHolder(mLayoutInflater.inflate(R.layout.timeline_item, parent, false), viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(mFeedList[position], position == 0)
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val date = itemView.text_timeline_date
        val message = itemView.text_timeline_title
        val temp = itemView.text_timeline_temp
        val timeline = itemView.hourly_forcast_timeline
        val image = itemView.circleView

        init {
            timeline.initLine(viewType)
        }

        fun bind(model: Hourly, isFirstPosition: Boolean) {
            val url: String

            date.text = model.dateTime.unixTimestampToString(HOUR_FORMAT)
            message.text = model.weathers[0].description.toFirstCapString()
            temp.text = model.temperature.kelvinToCelsius().toCelsiusString()

            if (isFirstPosition) {
                //double the image size
                image.requestLayout()
                image.layoutParams.height = image.layoutParams.height * 2
                image.layoutParams.width = image.layoutParams.width * 2
                url = ICON_URL_PREFIX + model.weathers[0].iconName + ICON_URL_POSTFIX_4X

                date.visibility = View.GONE
                message.visibility = View.GONE
                temp.visibility = View.GONE
            } else {
                url = ICON_URL_PREFIX + model.weathers[0].iconName + ICON_URL_POSTFIX_2X
            }

            Glide.with(itemView.context)
                    .load(url)
                    .into(image);
        }
    }

}
