package com.ynk.goodnews.activities

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ynk.goodnews.R
import com.ynk.goodnews.adapters.AdapterListNews
import com.ynk.goodnews.clicklisteners.AdapterItemClickListener
import com.ynk.goodnews.clicklisteners.NewsDialogClickListeners
import com.ynk.goodnews.databinding.NewsDialogBinding
import com.ynk.goodnews.extensions.changeMenuIconColor
import com.ynk.goodnews.extensions.setSystemBarColor
import com.ynk.goodnews.extensions.setSystemBarLight
import com.ynk.goodnews.model.News
import com.ynk.goodnews.utils.LocaleHelper
import com.ynk.goodnews.utils.Util
import com.ynk.goodnews.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), AdapterItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var ivToolbarCountry: ImageView

    private lateinit var viewModel: MainViewModel
    private lateinit var countrys: Array<String>
    private lateinit var countriesIcons: TypedArray
    private lateinit var adapterListNews: AdapterListNews
    private lateinit var pref: SharedPreferences

    private var newsList: MutableList<News> = mutableListOf()

    init {
        newsList = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        ivToolbarCountry = findViewById(R.id.ivToolbarCountry)
        recyclerView = findViewById(R.id.recyclerView)

        initVariables()
        initToolbar()
        initObservers()
        initAdapters()
        languageControl()

        if (pref.contains(countryPositionPref)) ivToolbarCountry.setImageResource(
            countriesIcons.getResourceId(pref.getInt(countryPositionPref, 0), 0)
        )
        pref.getString(Util.COUNTRY_PREF, "tr")?.let { viewModel.setCountryCode(it) }
    }

    private fun initVariables() {
        countrys = resources.getStringArray(R.array.countrys)
        countriesIcons = resources.obtainTypedArray(R.array.countrysIcons)

        pref = applicationContext.getSharedPreferences(Util.APP_NAME, MODE_PRIVATE)
    }

    private fun initAdapters() {
        adapterListNews = AdapterListNews(newsList, this)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapterListNews
    }

    private fun initObservers() {
        viewModel.newsLiveData.observe(this) { news ->
            newsList.clear()
            news?.let {
                newsList.addAll(it)
                adapterListNews.notifyItemRangeChanged(0, it.size)
            }
        }
    }

    private fun languageControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            && !pref.getBoolean(firstControl, false)
        ) {
            val primaryLocale = resources.configuration.locales[0]
            LocaleHelper.setLocale(applicationContext, primaryLocale.language)
            val position = getLanguagePosition(primaryLocale.language)
            pref.edit().putInt(countryPositionPref, position).apply()
            pref.edit().putBoolean(firstControl, true).apply()
            recreate()
        }
    }

    private fun getLanguagePosition(displayLanguage: String): Int {
        val codes = resources.getStringArray(R.array.countrysCodes)
        for (i in codes.indices) {
            if (codes[i] == displayLanguage) return i
        }
        return 0
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setSystemBarColor(R.color.white)
        setSystemBarLight()
    }

    private fun showLanguageDialog() {
        AlertDialog.Builder(this).setCancelable(false)
            .setTitle("Choose Country")
            .setSingleChoiceItems(countrys, pref.getInt(countryPositionPref, 0), null)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.ok) { dialog, _ ->
                val selectedPosition =
                    (dialog as AlertDialog).listView
                        .checkedItemPosition
                pref.edit().putInt(countryPositionPref, selectedPosition).apply()
                pref.edit().putString(Util.COUNTRY_PREF,
                    resources.getStringArray(R.array.countrysCodes)[selectedPosition]
                ).apply()
                LocaleHelper.setLocale(
                    applicationContext,
                    resources.getStringArray(R.array.countrysCodes)[selectedPosition]
                )
                recreate()
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        changeMenuIconColor(menu, Color.BLACK)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.let { it ->
            it.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            it.queryHint = getString(R.string.search_in_everything)
            it.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchNews(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
        return true
    }

    fun categoryClicked(view: View) {
        viewModel.newsCategoryClick(view.tag.toString())
    }

    fun countryClick(view: View?) {
        showLanguageDialog()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onNewsItemClick(news: News?) {
        showDialogPolygon(news)
    }

    private fun showDialogPolygon(news: News?) {
        val dialog = Dialog(this)
        val binding: NewsDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(applicationContext), R.layout.dialog_header_polygon,
            null, false)
        binding.news = news
        binding.listener = object : NewsDialogClickListeners {
            override fun onGotoWebSiteClick(url: String?) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            override fun onDismissClick() {
                dialog.dismiss()
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()
    }

    companion object{
        const val firstControl = "firstControl"
        const val countryPositionPref = "countryPositionPref"
    }
}