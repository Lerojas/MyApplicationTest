package com.androidavanzado.myapplicationtest.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidavanzado.myapplicationtest.*
import com.androidavanzado.myapplicationtest.ViewModel.HitsViewModel
import com.androidavanzado.myapplicationtest.data.api.RestEngine
import com.androidavanzado.myapplicationtest.data.api.ArticlesService
import com.androidavanzado.myapplicationtest.data.hitEliminated.HitEliminated
import com.androidavanzado.myapplicationtest.model.ArticlesDataCollectionItem
import com.androidavanzado.myapplicationtest.model.Hit
import retrofit2.Response


class MainActivity : AppCompatActivity(),
    CellClickListener {

    private lateinit var hitsViewModel: HitsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var arrayListHits: ArrayList<Hit>
    object MyConstants{ val URL = "url"}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hitsViewModel = kotlin.run {
            ViewModelProviders.of(this).get(HitsViewModel::class.java)
        }

        viewManager = LinearLayoutManager(this)
        arrayListHits = ArrayList()
        viewAdapter = HitAdapter(arrayListHits, this)

        swipeRefresh = findViewById(R.id.mySwipeRefresh)
        swipeRefresh.setOnRefreshListener {
            getData()
        }

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val swipeHandler = object : com.androidavanzado.myapplicationtest.view.ItemTouchHelper(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val hit = arrayListHits[position]
                val hitEliminated =
                    HitEliminated()
                hitEliminated.saveHitEliminated(applicationContext, hit)
                arrayListHits.removeAt(position)
                viewAdapter.notifyItemRemoved(position)
            }
}
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    fun getData(){

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if(isConnected){
            getDataApi()
        }
        else{
            getDataRoom()
        }
    }

    fun getDataApi(){

        val query : String = "android"
        val articlesService: ArticlesService = RestEngine.getRestEngine().create(
            ArticlesService::class.java)
        val result: retrofit2.Call<ArticlesDataCollectionItem> = articlesService.listArticles(query)

        result.enqueue(object : retrofit2.Callback<ArticlesDataCollectionItem> {
            override fun onFailure(call: retrofit2.Call<ArticlesDataCollectionItem>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                swipeRefresh.isRefreshing = false
            }

            override fun onResponse(call: retrofit2.Call<ArticlesDataCollectionItem>, response: Response<ArticlesDataCollectionItem>
            ) {
                val arrayHints : ArrayList<Hit>? = response.body()?.hits
                if (arrayHints != null) {
                    arrayListHits = HitEliminated().removeHitFromList(applicationContext, arrayHints)
                    hitsViewModel.saveDataRoom(arrayListHits)
                    refreshUI()
                }
                swipeRefresh.isRefreshing = false
            }
        })
    }

    override fun onCellClickListener(url: String) {
        val intent = Intent(this, WebViewActivity::class.java).apply {
            putExtra(MyConstants.URL, url)
        }
        startActivity(intent)
    }

    fun getDataRoom(){
        hitsViewModel.hits.observe(this@MainActivity, Observer<List<Hit>>{
            hits ->
            arrayListHits.clear()
            arrayListHits = HitEliminated().removeHitFromList(applicationContext, hits as ArrayList<Hit>)
            refreshUI()
            swipeRefresh.isRefreshing = false
        } )
    }

    fun refreshUI(){
        viewAdapter = HitAdapter(arrayListHits, this@MainActivity)
        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewAdapter.notifyDataSetChanged()
    }
}
