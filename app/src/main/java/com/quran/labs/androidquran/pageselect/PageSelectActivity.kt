package com.quran.labs.androidquran.pageselect

import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.quran.labs.androidquran.QuranApplication
import com.quran.labs.androidquran.R
import com.quran.labs.androidquran.ui.helpers.QuranDisplayHelper
import javax.inject.Inject

class PageSelectActivity : AppCompatActivity() {
  @Inject lateinit var presenter : PageSelectPresenter
  private lateinit var adapter : PageSelectAdapter
  private lateinit var viewPager: ViewPager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as QuranApplication).applicationComponent.inject(this)

    setContentView(R.layout.page_select)

    val display = windowManager.defaultDisplay
    val width = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
      QuranDisplayHelper.getWidthKitKat(display)
    else display.width

    adapter = PageSelectAdapter(LayoutInflater.from(this), width)

    viewPager = findViewById(R.id.pager)
    viewPager.adapter = adapter

    // let the next and previous pages be slightly visible
    val pageMargin = resources.getDimensionPixelSize(R.dimen.page_margin)
    val pagerPadding = pageMargin * 2
    viewPager.setPadding(pagerPadding, 0, pagerPadding, 0)
    viewPager.clipToPadding = false
    viewPager.pageMargin = pageMargin
  }

  override fun onResume() {
    super.onResume()
    presenter.bind(this)
  }

  override fun onPause() {
    presenter.unbind(this)
    super.onPause()
  }

  override fun onDestroy() {
    adapter.cleanUp()
    super.onDestroy()
  }

  fun onUpdatedData(data: List<PageTypeItem>) {
    adapter.replaceItems(data, viewPager)
  }
}