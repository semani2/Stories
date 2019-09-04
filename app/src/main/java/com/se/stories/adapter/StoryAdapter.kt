package com.se.stories.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

    class StoryViewHolder(inflater: LayoutInflater, parent: ViewGroup,
                          private val clickSubject: PublishSubject<StoryEntity>)
        : RecyclerView.ViewHolder(inflater.inflate(
        R.layout.story_item_layout, parent, false)) {

        private var storyCardView = itemView.findViewById<CardView>(R.id.story_layout)
        private var storyTitleTextView = itemView.findViewById<TextView>(R.id.story_title_text_view)
        private var storyAuthorTextView = itemView.findViewById<TextView>(R.id.story_author_text_view)
        private var storyCoverImageView = itemView.findViewById<ImageView>(R.id.story_cover_image_view)
        private var storyTextLayout = itemView.findViewById<ConstraintLayout>(R.id.story_text_layout)

        fun bind(storyEntity: StoryEntity) {
            storyTitleTextView.text = storyEntity.title
            storyAuthorTextView.text = storyEntity.authorName

            Glide.with(storyCoverImageView)
                .asBitmap()
                .load(storyEntity.cover)
                .fitCenter()
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        /* no op */
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Palette.from(resource).generate { palette ->
                            val darkVibrant = palette?.darkVibrantSwatch
                            if (darkVibrant != null) {
                                storyTextLayout.setBackgroundColor(darkVibrant.rgb)
                            } else {
                                storyTextLayout.setBackgroundColor(storyCoverImageView.resources
                                    .getColor(R.color.colorPrimary))
                            }
                        }
                        storyCoverImageView.setImageBitmap(resource)
                    }
                })

            storyCardView.setOnClickListener { clickSubject.onNext(storyEntity) }
        }
    }
}
