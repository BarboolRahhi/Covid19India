package com.codelectro.covid19india.ui.main.total

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.Total
import com.codelectro.covid19india.ui.main.MainActivity
import com.codelectro.covid19india.ui.toast
import com.codelectro.covid19india.util.ApiResult
import kotlinx.android.synthetic.main.fragment_total.*
import javax.inject.Inject

class TotalFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewmodel: TotalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).mainComponent.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = ViewModelProvider(this, viewModelFactory).get(TotalViewModel::class.java)

        val wedData: String =  "<div class=\"flourish-embed flourish-chart\" data-src=\"story/230114\"><script src=\"https://public.flourish.studio/resources/embed.js\"></script></div>"
        val mimeType: String = "text/html"
        val utfType: String = "UTF-8"
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.loadData(wedData,mimeType,utfType)

        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewmodel.getSummary().removeObservers(viewLifecycleOwner)
        viewmodel.getSummary().observe(viewLifecycleOwner, Observer {
            execute(it)
        })

    }

    private fun execute(apiResult: ApiResult<Total?>) {
        when (apiResult) {
            is ApiResult.Success -> setUI(apiResult.value)
            is ApiResult.GenericError -> Log.d(MainActivity.TAG, "onCreate: ${apiResult.errorMessage}")
            ApiResult.NetworkError -> Log.d(MainActivity.TAG, "onCreate: NetworkError")
        }
    }

    private fun setUI(total: Total?) {
        total?.let {
            confirmedCase.text = it.confirmed.toString()
            activeCase.text = it.active.toString()
            recoveredCase.text = it.recovered.toString()
            deathCase.text = it.deaths.toString()
        }

    }

}