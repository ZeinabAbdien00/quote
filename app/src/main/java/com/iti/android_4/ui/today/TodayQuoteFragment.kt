package com.iti.android_4.ui.today

import android.content.*
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.iti.android_4.R
import com.iti.android_4.databinding.FragmentTodayQuoteBinding
import com.iti.android_4.models.Quotes
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import com.iti.android_4.ui.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayQuoteFragment : Fragment() {

    private lateinit var binding: FragmentTodayQuoteBinding
    private lateinit var viewModel: TodayQuotesViewModel
    private lateinit var repository: BaseRepository
    private var toggle: Boolean = true
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var data: Quotes
    var quoteId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTodayQuoteBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = TodayQuotesViewModel(repository)
        sharedPreferences = activity!!.getSharedPreferences("MyShared", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observation()
    }

    private fun init() {
        binding.todayLayout.apply {
            toggle =
                if (binding.todayLayout.tvQuoteContent.text.toString() == sharedPreferences.getString(
                        "favorite",
                        ""
                    ).toString()
                ) {
                    //binding.todayLayout.btnFavorite.setImageResource(R.drawable.ic_favorite)

                    val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
                    drawable?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base), PorterDuff.Mode.SRC_IN)
                    binding.todayLayout.btnFavorite.setImageDrawable(drawable)


                    false
                } else {
                    binding.todayLayout.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
                    true
                }
        }
    }

    private fun observation() {

        viewModel.getQuotes()
        viewModel.quotes.observe(viewLifecycleOwner, Observer { response ->
            data = response
            initialData()
            init()
            onClickToSaveFavorite(response)
        })
    }

    private fun initialData() {
        binding.todayLayout.tvQuoteContent.text =
            data.results[0].content
        binding.todayLayout.tvAuthor.text =
            data.results[0].author
        binding.todayLayout.tvDate.text =
            data.results[0].dateModified
    }

    private fun onClickToSaveFavorite(response: Quotes?) {


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
//            btnFavorite.setImageResource(R.drawable.ic_favorite)

            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
            drawable?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.base), PorterDuff.Mode.SRC_IN)
            binding.todayLayout.btnFavorite.setImageDrawable(drawable)



            CoroutineScope(Dispatchers.Main).launch {
                viewModel.newQuote(
                    SavedQuoteLocalDataModel(
                        quote = tvQuoteContent.text.toString(),
                        author = tvAuthor.text.toString(),
                    )
                )
            }
            editor.putString("favorite", tvQuoteContent.text.toString())
            editor.apply()

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
                    author = tvAuthor.text.toString()
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
            tvQuoteContent.text = data.results[quoteId].content.toString()
            tvAuthor.text = data.results[quoteId].author.toString()
            tvDate.text = data.results[quoteId].dateModified.toString()

            if (quoteId == 0) {
                btnPrevious.isClickable = false
            } else {
                btnNext.isClickable = true
                quoteId--
            }
            init()
        }
    }

    private fun nextQuote() {
        binding.todayLayout.apply {
            tvQuoteContent.text = data.results[quoteId].content.toString()
            tvAuthor.text = data.results[quoteId].author.toString()

            if (quoteId == data.results.size - 1) {
                btnNext.isClickable = false
            } else {
                btnPrevious.isClickable = true
                quoteId++
            }
            init()
        }
    }
}