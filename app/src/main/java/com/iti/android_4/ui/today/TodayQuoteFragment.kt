package com.iti.android_4.ui.today

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        init()
        observation()

    }

    private fun init() {
        toggle = if (sharedPreferences.getBoolean("favorite", false)) {
            binding.todayLayout.btnFavorite.setImageResource(R.drawable.ic_favorite)
            false
        } else {
            binding.todayLayout.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
            true

        }
    }

    private fun observation() {
        viewModel.getQuotes()
        viewModel.quotes.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                binding.todayLayout.tvQuoteContent.text =
                    response.results[0].content.toString()
                binding.todayLayout.tvAuthor.text =
                    response.results[0].author.toString()
            }
            onClickToSaveFavorite(response)

        })
    }

    private fun onClickToSaveFavorite(response: Quotes?) {

        var quoteId = 1

        binding.todayLayout.apply {
            btnFavorite.setOnClickListener {
                toggle = if (!toggle) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_border)

                    CoroutineScope(Dispatchers.Main).launch {

                        viewModel.deleteQuote(
                            quote = tvQuoteContent.text.toString(),
                            author = tvAuthor.text.toString()
                        )
                    }
                    Toast.makeText(requireContext(), "Removed From Favorite", Toast.LENGTH_SHORT)
                        .show()
                    !toggle
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite)
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.newQuote(
                            SavedQuoteLocalDataModel(
                                quote = tvQuoteContent.text.toString(),
                                author = tvAuthor.text.toString(),
                            )
                        )
                    }
                    Toast.makeText(requireContext(), "Saved To Favorite", Toast.LENGTH_SHORT).show()
                    editor.putBoolean("favorite", toggle)
                    editor.apply()
                    !toggle
                }
            }

            btnShare.setOnClickListener {

                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_TEXT, tvQuoteContent.text.toString())

                startActivity(Intent.createChooser(sharingIntent, "Share text via"))
            }

            btnCopy.setOnClickListener {

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

            btnNext.setOnClickListener {
                tvQuoteContent.text = response!!.results[quoteId].content.toString()
                tvAuthor.text = response.results[quoteId].author.toString()

                if (quoteId == response.results.size - 1) {
                    btnNext.isClickable = false
                } else {
                    btnPrevious.isClickable = true
                    quoteId++
                }
            }
            btnPrevious.setOnClickListener {
                tvQuoteContent.text = response!!.results[quoteId].content.toString()
                tvAuthor.text = response.results[quoteId].author.toString()

                if (quoteId == 0) {
                    btnPrevious.isClickable = false
                } else {
                    btnNext.isClickable = true
                    quoteId--
                }
            }
        }
    }

}