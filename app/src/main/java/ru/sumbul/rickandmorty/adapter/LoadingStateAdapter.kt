package ru.sumbul.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.databinding.LoadStateBinding

class LoadingStateAdapter(
    private val retryListener: () -> Unit,
) : LoadStateAdapter<DataLoadingViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): DataLoadingViewHolder {
        val binding = LoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataLoadingViewHolder(binding, retryListener)
    }

    override fun onBindViewHolder(holder: DataLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}


class DataLoadingViewHolder(
    private val footerLoadingBinding: LoadStateBinding,
    private val retryListener: () -> Unit,
) : RecyclerView.ViewHolder(footerLoadingBinding.root) {

    fun bind(loadState: LoadState) {
        footerLoadingBinding.apply {
            footerLoadingBinding.progressBar.isVisible = loadState is LoadState.Loading
            footerLoadingBinding.retryButton.isVisible = loadState is LoadState.Error
            retryButton.setOnClickListener { retryListener() }
        }
    }
}

