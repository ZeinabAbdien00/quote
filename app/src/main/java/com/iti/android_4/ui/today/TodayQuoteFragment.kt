package com.iti.android_4.ui.today

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.iti.android_4.QuoteApplication
import com.iti.android_4.R
import com.iti.android_4.data.service.AlarmReceiver
import com.iti.android_4.databinding.FragmentTodayQuoteBinding
import com.iti.android_4.models.quotes.Quotes
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import com.iti.android_4.ui.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*

class TodayQuoteFragment : Fragment() {

    private lateinit var binding: FragmentTodayQuoteBinding
    private lateinit var viewModel: TodayQuotesViewModel
    private lateinit var repository: BaseRepository
    private var toggle: Boolean = true

    private lateinit var data: Quotes
    private lateinit var savedQuotes: List<SavedQuoteLocalDataModel>
    var quoteId = 0

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayQuoteBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = TodayQuotesViewModel(repository)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observation()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Remainder channel experimental"
            val description = "Please wake up! Do study"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AlarmReceiver.CHANNEL_ID, name, importance).apply {
                setDescription(description)
            }
            val notificationManager =
                context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

    }


    private fun setAlarm() {
        try {
            alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("body", binding.todayLayout.tvQuoteContent.text.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getBroadcast(
                    QuoteApplication.context,
                    AlarmReceiver.NOTIFICATION_ID,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                Toast.makeText(requireContext(), "sdkVersionMsg", Toast.LENGTH_SHORT).show()
            }

            val alarmTime = LocalTime.of(18, 15)
            var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            if (now.toLocalTime().isAfter(alarmTime)) {
                now = now.plusDays(1)
            }

            now = now.withHour(alarmTime.hour)
                .withMinute(alarmTime.minute)

            val utc = now.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime()
            val triggerAtMillis = utc.atZone(ZoneOffset.UTC)!!.toInstant()!!.toEpochMilli()

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerAtMillis,
                pendingIntent
            )

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)


        } catch (e: Exception) {

            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()

        }

    }

    private fun init() {
        viewModel.getSavedQuotes()
        if (savedQuotes.isNotEmpty()) {
            binding.todayLayout.apply {
                toggle =
                    if (savedQuotes.contains(
                            SavedQuoteLocalDataModel(
                                quote = tvQuoteContent.text.toString(),
                                author = tvAuthor.text.toString(),
                                date = tvDate.text.toString()
                            )
                        )
                    ) {
                        val drawable =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
                        drawable?.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.base),
                            PorterDuff.Mode.SRC_IN
                        )
                        btnFavorite.setImageDrawable(drawable)
                        false
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_favorite_border)
                        true
                    }
            }
        }
    }

    private fun observation() {


        viewModel.getQuotes()
        viewModel.getSavedQuotes()
        createNotificationChannel()

        viewModel.quotes.observe(viewLifecycleOwner, Observer { response ->
            data = response
            setData(quoteId)
            setAlarm()
            onClickToSaveFavorite()
        })
        viewModel.savedQuotes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                savedQuotes = it
                init()
            }
        })
    }

    private fun setData(quoteId: Int) {
        binding.todayLayout.tvQuoteContent.text =
            data.results[quoteId].content
        binding.todayLayout.tvAuthor.text =
            data.results[quoteId].author
        binding.todayLayout.tvDate.text =
            data.results[quoteId].dateAdded
        if (this.quoteId == 0) this.quoteId++
        init()
    }

    private fun onClickToSaveFavorite() {

        binding.todayLayout.apply {
            btnFavorite.setOnClickListener {
                toggle = if (!toggle) removeFromFav() else addToFav()
            }

            btnShare.setOnClickListener {
                shareQuote()
            }

            btnCopy.setOnClickListener {
                copyQuote()
            }

            btnNext.setOnClickListener {
                nextQuote()
            }
            btnPrevious.setOnClickListener {
                previousQuote()
            }
        }
    }

    private fun addToFav(): Boolean {
        binding.todayLayout.apply {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
            drawable?.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.base),
                PorterDuff.Mode.SRC_IN
            )
            binding.todayLayout.btnFavorite.setImageDrawable(drawable)
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.newQuote(
                    SavedQuoteLocalDataModel(
                        quote = tvQuoteContent.text.toString(),
                        author = tvAuthor.text.toString(),
                        date = tvDate.text.toString(),
                    )
                )
            }

            Toast.makeText(requireContext(), "Saved To Favorite", Toast.LENGTH_SHORT).show()
            return !toggle
        }
    }

    private fun removeFromFav(): Boolean {
        binding.todayLayout.apply {

            btnFavorite.setImageResource(R.drawable.ic_favorite_border)
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.deleteQuote(
                    quote = tvQuoteContent.text.toString(),
                    author = tvAuthor.text.toString(),
                )
            }
            Toast.makeText(requireContext(), "Removed From Favorite", Toast.LENGTH_SHORT)
                .show()
            return !toggle

        }
    }

    private fun shareQuote() {
        binding.todayLayout.apply {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, tvQuoteContent.text.toString())

            startActivity(Intent.createChooser(sharingIntent, "Share text via"))
        }
    }

    private fun copyQuote() {
        binding.todayLayout.apply {
            if (tvQuoteContent.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "There is no text", Toast.LENGTH_SHORT).show()
            } else {
                val clipboardManager =
                    activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Data ", tvQuoteContent.text.toString())
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(requireContext(), "Text copied to Clipboard", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun previousQuote() {
        binding.todayLayout.apply {
            setData(quoteId)
            init()
            if (quoteId == 0) {
                btnPrevious.isClickable = false
            } else {
                btnNext.isClickable = true
                quoteId--
            }
        }
    }

    private fun nextQuote() {
        binding.todayLayout.apply {
            setData(quoteId)
            init()
            if (quoteId == data.results.size - 1) {
                btnNext.isClickable = false
            } else {
                btnPrevious.isClickable = true
                quoteId++
            }
        }
    }
}