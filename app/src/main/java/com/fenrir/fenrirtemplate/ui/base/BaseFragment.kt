package com.fenrir.fenrirtemplate.ui.base

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fenrir.fenrirtemplate.BR
import com.fenrir.fenrirtemplate.R
import com.fenrir.fenrirtemplate.ui.activity.MainActivity
import com.fenrir.fenrirtemplate.ui.view.CustomToolbar
import com.fenrir.fenrirtemplate.ui.view.ToolbarMode
import com.fenrir.fenrirtemplate.util.BindLife
import com.fenrir.fenrirtemplate.util.DialogUtil
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


abstract class BaseFragment<Bind : ViewDataBinding, VM : BaseViewModel>
constructor(
    private val clazz: Class<VM>,
    private val bindingCreator: (LayoutInflater, ViewGroup?) -> Bind
) : Fragment(), BindLife, KodeinAware {

    constructor(clazz: Class<VM>, @LayoutRes layoutRes: Int) : this(clazz, { inflater, group ->
        DataBindingUtil.inflate(inflater, layoutRes, group, false)
    })

    val viewModel: VM by lazy {
        ViewModelProviders.of(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        ).get(clazz)
    }

    protected lateinit var binding: Bind

    override val kodein by kodein()

    override val compositeDisposable = CompositeDisposable()

    //method

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingCreator.invoke(layoutInflater, container)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        initObserver()
        initView()
        initData()
    }

    private fun setupToolBar() {
        view?.findViewById<CustomToolbar>(R.id.tool_bar)?.apply {
            (activity as? MainActivity)?.setSupportActionBar(this)
            when (mode) {
                ToolbarMode.Menu -> onNavClick = { (activity as? MainActivity)?.toggleDrawer() }
                ToolbarMode.Back -> onNavClick = { (activity as? MainActivity)?.onBackPressed() }
                else -> {
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.apiError.bindDialog(context!!, viewLifecycleOwner)
        viewModel.uiError.bindDialog(context!!, viewLifecycleOwner)

        viewModel.progressDialog.observeNonNull {
            if (it) DialogUtil.showProgressDialog(context)
            else DialogUtil.hideProgressDialog()
        }
    }

    abstract fun initView()

    abstract fun initData()

    //ext

    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(viewLifecycleOwner, Observer<T> { v -> observer(v) })

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable()
    }
}
