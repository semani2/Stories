package com.se.stories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.se.stories.R
import com.se.stories.data.db.entities.StoryEntity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * RecyclerView Adapter to display stories
 */
class StoryAdapter(private val list: List<StoryEntity>)
    : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private val clickSubject = PublishSubject.create<StoryEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StoryViewHolder(
        LayoutInflater.from(parent.context), parent, clickSubject)

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getClickEvent(): Observable<StoryEntity> = clickSubject

    class StoryViewHolder(inflater: LayoutInflater, private val parent: ViewGroup,
                          private val clickSubject: PublishSubject<StoryEntity>)
        : RecyclerView.ViewHolder(inflater.inflate(
        R.layout.story_item_layout, parent, false)) {

        private var storyCardView = itemView.findViewById<CardView>(R.id.story_layout)
        private var storyTitleTextView = itemView.findViewById<TextView>(R.id.story_title_text_view)
        private var storyAuthorTextView = itemView.findViewById<TextView>(R.id.story_author_text_view)
        private var storyCoverImageView = itemView.findViewById<ImageView>(R.id.story_cover_image_view)

        fun bind(storyEntity: StoryEntity) {
            storyTitleTextView.text = storyEntity.title
            storyAuthorTextView.text = storyEntity.authorName

            Glide.with(storyCoverImageView)
                .load(storyEntity.cover)
                .fitCenter()
                .into(storyCoverImageView)

            storyCardView.setOnClickListener { clickSubject.onNext(storyEntity) }
        }
    }
}
