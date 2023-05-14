package com.nimko.contacts_from_api.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimko.contacts_from_api.MainActivity
import com.nimko.contacts_from_api.R
import com.nimko.contacts_from_api.adapter.ClickItem
import com.nimko.contacts_from_api.adapter.MyItemRecyclerViewAdapter
import com.nimko.contacts_from_api.databinding.FragmentMainBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import com.nimko.contacts_from_api.model.MyViewModel


class MainFragment : Fragment() {

    private lateinit var binding:FragmentMainBinding
    private var adapter: MyItemRecyclerViewAdapter? = null
    lateinit var click:ClickItem
    lateinit var model:MyViewModel
    lateinit var command:Commandable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        click = activity as MainActivity
        command = activity as MainActivity
        model = (activity as MainActivity).model
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        binding.toolbar.inflateMenu(R.menu.menu)
        binding.toolbar.setTitle(R.string.app_name)
        binding.toolbar.menu.apply {
            onCreateOptionsMenu(this)
        }
        binding.buttonAdd.setOnClickListener {
            command.edit(null)
        }

        adapter = MyItemRecyclerViewAdapter(click)
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter


        model.values.observe(viewLifecycleOwner, {
            refreshList(it)
            Log.d("MainActivity", "Observer")
        })

        return binding.root
    }

    private fun progressBar(){
        val waitProgres = ItemForAdapter.Header("", true)
        adapter!!.values.add(0, waitProgres)
        adapter!!.refresh()
    }

    override fun onResume() {
        super.onResume()
        progressBar()
        model.apiReq()
    }

    private fun refreshList(allContacts: MutableList<ItemForAdapter.Person>) {
        allContacts.sortBy { it.firstName }
        var ch = allContacts[0].firstName[0]
        adapter!!.values.clear()
        adapter!!.values.add(ItemForAdapter.Header(ch.toString(), false))
        allContacts.forEach {
            val startCh = it.firstName[0]
            if (startCh != ch) {
                adapter!!.values.add(ItemForAdapter.Header(startCh.toString(),false))
                ch = startCh
            }
            adapter!!.values.add(it)
        }
        adapter!!.refresh()
        Log.d("List", "Refresh")
    }


    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(ch: String?): Boolean {
                if (!ch.isNullOrBlank()) {
                    adapter!!.values = model.find(ch).toMutableList()
                    adapter!!.refresh()
                } else {refreshList(model.find(null))}
                return true
            }
        })
        return true
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment()
    }
}