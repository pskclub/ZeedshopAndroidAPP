package com.boodabest.ui.product_single

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.boodabest.R
import com.boodabest.core.BaseFragment
import com.boodabest.database.Product
import com.boodabest.models.RepoOptions
import com.boodabest.repositories.product.ProductViewModel
import com.boodabest.utils.toSpanned
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_product_single.*
import kotlinx.android.synthetic.main.fragment_product_single_action_group.*
import kotlinx.android.synthetic.main.fragment_product_single_brand_nav.*
import kotlinx.android.synthetic.main.fragment_product_single_cover.*
import kotlinx.android.synthetic.main.fragment_product_single_detail.*
import kotlinx.android.synthetic.main.fragment_product_single_options_dialog.*


private const val PRODUCT_ID = "product_id"
private const val KEY_IS_SHOW_DIALOG = "isShowDialog"


class ProductSingleFragment : BaseFragment(R.layout.fragment_product_single) {
    private var isShowDialog: Boolean = false
    private var productId: String? = null


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreInstanceState(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            productId = it.getString(PRODUCT_ID)
        }

        app.updateBackAble(true)
        initProduct()
    }

    companion object {
        fun newInstance(productId: String) =
            ProductSingleFragment().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID, productId)
                }
            }
    }

    private fun initProduct() {
        if (isShowDialog) {
            optionsDialog.visibility = View.VISIBLE
        }

        val productViewModel: ProductViewModel by viewModels {
            viewModelFactory
        }

        productViewModel.setProductId(productId!!, RepoOptions(isNetworkOnly = true))
        productViewModel.item.observe(viewLifecycleOwner, Observer { product ->
            if (product.data !== null) {
                initProductDialog(product.data)
                app.updateTitle(product.data.title)
                txtTitle.text = product.data.title
                txtPrice.text = product.data.price
                txtDesc.text = product.data.description?.toSpanned() ?: ""
                txtBrandNavDesc.text =
                    getString(R.string.product_brand_nav, product.data.brand.title)
                Glide
                    .with(this)
                    .load(product.data.header?.coverImageURL)
                    .into(imgCover)

                Glide
                    .with(this)
                    .load(product.data.brand.thumbnailURL)
                    .into(imgBrandLogo)

                Glide
                    .with(this)
                    .load(product.data.brand.thumbnailURL)
                    .into(imgBrandLogoNav)

                coverContainer.setBackgroundColor(Color.parseColor(product.data.brand.backgroundColor))
                txtHeaderTitle.apply {
                    text = product.data.header?.title?.toSpanned() ?: ""
                    typeface = Typeface.DEFAULT_BOLD
                    setTextColor(Color.parseColor(product.data.header?.titleColor))
                }


                txtHeaderCaption.apply {
                    text = product.data.header?.caption?.toSpanned() ?: ""
                    setTextColor(Color.parseColor(product.data.header?.titleColor))
                }

                val productThumbnailAdapter = ProductThumbnailAdapter()
                productImageSlider.setSliderAdapter(productThumbnailAdapter)
                productThumbnailAdapter.renewItems(product.data.galleries)

                btnAddToCart.setOnClickListener {
                    onBtnAddToCartClick(it)
                }
                btnBuyNow.setOnClickListener {
                    onBtnBuyNowClick(it)
                }

                btnClose.setOnClickListener {
                    onBtnCloseClick(it)
                }

                cvBrand.setOnClickListener {
                    onBtnBrandNavClick(it)
                }
            }
        })
    }


    private fun onBtnBrandNavClick(it: View) {
        // TODO : go brand fragment
    }


    private fun onBtnCloseClick(it: View) {
        isShowDialog = false
        optionsDialog.visibility = View.GONE
    }

    private fun onBtnAddToCartClick(it: View) {
        isShowDialog = true
        optionsDialog.visibility = View.VISIBLE
    }

    private fun onBtnBuyNowClick(it: View) {
        isShowDialog = true
        optionsDialog.visibility = View.VISIBLE
    }


    private fun initProductDialog(product: Product) {
        txtDialogTitle.text = product.title
        txtDialogPrice.text = product.price
        Glide
            .with(this)
            .load(product.thumbnailURL)
            .into(ivDialogProductThumbnail)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_IS_SHOW_DIALOG, isShowDialog)
        super.onSaveInstanceState(outState)
    }


    private fun restoreInstanceState(bundle: Bundle?) {
        isShowDialog = bundle?.getBoolean(KEY_IS_SHOW_DIALOG) ?: false
    }
}
