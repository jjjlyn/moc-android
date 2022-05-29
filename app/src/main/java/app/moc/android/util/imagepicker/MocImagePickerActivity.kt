package app.moc.android.util.imagepicker

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridSpacingItemDecoration
import app.moc.android.util.imagepicker.adapter.AlbumAdapter
import app.moc.android.util.imagepicker.adapter.MediaAdapter
import app.moc.android.util.imagepicker.adapter.SelectedMediaAdapter
import app.moc.android.util.imagepicker.listener.OnItemClickListener
import app.moc.android.R
import app.moc.android.databinding.MocImagePickerActivityBinding
import app.moc.android.util.dp
import app.moc.android.util.imagepicker.model.MediaHeaderUIModel
import app.moc.android.util.imagepicker.model.MediaUIModel
import app.moc.android.util.imagepicker.model.toUIModel
import app.moc.android.util.imagepicker.util.GalleryUtil
import app.moc.android.util.imagepicker.util.MediaUtil
import app.moc.android.util.setVisible
import app.moc.model.Album
import app.moc.model.Media
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MocImagePickerActivity : AppCompatActivity() {

    private lateinit var binding: MocImagePickerActivityBinding

    private lateinit var builder: MocImagePickerBaseBuilder<*>

    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var selectedMediaAdapter: SelectedMediaAdapter
    private val albumAdapter: AlbumAdapter by lazy { AlbumAdapter() }

    private var selectedPosition = 0

    private var latestTmpUri: Uri? = null

    private val requestActivity : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = latestTmpUri ?: return@registerForActivityResult
        if (result.resultCode == RESULT_OK) {
            // refresh gallery
            MediaUtil.scanMedia(
                this,
                uri,
                onSuccess = {
                    loadMedia(true)
                    onMediaClick(uri)
                })
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaUtil.deleteMedia(this, uri)
            }
        }
    }

    companion object {
        private const val EXTRA_BUILDER = "extra_builder"
        private const val EXTRA_SELECTED_URI_LIST = "extra_selected_uri_list"

        fun getIntent(context: Context, builder: MocImagePickerBaseBuilder<*>) =
            Intent(context, MocImagePickerActivity::class.java)
                .apply {
                    putExtra(EXTRA_BUILDER, builder)
                }

        fun getSelectedUris(data: Intent): List<Uri>? =
            data.getParcelableArrayListExtra(EXTRA_SELECTED_URI_LIST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Moc_Main)
        super.onCreate(savedInstanceState)
        setSavedInstanceState(savedInstanceState)
        binding = MocImagePickerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupRecyclerView()
        setupSelectedMediaView()
        setupListener()
        loadMedia()
    }

    override fun onStart() {
        super.onStart()
        // 권한 처리
        builder.checkPermission(this@MocImagePickerActivity,
            loading = {},
            onSuccess = { permissionResult ->
                val isGranted = permissionResult.isGranted()
                if (isGranted) {
                    return@checkPermission
                } else {
                    onBackPressed()
                }
            },
            onError = { e ->

            }
        )
    }

    private fun setSavedInstanceState(savedInstanceState: Bundle?) {
        val bundle: Bundle? = when {
            savedInstanceState != null -> savedInstanceState
            else -> intent.extras
        }

        builder = bundle?.getParcelable(EXTRA_BUILDER)
            ?: MocImagePickerBaseBuilder<MocImagePickerBaseBuilder<*>>()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_BUILDER, builder)
        super.onSaveInstanceState(outState)
    }

    private fun loadMedia(isRefresh: Boolean = false) {
        GalleryUtil.getMedia(this@MocImagePickerActivity,
            onSuccess = { list ->
                albumAdapter.submitList(list)
                setSelectedAlbum(list, selectedPosition)
                if (isRefresh.not()) {
                    setSelectedUris(builder.selectedUris)
                }
            },
            onError = { e ->

            }
        )
    }

    private fun setSelectedUris(selectedUris: List<Uri>?) =
        selectedUris?.forEach { uri -> onMediaClick(uri) }

    private fun setupListener() {
        binding.containerDropDown.root.setOnClickListener {
            binding.isAlbumOpened = binding.isAlbumOpened.not()
        }
        binding.buttonConfirm.setOnClickListener {
            onMultiMediaDone()
        }
    }

    private fun onMultiMediaDone() {
        val selectedUris = mediaAdapter.selectedUris
        if (selectedUris.size < builder.minCount) {
            val message = builder.minCountMessage

        } else {
            val data = Intent().apply {
                putParcelableArrayListExtra(EXTRA_SELECTED_URI_LIST, ArrayList(selectedUris))
            }
            setResult(Activity.RESULT_OK, data)
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        setupAlbumRecyclerView()
        setupMediaRecyclerView()
        setupSelectedMediaRecyclerView()
    }

    private fun setupAlbumRecyclerView() {
        val albumAdapter = albumAdapter.apply {
            onAlbumItemClick = { itemPosition ->
                this@MocImagePickerActivity.setSelectedAlbum(this.currentList, itemPosition)
                binding.isAlbumOpened = false
            }
        }
        binding.listAlbum.adapter = albumAdapter
    }

    private fun setSelectedAlbum(currentAlbums: List<Album>, selectedPosition: Int) {
        val album = currentAlbums[selectedPosition]
//        if(this.selectedPosition == selectedPosition && binding.selectedAlbum == album){
//            return
//        }

        binding.selectedAlbum = album
        this.selectedPosition = selectedPosition
        albumAdapter.setSelectedAlbum(album)
        val result =
            mutableListOf<MediaUIModel>(MediaHeaderUIModel(title = "camera")).apply {
                addAll(album.mediaUris.map { it.toUIModel() })
            }
        mediaAdapter.submitList(result)
        binding.listData.layoutManager?.scrollToPosition(0)
    }

    private fun setupSelectedMediaRecyclerView() {
        selectedMediaAdapter = SelectedMediaAdapter().apply {
            onClearClickListener = { uri ->
                onMediaClick(uri)
            }
        }

        binding.listSelected.adapter = selectedMediaAdapter
    }

    private fun setupMediaRecyclerView() {
        binding.listData.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount = 3,
                spacing = 2.dp().toInt()
            )
        )
        mediaAdapter = MediaAdapter(builder,
            object : OnItemClickListener {
                override fun onItemClick(data: Media, itemPosition: Int, layoutPosition: Int) {
                    closeAlbum()
                    this@MocImagePickerActivity.onMediaClick(data.uri)
                }

                override fun onHeaderClick() {
                    // 사진 찍기
                    onCameraTileClick()
                }
            }).apply {
            onMediaAddListener = {
                // selected list scroll interaction => 추가될 때마다 마지막 포지션으로 이동하게끔 구현
                binding.listSelected.smoothScrollToPosition(selectedMediaAdapter.itemCount)
            }
        }
        binding.listData.adapter = mediaAdapter
    }

    private fun onCameraTileClick() {
        val (cameraIntent, uri) = MediaUtil.getMediaIntentUri(
            this,
            builder.mediaType,
            builder.savedDirectoryName
        )

        latestTmpUri = uri
        requestActivity.launch(cameraIntent)
    }

    private fun setupToolbar() {
        binding.toolbar.run {
            title = "제품사진 선택"
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun onMediaClick(uri: Uri) {
        onMultiMediaClick(uri)
    }

    private fun onMultiMediaClick(uri: Uri) {
        Timber.d("media uri: ${uri.path}")
        mediaAdapter.toggleMediaSelect(uri)
        selectedMediaAdapter.submitList(mediaAdapter.selectedUris.toMutableList())
        updateSelectedMediaView()
        updateButtonUI()
    }

    private fun updateButtonUI() {
        binding.buttonConfirm.setVisible(mediaAdapter.selectedUris.isNullOrEmpty().not())
        binding.buttonConfirm.text = "다음(${mediaAdapter.selectedUris.size}/3)"
    }

    private fun setupSelectedMediaView() {
        binding.containerSelected.run {
            if (mediaAdapter.selectedUris.size > 0) {
                layoutParams.height = 88.dp().toInt()
            } else {
                layoutParams.height = 0
            }
            requestLayout()
        }
    }

    private fun updateSelectedMediaView() {
        binding.containerSelected.post {
            binding.containerSelected.run {
                if (mediaAdapter.selectedUris.size > 0) {
                    slideView(
                        this, layoutParams.height,
                        88.dp().toInt()
                    )
                } else if (mediaAdapter.selectedUris.size == 0) {
                    slideView(this, layoutParams.height, 0)
                }
            }
        }
    }

    private fun slideView(view: View, currentHeight: Int, newHeight: Int) {
        val valueAnimator = ValueAnimator.ofInt(currentHeight, newHeight).apply {
            addUpdateListener {
                view.layoutParams.height = it.animatedValue as Int
                view.requestLayout()
            }
        }

        AnimatorSet().apply {
            interpolator = AccelerateDecelerateInterpolator()
            play(valueAnimator)
        }.start()
    }

    override fun onBackPressed() {
        if (isAlbumOpened()) {
            closeAlbum()
        }
        super.onBackPressed()
    }

    private fun isAlbumOpened(): Boolean = binding.isAlbumOpened

    private fun closeAlbum() {
        binding.isAlbumOpened = false
    }
}